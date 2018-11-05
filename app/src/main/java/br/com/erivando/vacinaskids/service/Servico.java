package br.com.erivando.vacinaskids.service;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import javax.inject.Inject;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.database.IDataManager;
import br.com.erivando.vacinaskids.database.model.Cartao;
import br.com.erivando.vacinaskids.di.component.DaggerServiceComponent;
import br.com.erivando.vacinaskids.di.component.ServiceComponent;
import br.com.erivando.vacinaskids.ui.activity.cartao.CartaoDetalheActivity;
import br.com.erivando.vacinaskids.ui.activity.main.MainActivity;
import br.com.erivando.vacinaskids.ui.application.AppAplicacao;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   31 de Outubro de 2018 as 10:19
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class Servico extends IntentService {

    public static final int NOTIFICATION_ID = 234;
    private Handler mHandler;


    // Notificacoes de sincronizacao geral
    public static final String ACTION_NOTIFY_SYNC_STATUS = "br.com.phaneronsoft.alarmmanagerstartservice.ACTION_NOTIFY_SYNC_STATUS";
    public static final String EXTRA_STATUS_PROGRESS = "extraStatusProgress";
    public static final String EXTRA_IS_FINISHED = "extraIsFinished";

    private final String TAG = getClass().getSimpleName();

    private Context mContext = this;

    // Progressbar
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotifyManager;
    private int notificationId = 3; // Nao pode ser 0
    private String mChannelId = "channel-sync";
    private String mChannelName = "Sinchronization";

    @Inject
    IDataManager iDataManager;


    public Servico() {
        super("Servico");
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
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            Thread.sleep(5000);
            getNotificacaoInicial();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stopSelf();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void getNotificacaoInicial(Context context, Class<?> cls, Class<?> clsDestino, String textBig, String textContentTitle, String summaryTextTitle, String contentText) {

        final String CANAL_ID = "NotificacoesID";

        Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_splash);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AppAplicacao.contextApp, CANAL_ID);
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

       // bigText.bigText("Inicie os regitros das vacinas através do cartão vacinal, cadastrando as imunizações de cada dose");
       // bigText.setBigContentTitle("Cartão Vacinal");
       // bigText.setSummaryText("Lembrete!");
       // mBuilder.setContentText("Cadastre as imunizações realizadas!");

        bigText.bigText(textBig);
        bigText.setBigContentTitle(textContentTitle);
        bigText.setSummaryText(summaryTextTitle);

        mBuilder.setContentText(contentText);
        mBuilder.setContentTitle(getString(R.string.app_name));
        mBuilder.setStyle(bigText);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setLargeIcon(icon1);
        mBuilder.setVibrate(new long[] {100, 300, 500, 800, 1000});
        mBuilder.setLights(Color.RED, 1000, 500);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setPriority(Notification.PRIORITY_MAX);

        // Cria uma intenção explícita para uma atividade em seu aplicativo
        Intent resultIntent = new Intent(AppAplicacao.contextApp, clsDestino);
        //resultIntent.putExtra("cartaoLista", "cartao");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(AppAplicacao.contextApp);
        stackBuilder.addParentStack(clsDestino);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(100, mBuilder.build());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void getNotificacaoInicial() {

        final String CANAL_ID = "NotificacoesID";

        Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_splash);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AppAplicacao.contextApp, CANAL_ID);
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

        bigText.bigText("Inicie os regitros das vacinas através do cartão vacinal, cadastrando as imunizações de cada dose");
        bigText.setBigContentTitle("Cartão Vacinal");
        bigText.setSummaryText("Lembrete!");

        mBuilder.setContentText("Cadastre as imunizações realizadas!");
        mBuilder.setContentTitle(getString(R.string.app_name));
        mBuilder.setStyle(bigText);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setLargeIcon(icon1);
        mBuilder.setVibrate(new long[] {100, 300, 500, 800, 1000});
        mBuilder.setLights(Color.RED, 1000, 500);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setPriority(Notification.PRIORITY_MAX);

        // Cria uma intenção explícita para uma atividade em seu aplicativo
        Intent resultIntent = new Intent(AppAplicacao.contextApp, CartaoDetalheActivity.class);
        //resultIntent.putExtra("cartaoLista", "cartao");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(AppAplicacao.contextApp);
        stackBuilder.addParentStack(CartaoDetalheActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(100, mBuilder.build());
    }

}
