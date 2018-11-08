package br.com.erivando.vacinaskids.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.service.Servico;
import br.com.erivando.vacinaskids.ui.activity.cartao.CartaoDetalheActivity;
import br.com.erivando.vacinaskids.ui.activity.main.MainActivity;

import static android.app.PendingIntent.FLAG_ONE_SHOT;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   01 de Novembro de 2018 as 23:54
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class AlarmNotificationReceiver extends BroadcastReceiver {

    String TAG = "AlarmNotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                LocalData localData = new LocalData(context);
                NotificationScheduler.setReminder(context, AlarmNotificationReceiver.class, localData.get_hour(), localData.get_min());
                return;
            }
        }
        //Trigger the notification
        NotificationScheduler.showNotification(
                context,
                Servico.class,
                CartaoDetalheActivity.class,
                "Inicie os regitros das vacinas através do cartão vacinal, cadastrando as imunizações de cada dose",
                "Cartão Vacinal",
                "Lembrete!",
                "Cadastre as imunizações realizadas!");
    }

}
