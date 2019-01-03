package br.com.erivando.proimuni.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
import br.com.erivando.proimuni.di.component.DaggerServiceComponent;
import br.com.erivando.proimuni.di.component.ServiceComponent;
import br.com.erivando.proimuni.ui.activity.cartao.CartaoDetalheActivity;
import br.com.erivando.proimuni.ui.activity.crianca.CriancaActivity;
import br.com.erivando.proimuni.ui.application.AppAplicacao;

import static br.com.erivando.proimuni.util.Uteis.getCapitalizeNome;

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

    private NotificationCompat.Builder notificationCompactBuild;
    private NotificationCompat.Builder repeatedNotificationInicial;
    private NotificationCompat.Builder repeatedNotificationPadrao;

    private List<Crianca> criancas;
    private List<Vacina> vacinas;
    private List<Dose> doses;
    private List<Idade> idades, listaIdade;
    private List<Imunizacao> imunizacoes;
   //private List<Imunizacao> naoImunizados;
    private List<Calendario> calendarios;
    private List<Cartao> cartoes;
    private List<String> statusCartaoVacinal;
    private Usuario usuario;
    private Imunizacao imunizacao;
    private Idade idade;
    private Cartao cartao;

    private Long semanasIdadeCrianca;

    private Calendario calendarioItem;

    //private Timer timer;
    //private TimerTask timerTask;

    private List<CharSequence> listaMsgAgrupada;

    private Random randomId;

    private Context context;

    private boolean isRunning;

    private Thread backgroundThread;

    //private String generoCrianca;

    private NotificationManager notificationManager;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1, new Notification());
        } else {
        }
        component.inject(this);
        this.context = AppAplicacao.contextApp;
        this.isRunning = false;
        this.backgroundThread = new Thread(realizaTarefa);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRunning = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable realizaTarefa = new Runnable() {
        public void run() {
            // Do something here
            processaNotificacao();
            stopSelf();
        }
    };

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

        if (iDataManager.getCurrentUserLoggedInMode() != IDataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
            for (Idade idade : listaIdade) {
                for (Calendario calendarioItem : calendarios) {
                    if (calendarioItem.getIdade().getId() == idade.getId() && "Pública".equalsIgnoreCase(calendarioItem.getVacina().getVaciRede())) {
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
            if (imunizacoes.isEmpty()) {
                String nomeUsuario = (usuario != null && usuario.getUsuaNome() != null) ? "Olá "+usuario.getUsuaNome() : "Olá "+ getCapitalizeNome(iDataManager.getCurrentUserName());
                repeatedNotificationInicial = showLocalNotification(context, CriancaActivity.class,
                        nomeUsuario + ", é importante que você cadastre sua(s) criança(s) para usufruir dos benefícios do "+getResources().getString(R.string.app_name)+". Vamos lá! Inicie o preenchimento do cartão vacinal!",
                        "Cadastre um Cartão Vacinal",
                        "Lembrete",
                        "Preencha no cartão as vacinas realizadas.",
                        0L
                );
                // Envia notificação
                getNotificationManager(context).notify(NOTIFICACAO_ID, Notification.FLAG_SHOW_LIGHTS, repeatedNotificationInicial.build());
            }
            //else {
            statusCartaoVacinal = new ArrayList<String>();
            for (Idade idade : listaIdade) {
                for (Calendario calendarioItem : calendarios) {
                    this.calendarioItem = calendarioItem;
                    if (calendarioItem.getIdade().getId() == idade.getId() && "Pública".equalsIgnoreCase(calendarioItem.getVacina().getVaciRede())) {
                        String mesesIdadeCalendario = idade.getIdadDescricao().toLowerCase();
                        for (Cartao cartao : cartoes) {
                            for (Crianca crianca : criancas) {
                                if (cartao.getCrianca().getId().equals(crianca.getId())) {
                                    this.cartao = cartao;
                                    this.idade = idade;
                                    semanasIdadeCrianca = (Calendar.getInstance().getTime().getTime() - cartao.getCrianca().getCriaNascimento().getTime()) / (1000L * 60 * 60 * 24 * 365 / 12);
                                    switch (mesesIdadeCalendario) {
                                        case "ao nascer":
                                            preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 1L, cartao);
                                            break;
                                        case "2 meses":
                                            preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 2L, cartao);
                                            break;
                                        case "3 meses":
                                            preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 3L, cartao);
                                            break;
                                        case "4 meses":
                                            preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 4L, cartao);
                                            break;
                                        case "5 meses":
                                            preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 5L, cartao);
                                            break;
                                        case "6 meses":
                                            preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 6L, cartao);
                                            break;
                                        case "7 meses":
                                            preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 7L, cartao);
                                            break;
                                        case "9 meses":
                                            preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 9L, cartao);
                                            break;
                                        case "12 meses":
                                            preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 12L, cartao);
                                            break;
                                        case "15 meses":
                                            preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 15L, cartao);
                                            break;
                                        case "18 meses":
                                            preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 18L, cartao);
                                            break;
                                        case "4 anos":
                                            preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 48L, cartao);
                                            break;
                                        case "5 anos":
                                            preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 60L, cartao);
                                            break;
                                        case "11 anos":
                                            preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 132L, cartao);
                                            break;
                                        case "9 a 14 anos":
                                            preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 108L, cartao);
                                            break;
                                        case "11 a 14 anos":
                                            preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 132L, cartao);
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

        }
    }

    private void preparaNotificacao(String mesesIdade, Long semanasIdadeCrianca, Long baseQuantSemanas, Cartao cartao) {
        List<String> listaVacina = new ArrayList<String>();
        for (Calendario calendario : calendarios) {
            //if (!imunizacoes.isEmpty()) {
                if (verificaImunizacoes(calendario.getVacina(), calendario.getDose(), cartao).isEmpty()) {
                    for (Crianca crianca : criancas) {
                        if (crianca.getId().equals(cartao.getId())) {
                            if (semanasIdadeCrianca >= baseQuantSemanas && calendario.getIdade().getIdadDescricao().equalsIgnoreCase(mesesIdade)) {
                                listaVacina.add(calendario.getIdade().getIdadDescricao());
                                listaVacina.add(calendario.getVacina().getVaciNome());
                                listaVacina.add(calendario.getDose().getDoseDescricao());
                                /*
                                Log.e("--------------", "---------------------------------");
                                Log.e("CARTAO/ID ", cartao.getId().toString());
                                Log.e("CARTAO/CRIANCA ", cartao.getCrianca().getCriaNome());
                                Log.e("IDADE ", listaVacina.get(0));
                                Log.e("VACINA ", listaVacina.get(1));
                                Log.e("DOSE ", listaVacina.get(2));
                                Log.e("--------------", "---------------------------------");
                                */
                                String bigTexto = aplicaStatusVacina(semanasIdadeCrianca, baseQuantSemanas, cartao, listaVacina.get(0));
                                threadNotificacao(cartao, listaVacina, bigTexto);
                            }
                        }
                    }
                } else {
                    //Log.e("IMUNIZADO..........> ", calendario.getVacina().getVaciNome() + " (" + calendario.getDose().getDoseDescricao() + ")");
                }
                listaVacina.clear();
           // } else {
            //}
        }
    }

    private List<Imunizacao> verificaImunizacoes(Vacina vacina, Dose dose, Cartao cartao) {
        List<Imunizacao> imunizacoes = iDataManager.obtemImunizacoes(new String[]{"vacina.id", "dose.id", "cartao.id"}, new Long[]{vacina.getId(), dose.getId(), cartao.getId()});
        return imunizacoes;
    }

    private void threadNotificacao(Cartao cartao, List<String> listaVacina, String bigTexto) {
        try {
            Thread.sleep(20000); //20Seg
            // notificação por etatus da vacina
            NOTIFICACAO_ID = "NOTIFICACAO_" + String.valueOf(randomId.nextInt((1000 - 1) + 1) + 1);
            String statusVacina = bigTexto.contains("Atenção!") ? "Vacina atrasada" : " Vacina precisa ser realizada";
            String generoCrianca = cartao.getCrianca().getCriaSexo().contains("Menino") ? "vacinado " : "vacinada";

            repeatedNotificationPadrao = showLocalNotification(context, CartaoDetalheActivity.class,
                    bigTexto,
                    statusVacina,
                    "Lembrete",
                    cartao.getCrianca().getCriaNome()+ " precisa ser "+ generoCrianca + " com "+listaVacina.get(2) + " de " + listaVacina.get(1)+".",
                    cartao.getId()
            );
            // envia notificação
            getNotificationManager(context).notify(NOTIFICACAO_ID, Notification.FLAG_SHOW_LIGHTS, repeatedNotificationPadrao.build());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopSelf();
    }

    private NotificationManager getNotificationManager(Context context) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    //private boolean verificaCartao(Imunizacao imunizacao) {
    //    return (imunizacao.getCartao().getCrianca().getId().equals(cartao.getCrianca().getId()));
    //}

    private String aplicaStatusVacina(Long semanasIdadeCrianca, Long baseQuantSemanas, Cartao cartao, String idadeVacinacao) {
       // String statusVencendo = "Existe vacina para " +idadeVacinacao.toUpperCase() + " no prazo de vacinação";
      //  String statusVencido = "Atenção! A vacina de " +idadeVacinacao.toUpperCase() + " está com prazo vencido";
        String statusVencendo = "Fique de olho na próxima dose de vacina para "+cartao.getCrianca().getCriaNome()+ ": " +idadeVacinacao.toUpperCase();
        String statusVencido = "Atenção! "+cartao.getCrianca().getCriaNome()+", possui vacina atrasada de "+idadeVacinacao.toUpperCase() +". Atualize o cartão vacinal!";

        String status = null;
        if (semanasIdadeCrianca == baseQuantSemanas) {
            status = statusVencendo;
        }
        if (semanasIdadeCrianca > baseQuantSemanas) {
            status = statusVencido;
        }
        if ("9 a 14 anos".equals(semanasIdadeCrianca)) {
            if (semanasIdadeCrianca > baseQuantSemanas && semanasIdadeCrianca <= baseQuantSemanas + 60L) {
                status = statusVencido;
            }
        }
        if ("11 a 14 anos".equals(semanasIdadeCrianca)) {
            if (semanasIdadeCrianca > baseQuantSemanas && semanasIdadeCrianca <= baseQuantSemanas + 36L) {
                status = statusVencido;
            }
        }
        return status;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private NotificationCompat.Builder showLocalNotification(Context context, Class<?> clsDestino, String textBig, String textContentTitle, String summaryTextTitle, String contentText, Long idCartao) {

        listaMsgAgrupada.add(textBig);
        Collections.sort(listaMsgAgrupada, Collections.reverseOrder());

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_dose);
        List<String> textoLongo = new ArrayList<String>();

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(textBig);
        bigTextStyle.setBigContentTitle(textContentTitle);
        bigTextStyle.setSummaryText(summaryTextTitle);
        for (CharSequence cs : listaMsgAgrupada) {
            if(textoLongo.size() == 1)
                textoLongo.clear();
            textoLongo.add(cs.toString());
        }
        listaMsgAgrupada.clear();
        textoLongo.add(contentText);
        bigTextStyle.bigText(textoLongo.toString().substring(1, textoLongo.toString().length() - 1));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build();
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICACAO_ID, textContentTitle, importance);
            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes);
            getNotificationManager(context);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationCompactBuild = new NotificationCompat.Builder(context, notificationChannel.getId());
        } else {
            notificationCompactBuild = new NotificationCompat.Builder(context, NOTIFICACAO_ID);
            notificationCompactBuild.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
        notificationCompactBuild.setDefaults(Notification.DEFAULT_ALL);
        notificationCompactBuild.setContentTitle(context.getString(R.string.app_name));
        notificationCompactBuild.setContentText(contentText);
        notificationCompactBuild.setTicker(contentText);
        notificationCompactBuild.setSmallIcon(R.drawable.ic_notificacao);
        notificationCompactBuild.setLargeIcon(largeIcon);
        notificationCompactBuild.setAutoCancel(true);
        notificationCompactBuild.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 500, 800});
        notificationCompactBuild.setLights(Color.BLUE, 1000, 500);

        notificationCompactBuild.setPriority(Notification.PRIORITY_MAX);
        notificationCompactBuild.setGroupSummary(true);
        notificationCompactBuild.setGroup(NOTIFICACAO_ID);
        notificationCompactBuild.setStyle(bigTextStyle);

        Intent resultIntent = new Intent(context, clsDestino);
        resultIntent.putExtra("cartao", idCartao);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(clsDestino);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent pendingIntent = PendingIntent.getActivities(context, NOTIFICACAO_REPLY, stackBuilder.getIntents(), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationCompactBuild.setContentIntent(pendingIntent);

        return notificationCompactBuild;
    }

}
