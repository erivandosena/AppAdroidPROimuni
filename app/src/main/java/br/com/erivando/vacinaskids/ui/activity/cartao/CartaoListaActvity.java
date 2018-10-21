package br.com.erivando.vacinaskids.ui.activity.cartao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.model.Cartao;
import br.com.erivando.vacinaskids.database.model.Imunizacao;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.activity.imunizacao.ImunizacaoActivity;
import br.com.erivando.vacinaskids.ui.adapter.CartaoAdapter;
import br.com.erivando.vacinaskids.ui.activity.main.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.vacinaskids.util.Uteis.habilitaTelaCheia;

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

    @BindView(R.id.fab)
    FloatingActionButton fabFloatingActionButton;

    @BindView(R.id.lista_cartoes)
    ListView cartoesListView;

    private Intent intent;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CartaoListaActvity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartao_lista);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        presenterCartao.onAttach(this);

        intent = getIntent();

        getCartao();

        cartoesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(intent != null) {
                    String acao = intent.getStringExtra("cartaoLista");
                    Intent intencao = null;
                    if ("cartao".equals(acao)) {
                        intencao = CartaoDetalheActivity.getStartIntent(CartaoListaActvity.this);
                        intencao.putExtra("cartao", ((Cartao)parent.getAdapter().getItem(position)).getId());
                    }
                    if ("edita".equals(acao)) {
                        intencao = CartaoActivity.getStartIntent(CartaoListaActvity.this);
                        intencao.putExtra("cartao", ((Cartao)parent.getAdapter().getItem(position)).getId());
                    }
                    if (intencao != null) {
                        startActivity(intencao);
                        finish();
                    }
                }
            }
        });

        setUp();
    }

    @Override
    protected void setUp() {
        habilitaTelaCheia(this);
    }

    @OnClick(R.id.btn_nav_voltar)
    public void onMainActivity() {
        this.onBackPressed();
    }

    @OnClick(R.id.fab)
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
            ListView lvCartoes = findViewById(R.id.lista_cartoes);
            CartaoAdapter adapter = new CartaoAdapter(this);
            lvCartoes.setAdapter(adapter);
            for (Cartao cartao : cartoes) {
                adapter.add(cartao);
            }
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
}
