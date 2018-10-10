package br.com.erivando.vacinaskids.ui.acoes.crianca;

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
import br.com.erivando.vacinaskids.database.model.Crianca;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.acoes.calendario.CalendarioActivity;
import br.com.erivando.vacinaskids.ui.adapter.CriancaAdapter;
import br.com.erivando.vacinaskids.ui.main.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.vacinaskids.util.Uteis.habilitaTelaCheia;

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

    @BindView(R.id.fab)
    FloatingActionButton fabFloatingActionButton;

    @BindView(R.id.lista_criancas)
    ListView criancasListView;

    private Intent intent;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CriancaListaActvity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crianca_lista);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        presenterCrianca.onAttach(this);

        intent = getIntent();

        getCrianca();

        criancasListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Callback method to be invoked when an item in this AdapterView has
             * been clicked.
             * <p>
             * Implementers can call getItemAtPosition(position) if they need
             * to access the data associated with the selected item.
             *
             * @param parent   The AdapterView where the click happened.
             * @param view     The view within the AdapterView that was clicked (this
             *                 will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id       The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

              //  Intent intent = new Intent(CriancaListaActvity.this, CriancaActivity.class);
              //  intent.putExtra("crianca", ((Crianca)parent.getAdapter().getItem(position)).getId());

                if(intent != null) {
                    String acao = intent.getStringExtra("criancaLista");
                    Intent intencao = null;
                    if ("edita".equals(acao)) {
                        intencao = CriancaActivity.getStartIntent(CriancaListaActvity.this);
                        intencao.putExtra("crianca", ((Crianca)parent.getAdapter().getItem(position)).getId());
                    }
                    if ("cartao".equals(acao)) {
                        //intencao = CartaoActivity.getStartIntent(CriancaListaActvity.this);
                        intencao = CalendarioActivity.getStartIntent(CriancaListaActvity.this);
                        intencao.putExtra("crianca", ((Crianca)parent.getAdapter().getItem(position)).getId());
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

    @OnClick(R.id.btn_nav_voltar)
    public void onMainActivity() {
        this.onBackPressed();
    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        if (view == fabFloatingActionButton) {
            openCriancaActivity();
        }
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
            ListView lvCriancas = (ListView) findViewById(R.id.lista_criancas);
            CriancaAdapter adapter = new CriancaAdapter(this);
            lvCriancas.setAdapter(adapter);
            for (Crianca crianca: criancas) {
                adapter.add(crianca);
            }
        }
        else {
            Toast.makeText(this, this.getString(R.string.texto_aviso_crianca_nao_cadastrada), Toast.LENGTH_LONG).show();
            openCriancaActivity();
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
    protected void setUp() {
        habilitaTelaCheia(this);
    }

    @Override
    public void getStartActivityForResult(Intent intentImagem, int requestImgCamera) {

    }
}
