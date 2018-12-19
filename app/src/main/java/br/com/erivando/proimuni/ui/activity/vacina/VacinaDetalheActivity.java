package br.com.erivando.proimuni.ui.activity.vacina;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import javax.inject.Inject;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.mvp.base.BaseActivity;
import br.com.erivando.proimuni.ui.activity.imunizacao.ImunizacaoActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   16 de Outubro de 2018 as 17:12
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class VacinaDetalheActivity extends BaseActivity implements VacinaMvpView {

    public static final String EXTRA_NOME = "vacina_nome";
    public static final String EXTRA_DESC = "vacina_descricao";
    public static final String EXTRA_ADMIN = "vacina_admin";

    @BindView(R.id.text_vacina_descricao)
    public TextView mTexVacinaDescricao;

    @BindView(R.id.text_vacina_administracao)
    public TextView mTexVacinaAdministracao;

    @BindView(R.id.fab_vacina_share)
    public FloatingActionButton floatingActionButtonVacinaShare;

    @Inject
    VacinaMvpPresenter<VacinaMvpView> vacinaPresenter;

    @BindView(R.id.text_titulo_toobar)
    TextView textViewTituloToobar;

    private String vacinaNome;
    private String vacinaDesc;
    private String vacinaAdmin;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ImunizacaoActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacina_detalhe);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        vacinaPresenter.onAttach(this);

        Intent intent = getIntent();
        vacinaNome = intent.getStringExtra(EXTRA_NOME);
        vacinaDesc = intent.getStringExtra(EXTRA_DESC);
        vacinaAdmin = intent.getStringExtra(EXTRA_ADMIN);

        /*
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                onBackPressed();
            }
        });

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(vacinaNome);
        */
        textViewTituloToobar.setText(vacinaNome);

        mTexVacinaDescricao.setText(vacinaDesc);
        mTexVacinaAdministracao.setText(vacinaAdmin);

        loadBackdrop();

        setUp();
    }

    @OnClick(R.id.fab_vacina_share)
    public void onClick(View view) {
        if (view == floatingActionButtonVacinaShare) {
            Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                    .setType("text/html")
                    .setSubject(getResources().getString(R.string.app_name))
                    .setChooserTitle(getResources().getString(R.string.app_name))
                    .setText(vacinaNome.toUpperCase() + "\n\n" +
                            vacinaDesc + "\n\n" +
                            vacinaAdmin + "\n\n" + getResources().getString(R.string.app_name) + "\n" + getResources().getString(R.string.app_link_download) + "\n\n♥ " + getResources().getString(R.string.app_slogan))
                    .createChooserIntent()
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (shareIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(shareIntent);
            }
        }
    }

    @Override
    protected void setUp() {
    }

    @OnClick(R.id.btn_nav_voltar)
    public void onClickVoltar(View v) {
        onBackPressed();
    }

    @Override
    public void onDestroy() {
        vacinaPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openVacinaActivity();
    }

    public void openVacinaActivity() {
        startActivity(VacinaActivity.getStartIntent(this));
        finish();
    }

    private void loadBackdrop() {
        //final ImageView imageView = findViewById(R.id.backdrop);
        //Glide.with(this).load(R.drawable.ic_vacina).apply(RequestOptions.centerCropTransform()).into(imageView);
    }

    @Override
    public Context getContextActivity() {
        return VacinaDetalheActivity.this;
    }
}
