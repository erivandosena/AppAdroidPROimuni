package br.com.erivando.vacinaskids.broadcast;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.ui.activity.cartao.CartaoDetalheActivity;
import br.com.erivando.vacinaskids.ui.application.AppAplicacao;

import static android.content.Context.ALARM_SERVICE;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   04 de Novembro de 2018 as 14:18
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class NotificationScheduler {
    public static final int DAILY_REMINDER_REQUEST_CODE = 100;
    public static final String TAG = "NotificationScheduler";

    public static void setReminder(Context context, Class<?> cls, int hour, int min) {
        Calendar calendar = Calendar.getInstance();

        Calendar setCalendar = Calendar.getInstance();
        setCalendar.set(Calendar.HOUR_OF_DAY, hour);
        setCalendar.set(Calendar.MINUTE, min);
        setCalendar.set(Calendar.SECOND, 0);

        // cancelar lembretes já agendados
        cancelReminder(context, cls);

        if (setCalendar.before(calendar))
            setCalendar.add(Calendar.DATE, 1);

        // Ativar um receptor
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public static void cancelReminder(Context context, Class<?> cls) {
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void showNotification(Context context, Class<?> cls, Class<?> clsDestino, String textBig, String textContentTitle, String summaryTextTitle, String contentText) {

        final String CANAL_ID = "NotificacoesID";

        Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_splash);
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
        mBuilder.setContentTitle(context.getString(R.string.app_name));
        mBuilder.setStyle(bigText);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setLargeIcon(icon1);
        mBuilder.setVibrate(new long[]{100, 300, 500, 800, 1000});
        mBuilder.setLights(Color.RED, 1000, 500);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        // Cria uma intenção explícita para uma atividade do aplicativo
        Intent resultIntent = new Intent(AppAplicacao.contextApp, clsDestino);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(AppAplicacao.contextApp);
        stackBuilder.addParentStack(clsDestino);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(DAILY_REMINDER_REQUEST_CODE, mBuilder.build());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void getNotificacaoInicial() {

        final String CANAL_ID = "NotificacoesID";

        Bitmap icon1 = BitmapFactory.decodeResource(AppAplicacao.contextApp.getResources(), R.drawable.ic_launcher_splash);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AppAplicacao.contextApp, CANAL_ID);
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

        bigText.bigText("Inicie os regitros das vacinas através do cartão vacinal, cadastrando as imunizações de cada dose");
        bigText.setBigContentTitle("Cartão Vacinal");
        bigText.setSummaryText("Lembrete!");

        mBuilder.setContentText("Cadastre as imunizações realizadas!");
        mBuilder.setContentTitle(AppAplicacao.contextApp.getResources().getString(R.string.app_name));
        mBuilder.setStyle(bigText);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setLargeIcon(icon1);
        mBuilder.setVibrate(new long[]{100, 300, 500, 800, 1000});
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
        NotificationManager mNotificationManager = (NotificationManager) AppAplicacao.contextApp.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(100, mBuilder.build());
    }

}
