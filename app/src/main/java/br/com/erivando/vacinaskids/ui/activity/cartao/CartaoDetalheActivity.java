package br.com.erivando.vacinaskids.ui.activity.cartao;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hendrix.pdfmyxml.PdfDocument;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.model.Calendario;
import br.com.erivando.vacinaskids.database.model.Cartao;
import br.com.erivando.vacinaskids.database.model.Dose;
import br.com.erivando.vacinaskids.database.model.Idade;
import br.com.erivando.vacinaskids.database.model.Imunizacao;
import br.com.erivando.vacinaskids.database.model.Vacina;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.activity.calendario.CalendarioMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.calendario.CalendarioMvpView;
import br.com.erivando.vacinaskids.ui.activity.idade.IdadeMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.idade.IdadeMvpView;
import br.com.erivando.vacinaskids.ui.activity.imunizacao.ImunizacaoMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.imunizacao.ImunizacaoMvpView;
import br.com.erivando.vacinaskids.ui.activity.main.MainActivity;
import br.com.erivando.vacinaskids.ui.adapter.VacinaRVA;
import br.com.erivando.vacinaskids.util.Uteis;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   19 de Outubro de 2018 as 20:08
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class CartaoDetalheActivity extends BaseActivity implements CartaoMvpView {

    private File EXPORT_PDF_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private String EXPORT_PDF_FILE_NAME = "CartaoDeVacinas";

    @Inject
    CartaoMvpPresenter<CartaoMvpView> presenter;

    @Inject
    IdadeMvpPresenter<IdadeMvpView> idadePresenter;

    @Inject
    CalendarioMvpPresenter<CalendarioMvpView> calendarioPresenter;

    @Inject
    ImunizacaoMvpPresenter<ImunizacaoMvpView> imunizacaoPresenter;


    @BindView(R.id.fab)
    FloatingActionButton fabFloatingActionButton;

    @BindView(R.id.image_cartao_vacinal)
    public ImageView mImagemCartao;

    @BindView(R.id.text_nome_crianca)
    public TextView mTextCrianca;

    @BindView(R.id.text_idade_crianca)
    public TextView mTextIdade;

    private Cartao cartao;
    private Intent intent;

    private Long idCartao;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CartaoDetalheActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartao_detalhe);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //executar o que quiser no clique da seta esquerda
                onBackPressed();
            }
        });
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getResources().getString(R.string.menu_item1));

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        presenter.onAttach(this);

        intent = getIntent();

        setUp();
    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        if (view == fabFloatingActionButton) {
            criaPDF(CartaoDetalheActivity.this);
        }
    }


    @Override
    protected void setUp() {
        if (intent != null) {
            idCartao = intent.getLongExtra("cartao", 0L);
            cartao = presenter.onCartaoCadastrado(idCartao);
            if(cartao == null) {
                startActivity(CartaoListaActvity.getStartIntent(this).putExtra("cartaoLista", "cartao"));
                finish();
            } else {
                if (cartao.getCrianca().getCriaFoto() != null)
                    mImagemCartao.setImageBitmap(Uteis.base64ParaBitmap(cartao.getCrianca().getCriaFoto()));
                mTextCrianca.setText(cartao.getCrianca().getCriaNome());
                mTextIdade.setText("Idade: " + Uteis.obtemIdadeCompleta(cartao.getCrianca().getCriaNascimento()));
            }
        }

        List<Idade> idadeList = idadePresenter.onIdadesCadastradas();
        List<Calendario> calendarioList = calendarioPresenter.onCalendariosCadastrados();

        List<Vacina> listaVacinas = new ArrayList<Vacina>();
        List<Dose> listaDoses = new ArrayList<Dose>();
        List<Idade> listaIdades = new ArrayList<Idade>();
        List<Imunizacao> listaImunizacoes = new ArrayList<Imunizacao>();
        for (Idade idade : idadeList) {
            RealmList<Vacina> vacinaItem = new RealmList<Vacina>();
            for (Calendario calendarioItem : calendarioList) {
                if (calendarioItem.getIdade().getId() == idade.getId()) {
                    listaVacinas.add(calendarioItem.getVacina());
                    listaDoses.add(calendarioItem.getDose());
                    listaIdades.add(calendarioItem.getIdade());
                    Imunizacao imunizacao = imunizacaoPresenter.onImunizacaoCadastrada(new String[]{"vacina.id", "dose.id", "cartao.id"}, new Long[]{calendarioItem.getVacina().getId(), calendarioItem.getDose().getId(), idCartao});

                    if (imunizacao != null) {
                        listaImunizacoes.add(imunizacao);
                    }
                }
            }
        }

        RecyclerView my_recycler_view = findViewById(R.id.cartao_lista_vacina_recyclerView);
        my_recycler_view.setHasFixedSize(true);
        VacinaRVA adapter = new VacinaRVA(listaVacinas, listaDoses, listaIdades, listaImunizacoes, cartao, this);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openCartaoListaActivity("cartao");
    }

    @Override
    public Context getContextActivity() {
        return CartaoDetalheActivity.this;
    }

    @Override
    public void openCartaoListaActivity(String acao) {
        Intent intent = CartaoListaActvity.getStartIntent(this);
        intent.putExtra("cartaoLista", acao);
        startActivity(intent);
        finish();
    }

    private void criaPDF(Context context) {

        final String texto = "        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eu mi dapibus, sodales ante vel, ultrices velit. Proin ac libero vitae mi bibendum fringilla quis id massa. Vivamus pharetra orci vitae tincidunt molestie. Morbi aliquam nulla diam, nec dapibus ligula suscipit in. Praesent eu tortor ut risus venenatis faucibus ut id felis. Nulla purus ipsum, varius id aliquet eu, ultricies at urna. Sed tempor ligula condimentum tempor auctor. Integer consequat cursus lobortis.\n" +
                "        Proin diam urna, mollis at luctus quis, suscipit sed mi. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin at laoreet ex. Aliquam elementum, nibh nec elementum pretium, urna risus pretium odio, in bibendum mi lorem ut tellus. Nullam volutpat volutpat dolor, nec pulvinar nunc pulvinar at. Proin elementum bibendum dapibus. Sed molestie laoreet nisi sit amet cursus. Sed in lobortis neque. Integer purus arcu, elementum sit amet blandit in, pharetra a nunc. Praesent placerat pellentesque ligula a accumsan. Phasellus sagittis lacus at tellus suscipit, sit amet consequat velit commodo. Vivamus pharetra nulla nec purus efficitur, vitae commodo lacus blandit. Aliquam risus libero, facilisis nec massa a, hendrerit volutpat leo. Duis in eros eros. Ut lacus felis, placerat ut malesuada ut, efficitur vitae leo. Integer sit amet neque nunc.\n" +
                "        Ut tincidunt diam id ornare maximus. Praesent libero dui, auctor a tortor quis, lobortis tincidunt est. Maecenas vel facilisis tellus. Phasellus finibus nisl sit amet cursus semper. Fusce convallis ullamcorper lorem, a egestas purus. Duis semper ante in arcu euismod pharetra. Quisque vitae consequat massa. Proin pellentesque nisl in elit accumsan, eget fringilla ligula maximus. Duis nec ipsum id nisi vehicula cursus. Quisque a semper ante. Pellentesque mattis neque tortor, sit amet accumsan nisi euismod eu. Duis accumsan lorem a auctor molestie. Quisque viverra dapibus est id sollicitudin. Suspendisse eu ex vitae ante sagittis gravida tempus ut justo. Integer tincidunt, ex sit amet condimentum mattis, ligula arcu venenatis ex, id consectetur augue lectus sit amet elit. Cras luctus purus quis magna efficitur pharetra.\n" +
                "        Maecenas pulvinar rutrum nulla at venenatis. Nam eu felis vitae nisi vestibulum dignissim. Vivamus tempor lorem nisi, in ullamcorper nisl mollis eu. Fusce a placerat arcu. Donec dictum ultrices elit, a condimentum quam imperdiet eu. Phasellus eu nulla neque. Integer laoreet, justo vitae vehicula varius, nibh metus ullamcorper lectus, nec fringilla urna ante vitae massa. Integer a nibh nec dui sodales semper. Nunc ipsum dolor, condimentum nec felis a, bibendum posuere velit. Curabitur nec dignissim ipsum.\n" +
                "        Cras sed diam id dui scelerisque luctus. Nullam facilisis porta massa. Suspendisse porta blandit erat sit amet lobortis. Pellentesque vel ultricies eros. Ut lectus nibh, viverra quis nulla in, malesuada congue ipsum. Mauris pretium ante et magna placerat efficitur tempor sed felis. Vestibulum efficitur mattis vestibulum. Vestibulum ullamcorper laoreet nisl sit amet imperdiet. Morbi eu ex quis quam sodales convallis. Nunc pharetra at mauris et vestibulum. Donec consequat nec nibh eget elementum. Fusce consequat hendrerit diam. Mauris bibendum, mauris vel auctor condimentum, ex odio ornare urna, at ultrices odio ante eget orci. Vivamus non quam aliquet nunc elementum facilisis. Curabitur vitae viverra erat.";


        //Implementar o View renderer
        AbstractViewRenderer page = new AbstractViewRenderer(context, R.layout.activity_cartao_vacinas) {
            //private String _text;

            //public void setText(String text) {
            //    _text = text;
            //}

            @Override
            protected void initView(View view) {
                //TextView tv_hello = (TextView)view.findViewById(R.id.tv_hello);
               // tv_hello.setText(texto);
                ImageView imagem = (ImageView) view.findViewById(R.id.imagem);
                imagem.setImageResource(R.drawable.ic_layout_cartao_pdf);
            }

        };

        // pode reutilizar o bitmap se for preciso
        page.setReuseBitmap(true);
        // Construção documento PDF
        PdfDocument doc = new PdfDocument(context);
        // adicione quantas páginas forem necessárias
        doc.addPage(page);
        doc.setRenderWidth(8268);
        doc.setRenderHeight(11693);
        doc.setOrientation(PdfDocument.A4_MODE.LANDSCAPE);
        doc.setProgressTitle(R.string.app_name);
        doc.setProgressMessage(R.string.gera_pdf);
        doc.setFileName(EXPORT_PDF_FILE_NAME);
        doc.setSaveDirectory(EXPORT_PDF_PATH);//context.getExternalFilesDir(null));
        doc.setInflateOnMainThread(false);
        doc.setListener(new PdfDocument.Callback() {
            @Override
            public void onComplete(File file) {
                Log.e(PdfDocument.TAG_PDF_MY_XML, "Completo");

                new AlertDialog.Builder(CartaoDetalheActivity.this)
                        .setIcon(R.drawable.ic_launcher_round)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage("Cartão Vacinal exportado para PDF com sucesso!")
                        .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                exibePDF(EXPORT_PDF_PATH + "/" + EXPORT_PDF_FILE_NAME);
                            }
                        })
                        .setCancelable(false)
                        .show();
            }

            @Override
            public void onError(Exception e) {
                Log.e(PdfDocument.TAG_PDF_MY_XML, "Erro");
                e.printStackTrace();
            }
        });

        Toast.makeText(context,  "Salvando PDF em: " + EXPORT_PDF_PATH + "/" + EXPORT_PDF_FILE_NAME+".pdf", Toast.LENGTH_LONG).show();

        doc.createPdf(context);


        // OU

        /*
        new PdfDocument.Builder(context).addPage(page).orientation(PdfDocument.A4_MODE.LANDSCAPE)
                .progressMessage(R.string.app_name).progressTitle(R.string.gera_pdf)
                .renderWidth(8031).renderHeight(11614)
                .saveDirectory(EXPORT_PDF_PATH).filename(EXPORT_PDF_FILE_NAME+2)
                .listener(new PdfDocument.Callback() {
                    @Override
                    public void onComplete(File file) {
                        Log.i(PdfDocument.TAG_PDF_MY_XML, "Completo");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i(PdfDocument.TAG_PDF_MY_XML, "Erro");
                    }
                }).create().createPdf(this);
       */

    }

    public void exibePDF(String local) {

        File arquivo = new File(local+ ".pdf");
        //Toast.makeText(getApplicationContext(), file.toString() , Toast.LENGTH_LONG).show();
        if(arquivo.exists()) {
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(arquivo), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Abrir arquivo");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruir instalar um leitor de PDF
                Toast.makeText(getApplicationContext(), "Instale um leitor de PDF!" , Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(getApplicationContext(), "Não foi possível exibir o documento." , Toast.LENGTH_LONG).show();
    }
}
