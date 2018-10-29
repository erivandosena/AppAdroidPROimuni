package br.com.erivando.vacinaskids.util.rx;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   26 de Outubro de 2018 as 14:54
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class GeocoderHandler extends Handler {
    private String locationAddress;

    @Override
    public void handleMessage(Message message) {
        String locationAddress;
        switch (message.what) {
            case 1:
                Bundle bundle = message.getData();
                locationAddress = bundle.getString("address");
                break;
            default:
                locationAddress = null;
        }
        this.locationAddress = locationAddress;
    }

    public String getLcationAddress() {
        return locationAddress;
    }
}
