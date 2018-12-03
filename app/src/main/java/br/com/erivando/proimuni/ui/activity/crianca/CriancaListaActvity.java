package br.com.erivando.proimuni.ui.activity.crianca;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Crianca;
import br.com.erivando.proimuni.mvp.base.BaseActivity;
import br.com.erivando.proimuni.ui.activity.main.MainActivity;
import br.com.erivando.proimuni.ui.adapter.CriancaRVA;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   12 de Agosto de 2018 as 15:14
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class CriancaListaActvity extends BaseActivity implements CriancaMvpView {

    @Inject
    CriancaMvpPresenter<CriancaMvpView> presenterCrianca;

    @BindView(R.id.toolbar_crianca_lista)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar_crianca_lista)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.fab)
    FloatingActionButton fabFloatingActionButton;

    @BindView(R.id.crianca_recyclerView)
    RecyclerView crianca_recycler_view;

    private List<Crianca> listaCriancas;

    private Intent intent;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CriancaListaActvity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crianca_lista);
        setUnBinder(ButterKnife.bind(this));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbar.setTitle(getResources().getString(R.string.text_lista_crianca_titulo));

        getActivityComponent().inject(this);

        presenterCrianca.onAttach(this);

        intent = getIntent();

        getCrianca();

        setUp();
    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        if (view == fabFloatingActionButton) {
            openCriancaActivity();
        }
    }

    @Override
    protected void setUp() {
        crianca_recycler_view.setHasFixedSize(true);
        CriancaRVA adapter = new CriancaRVA(listaCriancas, CriancaListaActvity.this, intent);
        crianca_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        crianca_recycler_view.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        presenterCrianca.onDetach();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openMainActivity();
    }

    private void getCrianca() {
        List<Crianca> criancas = presenterCrianca.onCriancaCadastrada();
        if (!criancas.isEmpty()) {
            listaCriancas = criancas;
        } else {
            Toast.makeText(this, this.getString(R.string.texto_aviso_crianca_nao_cadastrada), Toast.LENGTH_LONG).show();
            //openCriancaActivity();
        }
    }

    public void openCriancaActivity() {
        startActivity(CriancaActivity.getStartIntent(this));
        finish();
    }

    @Override
    public void openCriancaListaActivity(String acao) {
    }

    public void openMainActivity() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }

    @Override
    public void getStartActivityForResult(Intent intentImagem, int requestImgCamera) {
    }

    @Override
    public Context getContextActivity() {
        return CriancaListaActvity.this;
    }
}
