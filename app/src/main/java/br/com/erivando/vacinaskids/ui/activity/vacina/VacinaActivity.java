package br.com.erivando.vacinaskids.ui.activity.vacina;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.activity.main.MainActivity;
import br.com.erivando.vacinaskids.ui.fragment.vacina.VacinaPrivadaFragment;
import br.com.erivando.vacinaskids.ui.fragment.vacina.VacinaPublicaFragment;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 10:55
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class VacinaActivity extends BaseActivity implements VacinaMvpView {

    @Inject
    VacinaMvpPresenter<VacinaMvpView> presenter;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, VacinaActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacina);

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
        collapsingToolbar.setTitle(getResources().getString(R.string.text_vacinas_titulo));

        getActivityComponent().inject(this);

//        setUnBinder(ButterKnife.bind(this));

        presenter.onAttach(this);

        initViews();

        setUp();
    }

    private void initViews() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void setUp() {
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openMainActivity();
    }

    public void openMainActivity() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new VacinaPublicaFragment(), "Pública");
        adapter.addFragment(new VacinaPrivadaFragment(), "Privada");
        viewPager.setAdapter(adapter);
    }

    @Override
    public Context getContextActivity() {
        return VacinaActivity.this;
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitulos = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitulos.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitulos.get(position);
        }
    }
}
