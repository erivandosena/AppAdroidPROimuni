package br.com.erivando.vacinaskids.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import br.com.erivando.vacinaskids.service.Servico;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   08 de Novembro de 2018 as 23:39
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class Notifica extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, Servico.class));;
    }
}
