package br.com.erivando.vacinaskids.ui.acoes.cartao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.model.Cartao;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.acoes.crianca.CriancaListaActvity;
import br.com.erivando.vacinaskids.ui.main.MainActivity;
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

public class CartaoActivity extends BaseActivity implements CartaoMvpView{

    @Inject
    CartaoMvpPresenter<CartaoMvpView> presenter;

    private Cartao cartao;
    private Long id;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CartaoActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartao);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        presenter.onAttach(this);

        cartao = new Cartao();

        id = 0L;

        setUp();
    }

    @OnClick(R.id.btn_nav_voltar)
    public void onCriancaListaActivity() {
        this.onBackPressed();
    }

    @Override
    protected void setUp() {
        habilitaTelaCheia(this);

    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.openMainActivity();
    }

    public void openCriancaListaActivity() {
        startActivity(CriancaListaActvity.getStartIntent(this));
        finish();
    }

    public void openMainActivity() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }
}
