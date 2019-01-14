package br.com.erivando.proimuni.ui.activity.main;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;

import javax.inject.Inject;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.backup.RealmBackupRestore;
import br.com.erivando.proimuni.mvp.base.BaseActivity;
import br.com.erivando.proimuni.ui.activity.calendario.CalendarioActivity;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoListaActvity;
import br.com.erivando.proimuni.ui.activity.configuracao.ConfiguracaoActivity;
import br.com.erivando.proimuni.ui.activity.configuracao.ConfiguracaoMvpPresenter;
import br.com.erivando.proimuni.ui.activity.configuracao.ConfiguracaoMvpView;
import br.com.erivando.proimuni.ui.activity.crianca.CriancaListaActvity;
import br.com.erivando.proimuni.ui.activity.imunizacao.ImunizacaoMvpPresenter;
import br.com.erivando.proimuni.ui.activity.imunizacao.ImunizacaoMvpView;
import br.com.erivando.proimuni.ui.activity.login.LoginActivity;
import br.com.erivando.proimuni.ui.activity.login.LoginMvpPresenter;
import br.com.erivando.proimuni.ui.activity.login.LoginMvpView;
import br.com.erivando.proimuni.ui.activity.mapa.MapaActivity;
import br.com.erivando.proimuni.ui.activity.usuario.CadastroUsuarioActivity;
import br.com.erivando.proimuni.ui.activity.vacina.VacinaActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.proimuni.database.backup.RealmBackupRestore.BACKUP_FILE;
import static br.com.erivando.proimuni.util.Uteis.exibeAvaliacaoDialog;
import static br.com.erivando.proimuni.util.Uteis.getCapitalizeNome;

