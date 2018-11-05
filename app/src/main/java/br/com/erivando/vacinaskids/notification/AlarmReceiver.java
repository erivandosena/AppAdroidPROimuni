package br.com.erivando.vacinaskids.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.ui.activity.cartao.CartaoDetalheActivity;
import br.com.erivando.vacinaskids.ui.application.AppAplicacao;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   04 de Novembro de 2018 as 20:45
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Criar notificação
        Notification repeatedNotification = showLocalNotification(context, CartaoDetalheActivity.class,
                "Inicie os regitros das vacinas através do cartão vacinal, cadastrando as imunizações de cada dose",
                "Cartão Vacinal",
                "Lembrete!",
                "Cadastre as imunizações realizadas!"
        ).build();

        // Envia notificação local
        NotificationHelper.getNotificationManager(context).notify(NotificationHelper.ALARM_TYPE_ELAPSED, repeatedNotification);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static NotificationCompat.Builder showLocalNotification(Context context, Class<?> clsDestino, String textBig, String textContentTitle, String summaryTextTitle, String contentText) {

        final String CANAL_ID = "NotificacoesID";

        Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground);
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
        mBuilder.setSmallIcon(android.R.drawable.ic_dialog_info);
        mBuilder.setLargeIcon(icon1);
        mBuilder.setVibrate(new long[]{100, 300, 500, 800, 1000});
        mBuilder.setLights(0xff0080FF, 2000, 3000);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setAutoCancel(true);
        // Cria uma intenção explícita para uma atividade do aplicativo
        Intent resultIntent = new Intent(AppAplicacao.contextApp, clsDestino);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(AppAplicacao.contextApp);
        stackBuilder.addParentStack(clsDestino);
        stackBuilder.addNextIntent(resultIntent);

     //   PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivities(context, NotificationHelper.ALARM_TYPE_ELAPSED, stackBuilder.getIntents(), PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pendingIntent);
        //NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //mNotificationManager.notify(DAILY_REMINDER_REQUEST_CODE, mBuilder.build());

        return mBuilder;
    }

}
