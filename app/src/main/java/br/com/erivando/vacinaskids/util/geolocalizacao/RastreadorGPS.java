package br.com.erivando.vacinaskids.util.geolocalizacao;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   26 de Outubro de 2018 as 12:35
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class RastreadorGPS extends Service implements LocationListener {

    private final Context mContext;

    // flag para GPS status
    boolean isGPSEnabled = false;

    // flag para network status
    boolean isNetworkEnabled = false;

    // flag para GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // A distância mínima para alterar atualizações em metros
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // O tempo mínimo entre atualizações em milissegundos
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaração Location Manager
    protected LocationManager locationManager;

    public RastreadorGPS(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // obtem status do GPS
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // obtem status da Rede
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // nenhum provedor de rede habilitado
            } else {
                this.canGetLocation = true;
                // Primeiro, obter a localização do provedor de rede
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return null;
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.d("Network", "Rede");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                // se o GPS estiver habilitado obter lat/long usando o GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("GPS Enabled", "GPS Ligado!");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Pare de usar o ouvinte de GPS
     * Chamar esta função irá parar de usar o GPS no seu aplicativo
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(RastreadorGPS.this);
        }
    }

    /**
     * Função para obter latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Função para obter longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Função para verificar GPS / Wi-Fi habilitado
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Função para mostrar a caixa de diálogo de alerta de configurações
     * Ao pressionar o botão Configurações irá iniciar Opções de Configurações
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("Ajustes do GPS");
        alertDialog.setMessage("GPS não está ativado. Você quer ir ao menu de configurações?");

        // Botão de configuração
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // Botão de canecalamento
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
