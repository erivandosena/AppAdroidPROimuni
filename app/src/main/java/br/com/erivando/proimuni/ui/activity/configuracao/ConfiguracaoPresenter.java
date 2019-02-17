package br.com.erivando.proimuni.ui.activity.configuracao;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import javax.inject.Inject;

import br.com.erivando.proimuni.broadcast.Notificacao;
import br.com.erivando.proimuni.database.IDataManager;
import br.com.erivando.proimuni.mvp.base.BasePresenter;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   11 de Novembro de 2018 as 11:53
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */


public class ConfiguracaoPresenter<V extends ConfiguracaoMvpView> extends BasePresenter<V> implements ConfiguracaoMvpPresenter<V> {

    private Intent alarm;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;

    @Inject
    public ConfiguracaoPresenter(IDataManager iDataManager) {
        super(iDataManager);
    }

    @Override
    public void iniciaServicoNotificacao(Context context) {
        alarm = new Intent(context, Notificacao.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(context, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        long intervalo = (50000 * 1440) / 2L;
        if (alarmRunning == false) {
            pendingIntent = PendingIntent.getBroadcast(context, 0, alarm, 0);
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), intervalo, pendingIntent);
        }
    }

    @Override
    public void finalizaServicoNotificacao(Context context) {
        alarm = new Intent(context, Notificacao.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(context, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if (alarmRunning == true) {
            pendingIntent = PendingIntent.getBroadcast(context, 0, alarm, 0);
            pendingIntent.cancel();
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
    }

    @Override
    public boolean onNotificacoes() {
        return getIDataManager().isNotificacoes();
    }

    @Override
    public void onAtualizaNotificacoes(boolean defValue) {
        getIDataManager().setNotificacoes(defValue);
    }

    @Override
    public boolean onRedeVacinas() {
        return getIDataManager().isRedeVacinas();
    }

    @Override
    public void onAtualizaRedeVacinas(boolean defValue) {
        getIDataManager().setRedeVacinas(defValue);
    }
}
