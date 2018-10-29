package br.com.erivando.vacinaskids.ui.activity.imunizacao;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.model.Vacina;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.activity.cartao.CartaoDetalheActivity;
import br.com.erivando.vacinaskids.ui.activity.vacina.VacinaMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.vacina.VacinaMvpView;
import br.com.erivando.vacinaskids.ui.application.AppAplicacao;
import br.com.erivando.vacinaskids.util.HeaderView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   16 de Outubro de 2018 as 12:49
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class ImunizacaoActivity extends BaseActivity implements ImunizacaoMvpView{

    @Inject
    ImunizacaoMvpPresenter<ImunizacaoMvpView> presenter;

    @Inject
    VacinaMvpPresenter<VacinaMvpView> vacinaPresenter;

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

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

   // @BindView(R.id.toolbar_header_view)
   // HeaderView toolbarHeaderView;

    @BindView(R.id.float_header_view)
    HeaderView floatHeaderView;

    @BindView(R.id.text_imunizacao_vacina_descricao)
    public TextView mTexVacinaDescricao;

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

        getActivityComponent().inject(this);

        presenter.onAttach(this);

        intent = getIntent();

        setUp();
    }

    @Override
    protected void setUp() {
        if(intent != null) {
            idVacina = intent.getLongExtra("vacina", 0L);
            idDose = intent.getLongExtra("dose", 0L);
            idIdade = intent.getLongExtra("idade", 0L);
            idCartao = intent.getLongExtra("cartao", 0L);

            if(presenter.onImunizacaoCadastrada(new String[]{"vacina.id", "dose.id"}, new Long[]{idVacina, idDose}) != null) {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_launcher_round)
                        .setTitle(AppAplicacao.contextApp.getResources().getString(R.string.app_name))
                        .setMessage("Olá! \nEsta imunização já foi registrada!")
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

            Vacina vacina = vacinaPresenter.onVacinaCadastrada(idVacina);
            floatHeaderView.bindTo(null, vacina.getVaciNome());
            mTexVacinaDescricao.setText(vacina.getVaciDescricao());
        }
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
    public Context getContextActivity() {
        return ImunizacaoActivity.this;
    }
}
