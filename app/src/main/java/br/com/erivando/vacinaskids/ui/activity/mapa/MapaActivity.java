package br.com.erivando.vacinaskids.ui.activity.mapa;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.activity.main.MainActivity;
import br.com.erivando.vacinaskids.util.geolocalizacao.GooglePlacesReadTask;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MapaActivity extends BaseActivity implements OnMapReadyCallback, MapaMvpView {

    private static final String GOOGLE_API_KEY = "AIzaSyDfjILD6c4LpiXBEHJrEvWdk726eXlyLZA";



    private GoogleMap mMap;
    private LocationManager locationManager;

    private double mLatitudePlaces;
    private double mLongitudePlaces;

    private GoogleMap.OnCameraIdleListener onCameraIdleListener;

    private int PROXIMITY_RADIUS_1 = 5000;
    private int PROXIMITY_RADIUS_2 = 10000;
    private int REQUEST_CHECK_SETTINGS = 100;
    final static int REQUEST_LOCATION = 199;

    @BindView(R.id.toolbar_mapa)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar_mapa)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.btn_pesquisa)
    Button buttonPesquisa;

    @BindView(R.id.text_pesquisa)
    TextView textPesquisa;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MapaActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mapa);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbar.setTitle(getResources().getString(R.string.text_mapa_titulo));

        if (!isGooglePlayServicesAvailable()) {
            onError("Não foi possível conexão com GooglePlayServices");
        } else {
            // Obtenha o SupportMapFragment e seja notificado quando o mapa estiver pronto para ser usado.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            buttonPesquisa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPesquisa(textPesquisa.getText().toString());
                }
            });
        }
    }

    @Override
    protected void setUp() {
    }

    private void getPesquisa(String pesquisa) {
        // https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=mongolian%20grill&inputtype=textquery&fields=photos,formatted_address,name,opening_hours,rating&locationbias=circle:2000@47.6918452,-122.2226413&key=YOUR_API_KEY
        // https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-3.8059733333333337,-38.554425&radius=5000&keyword=posto%20de%20sa%C3%BAde&key=AIzaSyDfjILD6c4LpiXBEHJrEvWdk726eXlyLZA

        if ((pesquisa == null) || (pesquisa.trim().isEmpty())) {
            pesquisa = "Posto de Saúde";

            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            googlePlacesUrl.append("location=" + mLatitudePlaces + "," + mLongitudePlaces);
            googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS_1);
            googlePlacesUrl.append("&keyword=" + Uri.encode(pesquisa));
            googlePlacesUrl.append("&fields=photos,formatted_address,name,opening_hours,rating");
            googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);

            GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
            Object[] toPass = new Object[2];
            toPass[0] = mMap;
            toPass[1] = googlePlacesUrl.toString();
            googlePlacesReadTask.execute(toPass);

        } else {
            if (pesquisa.trim().toLowerCase().contains("vacina") || pesquisa.trim().toLowerCase().contains("vacinação") || pesquisa.trim().toLowerCase().contains("vacinacao")) {
                pesquisa = "Posto de Saúde " + pesquisa;
            }
            if (!pesquisa.trim().toLowerCase().contains("saúde") || !pesquisa.trim().toLowerCase().contains("saude")) {
                pesquisa = "Posto de Saúde " + pesquisa;
            }

            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            googlePlacesUrl.append("location=" + mLatitudePlaces + "," + mLongitudePlaces);
            googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS_2);
            googlePlacesUrl.append("&keyword=" + Uri.encode(pesquisa));
            googlePlacesUrl.append("&fields=photos,formatted_address,name,opening_hours,rating");
            googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);

            GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
            Object[] toPass = new Object[2];
            toPass[0] = mMap;
            toPass[1] = googlePlacesUrl.toString();
            googlePlacesReadTask.execute(toPass);
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.openMainActivity();
    }

    public void openMainActivity() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }

    /**
     * Manipula o mapa uma vez disponível.
     * Este retorno de chamada é acionado quando o mapa está pronto para ser usado.
     * É aqui que podemos adicionar marcadores ou linhas, adicionar ouvintes ou mover a câmera. Nesse caso,
     * nós apenas adicionamos um marcador perto de Sydney, Austrália.
     * Se o Google Play Services não estiver instalado no dispositivo, o usuário será solicitado a instalar
     * dentro do SupportMapFragment. Este método só será acionado quando o usuário tiver
     * Instalado o Google Play Services e retornado ao aplicativo.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Considere chamar
            // ActivityCompat#requestPermissions
            // aqui para solicitar as permissões perdidas e, em seguida, substituir
            // public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
            // para lidar com o caso em que o usuário concede a permissão. Veja a documentação
            // para ActivityCompat#requestPermissions para mais detalhes.
            return;
        }
        Location locationPlaces = locationManager.getLastKnownLocation(bestProvider);

        Log.e("locationPlaces", String.valueOf(locationPlaces));

        if (locationPlaces != null) {
            mLatitudePlaces = locationPlaces.getLatitude();
            mLongitudePlaces = locationPlaces.getLongitude();

            LatLng latLngPlaces = new LatLng(mLatitudePlaces, mLongitudePlaces);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngPlaces, 11.5f));

            getPesquisa(null);

        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location locationUser = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (locationUser != null) {
            final LatLng latLngUser = new LatLng(locationUser.getLatitude(), locationUser.getLongitude());

            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    Geocoder geocoder = new Geocoder(MapaActivity.this);
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latLngUser.latitude, latLngUser.longitude, 1);
                        if (addressList != null && addressList.size() > 0) {
                            String locality = addressList.get(0).getLocality();
                            String subLocality = addressList.get(0).getSubLocality();
                            String adminArea = addressList.get(0).getAdminArea();
                            mMap.addMarker(new MarkerOptions().position(latLngUser).title("Você").snippet((subLocality != null ? subLocality + "\n" + (locality != null ? locality : "") + " - " + (adminArea != null ? adminArea : "") : "Local atual")).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round)));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngUser, 12.1f));
        }

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Context context = MapaActivity.this;

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

    }

    @Override
    public Context getContextActivity() {
        return getContextActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("onActivityResult()", Integer.toString(resultCode));

        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        // Todas as alterações necessárias realizadas com sucesso
                        Toast.makeText(MapaActivity.this, "GPS ativado!", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        // O usuário foi solicitado a alterar as configurações, mas optou por não fazer
                        Toast.makeText(MapaActivity.this, "GPS desativado!", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }

    }



}
