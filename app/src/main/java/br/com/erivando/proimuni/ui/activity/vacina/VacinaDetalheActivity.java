package br.com.erivando.proimuni.ui.activity.vacina;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.mvp.base.BaseActivity;
import br.com.erivando.proimuni.ui.activity.calendario.CalendarioActivity;
import br.com.erivando.proimuni.ui.activity.imunizacao.ImunizacaoActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.proimuni.util.Uteis.resizeCustomizedToobar;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   16 de Outubro de 2018 as 17:12
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class VacinaDetalheActivity extends BaseActivity implements VacinaMvpView {

    public static final String EXTRA_NOME = "vacina_nome";
    public static final String EXTRA_REDE = "vacina_rede";
    public static final String EXTRA_DESC = "vacina_descricao";
    public static final String EXTRA_ADMIN = "vacina_admin";
    public static final String EXTRA_DOSES = "vacina_doses";
    public static final String EXTRA_FAIXA = "vacina_faixa";
    public static final String EXTRA_CONTRAINDICACOES = "vacina_contraindicacoes";
    public static final String EXTRA_FONTE = "vacina_fonte";

    @Inject
    public VacinaMvpPresenter<VacinaMvpView> vacinaPresenter;

    @BindView(R.id.text_vacina_toobar)
    public TextView mTextViewTitulo;

    @BindView(R.id.layout_vacina_toobar)
    public LinearLayout linearLayoutTitulo;

    @BindView(R.id.text_vacina_descricao)
    public TextView mTexVacinaDescricao;

    @BindView(R.id.text_vacina_administracao)
    public TextView mTexVacinaAdministracao;

    @BindView(R.id.text_vacina_doses)
    public TextView mTexVacinaDoses;

    @BindView(R.id.text_vacina_faixa)
    public TextView mTexVacinaFaixa;

    @BindView(R.id.text_vacina_contraindicacoes)
    public TextView mTexVacinaContraindicacoes;

    @BindView(R.id.text_vacina_fonte)
    public TextView mTexVacinaFonte;

    @BindView(R.id.fab_vacina_share)
    public FloatingActionButton floatingActionButtonVacinaShare;

    @BindView(R.id.text_titulo_toobar)
    TextView textViewTituloToobar;

    @BindView(R.id.layout_toobar)
    LinearLayout linearLayoutToobar;

    private String vacinaNome;
    private String vacinaRede;
    private String vacinaDesc;
    private String vacinaAdmin;
    private String vacinaDoses;
    private String vacinaFaixa;
    private String vacinaContraindicacoes;
    private String vacinaFonte;
    private String activityOrigem;

    private Intent intent;

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
        intent = getIntent();
        vacinaNome = intent.getStringExtra(EXTRA_NOME);
        vacinaRede = intent.getStringExtra(EXTRA_REDE);
        vacinaDesc = intent.getStringExtra(EXTRA_DESC);
        vacinaAdmin = intent.getStringExtra(EXTRA_ADMIN);
        vacinaDoses = intent.getStringExtra(EXTRA_DOSES);
        vacinaFaixa = intent.getStringExtra(EXTRA_FAIXA);
        vacinaContraindicacoes = intent.getStringExtra(EXTRA_CONTRAINDICACOES);
        vacinaFonte = intent.getStringExtra(EXTRA_FONTE);

        activityOrigem = intent.getStringExtra("Activity");
        setUp();
    }

    @OnClick(R.id.fab_vacina_share)
    public void onClick(View view) {
        if (view == floatingActionButtonVacinaShare) {
            Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                    .setType("text/html")
                    .setSubject("Sobre vacina "+vacinaNome.toUpperCase())
                    .setChooserTitle("Compartilhar informação de vacina")
                    .setText(vacinaNome.toUpperCase() + "\n\n" +
                            vacinaDesc + "\n\nDoses:\n" +
                            vacinaDoses + "\n\nFaixa etária:\n" +
                            vacinaFaixa + "\n\nLocal de administração:\n" +
                            vacinaAdmin + "\n\nContraindicações:\n" +
                            vacinaContraindicacoes + "\n\nFonte:\n" +
                            vacinaFonte + "\n\n" + getResources().getString(R.string.app_name) + "\n" + getResources().getString(R.string.app_link_download)+"\n\n")
                    .createChooserIntent()
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (shareIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(shareIntent);
            }
        }
    }

    @Override
    protected void setUp() {

        if ("Pública".equalsIgnoreCase(vacinaRede))
            linearLayoutTitulo.setBackgroundColor(getResources().getColor((R.color.colorPink)));

        if ("Privada".equalsIgnoreCase(vacinaRede))
            linearLayoutTitulo.setBackgroundColor(getResources().getColor((R.color.colorPurple)));

        textViewTituloToobar.setText(getResources().getString(R.string.text_vacinas_titulo));
        mTextViewTitulo.setText(vacinaNome);
        mTexVacinaDescricao.setText(vacinaDesc);
        mTexVacinaAdministracao.setText(vacinaAdmin);

        mTexVacinaDoses.setText(vacinaDoses);
        mTexVacinaFaixa.setText(vacinaFaixa);
        mTexVacinaContraindicacoes.setText(vacinaContraindicacoes);
        mTexVacinaFonte.setText(vacinaFonte);

        loadBackdrop();

        resizeCustomizedToobar(linearLayoutToobar);
    }

    @OnClick(R.id.btn_nav_voltar)
    public void onClickVoltar(View v) {
        if (activityOrigem != null)
            openCalendarioActivity();
        else
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

    public void openCalendarioActivity() {
        startActivity(CalendarioActivity.getStartIntent(this));
        finish();
    }

    private void loadBackdrop() {
    }

    @Override
    public Context getContextActivity() {
        return VacinaDetalheActivity.this;
    }
}
