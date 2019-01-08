package br.com.erivando.proimuni.ui.activity.configuracao;

import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import br.com.erivando.proimuni.R;
import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.database.model.Calendario;
import br.com.erivando.proimuni.database.model.Cartao;
import br.com.erivando.proimuni.database.model.Crianca;
import br.com.erivando.proimuni.database.model.Dose;
import br.com.erivando.proimuni.database.model.Idade;
import br.com.erivando.proimuni.database.model.Imunizacao;
import br.com.erivando.proimuni.database.model.Usuario;
import br.com.erivando.proimuni.database.model.Vacina;
import br.com.erivando.proimuni.mvp.base.BaseActivity;
import br.com.erivando.proimuni.ui.activity.main.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hypertrack.smart_scheduler.SmartScheduler;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   11 de Novembro de 2018 as 11:52
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class ConfiguracaoActivity extends BaseActivity implements ConfiguracaoMvpView {

    @Inject
    ConfiguracaoMvpPresenter<ConfiguracaoMvpView> presenter;

    @BindView(R.id.text_titulo_toobar)
    TextView textViewTituloToobar;

    private List<Crianca> criancas;
    private List<Vacina> vacinas;
    private List<Dose> doses;
    private List<Idade> idades, listaIdade;
    private List<Imunizacao> imunizacoes;
    private List<Calendario> calendarios;
    private List<Cartao> cartoes;
    private List<String> statusCartaoVacinal;
    private Usuario usuario;
    private Imunizacao imunizacao;
    private Idade idade;
    private Cartao cartao;
    private Long mesesIdadeCrianca;
    private Timer timer;
    private TimerTask timerTask;
    private List<CharSequence> listaMsgAgrupada;
    private Random randomId;
    private JobScheduler mScheduler;
    private Intent intent;

    @BindView(R.id.btn_toggle_notificacao)
    ToggleButton toggleButtonNotificacoes;

    @BindView(R.id.btn_toggle_vacinas_opcionais)
    ToggleButton toggleButtonOpcionais;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ConfiguracaoActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        textViewTituloToobar.setText(getResources().getString(R.string.menu_config));
        intent = getIntent();
        setUp();
    }

    @Override
    protected void setUp() {
        toggleButtonNotificacoes.setChecked(presenter.onNotificacoes());
        toggleButtonOpcionais.setChecked(presenter.onRedeVacinas());
    }

    @OnClick(R.id.btn_nav_voltar)
    public void onClickVoltar(View v) {
        onBackPressed();
    }

    @Override
    public Context getContextActivity() {
        return ConfiguracaoActivity.this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.openMainActivity();
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    private void openMainActivity() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }

    @OnClick(R.id.btn_toggle_notificacao)
    public void onNotificacaoClick(View view) {
        boolean onClick = ((ToggleButton) view).isChecked();
        if(onClick){
            presenter.onAtualizaNotificacoes(true);
        }
        else {
            presenter.onAtualizaNotificacoes(false);
        }
        if(toggleButtonNotificacoes.isChecked() != onClick)
            toggleButtonNotificacoes.setChecked(presenter.onNotificacoes());
        if(presenter.onNotificacoes())
            presenter.finalizaServicoNotificacao(ConfiguracaoActivity.this);
        else
            presenter.iniciaServicoNotificacao(ConfiguracaoActivity.this);
    }

    @OnClick(R.id.btn_toggle_vacinas_opcionais)
    public void onOpcionaisClick(View view) {
        boolean onClick = ((ToggleButton) view).isChecked();
        if(onClick){
            presenter.onAtualizaRedeVacinas(true);
        }
        else {
            presenter.onAtualizaRedeVacinas(false);
        }
        if(toggleButtonOpcionais.isChecked() != onClick)
            toggleButtonOpcionais.setChecked(presenter.onRedeVacinas());
    }

}


