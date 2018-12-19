package br.com.erivando.proimuni.ui.activity.cartao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Cartao;
import br.com.erivando.proimuni.mvp.base.BaseActivity;
import br.com.erivando.proimuni.ui.activity.main.MainActivity;
import br.com.erivando.proimuni.ui.adapter.CartaoRVA;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   11 de Outubro de 2018 as 07:13
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CartaoListaActvity extends BaseActivity implements CartaoMvpView {

    @Inject
    CartaoMvpPresenter<CartaoMvpView> presenterCartao;

    //@BindView(R.id.toolbar_cartao_lista)
    //Toolbar toolbar;

    //@BindView(R.id.collapsing_toolbar_cartao_lista)
    //CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.fab_cartao_add)
    FloatingActionButton fabFloatingActionButton;

    @BindView(R.id.cartao_recyclerView)
    RecyclerView cartao_recycler_view;

    @BindView(R.id.text_titulo_toobar)
    TextView textViewTituloToobar;

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
        /*
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbar.setTitle(getResources().getString(R.string.text_lista_cartao_titulo));
        */

        textViewTituloToobar.setText(getResources().getString(R.string.text_lista_calendario_titulo));

        getActivityComponent().inject(this);
        presenterCartao.onAttach(this);
        intent = getIntent();

        getCartao();

        setUp();
    }

    @Override
    protected void setUp() {
        cartao_recycler_view.setHasFixedSize(true);
        CartaoRVA adapter = new CartaoRVA(listaCartoes, CartaoListaActvity.this, intent);
        cartao_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cartao_recycler_view.setAdapter(adapter);
    }

    @OnClick(R.id.btn_nav_voltar)
    public void onClickVoltar(View v) {
        onBackPressed();
    }

    @OnClick(R.id.fab_cartao_add)
    public void onClick(View view) {
        if (view == fabFloatingActionButton) {
            openCartaoActivity();
        }
    }

    @Override
    public void onDestroy() {
        presenterCartao.onDetach();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openMainActivity();
    }

    private void getCartao() {
        List<Cartao> cartoes = presenterCartao.onCartaoCadastrados();
        if (!cartoes.isEmpty()) {
            listaCartoes = cartoes;
        } else {
            Toast.makeText(this, this.getString(R.string.texto_aviso_cartao_nao_cadastrado), Toast.LENGTH_LONG).show();
        }
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
