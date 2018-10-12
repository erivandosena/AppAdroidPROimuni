package br.com.erivando.vacinaskids.ui.acoes.calendario;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.model.Calendario;
import br.com.erivando.vacinaskids.database.datamodule.CalendarioDataModel;
import br.com.erivando.vacinaskids.database.model.Dose;
import br.com.erivando.vacinaskids.database.model.Idade;
import br.com.erivando.vacinaskids.database.model.Vacina;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.acoes.dose.DoseMvpPresenter;
import br.com.erivando.vacinaskids.ui.acoes.dose.DoseMvpView;
import br.com.erivando.vacinaskids.ui.acoes.idade.IdadeMvpPresenter;
import br.com.erivando.vacinaskids.ui.acoes.idade.IdadeMvpView;
import br.com.erivando.vacinaskids.ui.acoes.vacina.VacinaMvpPresenter;
import br.com.erivando.vacinaskids.ui.acoes.vacina.VacinaMvpView;
import br.com.erivando.vacinaskids.ui.adapter.CalendarioAdapter;
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

public class CalendarioActivity extends BaseActivity implements CalendarioMvpView {

    @Inject
    CalendarioMvpPresenter<CalendarioMvpView> presenter;

    @Inject
    VacinaMvpPresenter<VacinaMvpView> vacinaPresenter;

    @Inject
    IdadeMvpPresenter<IdadeMvpView> idadePresenter;

    @Inject
    DoseMvpPresenter<DoseMvpView> dosePresenter;

    private String[][] SPACESHIPS;


    String[] spaceProbeHeaders;
    String[][] spaceProbes;

    List<Idade> idades;

    List<Dose> doses;

    List<Vacina> vacinas;

    List<Calendario> calendarios;

    private ArrayList<CalendarioDataModel> allSampleData;

    private RecyclerView recycler;
    private RecyclerView.LayoutManager manager;
    private CalendarioAdapter adapter;
    private List<String> list;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CalendarioActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

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
        doses = dosePresenter.onDosesCadastradas();
        vacinas = vacinaPresenter.onVacinasCadastradas();
        calendarios = presenter.onCalendariosCadastrados();

        allSampleData = new ArrayList<CalendarioDataModel>();

        createDummyData();

        RecyclerView my_recycler_view = (RecyclerView) findViewById(R.id.recyclerView);
        my_recycler_view.setHasFixedSize(true);
        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(this, allSampleData);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(adapter);
    }

    public void createDummyData() {
        idades = idadePresenter.onIdadesCadastradas();
        calendarios = presenter.onCalendariosCadastrados();

        for (Idade idade : idades) {
            CalendarioDataModel dm = new CalendarioDataModel();
            dm.setHeaderTitulo(idade.getIdadDescricao());
            ArrayList<Vacina> singleItem = new ArrayList<Vacina>();
            for (Calendario calendario : calendarios) {
                if (calendario.getIdade().getId() == idade.getId())
                    singleItem.add(calendario.getVacina());
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
        Calendario spaceprobe = new Calendario();
        List<Calendario> spaceprobeList = presenter.onCalendariosCadastrados();

        List<Vacina> vacinas = vacinaPresenter.onVacinasCadastradas();

        int linha = 0;
        spaceProbeHeaders = new String[vacinas.size()];
        for (Vacina vacina : vacinas) {
            spaceProbeHeaders[linha] = vacina.getVaciNome();
            linha++;
        }

        spaceProbes = new String[spaceprobeList.size()][spaceprobeList.size()];
        int linhaVacina = 0;
        for (Calendario calendario : spaceprobeList) {
            spaceProbes[linhaVacina][linhaVacina] = calendario.getIdade().getIdadDescricao();
            linhaVacina++;
        }

    }

}
