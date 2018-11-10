package br.com.erivando.vacinaskids.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

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
import br.com.erivando.vacinaskids.di.component.DaggerServiceComponent;
import br.com.erivando.vacinaskids.di.component.ServiceComponent;
import br.com.erivando.vacinaskids.notification.NotificationHelper;
import br.com.erivando.vacinaskids.ui.activity.cartao.CartaoDetalheActivity;
import br.com.erivando.vacinaskids.ui.application.AppAplicacao;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   31 de Outubro de 2018 as 10:19
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class Servico extends Service {

    private String NOTIFICACAO_ID = "NOTIFICACAO_0";
    private int NOTIFICACAO_REPLY = 60000 * 1440;

    @Inject
    IDataManager iDataManager;

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

    private Context context;

    public Servico(Context context) {
        super();
        this.context = context;
    }

    public Servico() {
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, Servico.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceComponent component = DaggerServiceComponent.builder().applicationComponent(((AppAplicacao) getApplication()).getComponent()).build();
        component.inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent("SensorServico");
        sendBroadcast(broadcastIntent);
        stopTimerTask();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        //agendar o temporizador, para acordar a cada 1 segundo
        timer.schedule(timerTask, (60000 * 1440)/2, 60000 * 1440); //24Hs Agenda
    }

    /**
     * define o temporizador para imprimir o contador a cada x segundos
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                processaNotificacao();
            }
        };
    }

    /**
     * não necessário
     */
    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

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
            repeatedNotificationInicial = showLocalNotification(context, CartaoDetalheActivity.class,
                    "Olá "+nomeUsuario + ", inicie os registros das vacinas através do cartão vacinal, cadastrando as imunizações de cada dose realizada no posto de saúde ou em clínica particular",
                    "Cartão Vacinal",
                    "Lembrete",
                    "Entre no cartão vacinal e cadastre as vacinas realizadas!"
            ).build();

            // Envia notificação
            if (imunizacoes.isEmpty()) {
                NotificationHelper.getNotificationManager(context).notify(NOTIFICACAO_ID, Notification.FLAG_SHOW_LIGHTS, repeatedNotificationInicial);
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
                            repeatedNotificationPadrao = showLocalNotification(context, CartaoDetalheActivity.class,
                                    statusCartaoVacinal.get(i),// + "\nAtenção para " + listaVacina.toString().substring(1, listaVacina.toString().length() - 1).replace(",", " e")+".",
                                    "Cartão Vacinal",
                                    "Lembrete",
                                    cartao.getCrianca().getCriaNome() + " precisa ser " + generoCrianca + " com " + listaVacina.toString().substring(1, listaVacina.toString().length() - 1).replace(",", " e")+"."
                            ).build();

                            try {
                                Thread.sleep(20000); //20Seg
                                NotificationHelper.getNotificationManager(context).notify(NOTIFICACAO_ID, Notification.FLAG_SHOW_LIGHTS, repeatedNotificationPadrao);
                                listaVacina.clear();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            this.stopSelf();
                        }
                    }
                }
            }
        }
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
}
