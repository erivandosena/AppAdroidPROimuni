package br.com.erivando.proimuni.ui.activity.imunizacao;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Imunizacao;
import br.com.erivando.proimuni.database.model.Vacina;
import br.com.erivando.proimuni.mvp.base.BaseActivity;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoDetalheActivity;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoListaActvity;
import br.com.erivando.proimuni.ui.activity.vacina.VacinaMvpPresenter;
import br.com.erivando.proimuni.ui.activity.vacina.VacinaMvpView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.proimuni.util.Uteis.getMesesDatas;
import static br.com.erivando.proimuni.util.Uteis.resizeCustomizedToobar;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   16 de Outubro de 2018 as 12:49
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class ImunizacaoActivity extends BaseActivity implements ImunizacaoMvpView {

    @Inject
    ImunizacaoMvpPresenter<ImunizacaoMvpView> presenter;
    @Inject
    VacinaMvpPresenter<VacinaMvpView> vacinaPresenter;

    @BindView(R.id.text_nome_dose)
    public TextView mTextDose;

    @BindView(R.id.text_nome_agente)
    public TextView mTextAgente;

    @BindView(R.id.text_nome_unidade)
    public TextView mTextUnidade;

    @BindView(R.id.text_lote_vacina)
    public TextView mTextLote;

    @BindView(R.id.text_data_imunizacao)
    public TextView mTextData;

    @BindView(R.id.btn_registra_imunizacao)
    public Button mRegistrar;

    //@BindView(R.id.text_imunizacao_vacina_descricao)
    //public TextView mTexVacinaDescricao;

   // @BindView(R.id.toolbar)
   // Toolbar toolbar;

    // @BindView(R.id.toolbar_header_view)
    // HeaderView toolbarHeaderView;
    //@BindView(R.id.collapsing_toolbar)
    //CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.text_titulo_toobar)
    TextView textViewTituloToobar;

   // @BindView(R.id.float_header_view)
    //HeaderView floatHeaderView;

    @BindView(R.id.layout_toobar)
    LinearLayout linearLayoutToobar;

    Intent intent;

    private Long idVacina;
    private Long idDose;
    private Long idIdade;
    private Long idCartao;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ImunizacaoActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imunizacao_detalhe);

        setUnBinder(ButterKnife.bind(this));
        /*
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //executar o que quiser no clique da seta esquerda
                onBackPressed();
            }
        });
        collapsingToolbar.setTitle(getResources().getString(R.string.text_imunizacao_titulo));
        //toolbarHeaderView.bindTo("BCG", "Pentavalente");
        */


        getActivityComponent().inject(this);

        presenter.onAttach(this);

        intent = getIntent();

        setUp();

        mTextData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int ano, int mes, int dia) {
                        //mes = mes+1;
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
                        Calendar calendario = Calendar.getInstance();
                        calendario.set(ano, mes, dia);
                        //String data = dia + "/" + mes + "/" + ano;
                        mTextData.setText(dateFormat.format(calendario.getTime()));
                    }
                };
                Calendar calendario = GregorianCalendar.getInstance();
                DatePickerDialog d = new DatePickerDialog(ImunizacaoActivity.this, dpd,
                        calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH));
                d.show();
            }
        });
    }

    @Override
    protected void setUp() {
        if (intent != null) {
            idVacina = intent.getLongExtra("vacina", 0L);
            idDose = intent.getLongExtra("dose", 0L);
            idIdade = intent.getLongExtra("idade", 0L);
            idCartao = intent.getLongExtra("cartao", 0L);

            Vacina vacina = vacinaPresenter.onVacinaCadastrada(idVacina);
            textViewTituloToobar.setText(vacina.getVaciNome());
            presenter.onNomeDosePorId(idDose);

            Imunizacao imunizacao = presenter.onImunizacaoCadastrada(new String[]{"vacina.id", "dose.id", "cartao.id"}, new Long[]{idVacina, idDose, idCartao});

            if (imunizacao != null) {

                List<Imunizacao> imunizacoesRealizadas = presenter.onImunizacoesCartaoCrianca(new String[]{"vacina.id", "dose.id", "cartao.id"}, new Long[]{idVacina, idDose, idCartao});
                Log.e("imunizacoesRealizadas", String.valueOf(imunizacoesRealizadas.size()));

                if(("Menina".equalsIgnoreCase(imunizacao.getCartao().getCrianca().getCriaSexo()) | "Menino".equalsIgnoreCase(imunizacao.getCartao().getCrianca().getCriaSexo())) && "HPV".equalsIgnoreCase(vacina.getVaciNome()) && imunizacoesRealizadas.size() < 2) {

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt","br"));
                    Calendar data = Calendar.getInstance();
                    Date dataAtual = data.getTime();
                    int periodo = getMesesDatas(imunizacao.getImunData(), dataAtual);

                    if(periodo < 6) {

                        new AlertDialog.Builder(this)
                                .setIcon(R.drawable.ic_launcher_round)
                                .setTitle(textViewTituloToobar.getText().toString())
                                .setMessage("\nImunização ainda não necessária!\n\nÉ preciso um prazo mínimo de 6 meses da 1ª dose realizada em "+sdf.format(imunizacao.getImunData())+" há "+ (periodo == 1 ? periodo+" mês." : periodo+" meses."))
                                .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            openCartaoDetalheActivity(idCartao);
                                        } catch (Throwable throwable) {
                                            throwable.printStackTrace();
                                        }
                                    }
                                })
                                .setCancelable(false)
                                .show();

                    }

                } else {
                    avisoPadrao();
                }
            }

        }

        resizeCustomizedToobar(linearLayoutToobar);
    }

    private void avisoPadrao() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher_round)
                .setTitle(textViewTituloToobar.getText().toString())
                .setMessage("\nImunização já registrada!\n")
                .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            openCartaoDetalheActivity(idCartao);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                })
                .setCancelable(false)
                .show();
    }

    @OnClick(R.id.btn_nav_voltar)
    public void onClickVoltar(View v) {
        onBackPressed();
    }

    @OnClick(R.id.btn_registra_imunizacao)
    public void onCadasrarClick(View v) {
        presenter.onCadasrarClick(0L, mTextData.getText().toString(), mTextAgente.getText().toString(), mTextUnidade.getText().toString(), mTextLote.getText().toString(), idVacina, idDose, idIdade, idCartao);
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openCartaoDetalheActivity(idCartao);
    }

    @Override
    public void openCartaoDetalheActivity(Long idLong) {
        Intent intent = CartaoDetalheActivity.getStartIntent(this);
        intent.putExtra("cartao", idLong);
        startActivity(intent);
        finish();
    }

    @Override
    public void openCartaoListaActivity(String acao) {
        Intent intent = CartaoListaActvity.getStartIntent(ImunizacaoActivity.this);
        intent.putExtra("cartaoLista", acao);
        startActivity(intent);
        finish();
    }

    @Override
    public void getNomeDose(String nomeDose) {
        mTextDose.setText(nomeDose);
    }

    @Override
    public Context getContextActivity() {
        return ImunizacaoActivity.this;
    }
}
