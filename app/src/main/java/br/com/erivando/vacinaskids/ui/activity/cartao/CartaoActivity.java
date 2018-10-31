package br.com.erivando.vacinaskids.ui.activity.cartao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.model.Cartao;
import br.com.erivando.vacinaskids.database.model.Crianca;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.activity.crianca.CriancaListaActvity;
import br.com.erivando.vacinaskids.ui.activity.crianca.CriancaMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.crianca.CriancaMvpView;
import br.com.erivando.vacinaskids.ui.activity.main.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.vacinaskids.util.Uteis.habilitaTelaCheia;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   09 de Agosto de 2018 as 10:51
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CartaoActivity extends BaseActivity implements CartaoMvpView {

    @Inject
    CartaoMvpPresenter<CartaoMvpView> presenter;

    @Inject
    CriancaMvpPresenter<CriancaMvpView> presenterCrianca;

    @BindView(R.id.toolbar_cartao)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar_cartao)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.crianca)
    Spinner comboCartao;

    private Intent intent;
    private Cartao cartao;
    private List<Crianca> criancas;
    private Long id;
    private ArrayAdapter<Crianca> adapterCartao;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CartaoActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartao);
        setUnBinder(ButterKnife.bind(this));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbar.setTitle(getResources().getString(R.string.text_cartao_titulo));

        getActivityComponent().inject(this);
        presenter.onAttach(this);

        id = 0L;

        cartao = new Cartao();

        intent = getIntent();

        getCrianca();

        setUp();
    }

    @OnClick(R.id.btn_cadadastar_cartao)
    public void onCadasrarClick(View v) {
        Long idCrianca = criancas.get(comboCartao.getSelectedItemPosition()).getId();
        cartao = presenter.onCartaoCadastradoPorCrianca(idCrianca);
        if (cartao == null) {
            presenter.onCadasrarClick(id, idCrianca);
        } else {
            if (cartao.getCrianca().getId() == idCrianca)
                Toast.makeText(this, this.getString(R.string.texto_aviso_crianca_cartao_cadastrada), Toast.LENGTH_LONG).show();
            else
                presenter.onCadasrarClick(cartao.getId(), idCrianca);
        }
    }

    @Override
    protected void setUp() {
        if (intent != null) {
            id = intent.getLongExtra("cartao", 0L);
            cartao = presenter.onCartaoCadastrado(id);
            if (cartao != null) {
                comboCartao.setSelection(adapterCartao.getPosition(cartao.getCrianca()));
            }
        }
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.openCartaoListaActivity("edita");
    }

    public void openMainActivity() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }

    private void getCrianca() {
        criancas = presenterCrianca.onCriancaCadastrada();
        if (!criancas.isEmpty()) {
            adapterCartao = new ArrayAdapter<Crianca>(this, android.R.layout.simple_spinner_item, criancas);
            adapterCartao.setDropDownViewResource(android.R.layout.simple_spinner_item);
            comboCartao.setAdapter(adapterCartao);
        } else {
            Toast.makeText(this, this.getString(R.string.texto_aviso_crianca_nao_cadastrada), Toast.LENGTH_LONG).show();
            openCriancaActivity();
        }
    }

    public void openCriancaActivity() {
        startActivity(CriancaListaActvity.getStartIntent(this));
        finish();
    }

    @Override
    public void openCartaoListaActivity(String acao) {
        Intent intent = CartaoListaActvity.getStartIntent(CartaoActivity.this);
        if ("edita".equals(acao))
            intent.putExtra("cartaoLista", acao);
        startActivity(intent);
        finish();
    }

    @Override
    public Context getContextActivity() {
        return CartaoActivity.this;
    }
}
