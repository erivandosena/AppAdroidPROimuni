package br.com.erivando.vacinaskids.notification;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   04 de Novembro de 2018 as 20:45
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class NotificationHelper {
    public static int ALARM_TYPE_RTC = 100;
    private static AlarmManager alarmManagerRTC;
    private static PendingIntent alarmIntentRTC;

    public static int ALARM_TYPE_ELAPSED = 101;
    private static AlarmManager alarmManagerElapsed;
    private static PendingIntent alarmIntentElapsed;

    /**
     * This is the real time /wall clock time
     * @param context
     */
    public static void scheduleRepeatingRTCNotification(Context context, String hour, String min) {
        //obter a instância do calendário para poder selecionar a hora em que a notificação deve ser agendada
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        //Definir a hora do dia (8:00 aqui) quando a notificação será enviada todos os dias (padrão)
        calendar.set(Calendar.HOUR_OF_DAY, Integer.getInteger(hour, 10), Integer.getInteger(min, 0));

        //Definindo a intenção para a classe na qual a mensagem de transmissão de alarme será tratada
        Intent intent = new Intent(context, AlarmReceiver.class);
        //Setting alarm pending intent
        alarmIntentRTC = PendingIntent.getBroadcast(context, ALARM_TYPE_RTC, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //obtendo instância do serviço AlarmManager
        alarmManagerRTC = (AlarmManager)context.getSystemService(ALARM_SERVICE);

        // Configurando o alarme para ativar o dispositivo todos os dias no horário do relógio.
        // AlarmManager.RTC_WAKEUP é responsável por despertar o dispositivo com certeza, o que pode não ser uma boa prática durante o tempo
        // Use o RTC quando não precisar ativar o dispositivo, mas quiser entregar a notificação sempre que o dispositivo for ativado
        // Vamos usar o RTC.WAKEUP apenas para demonstração
        alarmManagerRTC.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntentRTC);
    }

    /***
     * Esta é outra maneira de programar notificações usando o tempo decorrido.
     * É baseado no tempo relativo desde que o dispositivo foi inicializado.
     * @param context
     */
    public static void scheduleRepeatingElapsedNotification(Context context) {
        // Definindo a intenção para a classe em que a notificação será tratada
        Intent intent = new Intent(context, AlarmReceiver.class);

        // Definir a intenção pendente de responder à transmissão enviada pelo AlarmManager todos os dias às 8h
        alarmIntentElapsed = PendingIntent.getBroadcast(context, ALARM_TYPE_ELAPSED, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //obtendo instância do serviço AlarmManager
        alarmManagerElapsed = (AlarmManager)context.getSystemService(ALARM_SERVICE);

        // Inexacto alarme todos os dias desde que o dispositivo foi inicializado.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);

        //alarmManagerElapsed.setInexactRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_HALF_DAY, alarmIntentElapsed);
        alarmManagerElapsed.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntentElapsed);
    }

    public static void cancelAlarmRTC() {
        if (alarmManagerRTC!= null) {
            alarmManagerRTC.cancel(alarmIntentRTC);
        }
    }

    public static void cancelAlarmElapsed() {
        if (alarmManagerElapsed!= null) {
            alarmManagerElapsed.cancel(alarmIntentElapsed);
        }
    }

    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * Ativar o receptor de inicialização para manter os alarmes configurados
     * para notificações nas reinicializações do dispositivo
     */
    public static void enableBootReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, AlarmBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    /**
     * Desativar o receptor de inicialização quando o usuário cancela/cancela notificações
     */
    public static void disableBootReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, AlarmBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
}
