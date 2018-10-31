package br.com.erivando.vacinaskids.ui.activity.calendario;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.model.Calendario;
import br.com.erivando.vacinaskids.database.model.Dose;
import br.com.erivando.vacinaskids.database.model.Idade;
import br.com.erivando.vacinaskids.database.model.Vacina;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.activity.idade.IdadeMvpPresenter;
import br.com.erivando.vacinaskids.ui.activity.idade.IdadeMvpView;
import br.com.erivando.vacinaskids.ui.activity.main.MainActivity;
import br.com.erivando.vacinaskids.ui.adapter.CalendarioRVAdapter;
import butterknife.BindView;
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
    IdadeMvpPresenter<IdadeMvpView> idadePresenter;

    @BindView(R.id.toolbar_calendario)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar_calendario)
    CollapsingToolbarLayout collapsingToolbar;

    List<Idade> idades;
    List<Calendario> calendarios;
    List<Dose> doses;
    List<Vacina> vacinas;
    private ArrayList<Calendario> calendarioCompleto;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CalendarioActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        setUnBinder(ButterKnife.bind(this));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbar.setTitle(getResources().getString(R.string.text_lista_calendario_titulo));

        getActivityComponent().inject(this);

        presenter.onAttach(this);

        setUp();
    }


    @Override
    protected void setUp() {
        calendarios = presenter.onCalendariosCadastrados();
        //vacinas = vacinaPresenter.onVacinasCadastradas();
        //doses = dosePresenter.onDosesCadastradas();
        idades = idadePresenter.onIdadesCadastradas();

        calendarioCompleto = new ArrayList<Calendario>();

        createDummyData();

        RecyclerView my_recycler_view = findViewById(R.id.calendario_recyclerView);
        my_recycler_view.setHasFixedSize(true);
        CalendarioRVAdapter adapter = new CalendarioRVAdapter(calendarioCompleto, this);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(adapter);
    }

    public void createDummyData() {
        //idades = idadePresenter.onIdadesCadastradas();
        //calendarios = presenter.onCalendariosCadastrados();

        for (Idade idade : idades) {
            Calendario dm = new Calendario();
            dm.setTituloIdade(idade.getIdadDescricao());
            RealmList<Vacina> itemVacina = new RealmList<Vacina>();
            RealmList<Dose> itemDose = new RealmList<Dose>();
            RealmList<Idade> itemIdade = new RealmList<Idade>();
            for (Calendario calendario : calendarios) {
                if (calendario.getIdade().getId() == idade.getId()) {
                    itemVacina.add(calendario.getVacina());
                    itemDose.add(calendario.getDose());
                    itemIdade.add(calendario.getIdade());
                }
            }
            dm.setVacinasInSection(itemVacina);
            dm.setDosesInSection(itemDose);
            dm.setIdadesInSection(itemIdade);
            calendarioCompleto.add(dm);
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

    @Override
    public Context getContextActivity() {
        return CalendarioActivity.this;
    }
}