/**
 * Projeto:     VacinasKIDs
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 10:58
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class MainActivity extends BaseActivity implements MainMvpView {

    @Inject
    MainMvpPresenter<MainMvpView> presenter;

    @Inject
    LoginMvpPresenter<LoginMvpView> loginPresenter;

    @Inject
    ConfiguracaoMvpPresenter<ConfiguracaoMvpView> configuracaoPresenter;

    @BindView(R.id.drawer_view)
    DrawerLayout drawer;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.btn_drawer)
    ImageButton buttonBarDrawerToggle;

    @BindView(R.id.imagem_menu)
    RoundedImageView roundedImageMenu;

    @BindView(R.id.text_menu)
    TextView textViewMenu;

    private TextView nomeTextView;

    private TextView emailTextView;

    private RoundedImageView perfilImageView;

    private ImageButton buttonBarDrawerToggleClose;

    private RealmBackupRestore backupRestore;

    private Handler handler;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_main);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        presenter.onAttach(this);
        setupNavMenu();
        presenter.onNavMenuCreated();
        setUp();
        hideLoading();
    }

    @OnClick(R.id.btn_cartao)
    public void onCartaoVacinal() {
        openCartaoListaActivity("cartao");
    }

    @OnClick(R.id.btn_vacina)
    public void onVacina() {
        openVacinaActivity();
    }

    @OnClick(R.id.btn_calendario)
    public void onCalendarioVacinal() {
        openCalendarioVacinal();
    }

    @OnClick(R.id.btn_posto)
    public void onMapa() {
        openMapaPostosVacinacao();
    }

    @Override
    protected void setUp() {
        onInicializacoes();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Drawable drawable = item.getIcon();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
        switch (item.getItemId()) {
            case R.id.nav_item_cartao:
                onCartaoVacinal();
                return true;
            case R.id.nav_item_vacina:
                onVacina();
                return true;
            case R.id.nav_item_calendario:
                onCalendarioVacinal();
                return true;
            case R.id.nav_item_postos:
                onMapa();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginPresenter.getGoogleSignInClient().getInstanceId();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (drawer != null)
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void setupNavMenu() {
        View headerLayout = navigationView.getHeaderView(0);

        perfilImageView = headerLayout.findViewById(R.id.img_imagem_perfil);
        nomeTextView = headerLayout.findViewById(R.id.text_nome_perfil);
        emailTextView = headerLayout.findViewById(R.id.text_email_perfil);

        buttonBarDrawerToggleClose = headerLayout.findViewById(R.id.btn_drawer_close);
        buttonBarDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawer.isDrawerOpen(GravityCompat.START))
                    drawer.openDrawer(Gravity.START);
                else
                    drawer.closeDrawer(Gravity.END);
            }
        });

        buttonBarDrawerToggleClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawer.isDrawerOpen(Gravity.END))
                    drawer.closeDrawer(Gravity.START);
                else
                    drawer.closeDrawer(Gravity.END);

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.nav_item_usuario:
                        openCadastroUsuarioActivity();
                        return true;
                    case R.id.nav_item_crianca:
                        openCriancaListaActivity("edita");
                        return true;
                    case R.id.nav_item_backup:
                        executaBackup();
                        return true;
                    case R.id.nav_item_restauracao:
                        executaRestauracao();
                        return true;
                    case R.id.nav_item_configuracao:
                        openConfiguracoesActivity();
                        return true;
                    case R.id.nav_item_compartilhar:
                        onCompartilhaApp();
                        return true;
                    case R.id.nav_item_avaliacao:
                        presenter.onDrawerRateUsClick();
                        return true;
                        /*
                    case R.id.nav_item_curiosidade:
                        Toast.makeText(AppAplicacao.contextApp, getResources().getString(R.string.menu_curiosidade) + "\n\nAinda não implementado! :(\n", Toast.LENGTH_SHORT).show();
                        return true;
                        */
                    case R.id.nav_item_sobre:
                        presenter.onDrawerOptionAboutClick();
                        return true;
                    case R.id.nav_item_logout:
                        presenter.onDrawerOptionLogoutClick();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void onFacebookSignOut() {
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onGooleSignOut() {
        loginPresenter.onGooleSignOut(MainActivity.this);
    }

    @Override
    public void onCompartilhaApp() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/html")
                .setSubject(getResources().getString(R.string.app_name))
                .setChooserTitle(getResources().getString(R.string.menu_compartilhar) + " " + getResources().getString(R.string.app_name))
                .setText("Olá!\n" + getResources().getString(R.string.app_name) + " " + getResources().getString(R.string.app_mensagem_indicacao) + "\n" + getResources().getString(R.string.app_link_download) + "\n") //\n♥ " + getResources().getString(R.string.app_slogan))
                .createChooserIntent()
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }

    @Override
    public void onInicializacoes() {
        handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(!presenter.onConfigNotificacoes())
                    configuracaoPresenter.iniciaServicoNotificacao(MainActivity.this);
            }
        });
    }

    public void showMapaActivity() {
        if (isNetworkConnected()) {
            if (isNetworkConnected()) {
                startActivity(MapaActivity.getStartIntent(this));
                finish();
            }
        }
    }

    @Override
    public void openLoginActivity() {
        startActivity(LoginActivity.getStartIntent(this));
        finish();
    }

    @Override
    public void openVacinaActivity() {
        startActivity(VacinaActivity.getStartIntent(this));
        finish();
    }

    @Override
    public void openCadastroUsuarioActivity() {
        startActivity(CadastroUsuarioActivity.getStartIntent(this));
        finish();
    }

    @Override
    public void openCalendarioVacinal() {
        startActivity(CalendarioActivity.getStartIntent(this));
        finish();
    }

    @Override
    public void openCartaoListaActivity(String acao) {
        Intent intent = CartaoListaActvity.getStartIntent(MainActivity.this);
        intent.putExtra("cartaoLista", acao);
        startActivity(intent);
        finish();
    }

    public void openConfiguracoesActivity() {
        Intent intent = ConfiguracaoActivity.getStartIntent(MainActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void openCriancaListaActivity(String acao) {
        Intent intent = CriancaListaActvity.getStartIntent(MainActivity.this);
        if ("edita".equals(acao))
            intent.putExtra("criancaLista", acao);
        if ("cartao".equals(acao))
            intent.putExtra("criancaLista", acao);
        startActivity(intent);
        finish();
    }

    public void openMapaPostosVacinacao() {
        showMapaActivity();
    }

    @Override
    public void updateUserName(String currentUserName) {
        nomeTextView.setText(currentUserName);
        textViewMenu.setText(getCapitalizeNome(currentUserName));
    }

    @Override
    public void updateUserEmail(String currentUserEmail) {
        emailTextView.setText(currentUserEmail);
    }

    @Override
    public void updateUserProfilePic(String currentUserProfilePicUrl) {
        try {
            URL imageURL = new URL(currentUserProfilePicUrl);
            InputStream in = (InputStream) imageURL.getContent();
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            perfilImageView.setImageBitmap(bitmap);
            roundedImageMenu.setImageBitmap(bitmap);
        } catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAppVersion() {
       // String version = getString(R.string.versao_app) + " " + BuildConfig.VERSION_NAME;
       // versaoAppTextView.setText(version);
    }

    @Override
    public void showRateUsDialog() {
        exibeAvaliacaoDialog(this);
    }

    @Override
    public void closeNavigationDrawer() {
        if (drawer != null) {
            drawer.closeDrawer(Gravity.START);
        }
    }

    @Override
    public void lockDrawer() {
        if (drawer != null)
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void unlockDrawer() {
        if (drawer != null)
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void executaBackup() {
        backupRestore = new RealmBackupRestore(MainActivity.this);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setIcon(R.drawable.ic_launcher_round);
        alertDialog.setTitle(getResources().getString(R.string.menu_backup_copia));
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Esta cópia de seguranca substituirá localmente a cópia anterior.\n\nConfirme Sim para continuar ou Não para cancelar.");
        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                backupRestore.backup();

                if (BACKUP_FILE != null && emailTextView.getText() != null) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContextActivity());
                    alertDialog.setIcon(R.drawable.ic_launcher_round);
                    alertDialog.setTitle(getContextActivity().getResources().getString(R.string.titulo_backup_copia_email));
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Para maior segurança, uma cópia dos dados pode ser enviada para outros dispositivos através de Bluetooth ou E-mail.(Recomendado)\n\nConfirme Sim para continuar ou Não para cancelar.");
                    alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            File filelocation = new File(BACKUP_FILE);
                            Uri path = Uri.fromFile(filelocation);
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setType("text/plain");
                            String to[] = {emailTextView.getText().toString()};
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + path));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getContextActivity().getResources().getString(R.string.texto_email_anexo_backup) + " " + getContextActivity().getString(R.string.app_name));
                            //emailIntent.putExtra(Intent.EXTRA_TEXT, getContextActivity().getResources().getString(R.string.texto_email_senha) + " " + getCapitalizeNome(nomeTextView.getText().toString()) + "\n\n" + getContextActivity().getResources().getString(R.string.texto_envio_anexo) + "\n\n© " + Calendar.getInstance().get(Calendar.YEAR) + " " + getContextActivity().getResources().getString(R.string.app_name) + "\n" + getContextActivity().getResources().getString(R.string.app_slogan)+"\n\n");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, getContextActivity().getResources().getString(R.string.texto_envio_anexo) + " " + getCapitalizeNome(nomeTextView.getText().toString()) + "\n\n© " + Calendar.getInstance().get(Calendar.YEAR) + " " + getContextActivity().getResources().getString(R.string.app_name) + "\n\n");
                            startActivity(Intent.createChooser(emailIntent, getContextActivity().getResources().getString(R.string.titulo_backup_copia_email)));
                        }
                    });

                    alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();


                }
            }
        });
        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void executaRestauracao() {
        backupRestore = new RealmBackupRestore(MainActivity.this);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setIcon(R.drawable.ic_launcher_round);
        alertDialog.setTitle(getResources().getString(R.string.menu_backup_restaura));
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Esta restauração substituirá seus dados atuais do aplicativo.\n\nConfirme Sim para continuar ou Não para cancelar.");
        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                showLoading();

                configuracaoPresenter.finalizaServicoNotificacao(MainActivity.this);

                backupRestore.restore();

                hideLoading();
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_launcher_round)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage("É necessário sair do aplicativo para concluir o procedimento de restauração dos dados.")
                        .setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    presenter.onDrawerOptionLogoutClick();
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                } finally {
                                    finish();
                                    System.exit(1);
                                }
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });
        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public Context getContextActivity() {
        return MainActivity.this;
    }

}
