package br.com.erivando.vacinaskids.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   03 de Agosto de 2018 as 08:53
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public class InternetDetector {

    private Context mcontext;

    public InternetDetector(Context context) {
        this.mcontext = context;
    }

    public boolean checkMobileInternetConn() {
        ConnectivityManager connectivity = (ConnectivityManager) mcontext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo info1 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo info2 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (info1 != null && info2 != null) {
                if (info1.isConnected() || info2.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
}
