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
import android.support.annotation.RequiresApi;
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
import br.com.erivando.proimuni.ui.activity.crianca.CriancaListaActvity;
import br.com.erivando.proimuni.ui.activity.imunizacao.ImunizacaoActivity;
import br.com.erivando.proimuni.ui.application.AppAplicacao;
import io.realm.RealmList;

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
    private List<Idade> idades;
    private List<Dose> doses;
    private List<Idade> listaIdade;
    private List<Imunizacao> imunizacoes;
    private List<Calendario> calendarios;
    private List<Cartao> cartoes;
    private List<String> statusCartaoVacinal;
    private Usuario usuario;
    private Imunizacao imunizacao;
    private Idade idade;
    private Cartao cartao;
    private Long semanasIdadeCrianca;
    private Calendario calendarioItem;
    private List<CharSequence> listaMsgAgrupada;
    private Random randomId;
    private Context context;
    private boolean isRunning;
    private Thread backgroundThread;
    private NotificationManager notificationManager;
    private List<Long> idLimiteLembrete;
    private List<Idade> idadesRemovidas;

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
            startForeground(2, new Notification());
        } else {
           // startForeground(1, new Notification());
        }
        component.inject(this);
        this.context = AppAplicacao.contextApp;
        this.isRunning = false;
        this.backgroundThread = new Thread(realizaTarefa);
        this.idLimiteLembrete = new ArrayList<Long>();
        this.idadesRemovidas = new ArrayList<Idade>();
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
            if (!iDataManager.isNotificacoes())
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
        idades = iDataManager.obtemIdades();
        calendarios = iDataManager.isRedeVacinas() ? iDataManager.obtemCalendarios() : iDataManager.obtemCalendarios(new String[]{"vacina.vaciRede", "Pública"});

        imunizacoes = new ArrayList<Imunizacao>();
        vacinas = new ArrayList<Vacina>();
        doses = new ArrayList<Dose>();
        listaIdade = new ArrayList<Idade>();

        if (iDataManager.getCurrentUserLoggedInMode() != IDataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
            for (Idade idade : idades)
                listaIdade.add(idade);

            for (Idade idade : listaIdade) {

                Calendario calendario = new Calendario();
                RealmList<Vacina> itemVacina = new RealmList<Vacina>();

                for (Calendario calendarioItem : calendarios) {
                    if (calendarioItem.getIdade().getId() == idade.getId()) {
                        vacinas.add(calendarioItem.getVacina());
                        doses.add(calendarioItem.getDose());
                        itemVacina.add(calendarioItem.getVacina());

                        for (Cartao cartao : cartoes) {
                            imunizacao = iDataManager.obtemImunizacao(new String[]{"vacina.id", "dose.id", "cartao.id"}, new Long[]{calendarioItem.getVacina().getId(), calendarioItem.getDose().getId(), (cartao != null) ? cartao.getId() : 0L});
                            if (imunizacao != null) {
                                imunizacoes.add(imunizacao);
                            }
                        }
                    }
                }

                calendario.setVacinasInSection(itemVacina);

                if(calendario.getVacinasInSection().size() == 0)
                    if(!idadesRemovidas.contains(idade))
                        idadesRemovidas.add(idade);
            }

            listaIdade.removeAll(idadesRemovidas);

            // notificação inicial geral sem criança
            if (criancas.isEmpty() || cartoes.isEmpty()) {
                String nomeUsuario = (usuario != null && usuario.getUsuaNome() != null) ? "Olá "+usuario.getUsuaNome() : "Olá "+ getCapitalizeNome(iDataManager.getCurrentUserName());
                repeatedNotificationInicial = showLocalNotification(context, CriancaListaActvity.class,
                        nomeUsuario + ", é importante que você cadastre sua(s) criança(s) para usufruir dos benefícios do "+getResources().getString(R.string.app_name)+". Vamos lá! Inicie o preenchimento do perfil da criança",
                        "Cadastrar Criança",
                        "Lembrete",
                        "preencha no cartão as informações das vacinas realizadas.",
                        0L, //idCartao
                        0L, //idVacina
                        0L, //idDose
                        0L  //idIdade
                );
                // Envia notificação
                getNotificationManager(context).notify(NOTIFICACAO_ID, Notification.FLAG_SHOW_LIGHTS, repeatedNotificationInicial.build());
            }

            statusCartaoVacinal = new ArrayList<String>();

            for (Idade idade : listaIdade) {
                for (Calendario calendarioItem : calendarios) {
                    this.calendarioItem = calendarioItem;
                    if (calendarioItem.getIdade().getId() == idade.getId()) {
                        String mesesIdadeCalendario = idade.getIdadDescricao().toLowerCase();
                        for (Cartao cartao : cartoes) {
                            for (Crianca crianca : criancas) {
                                if (cartao.getCrianca().getId() == crianca.getId()) {
                                    this.cartao = cartao;
                                    this.idade = idade;
                                    // notificação inicial geral sem imunizações
                                    if(!idLimiteLembrete.contains(cartao.getId())) {
                                        idLimiteLembrete.add(cartao.getId());
                                        notificaLembreteImunizacao(cartao);
                                    }
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
                                        case "2 anos":
                                            preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 24L, cartao);
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
                                            if( "Menina".equalsIgnoreCase(crianca.getCriaSexo()))
                                                preparaNotificacao(mesesIdadeCalendario, semanasIdadeCrianca, 108L, cartao);
                                            break;
                                        case "11 a 14 anos":
                                            if( "Menino".equalsIgnoreCase(crianca.getCriaSexo()))
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

    private void notificaLembreteImunizacao(final Cartao cartao) {
        if (!criancas.isEmpty() && imunizacoes.isEmpty()) {
            String nomeUsuario = (usuario != null && usuario.getUsuaNome() != null) ? "Olá "+usuario.getUsuaNome() : "Olá "+ getCapitalizeNome(iDataManager.getCurrentUserName());
            repeatedNotificationInicial = showLocalNotification(context, CartaoDetalheActivity.class,
                    nomeUsuario + ", é importante que você cadastre as doses das vacinas realizadas para usufruir dos benefícios do "+getResources().getString(R.string.app_name)+". Vamos lá! Inicie o cadastro das doses",
                    "Cadastrar imunização de "+cartao.getCrianca().getCriaNome(),
                    "Lembrete",
                    "selecione as vacinas no cartão, preencha e salve as informações das doses realizadas.",
                    cartao.getId(),
                    0L, //idVacina
                    0L, //idDose
                    0L  //idIdade
            );
            // Envia notificação
            getNotificationManager(context).notify(NOTIFICACAO_ID, Notification.FLAG_SHOW_LIGHTS, repeatedNotificationInicial.build());
        }

    }

    private void preparaNotificacao(String mesesIdade, Long semanasIdadeCrianca, Long baseQuantSemanas, Cartao cartao) {
        List<String> listaVacina = new ArrayList<String>();
        for (Calendario calendario : calendarios) {
            if (verificaImunizacoes(calendario.getVacina(), calendario.getDose(), cartao).isEmpty()) {
                for (Crianca crianca : criancas) {
                    if (crianca.getId().equals(cartao.getId())) {
                        if ("Pneumocócica 23V".equalsIgnoreCase(calendarioItem.getVacina().getVaciNome())) {
                            if (crianca.isCriaEtnia())
                                defineStatusVacina(mesesIdade, semanasIdadeCrianca, baseQuantSemanas, calendario, listaVacina);
                            break;
                        }
                        defineStatusVacina(mesesIdade, semanasIdadeCrianca, baseQuantSemanas, calendario, listaVacina);
                    }
                }
            } else {
                listaVacina.clear();
                for (Crianca crianca : criancas) {
                    List<Imunizacao> dosesImunizadas = iDataManager.obtemImunizacoes(new String[]{"vacina.id", "dose.id", "cartao.id"}, new Long[]{calendario.getVacina().getId(), calendario.getDose().getId(), cartao.getId()});
                    if (crianca.getId().equals(cartao.getId()) && "HPV".equalsIgnoreCase(calendario.getVacina().getVaciNome()) && dosesImunizadas.size() < 2) {
                        defineStatusVacina(mesesIdade, semanasIdadeCrianca, baseQuantSemanas, calendario, listaVacina);
                    }
                }
            }
            listaVacina.clear();
        }
    }

    private void defineStatusVacina(String mesesIdade, Long semanasIdadeCrianca, Long baseQuantSemanas, Calendario calendario, List<String> listaVacina) {
        if (semanasIdadeCrianca >= baseQuantSemanas && calendario.getIdade().getIdadDescricao().equalsIgnoreCase(mesesIdade)) {
            listaVacina.add(calendario.getIdade().getIdadDescricao());
            listaVacina.add(calendario.getVacina().getVaciNome());
            listaVacina.add(calendario.getDose().getDoseDescricao());

            String bigTexto = aplicaStatusVacina(semanasIdadeCrianca, baseQuantSemanas, cartao, listaVacina.get(0));
            threadNotificacao(cartao, calendario.getVacina(), calendario.getDose(), calendario.getIdade(), listaVacina, bigTexto);
        }
    }

    private List<Imunizacao> verificaImunizacoes(Vacina vacina, Dose dose, Cartao cartao) {
        List<Imunizacao> imunizacoes = iDataManager.obtemImunizacoes(new String[]{"vacina.id", "dose.id", "cartao.id"}, new Long[]{vacina.getId(), dose.getId(), cartao.getId()});
        return imunizacoes;
    }

    private String aplicaStatusVacina(Long semanasIdadeCrianca, Long baseQuantSemanas, Cartao cartao, String idadeVacinacao) {

        String statusVencendo = "Fique de olho na próxima dose de vacina para "+cartao.getCrianca().getCriaNome()+ ": " +idadeVacinacao.toUpperCase();
        String statusVencido = "Atenção! "+cartao.getCrianca().getCriaNome()+", possui vacina atrasada de "+idadeVacinacao.toUpperCase() +". Atualize o cartão vacinal";

        String status = null;

        if(baseQuantSemanas == 1L) {
            if (semanasIdadeCrianca < 1L) {
                status = statusVencendo;
            } else if (semanasIdadeCrianca >= 1L){
                status = statusVencido;
            }
        }
        if (semanasIdadeCrianca == baseQuantSemanas) {
            status = statusVencendo;
        }
        if (semanasIdadeCrianca > baseQuantSemanas) {
            status = statusVencido;
        }
        if ("Menina".equalsIgnoreCase(cartao.getCrianca().getCriaSexo()))
            if ("9 a 14 anos".equals(semanasIdadeCrianca)) {
                if (semanasIdadeCrianca > baseQuantSemanas && semanasIdadeCrianca <= baseQuantSemanas + 60L) {
                    status = statusVencido;
                }
            }
        if ("Menino".equalsIgnoreCase(cartao.getCrianca().getCriaSexo()))
            if ("11 a 14 anos".equals(semanasIdadeCrianca)) {
                if (semanasIdadeCrianca > baseQuantSemanas && semanasIdadeCrianca <= baseQuantSemanas + 36L) {
                    status = statusVencido;
                }
            }
        return status;
    }

    private void threadNotificacao(Cartao cartao, Vacina vacina, Dose dose, Idade idade, List<String> listaVacina, String bigTexto) {
        try {
            Thread.sleep(20000); //20Seg
            // notificação por etatus da vacina
            NOTIFICACAO_ID = "NOTIFICACAO_" + String.valueOf(randomId.nextInt((1000 - 1) + 1) + 1);
            String statusVacina = bigTexto.contains("Atenção!") ? "Vacina atrasada" : " Vacina precisa ser realizada";
            String generoCrianca = cartao.getCrianca().getCriaSexo().contains("Menino") ? "vacinado " : "vacinada";

            repeatedNotificationPadrao = showLocalNotification(context, ImunizacaoActivity.class,
                    bigTexto,
                    statusVacina,
                    "Lembrete",
                    cartao.getCrianca().getCriaNome()+ " precisa ser "+ generoCrianca + " com "+listaVacina.get(2) + " de " + listaVacina.get(1)+".",
                    cartao.getId(),
                    vacina.getId(),
                    dose.getId(),
                    idade.getId()
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private NotificationCompat.Builder showLocalNotification(Context context, Class<?> clsDestino, String textBig, String textContentTitle, String summaryTextTitle, String contentText, Long idCartao, Long idVacina, Long idDose, Long idIdade) {

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
        if ( idVacina != null && idDose != null && idIdade != null) {
            resultIntent.putExtra("vacina", idVacina);
            resultIntent.putExtra("dose", idDose);
            resultIntent.putExtra("idade", idIdade);
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(clsDestino);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent pendingIntent = PendingIntent.getActivities(context, NOTIFICACAO_REPLY, stackBuilder.getIntents(), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationCompactBuild.setContentIntent(pendingIntent);

        return notificationCompactBuild;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startNotificacaoForeground(Context context, Class<?> clsDestino, String textBig, String textContentTitle, String summaryTextTitle, String contentText, Long idCartao, Long idVacina, Long idDose, Long idIdade){
        //String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        //String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICACAO_ID, textContentTitle, NotificationManager.IMPORTANCE_HIGH);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, chan.getId());
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_notificacao)
                .setContentTitle(context.getString(R.string.app_name))
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

}
