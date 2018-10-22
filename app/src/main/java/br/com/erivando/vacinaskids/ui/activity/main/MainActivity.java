package br.com.erivando.vacinaskids.ui.activity.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.BuildConfig;
import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.custom.imagem.RoundedImageView;
import br.com.erivando.vacinaskids.database.backup.RealmBackupRestore;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.activity.calendario.CalendarioActivity;
import br.com.erivando.vacinaskids.ui.activity.cartao.CartaoListaActvity;
import br.com.erivando.vacinaskids.ui.activity.crianca.CriancaListaActvity;
import br.com.erivando.vacinaskids.ui.activity.login.LoginActivity;
import br.com.erivando.vacinaskids.ui.activity.login.LoginMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.login.LoginMvpView;
import br.com.erivando.vacinaskids.ui.activity.usuario.CadastroUsuarioActivity;
import br.com.erivando.vacinaskids.ui.activity.vacina.VacinaActivity;
import br.com.erivando.vacinaskids.ui.fragment.sobre.SobreFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.vacinaskids.util.Uteis.exibeAvaliacaoDialog;

/**
 * Projeto:     VacinasKIDs
 * Autor:       Erivando Sena
 * Data/Hora:   14 de Julho de 2018 as 10:58
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class MainActivity extends BaseActivity implements MainMvpView {

    private static final String TAG = "LoginActivity";

    @Inject
    MainMvpPresenter<MainMvpView> presenter;

    @Inject
    LoginMvpPresenter<LoginMvpView> loginPresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_view)
    DrawerLayout drawer;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.text_versao_app)
    TextView versaoAppTextView;

    @BindView(R.id.btn_calendario_vacinal)
    ImageButton calendarioImageButton;

    @BindView(R.id.btn_cartao_vacinal)
    ImageButton cartaoImageButton;

    @BindView(R.id.btn_crianca)
    ImageButton criancaImageButton;

    @BindView(R.id.btn_vacina)
    ImageButton vacinaImageButton;

    private TextView nomeTextView;

    private TextView emailTextView;

    private RoundedImageView perfilImageView;

    private ActionBarDrawerToggle drawerToggle;

    private RealmBackupRestore backupRestore;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        presenter.onAttach(this);

        backupRestore = new RealmBackupRestore(this);

        setUp();
    }

    @OnClick(R.id.btn_calendario_vacinal)
    public void onCalendarioVacinal() {
        openCalendarioVacinal();
    }

    @OnClick(R.id.btn_cartao_vacinal)
    public void onCartaoVacinal() {
        openCartaoListaActivity("cartao");
    }

    @OnClick(R.id.btn_crianca)
    public void onCrianca() {
        openCriancaListaActivity("edita");
    }

    @OnClick(R.id.btn_vacina)
    public void onVacina() {
        openVacinaActivity();
    }

    @Override
    protected void setUp() {
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        setupNavMenu();
        presenter.onNavMenuCreated();
        setupCardContainerView();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(SobreFragment.TAG);
        if (fragment == null) {
            super.onBackPressed();
        } else {
              onFragmentDetached(SobreFragment.TAG);
        }
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
            case R.id.action_edita_usuario:
                openCadastroUsuarioActivity();
                return true;
            case R.id.action_edita_crianca:
                openCriancaListaActivity("edita");
                return true;
            case R.id.action_edita_cartao:
                openEditaCartaoActivity("edita");
                return true;
            case R.id.action_postos:
                return true;
            case R.id.action_backup:
                executaBackup();
                return true;
            case R.id.action_restore:
                executaRestauracao();
                return true;
            case R.id.action_share:
                onCompartilhaApp();
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

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        drawer.closeDrawer(GravityCompat.START);
                        switch (item.getItemId()) {
                            case R.id.nav_item_about:
                                presenter.onDrawerOptionAboutClick();
                                return true;
                            case R.id.nav_item_rate_us:
                                presenter.onDrawerRateUsClick();
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
    public void setupCardContainerView() {

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
                .setChooserTitle(getResources().getString(R.string.app_name))
                .setText("Olá!\n"+getResources().getString(R.string.app_name)+" "+getResources().getString(R.string.app_mensagem_indicacao) + "\n"+ getResources().getString(R.string.app_link_download) +"\n\n♥ "+getResources().getString(R.string.app_slogan))
                .createChooserIntent()
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }

    @Override
    public void showAboutFragment() {
        lockDrawer();
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.cl_root_view, SobreFragment.newInstance(), SobreFragment.TAG)
                .commit();
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

    // @Override
    public void openEditaCartaoActivity(String acao) {
        Intent intent = CartaoListaActvity.getStartIntent(MainActivity.this);
        intent.putExtra("cartaoLista", acao);
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

    @Override
    public void updateUserName(String currentUserName) {
        nomeTextView.setText(currentUserName);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAppVersion() {
        String version = getString(R.string.versao_app) + " " + BuildConfig.VERSION_NAME;
        versaoAppTextView.setText(version);
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

    @Override
    public void onFragmentAttached() {
    }

    @Override
    public void onFragmentDetached(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .remove(fragment)
                    .commitNow();
            unlockDrawer();
        }
    }

    private void executaBackup() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setIcon(R.drawable.ic_launcher_round);
        alertDialog.setTitle(getResources().getString(R.string.app_name)+ " | Copiar");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Esta cópia de seguranca substituirá a cópia anterior.\n\nConfirme Sim para continuar ou Não para cancelar.");
        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                backupRestore.backup();
            }
        });
        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void executaRestauracao(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setIcon(R.drawable.ic_launcher_round);
        alertDialog.setTitle(getResources().getString(R.string.app_name)+ " | Restaurar");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Esta restauração substituirá seus dados atuais do aplicativo.\n\nConfirme Sim para continuar ou Não para cancelar.");
        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                backupRestore.restore();
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_launcher_round)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage("O aplicativo será fechado para concluir o procedimento de restauração dos dados.")
                        .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    finish();
                                    System.exit(1);
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
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
