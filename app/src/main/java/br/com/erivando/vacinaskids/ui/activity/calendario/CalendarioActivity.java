package br.com.erivando.vacinaskids.ui.activity.calendario;

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
import br.com.erivando.vacinaskids.database.model.Dose;
import br.com.erivando.vacinaskids.database.model.Idade;
import br.com.erivando.vacinaskids.database.model.Vacina;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.activity.dose.DoseMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.dose.DoseMvpView;
import br.com.erivando.vacinaskids.ui.activity.idade.IdadeMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.idade.IdadeMvpView;
import br.com.erivando.vacinaskids.ui.activity.main.MainActivity;
import br.com.erivando.vacinaskids.ui.activity.vacina.VacinaMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.vacina.VacinaMvpView;
import br.com.erivando.vacinaskids.ui.adapter.CalendarioRVAdapter;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;

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

    List<Idade> idades;
    List<Calendario> calendarios;
    List<Dose> doses;
    List<Vacina> vacinas;
    private List<Calendario> allSampleData;

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

        setUp();
    }

    @OnClick(R.id.btn_nav_voltar)
    public void onClickVoltar() {
        this.onBackPressed();
    }

    @Override
    protected void setUp() {
        habilitaTelaCheia(this);

        idades = idadePresenter.onIdadesCadastradas();
        calendarios = presenter.onCalendariosCadastrados();
        doses = dosePresenter.onDosesCadastradas();
        vacinas = vacinaPresenter.onVacinasCadastradas();

        allSampleData = new ArrayList<Calendario>();

        createDummyData();

        RecyclerView my_recycler_view = findViewById(R.id.calendario_recyclerView);
        my_recycler_view.setHasFixedSize(true);
        CalendarioRVAdapter adapter = new CalendarioRVAdapter(this, (ArrayList<Calendario>) allSampleData);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(adapter);
    }

    public void createDummyData() {
        idades = idadePresenter.onIdadesCadastradas();
        calendarios = presenter.onCalendariosCadastrados();

        for (Idade idade : idades) {
            Calendario dm = new Calendario();
            dm.setHeaderTitulo(idade.getIdadDescricao());
            RealmList<Vacina> singleItem = new RealmList<Vacina>();
            for (Calendario calendario : calendarios) {
                if (calendario.getIdade().getId() == idade.getId())
                    singleItem.add(calendario.getVacina());
            }
            dm.setVacinasInSection(singleItem);
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

}
