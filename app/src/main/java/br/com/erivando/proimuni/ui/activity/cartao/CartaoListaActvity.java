package br.com.erivando.proimuni.ui.activity.cartao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Cartao;
import br.com.erivando.proimuni.mvp.base.BaseActivity;
import br.com.erivando.proimuni.ui.activity.crianca.CriancaActivity;
import br.com.erivando.proimuni.ui.activity.crianca.CriancaListaActvity;
import br.com.erivando.proimuni.ui.activity.crianca.CriancaMvpPresenter;
import br.com.erivando.proimuni.ui.activity.crianca.CriancaMvpView;
import br.com.erivando.proimuni.ui.activity.main.MainActivity;
import br.com.erivando.proimuni.ui.adapter.CartaoRVA;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.proimuni.util.Uteis.resizeCustomizedToobar;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   11 de Outubro de 2018 as 07:13
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CartaoListaActvity extends BaseActivity implements CartaoMvpView {

    @Inject
    CartaoMvpPresenter<CartaoMvpView> presenter;

    @Inject
    CriancaMvpPresenter<CriancaMvpView> presenterCrianca;

    @BindView(R.id.fab_crianca_add)
    FloatingActionButton fabFloatingActionButton;

    @BindView(R.id.cartao_recyclerView)
    RecyclerView cartao_recycler_view;

    @BindView(R.id.text_titulo_toobar)
    TextView textViewTituloToobar;

    @BindView(R.id.layout_toobar)
    LinearLayout linearLayoutToobar;

    private List<Cartao> listaCartoes;

    private Intent intent;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CartaoListaActvity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartao_lista);
        setUnBinder(ButterKnife.bind(this));
        textViewTituloToobar.setText(getResources().getString(R.string.text_lista_cartao_titulo));
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        intent = getIntent();

        getCartao();

        setUp();
    }

    @Override
    protected void setUp() {
        CartaoRVA adapter = new CartaoRVA(listaCartoes, CartaoListaActvity.this, intent);
        cartao_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cartao_recycler_view.setAdapter(adapter);
        cartao_recycler_view.setHasFixedSize(true);

        resizeCustomizedToobar(linearLayoutToobar);
    }

    @OnClick(R.id.fab_crianca_add)
    public void onClick(View view) {
        if (view == fabFloatingActionButton) {
            openCriancaActivity();
        }
    }

    @OnClick(R.id.btn_nav_voltar)
    public void onClickVoltar(View v) {
        onBackPressed();
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

    private void getCartao() {
        List<Cartao> cartoes = presenter.onCartaoCadastrados();
        if (!cartoes.isEmpty()) {
            listaCartoes = cartoes;
        } else {
            Toast.makeText(this, this.getString(R.string.texto_aviso_cartao_nao_cadastrado), Toast.LENGTH_LONG).show();
            openCriancaListaActivity();
        }
    }

    public void openCriancaListaActivity() {
        startActivity(CriancaListaActvity.getStartIntent(this));
        finish();
    }

    public void openCriancaActivity() {
        Intent intent = CriancaActivity.getStartIntent(CartaoListaActvity.this);
        intent.putExtra("cartaoLista", "edita");
        startActivity(intent);
        finish();
    }

    public void openCartaoActivity() {
        startActivity(CartaoActivity.getStartIntent(this));
        finish();
    }

    public void openMainActivity() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }

    @Override
    public void openCartaoListaActivity(String acao) {
    }

    @Override
    public Context getContextActivity() {
        return CartaoListaActvity.this;
    }
}
