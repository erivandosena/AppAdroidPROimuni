package br.com.erivando.proimuni.ui.fragment.sobre;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import br.com.erivando.proimuni.BuildConfig;
import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.mvp.base.BaseActivity;
import br.com.erivando.proimuni.ui.activity.main.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   22 de Julho de 2018 as 11:57
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class Sobre extends BaseActivity implements SobreMvpView {

    @Inject
    SobreMvpPresenter<SobreMvpView> presenter;

    @BindView(R.id.text_sobre_versao_app)
    TextView versaoAppSobreTextView;

    @BindView(R.id.text_titulo_toobar)
    TextView textViewTituloToobar;

    //@BindView(R.id.toolbar_sobre)
    //Toolbar toolbar;

    //@BindView(R.id.collapsing_toolbar_sobre)
    //CollapsingToolbarLayout collapsingToolbar;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, Sobre.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sobre);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        presenter.onAttach(this);

        textViewTituloToobar.setText(getResources().getString(R.string.text_sobre_titulo));

        /*
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbar.setTitle(getResources().getString(R.string.text_sobre_titulo));
        */
    }

    @Override
    protected void setUp() {
    }

    @OnClick(R.id.btn_nav_voltar)
    public void onClickVoltar(View v) {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.openMainActivity();
    }

    @Override
    public void updateAppVersion() {
        String version = getString(R.string.versao_app) + ": " + BuildConfig.VERSION_NAME;
        versaoAppSobreTextView.setText(version);
    }

    @Override
    public Context getContextActivity() {
        return this;
    }

    public void openMainActivity() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }

}
