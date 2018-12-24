package br.com.erivando.proimuni.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.erivando.proimuni.R;

/**
 * Projeto:     VacinasKIDs
 * Autor:       Erivando Sena
 * Data/Hora:   08 de Julho de 2018 as 17:08
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */

public final class NetworkUtils {

    private NetworkUtils() {
        // This utility class is not publicly instantiable
    }

    public static boolean isNetworkConnected(Context context) {
        /*
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected)
            Toast.makeText(context, R.string.aviso_sem_internet, Toast.LENGTH_SHORT).show();
        return isConnected;
        */


        /*
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
        } else {
            Toast.makeText(context, R.string.aviso_sem_internet, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
        */


        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        try {
                            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com.br").openConnection());
                            urlc.setRequestProperty("User-Agent", "Test");
                            urlc.setRequestProperty("Connection", "close");
                            urlc.setConnectTimeout(500); //choose your own timeframe
                            urlc.setReadTimeout(500); //choose your own timeframe
                            urlc.connect();
                            return (urlc.getResponseCode() == 200);
                        } catch (IOException e) {
                            Toast.makeText(context, R.string.aviso_sem_internet, Toast.LENGTH_SHORT).show();
                            return false;  //connectivity exists, but no internet.
                        }
                    }

        }
        Toast.makeText(context, R.string.aviso_sem_internet, Toast.LENGTH_SHORT).show();
        return false;
    }

}
