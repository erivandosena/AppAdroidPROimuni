package br.com.erivando.proimuni.ui.activity.calendario;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.model.Calendario;
import br.com.erivando.proimuni.database.model.Dose;
import br.com.erivando.proimuni.database.model.Idade;
import br.com.erivando.proimuni.database.model.Vacina;
import br.com.erivando.proimuni.mvp.base.BaseActivity;
import br.com.erivando.proimuni.ui.activity.idade.IdadeMvpPresenter;
import br.com.erivando.proimuni.ui.activity.idade.IdadeMvpView;
import br.com.erivando.proimuni.ui.activity.main.MainActivity;
import br.com.erivando.proimuni.ui.adapter.CalendarioRVA;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;

import static br.com.erivando.proimuni.util.Uteis.resizeCustomizedToobar;

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

    @BindView(R.id.text_titulo_toobar)
    TextView textViewTituloToobar;

    @BindView(R.id.layout_toobar)
    LinearLayout linearLayoutToobar;

    List<Idade> idades;
    List<Calendario> calendarios;
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

        textViewTituloToobar.setText(getResources().getString(R.string.text_lista_calendario_titulo));
        getActivityComponent().inject(this);

        presenter.onAttach(this);

        setUp();
    }

    @OnClick(R.id.btn_nav_voltar)
    public void onClickVoltar(View v) {
        onBackPressed();
    }

    @Override
    protected void setUp() {
        calendarios = presenter.onCalendariosCadastrados();
        idades = idadePresenter.onIdadesCadastradas();
        calendarioCompleto = new ArrayList<Calendario>();

        criaDadosCalendario();

        RecyclerView recyclerViewCalendario = findViewById(R.id.calendario_recyclerView);
        CalendarioRVA adapter = new CalendarioRVA(calendarioCompleto, this);
        recyclerViewCalendario.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewCalendario.setAdapter(adapter);
        recyclerViewCalendario.setHasFixedSize(true);

        resizeCustomizedToobar(linearLayoutToobar);
    }

    public void criaDadosCalendario() {
        for (Idade idade : idades) {

            Calendario calendario = new Calendario();
            calendario.setTituloIdade(idade.getIdadDescricao());

            RealmList<Vacina> itemVacina = new RealmList<Vacina>();
            RealmList<Dose> itemDose = new RealmList<Dose>();
            RealmList<Idade> itemIdade = new RealmList<Idade>();

            for (Calendario itemCalendario : calendarios) {
                if (itemCalendario.getIdade().getId() == idade.getId()) {
                    itemVacina.add(itemCalendario.getVacina());
                    itemDose.add(itemCalendario.getDose());
                    itemIdade.add(itemCalendario.getIdade());
                }
            }
            calendario.setVacinasInSection(itemVacina);
            calendario.setDosesInSection(itemDose);
            calendario.setIdadesInSection(itemIdade);

            calendarioCompleto.add(calendario);
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
