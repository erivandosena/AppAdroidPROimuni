package br.com.erivando.vacinaskids.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.BuildConfig;
import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.custom.imagem.RoundedImageView;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.login.LoginActivity;
import br.com.erivando.vacinaskids.ui.sobre.SobreFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_view)
    DrawerLayout drawer;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.text_versao_app)
    TextView versaoAppTextView;

   // @BindView(R.id.cards_container)
   // SwipePlaceHolderView cardsContainerView;

    private TextView nomeTextView;

    private TextView emailTextView;

    private RoundedImageView perfilImageView;

    private ActionBarDrawerToggle drawerToggle;

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

        setUp();
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
        presenter.onViewInitialized();
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
            case R.id.action_cut:
                return true;
            case R.id.action_copy:
                return true;
            case R.id.action_share:
                return true;
            case R.id.action_delete:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                            case R.id.nav_item_feed:
                                presenter.onDrawerMyFeedClick();
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
    public void openLoginActivity() {
          startActivity(LoginActivity.getStartIntent(this));
          finish();
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
    public void updateUserName(String currentUserName) {
        nomeTextView.setText(currentUserName);
    }

    @Override
    public void updateUserEmail(String currentUserEmail) {
        emailTextView.setText(currentUserEmail);
    }

    @Override
    public void updateUserProfilePic(String currentUserProfilePicUrl) {
        //carregar URL de imagem do perfil em ANImageView
    }

    @Override
    public void updateAppVersion() {
        String version = getString(R.string.versao_app) + " " + BuildConfig.VERSION_NAME;
        versaoAppTextView.setText(version);
    }

    @Override
    public void showRateUsDialog() {
         /// RateUsDialog.newInstance().show(getSupportFragmentManager());
    }

    @Override
    public void openMyFeedActivity() {
        //   startActivity(FeedActivity.getStartIntent(this));
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

}
