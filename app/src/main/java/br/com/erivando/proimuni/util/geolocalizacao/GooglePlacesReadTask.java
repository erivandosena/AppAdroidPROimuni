package br.com.erivando.proimuni.util.geolocalizacao;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   25 de Outubro de 2018 as 07:26
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {
    String googlePlacesData = null;
    GoogleMap mMap;

    @Override
    protected String doInBackground(Object... inputObj) {
        try {
            mMap = (GoogleMap) inputObj[0];
            String googlePlacesUrl = (String) inputObj[1];
            Http http = new Http();
            googlePlacesData = http.read(googlePlacesUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask();
        Object[] toPass = new Object[2];
        toPass[0] = mMap;
        toPass[1] = result;
        placesDisplayTask.execute(toPass);
    }
}
