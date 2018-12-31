package br.com.erivando.proimuni.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import br.com.erivando.proimuni.service.Servico;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   08 de Novembro de 2018 as 23:39
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class Notificacao extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent background = new Intent(context, Servico.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(background);
        } else {
            context.startService(background);
        }
    }
}
