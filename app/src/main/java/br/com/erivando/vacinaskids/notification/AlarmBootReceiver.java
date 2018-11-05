package br.com.erivando.vacinaskids.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   04 de Novembro de 2018 as 20:45
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class AlarmBootReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            NotificationHelper.scheduleRepeatingElapsedNotification(context);
            NotificationHelper.scheduleRepeatingRTCNotification(context, "10", "0");
        }
    }
}
