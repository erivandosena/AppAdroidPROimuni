package br.com.erivando.vacinaskids.ui.activity.notificacao;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.database.model.Calendario;
import br.com.erivando.vacinaskids.database.model.Cartao;
import br.com.erivando.vacinaskids.database.model.Crianca;
import br.com.erivando.vacinaskids.database.model.Dose;
import br.com.erivando.vacinaskids.database.model.Idade;
import br.com.erivando.vacinaskids.database.model.Imunizacao;
import br.com.erivando.vacinaskids.database.model.Usuario;
import br.com.erivando.vacinaskids.database.model.Vacina;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.service.Servico;
import br.com.erivando.vacinaskids.ui.activity.cartao.CartaoDetalheActivity;
import br.com.erivando.vacinaskids.ui.activity.main.MainActivity;
import br.com.erivando.vacinaskids.ui.application.AppAplicacao;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hypertrack.smart_scheduler.Job;
import io.hypertrack.smart_scheduler.SmartScheduler;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   11 de Novembro de 2018 as 11:52
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class NotificacaoActivity extends BaseActivity implements NotificacaoMvpView, SmartScheduler.JobScheduledCallback {

    public static final int JOB_ID = 0;
    private static final String TAG = NotificacaoActivity.class.getSimpleName();
    public static final String JOB_PERIODIC_TASK_TAG = "br.com.erivando.vacinaskids.ui.activity.notificacao.JobPeriodicTask";
    //
    private String NOTIFICACAO_ID = "NOTIFICACAO_0";
    private int NOTIFICACAO_REPLY = 60000 * 1440;

    @Inject
    IDataManager iDataManager;

    @Inject
    NotificacaoMvpPresenter<NotificacaoMvpView> presenter;

    @BindView(R.id.toolbar_configuracoes)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar_configuracoes)
    CollapsingToolbarLayout collapsingToolbar;

    // Notificação
    private Notification repeatedNotificationInicial;
    private Notification repeatedNotificationPadrao;

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
    //

    private JobScheduler mScheduler;

    private Intent intent;

    @BindView(R.id.btn_on_notificacao)
    Button buttonLigaNotificacoes;

    @BindView(R.id.btn_off_notificacao)
    Button buttonDesligaNotificacoes;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, NotificacaoActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        setUnBinder(ButterKnife.bind(this));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbar.setTitle(getResources().getString(R.string.text_configuracoes_titulo));

        getActivityComponent().inject(this);
        presenter.onAttach(this);

        intent = getIntent();

        setUp();
    }

    @Override
    protected void setUp() {
    }

    @Override
    public Context getContextActivity() {
        return NotificacaoActivity.this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.openMainActivity();
    }

    private void openMainActivity() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }

    // SmartScheduler

    @Override
    public void onJobScheduled(Context context, final Job job) {
        if (job != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //  Toast.makeText(NotificacaoActivity.this, "Notificação: " + job.getJobId() + " agendada!", Toast.LENGTH_SHORT).show();
                    processaNotificacao();

                }
            });
            //Log.d(TAG, "Notificação: " + job.getJobId() + " agendadada!");

            if (!job.isPeriodic()) {
                buttonLigaNotificacoes.setAlpha(1.0f);
                buttonLigaNotificacoes.setEnabled(true);
            }
        }
    }

    @OnClick(R.id.btn_on_notificacao)
    public void onAtivarNotificacaoClick(View view) {
        SmartScheduler jobScheduler = SmartScheduler.getInstance(this);

        // Check if any periodic job is currently scheduled
        if (jobScheduler.contains(JOB_ID)) {
            removePeriodicJob();
            return;
        }

        // Create a new job with specified params
        Job job = createJob();
        if (job == null) {
            //Toast.makeText(NotificacaoActivity.this, "Invalid paramteres specified. " + "Please try again with correct job params.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Schedule current created job
        if (jobScheduler.addJob(job)) {
           // Toast.makeText(NotificacaoActivity.this, "Notificações ativadas", Toast.LENGTH_SHORT).show();

            if (job.isPeriodic()) {
                buttonLigaNotificacoes.setText(getString(R.string.btn_desativa_notificacao));
            } else {
                buttonLigaNotificacoes.setAlpha(0.5f);
                buttonLigaNotificacoes.setEnabled(false);
            }
        }

    }

    private Job createJob() {
        int jobType = getJobType();
        int networkType = getNetworkTypeForJob();
        boolean requiresCharging = false;//requiresChargingSwitch.isChecked();
        boolean isPeriodic = true;//isPeriodicSwitch.isChecked();

        /*
        String intervalInMillisString = "30000";//intervalInMillisEditText.getText().toString();
        if (TextUtils.isEmpty(intervalInMillisString)) {
            return null;
        }
        */

        //Long intervalInMillis = Long.parseLong(intervalInMillisString);

        Long intervalInMillis = 30000L; //(60000 * 1440)/2L; //12Hs
        Job.Builder builder = new Job.Builder(JOB_ID, this, jobType, JOB_PERIODIC_TASK_TAG)
                .setRequiredNetworkType(networkType)
                .setRequiresCharging(requiresCharging)
                .setIntervalMillis(intervalInMillis);

        if (isPeriodic) {
            builder.setPeriodic(intervalInMillis);
        }

        return builder.build();
    }

    private int getJobType() {
        int jobTypeSelectedPos = 3;///jobTypeSpinner.getSelectedItemPosition();
        switch (jobTypeSelectedPos) {
            default:
            case 1:
                return Job.Type.JOB_TYPE_HANDLER;
            case 2:
                return Job.Type.JOB_TYPE_ALARM;
            case 3:
                return Job.Type.JOB_TYPE_PERIODIC_TASK;
        }
    }

    private int getNetworkTypeForJob() {
        int networkTypeSelectedPos = 0;//networkTypeSpinner.getSelectedItemPosition();
        switch (networkTypeSelectedPos) {
            default:
            case 0:
                return Job.NetworkType.NETWORK_TYPE_ANY;
            case 1:
                return Job.NetworkType.NETWORK_TYPE_CONNECTED;
            case 2:
                return Job.NetworkType.NETWORK_TYPE_UNMETERED;
        }
    }

    private void removePeriodicJob() {
        buttonDesligaNotificacoes.setText(getString(R.string.btn_ativa_notificacao));

        SmartScheduler jobScheduler = SmartScheduler.getInstance(this);
        if (!jobScheduler.contains(JOB_ID)) {
            //Toast.makeText(NotificacaoActivity.this, "No job exists with JobID: " + JOB_ID, Toast.LENGTH_SHORT).show();
          //  Toast.makeText(NotificacaoActivity.this, "Não existem notificações ativas", Toast.LENGTH_SHORT).show();
            return;
        }

        if (jobScheduler.removeJob(JOB_ID)) {
           // Toast.makeText(NotificacaoActivity.this, "Notificações desativas", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_off_notificacao)
    public void onDesativarNotificacaoClick(View view) {
        SmartScheduler smartScheduler = SmartScheduler.getInstance(getApplicationContext());
        smartScheduler.removeJob(JOB_ID);

        buttonDesligaNotificacoes.setText(getString(R.string.btn_ativa_notificacao));
        buttonDesligaNotificacoes.setEnabled(true);
        buttonDesligaNotificacoes.setAlpha(1.0f);
    }

    // Notificação

    public void processaNotificacao() {
        randomId = new Random();
        listaMsgAgrupada = new LinkedList<>();

        usuario = iDataManager.obtemUsuario();
        criancas = iDataManager.obtemCriancas();
        cartoes = iDataManager.obtemCartoes();
        listaIdade = iDataManager.obtemIdades();
        calendarios = iDataManager.obtemCalendarios();

        imunizacoes = new ArrayList<Imunizacao>();
        vacinas = new ArrayList<Vacina>();
        doses = new ArrayList<Dose>();
        idades = new ArrayList<Idade>();

        if (usuario != null && !criancas.isEmpty() && !cartoes.isEmpty()) {
            for (Idade idade : listaIdade) {
                for (Calendario calendarioItem : calendarios) {
                    if (calendarioItem.getIdade().getId() == idade.getId()) {
                        vacinas.add(calendarioItem.getVacina());
                        doses.add(calendarioItem.getDose());
                        idades.add(calendarioItem.getIdade());
                        for (Cartao cartao : cartoes) {
                            imunizacao = iDataManager.obtemImunizacao(new String[]{"vacina.id", "dose.id", "cartao.id"}, new Long[]{calendarioItem.getVacina().getId(), calendarioItem.getDose().getId(), (cartao != null) ? cartao.getId() : 0L});
                            if (imunizacao != null) {
                                imunizacoes.add(imunizacao);
                            }
                        }
                    }
                }
            }

            // notificação inicial geral
            String nomeUsuario = usuario != null ? usuario.getUsuaNome() : "Olá";
            repeatedNotificationInicial = showLocalNotification(getContextActivity(), CartaoDetalheActivity.class,
                    "Olá " + nomeUsuario + ", inicie os registros das vacinas através do cartão vacinal, cadastrando as imunizações de cada dose realizada no posto de saúde ou em clínica particular",
                    "Cartão Vacinal",
                    "Lembrete",
                    "Entre no cartão vacinal e cadastre as vacinas realizadas!"
            ).build();

            // Envia notificação
            if (imunizacoes.isEmpty()) {
                getNotificationManager(getContextActivity()).notify(NOTIFICACAO_ID, Notification.FLAG_SHOW_LIGHTS, repeatedNotificationInicial);
            } else {
                statusCartaoVacinal = new ArrayList<String>();
                for (Idade idade : listaIdade) {
                    for (Calendario calendarioItem : calendarios) {
                        if (calendarioItem.getIdade().getId() == idade.getId()) {
                            String mesesIdadeCalendario = idade.getIdadDescricao().toLowerCase();
                            for (Cartao cartao : cartoes) {
                                for (Crianca crianca : criancas) {
                                    if (cartao.getCrianca().getId().equals(crianca.getId())) {
                                        this.cartao = cartao;
                                        this.idade = idade;

                                        //Log.e("mesesIdadeCalendario", String.valueOf(mesesIdadeCalendario));

                                        switch (mesesIdadeCalendario) {
                                            case "ao nascer":
                                                mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                if (!imunizacoes.isEmpty()) {
                                                    for (Imunizacao imunizacao : imunizacoes) {
                                                        if (!verificaCartao(imunizacao)) {
                                                            //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                            aplicaStatusVacina(mesesIdadeCrianca, 1L);
                                                            break;
                                                        } else {
                                                            aplicaStatusVacina(mesesIdadeCrianca, 1L);
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                    aplicaStatusVacina(mesesIdadeCrianca, 1L);
                                                }
                                                break;
                                            case "2 meses":
                                                if (!imunizacoes.isEmpty()) {
                                                    for (Imunizacao imunizacao : imunizacoes) {
                                                        if (!verificaCartao(imunizacao)) {
                                                            //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                            aplicaStatusVacina(mesesIdadeCrianca, 2L);
                                                            break;
                                                        } else {
                                                            aplicaStatusVacina(mesesIdadeCrianca, 2L);
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                    aplicaStatusVacina(mesesIdadeCrianca, 2L);
                                                }
                                                break;
                                            case "3 meses":
                                                if (!imunizacoes.isEmpty()) {
                                                    for (Imunizacao imunizacao : imunizacoes) {
                                                        if (!verificaCartao(imunizacao)) {
                                                            //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                            aplicaStatusVacina(mesesIdadeCrianca, 3L);
                                                            break;
                                                        } else {
                                                            aplicaStatusVacina(mesesIdadeCrianca, 3L);
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                    aplicaStatusVacina(mesesIdadeCrianca, 3L);
                                                }
                                                break;
                                            case "4 meses":
                                                if (!imunizacoes.isEmpty()) {
                                                    for (Imunizacao imunizacao : imunizacoes) {
                                                        if (!verificaCartao(imunizacao)) {
                                                            //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                            aplicaStatusVacina(mesesIdadeCrianca, 4L);
                                                            break;
                                                        } else {
                                                            aplicaStatusVacina(mesesIdadeCrianca, 4L);
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                    aplicaStatusVacina(mesesIdadeCrianca, 4L);
                                                }
                                                break;
                                            case "5 meses":
                                                if (!imunizacoes.isEmpty()) {
                                                    for (Imunizacao imunizacao : imunizacoes) {
                                                        if (!verificaCartao(imunizacao)) {
                                                            //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                            aplicaStatusVacina(mesesIdadeCrianca, 5L);
                                                            break;
                                                        } else {
                                                            aplicaStatusVacina(mesesIdadeCrianca, 5L);
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                    aplicaStatusVacina(mesesIdadeCrianca, 5L);
                                                }
                                                break;
                                            case "6 meses":
                                                if (!imunizacoes.isEmpty()) {
                                                    for (Imunizacao imunizacao : imunizacoes) {
                                                        if (!verificaCartao(imunizacao)) {
                                                            //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                            aplicaStatusVacina(mesesIdadeCrianca, 6L);
                                                            break;
                                                        } else {
                                                            aplicaStatusVacina(mesesIdadeCrianca, 6L);
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                    aplicaStatusVacina(mesesIdadeCrianca, 6L);
                                                }
                                                break;
                                            case "7 meses":
                                                if (!imunizacoes.isEmpty()) {
                                                    for (Imunizacao imunizacao : imunizacoes) {
                                                        if (!verificaCartao(imunizacao)) {
                                                            //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                            aplicaStatusVacina(mesesIdadeCrianca, 7L);
                                                            break;
                                                        } else {
                                                            aplicaStatusVacina(mesesIdadeCrianca, 7L);
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                    aplicaStatusVacina(mesesIdadeCrianca, 7L);
                                                }
                                                break;
                                            case "9 meses":
                                                if (!imunizacoes.isEmpty()) {
                                                    for (Imunizacao imunizacao : imunizacoes) {
                                                        if (!verificaCartao(imunizacao)) {
                                                            //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                            aplicaStatusVacina(mesesIdadeCrianca, 9L);
                                                            break;
                                                        } else {
                                                            aplicaStatusVacina(mesesIdadeCrianca, 9L);
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                    aplicaStatusVacina(mesesIdadeCrianca, 9L);
                                                }
                                                break;
                                            case "12 meses":
                                                if (!imunizacoes.isEmpty()) {
                                                    for (Imunizacao imunizacao : imunizacoes) {
                                                        if (!verificaCartao(imunizacao)) {
                                                            //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                            aplicaStatusVacina(mesesIdadeCrianca, 12L);
                                                            break;
                                                        } else {
                                                            aplicaStatusVacina(mesesIdadeCrianca, 12L);
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                    aplicaStatusVacina(mesesIdadeCrianca, 12L);
                                                }
                                                break;
                                            case "15 meses":
                                                if (!imunizacoes.isEmpty()) {
                                                    for (Imunizacao imunizacao : imunizacoes) {
                                                        if (!verificaCartao(imunizacao)) {
                                                            //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                            aplicaStatusVacina(mesesIdadeCrianca, 15L);
                                                            break;
                                                        } else {
                                                            aplicaStatusVacina(mesesIdadeCrianca, 15L);
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                    aplicaStatusVacina(mesesIdadeCrianca, 15L);
                                                }
                                                break;
                                            case "18 meses":
                                                if (!imunizacoes.isEmpty()) {
                                                    for (Imunizacao imunizacao : imunizacoes) {
                                                        if (!verificaCartao(imunizacao)) {
                                                            //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                            aplicaStatusVacina(mesesIdadeCrianca, 18L);
                                                            break;
                                                        } else {
                                                            aplicaStatusVacina(mesesIdadeCrianca, 18L);
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                    aplicaStatusVacina(mesesIdadeCrianca, 18L);
                                                }
                                                break;
                                            case "4 anos":
                                                if (!imunizacoes.isEmpty()) {
                                                    for (Imunizacao imunizacao : imunizacoes) {
                                                        if (!verificaCartao(imunizacao)) {
                                                            //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                            aplicaStatusVacina(mesesIdadeCrianca, 48L);
                                                            break;
                                                        } else {
                                                            aplicaStatusVacina(mesesIdadeCrianca, 48L);
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                    aplicaStatusVacina(mesesIdadeCrianca, 48L);
                                                }
                                                break;
                                            case "5 anos":
                                                if (!imunizacoes.isEmpty()) {
                                                    for (Imunizacao imunizacao : imunizacoes) {
                                                        if (!verificaCartao(imunizacao)) {
                                                            //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                            aplicaStatusVacina(mesesIdadeCrianca, 60L);
                                                            break;
                                                        } else {
                                                            aplicaStatusVacina(mesesIdadeCrianca, 60L);
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                    aplicaStatusVacina(mesesIdadeCrianca, 60L);
                                                }
                                                break;
                                            case "11 anos":
                                                if (!imunizacoes.isEmpty()) {
                                                    for (Imunizacao imunizacao : imunizacoes) {
                                                        if (!verificaCartao(imunizacao)) {
                                                            //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                            aplicaStatusVacina(mesesIdadeCrianca, 132L);
                                                            break;
                                                        } else {
                                                            aplicaStatusVacina(mesesIdadeCrianca, 132L);
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                    aplicaStatusVacina(mesesIdadeCrianca, 132L);
                                                }
                                                break;
                                            case "9 a 14 anos":
                                                if (!imunizacoes.isEmpty()) {
                                                    for (Imunizacao imunizacao : imunizacoes) {
                                                        if (!verificaCartao(imunizacao)) {
                                                            //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                            aplicaStatusVacina(mesesIdadeCrianca, 108L);
                                                            break;
                                                        } else {
                                                            aplicaStatusVacina(mesesIdadeCrianca, 108L);
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                    aplicaStatusVacina(mesesIdadeCrianca, 108L);
                                                }
                                                break;
                                            case "11 a 14 anos":
                                                if (!imunizacoes.isEmpty()) {
                                                    for (Imunizacao imunizacao : imunizacoes) {
                                                        if (!verificaCartao(imunizacao)) {
                                                            //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                            aplicaStatusVacina(mesesIdadeCrianca, 132L);
                                                            break;
                                                        } else {
                                                            aplicaStatusVacina(mesesIdadeCrianca, 132L);
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    //Long mesesIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                                    aplicaStatusVacina(mesesIdadeCrianca, 132L);
                                                    break;
                                                }
                                                break;
                                            default:
                                                break;
                                        }
                                        break;
                                    }

                                }
                            }
                            break;
                        }
                    }
                }

                final List<String> listaVacina = new ArrayList<String>();
                for (Cartao cartao : cartoes) {
                    for (int i = 0; i < statusCartaoVacinal.size(); i++) {
                        if (statusCartaoVacinal.get(i).toLowerCase().contains(cartao.getCrianca().getCriaNome().toLowerCase())) {
                            for (Calendario calendario : calendarios) {
                                if (statusCartaoVacinal.get(i).toLowerCase().contains(calendario.getIdade().getIdadDescricao().toLowerCase())) {
                                    // Log.e("CRIANÇA;", cartao.getCrianca().getCriaNome());
                                    // Log.e("IDADE;", calendario.getIdade().getIdadDescricao());
                                    // Log.e("VACINA;", calendario.getVacina().getVaciNome());
                                    // Log.e("DOSE;", calendario.getDose().getDoseDescricao());
                                    // Log.e("REDE;", calendario.getVacina().getVaciRede());
                                    if (!imunizacoes.isEmpty()) {
                                        for (Imunizacao imunizacao : imunizacoes) {
                                            if (!calendario.getVacina().getId().equals(imunizacao.getVacina().getId()) && !calendario.getDose().getId().equals(imunizacao.getVacina().getId())) {
                                                listaVacina.add(calendario.getVacina().getVaciNome());
                                            }
                                        }
                                    } else {
                                        listaVacina.add(calendario.getVacina().getVaciNome());
                                    }
                                }
                            }
                            //Log.e("CARTAO:", cartao.getId() + " Olá! " + usuario.getUsuaNome() + "," + statusCartaoVacinal.get(i) + "\nAtenção para: " + listaVacina.toString().substring(1, listaVacina.toString().length() - 1).replace(",", " e"));
                            // notificação por etatus de vacina
                            NOTIFICACAO_ID = "NOTIFICACAO_" + String.valueOf(randomId.nextInt((1000 - 1) + 1) + 1);
                            String generoCrianca = "Menino".equalsIgnoreCase(cartao.getCrianca().getCriaSexo()) ? "vacinado" : "vacinada";
                            repeatedNotificationPadrao = showLocalNotification(getContextActivity(), CartaoDetalheActivity.class,
                                    statusCartaoVacinal.get(i),// + "\nAtenção para " + listaVacina.toString().substring(1, listaVacina.toString().length() - 1).replace(",", " e")+".",
                                    "Cartão Vacinal",
                                    "Lembrete",
                                    cartao.getCrianca().getCriaNome() + " precisa ser " + generoCrianca + " com " + listaVacina.toString().substring(1, listaVacina.toString().length() - 1).replace(",", " e") + "."
                            ).build();

                            try {
                                Thread.sleep(20000); //20Seg
                                getNotificationManager(getContextActivity()).notify(NOTIFICACAO_ID, Notification.FLAG_SHOW_LIGHTS, repeatedNotificationPadrao);
                                listaVacina.clear();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //this.stopSelf();
                        }
                    }
                }
            }
        }
    }

    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private boolean verificaCartao(Imunizacao imunizacao) {
        return (imunizacao.getCartao().getCrianca().getId().equals(cartao.getCrianca().getId()));
    }

    private void aplicaStatusVacina(Long mesesIdadeCrianca, Long semanas) {
        String statusVencendo = idade.getIdadDescricao().toUpperCase() + " vencendo o prazo";
        String statusVencido = idade.getIdadDescricao().toUpperCase() + " com prazo vencido";

        if (mesesIdadeCrianca == semanas) {
            statusCartaoVacinal.add("Atenção para vacinação de " + cartao.getCrianca().getCriaNome() + ", existe vacina" + ("ao nascer".equals(idade.getIdadDescricao().toLowerCase()) ? " necessária " + statusVencendo : " necessária na idade de " + statusVencendo));
        }
        if (mesesIdadeCrianca > semanas) {
            statusCartaoVacinal.add("Mantenha em dia a vacinação de " + cartao.getCrianca().getCriaNome() + ", existe vacina" + ("ao nascer".equals(idade.getIdadDescricao().toLowerCase()) ? " necessária " + statusVencido : " necessária na idade de " + statusVencido));
        }
        if ("9 a 14 anos".equals(mesesIdadeCrianca)) {
            if (mesesIdadeCrianca > semanas && mesesIdadeCrianca <= semanas + 60L) {
                statusCartaoVacinal.add("Mantenha em dia a vacinação de " + cartao.getCrianca().getCriaNome() + ", existe vacina" + ("ao nascer".equals(idade.getIdadDescricao().toLowerCase()) ? " necessária " + statusVencido : " necessária na idade de " + statusVencido));
            }
        }
        if ("11 a 14 anos".equals(mesesIdadeCrianca)) {
            if (mesesIdadeCrianca > semanas && mesesIdadeCrianca <= semanas + 36L) {
                statusCartaoVacinal.add("Mantenha em dia a vacinação de " + cartao.getCrianca().getCriaNome() + ", existe vacina" + ("ao nascer".equals(idade.getIdadDescricao().toLowerCase()) ? " necessária " + statusVencido : " necessária na idade de " + statusVencido));
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private NotificationCompat.Builder showLocalNotification(Context context, Class<?> clsDestino, String textBig, String textContentTitle, String summaryTextTitle, String contentText) {

        listaMsgAgrupada.add(textBig);

        List<String> textoLongo = new ArrayList<String>();
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_gerenciar);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AppAplicacao.contextApp, NOTIFICACAO_ID);
        mBuilder.setContentTitle(context.getString(R.string.app_name));
        mBuilder.setContentText(contentText);
        mBuilder.setSmallIcon(R.drawable.ic_vacina);
        mBuilder.setLargeIcon(largeIcon);
        mBuilder.setAutoCancel(true);
        mBuilder.setVibrate(new long[]{100, 300, 500, 800, 1000});
        mBuilder.setLights(Color.BLUE, 1000, 500);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setGroupSummary(true);
        mBuilder.setGroup(NOTIFICACAO_ID);

        /*
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(textBig);
        inboxStyle.setBigContentTitle(textContentTitle);
        inboxStyle.setSummaryText(summaryTextTitle);
        */

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(textBig);
        bigTextStyle.setBigContentTitle(textContentTitle);
        bigTextStyle.setSummaryText(summaryTextTitle);
        for (CharSequence cs : listaMsgAgrupada) {
            textoLongo.add(cs.toString());
        }
        textoLongo.add(contentText);
        bigTextStyle.bigText(textoLongo.toString().substring(1, textoLongo.toString().length() - 1));
        mBuilder.setStyle(bigTextStyle);

        // Cria uma intenção explícita para uma atividade do aplicativo
        Intent resultIntent = new Intent(AppAplicacao.contextApp, clsDestino);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(AppAplicacao.contextApp);
        stackBuilder.addParentStack(clsDestino);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent pendingIntent = PendingIntent.getActivities(context, NOTIFICACAO_REPLY, stackBuilder.getIntents(), PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        return mBuilder;
    }

    //


    /**
     * onClick method that schedules the jobs based on the parameters set
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void scheduleJob() {
        mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        //RadioGroup networkOptions = (RadioGroup) findViewById(R.id.networkOptions);

        int selectedNetworkID = 0;//networkOptions.getCheckedRadioButtonId();
        boolean mPeriodicSwitch = true;
        boolean mDeviceChargingSwitch = true;
        boolean mDeviceIdleSwitch = true;

        int selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
        switch(selectedNetworkID){
            case 0:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
                break;
            case 1:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;
                break;
            case 2:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED;
                break;
        }

        ComponentName serviceName = new ComponentName(getPackageName(), Servico.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName).setRequiredNetworkType(selectedNetworkOption).setRequiresDeviceIdle(mDeviceIdleSwitch).setRequiresCharging(mDeviceChargingSwitch);

        int seekBarInteger = 30;//mSeekBar.getProgress();
        boolean seekBarSet = seekBarInteger > 0;

        //Set the job parameters based on the periodic switch.
        if (mPeriodicSwitch){
            if (seekBarSet){
                builder.setPeriodic(seekBarInteger * 1000);
            } else {
               // Toast.makeText(NotificacaoActivity.this, R.string.no_interval_toast,Toast.LENGTH_SHORT).show();
            }
        } else {
            if (seekBarSet) {
                builder.setOverrideDeadline(seekBarInteger * 1000);
            }
        }

        boolean constraintSet =
                selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE
                || mDeviceChargingSwitch || mDeviceIdleSwitch || seekBarSet;

        if(constraintSet) {
            //Schedule the job and notify the user
            JobInfo myJobInfo = builder.build();
            mScheduler.schedule(myJobInfo);
           // Toast.makeText(this, R.string.job_scheduled, Toast.LENGTH_SHORT).show();
        } else {
           // Toast.makeText(this, R.string.no_constraint_toast, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * onClick method for cancelling all existing jobs
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void cancelJobs() {
        if (mScheduler != null){
            mScheduler.cancelAll();;
            mScheduler = null;
           // Toast.makeText(this, R.string.jobs_canceled, Toast.LENGTH_SHORT).show();
        }
    }
}


