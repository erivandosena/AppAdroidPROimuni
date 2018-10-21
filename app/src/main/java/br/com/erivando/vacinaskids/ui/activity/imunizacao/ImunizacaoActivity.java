package br.com.erivando.vacinaskids.ui.activity.imunizacao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.activity.cartao.CartaoDetalheActivity;
import br.com.erivando.vacinaskids.ui.activity.cartao.CartaoListaActvity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.vacinaskids.util.Uteis.habilitaTelaCheia;

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

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ImunizacaoActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imunizacao_detalhe);

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
        collapsingToolbar.setTitle(getResources().getString(R.string.text_imunizacao_titulo));

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        presenter.onAttach(this);

        setUp();
    }

    @Override
    protected void setUp() {

    }

    @OnClick(R.id.btn_registra_imunizacao)
    public void onRegistraImunizacao() {

    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openCartaoDetalheActivity("cartao");
    }

    public void openCartaoDetalheActivity(String acao) {
        Intent intent = CartaoDetalheActivity.getStartIntent(this);
        intent.putExtra("cartaoLista", acao);
        startActivity(intent);
        finish();
    }

}
