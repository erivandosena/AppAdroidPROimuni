package br.com.erivando.proimuni.ui.activity.crianca;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import br.com.erivando.proimuni.BuildConfig;
import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.database.model.Crianca;
import br.com.erivando.proimuni.database.model.Usuario;
import br.com.erivando.proimuni.mvp.base.BasePresenter;
import br.com.erivando.proimuni.ui.application.AppAplicacao;
import br.com.erivando.proimuni.util.ConverteBase64Task;
import br.com.erivando.proimuni.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

import static android.app.Activity.RESULT_OK;
import static br.com.erivando.proimuni.util.Uteis.REQUEST_IMG_CAMERA;
import static br.com.erivando.proimuni.util.Uteis.REQUEST_IMG_GALERIA;
import static br.com.erivando.proimuni.util.Uteis.bitmapParaBase64;
import static br.com.erivando.proimuni.util.Uteis.criaArquivoImagem;
import static br.com.erivando.proimuni.util.Uteis.getCapitalizeNome;
import static br.com.erivando.proimuni.util.Uteis.getParseDateString;
import static br.com.erivando.proimuni.util.Uteis.isDataValida;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 10:53
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CriancaPresenter<V extends CriancaMvpView> extends BasePresenter<V> implements CriancaMvpPresenter<V> {

    private File arquivoImagem;

    @Inject
    public CriancaPresenter(IDataManager iDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(iDataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onCadasrarClick(Long id, String nome, String nascimento, String responsavel, String sexo, Bitmap foto) {
        if ((nome == null) || (nome.isEmpty()) || !nome.matches("^[a-zA-Za-zà-ú]+ [a-zA-ZA-ZÀ-Ú]+.*")) {
            getMvpView().onError(R.string.erro_text_nome_completo);
            return;
        }

        if (!isDataValida(nascimento)) {
            getMvpView().onError(R.string.erro_text_data);
            return;
        }

        if (sexo == null || sexo.isEmpty()) {
            getMvpView().onError(R.string.text_valida_sexo);
            return;
        }

        getMvpView().showLoading();
        nome = getCapitalizeNome(nome.trim());
        Usuario usuario = getIDataManager().obtemUsuario(getIDataManager().obtemUsuario().getId());

        Crianca crianca = new Crianca();
        crianca.setId((id == 0L) ? (long) getIDataManager().getCriancaID().incrementAndGet() : id);
        crianca.setCriaNome(nome);
        crianca.setCriaNascimento(getParseDateString(nascimento).getTime());
        crianca.setCriaSexo(sexo);
        crianca.setUsuario(usuario);
        crianca.setCriaFoto((foto != null) ? bitmapParaBase64(foto) : null);

        try {
            if (onNovoAtualizaCrianca(crianca)) {
                getMvpView().showMessage(R.string.text_cadastro_sucesso);
            } else {
                getMvpView().showMessage(R.string.text_valida_cadastro);
                return;
            }
            if (!isViewAttached()) {
                return;
            }
            getMvpView().openCriancaListaActivity("edita");
        } catch (Exception ex) {
            getMvpView().onError(ex.getMessage());
            getMvpView().onError(R.string.erro_text_cadastro);
        } finally {
            getMvpView().hideLoading();
        }
    }

    @Override
    public boolean onNovoAtualizaCrianca(Crianca crianca) {
        return getIDataManager().novoAtualizaCrianca(crianca);
    }

    @Override
    public List<Crianca> onCriancaCadastrada() {
        return getIDataManager().obtemCriancas();
    }

    @Override
    public Crianca onCriancaCadastrada(Long id) {
        return getIDataManager().obtemCrianca(id);
    }

    @Override
    public void selecionarImagem(final Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, AppAplicacao.contextApp.getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {context.getString(R.string.texto_cadastro_opcao_camera), context.getString(R.string.texto_cadastro_opcao_imagens), context.getString(R.string.texto_botao_dialogo_cancela)};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                builder.setIcon(R.mipmap.ic_launcher_round);
                builder.setTitle(context.getString(R.string.texto_botao_dialogo_titulo));
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals(context.getString(R.string.texto_cadastro_opcao_camera))) {
                            dialog.dismiss();

                            imagemIntentCamera(context);

                        } else if (options[item].equals(context.getString(R.string.texto_cadastro_opcao_imagens))) {
                            dialog.dismiss();

                            imagemIntentGaleria();

                        } else if (options[item].equals(context.getString(R.string.texto_botao_dialogo_cancela))) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(AppAplicacao.contextApp, context.getString(R.string.texto_aviso_permissao_camera), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(AppAplicacao.contextApp, context.getString(R.string.texto_aviso_permissao_camera), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void imagemIntentCamera(final Context context) {
        Intent intentImagem = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentImagem.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intentImagem.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (AppAplicacao.contextApp.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            if (intentImagem.resolveActivity(AppAplicacao.contextApp.getPackageManager()) != null) {
                File arquivo = new File(AppAplicacao.contextApp.getObbDir(), context.getString(R.string.app_name));
                arquivoImagem = criaArquivoImagem(arquivo);
                if (arquivoImagem != null) {
                    Uri photoURI = FileProvider.getUriForFile(AppAplicacao.contextApp, BuildConfig.APPLICATION_ID, arquivoImagem);
                    List<ResolveInfo> resolvedIntentActivities = AppAplicacao.contextApp.getPackageManager().queryIntentActivities(intentImagem, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                        String packageName = resolvedIntentInfo.activityInfo.packageName;
                        AppAplicacao.contextApp.grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    intentImagem.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                }
                getMvpView().getStartActivityForResult(intentImagem, REQUEST_IMG_CAMERA);
            }
        } else
            Toast.makeText(AppAplicacao.contextApp, context.getString(R.string.texto_aviso_camera_ausente), Toast.LENGTH_LONG).show();
    }

    @Override
    public void imagemIntentGaleria() {
        Intent intentImagem = new Intent(Intent.ACTION_PICK);
        intentImagem.setAction(Intent.ACTION_GET_CONTENT);
        intentImagem.setType("image/*");
        getMvpView().getStartActivityForResult(intentImagem, REQUEST_IMG_GALERIA);
    }

    @Override
    public Bitmap onActivityResult(int requestCode, int resultCode, Intent data, final Context context, final ImageButton imageButton) {
        final int retornoRequestCode = requestCode & 0x0000ffff;
        Bitmap imagemBitmapFoto = null;
        try {
            if (resultCode == RESULT_OK) {
                if (retornoRequestCode == REQUEST_IMG_CAMERA) {
                    imageButton.setAlpha(new Float(1.0));
                    Uri uriImagem = Uri.fromFile(arquivoImagem);
                    if (arquivoImagem.length() > 0) {
                        ConverteBase64Task task = new ConverteBase64Task(arquivoImagem);
                        task.setImageCompressiorListner(new ConverteBase64Task.ImageCompressiorListner() {
                            @Override
                            public void onImageCompressed(Bitmap bitmap) {
                                imageButton.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(AppAplicacao.contextApp, context.getString(R.string.texto_aviso_erro_processo_imagem), Toast.LENGTH_LONG).show();
                            }
                        });
                        task.execute();
                        MediaScannerConnection.scanFile(AppAplicacao.contextApp,
                                new String[]{arquivoImagem.getAbsolutePath()},
                                new String[]{"image/jpg"},
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    public void onScanCompleted(String path, Uri uri) {
                                        try {
                                            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                            mediaScanIntent.setData(uri);
                                            AppAplicacao.contextApp.sendBroadcast(mediaScanIntent);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(AppAplicacao.contextApp, context.getString(R.string.texto_aviso_erro_aquisicao_imagem), Toast.LENGTH_LONG).show();
                    }
                } else if (retornoRequestCode == REQUEST_IMG_GALERIA) {
                    if (data != null) {
                        imageButton.setAlpha(new Float(1.0));
                        Uri uriImagem = data.getData();
                        imageButton.setImageURI(uriImagem);
                        imagemBitmapFoto = ((BitmapDrawable) imageButton.getDrawable()).getBitmap();
                    } else
                        Toast.makeText(AppAplicacao.contextApp, context.getString(R.string.texto_aviso_erro_aquisicao_imagem), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception ex) {
            ex.getStackTrace();
            Toast.makeText(AppAplicacao.contextApp, context.getString(R.string.texto_aviso_erro_sistema_desatualizado), Toast.LENGTH_LONG).show();
        }
        return imagemBitmapFoto;
    }
}
