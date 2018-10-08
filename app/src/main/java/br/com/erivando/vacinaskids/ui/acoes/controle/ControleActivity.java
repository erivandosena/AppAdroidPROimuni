package br.com.erivando.vacinaskids.ui.acoes.controle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.model.Controle;
import br.com.erivando.vacinaskids.database.model.ControleDataModel;
import br.com.erivando.vacinaskids.database.model.Idade;
import br.com.erivando.vacinaskids.database.model.Vacina;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.acoes.idade.IdadeMvpPresenter;
import br.com.erivando.vacinaskids.ui.acoes.idade.IdadeMvpView;
import br.com.erivando.vacinaskids.ui.acoes.vacina.VacinaMvpPresenter;
import br.com.erivando.vacinaskids.ui.acoes.vacina.VacinaMvpView;
import br.com.erivando.vacinaskids.ui.adapter.AdapterControle;
import br.com.erivando.vacinaskids.ui.adapter.RecyclerViewDataAdapter;
import br.com.erivando.vacinaskids.ui.main.MainActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.erivando.vacinaskids.util.Uteis.habilitaTelaCheia;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   17 de Agosto de 2018 as 00:48
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class ControleActivity extends BaseActivity implements ControleMvpView {

    @Inject
    ControleMvpPresenter<ControleMvpView> presenter;

    @Inject
    VacinaMvpPresenter<VacinaMvpView> vacinaPresenter;

    @Inject
    IdadeMvpPresenter<IdadeMvpView> idadePresenter;

    private String[][] SPACESHIPS;


    String[] spaceProbeHeaders;
    String[][] spaceProbes;

    List<Idade> idades;

    List<Vacina> vacinas;

    List<Controle> controles;

    private ArrayList<ControleDataModel> allSampleData;

    private RecyclerView recycler;
    private RecyclerView.LayoutManager manager;
    private AdapterControle adapter;
    private List<String> list;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ControleActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        presenter.onAttach(this);

        // populaTabela();

        setUp();

        //setContentView(new TabelaLayout(this, idades, vacinas));
    }

    @OnClick(R.id.btn_nav_voltar)
    public void onClickVoltar() {
        this.onBackPressed();
    }

    @Override
    protected void setUp() {
        habilitaTelaCheia(this);

        idades = idadePresenter.onIdadesCadastradas();
        vacinas = vacinaPresenter.onVacinasCadastradas();
        controles = presenter.onControlesCadastrados();

        allSampleData = new ArrayList<ControleDataModel>();

        createDummyData();

        RecyclerView my_recycler_view = (RecyclerView) findViewById(R.id.recyclerView);
        my_recycler_view.setHasFixedSize(true);
        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(this, allSampleData);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(adapter);
    }

    public void createDummyData() {
        idades = idadePresenter.onIdadesCadastradas();
        controles = presenter.onControlesCadastrados();

        for (Idade idade : idades) {
            ControleDataModel dm = new ControleDataModel();
            dm.setHeaderTitulo(idade.getIdadDescricao());
            ArrayList<Vacina> singleItem = new ArrayList<Vacina>();
            for (Controle controle : controles) {
                if (controle.getIdade().getId() == idade.getId())
                    singleItem.add(controle.getVacina());
            }
            dm.setItensInSection(singleItem);
            allSampleData.add(dm);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.openMainActivity();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    public void openMainActivity() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }

    private void populaTabela() {
        Controle spaceprobe = new Controle();
        List<Controle> spaceprobeList = presenter.onControlesCadastrados();

        List<Vacina> vacinas = vacinaPresenter.onVacinasCadastradas();

        int linha = 0;
        spaceProbeHeaders = new String[vacinas.size()];
        for (Vacina vacina : vacinas) {
            spaceProbeHeaders[linha] = vacina.getVaciNome();
            linha++;
        }

        spaceProbes = new String[spaceprobeList.size()][spaceprobeList.size()];
        int linhaVacina = 0;
        for (Controle controle : spaceprobeList) {
            spaceProbes[linhaVacina][linhaVacina] = controle.getIdade().getIdadDescricao();
            linhaVacina++;
        }

    }

}
