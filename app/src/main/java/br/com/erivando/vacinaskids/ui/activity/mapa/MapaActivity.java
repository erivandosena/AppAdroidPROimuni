package br.com.erivando.vacinaskids.ui.activity.mapa;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.activity.main.MainActivity;
import br.com.erivando.vacinaskids.ui.application.AppAplicacao;
import br.com.erivando.vacinaskids.util.Uteis;
import br.com.erivando.vacinaskids.util.geolocalizacao.GooglePlacesReadTask;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Projeto:     VacinasKIDS
 * Autor:       Erivando Sena
 * Data/Hora:   28 de Outubro de 2018 as 13:40
 * Local:       Fortaleza/CE
 * E-mail:      erivandoramos@bol.com.br
 */
public class MapaActivity extends BaseActivity implements MapaMvpView, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String GOOGLE_API_KEY = "AIzaSyDL6QJ2bIdajnGjaGWzdCTlCFNbEMx0BRY";

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;

    private int PROXIMITY_RADIUS_1 = 5000;
    private int PROXIMITY_RADIUS_2 = 10000;
    private LocationManager locationManager;
    private double mLatitudePlaces;
    private double mLongitudePlaces;
    private boolean statusChangedMapa;
    private Drawable iconeDrawable;

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

        getActivityComponent().inject(this);

        iconeDrawable = getResources().getDrawable(R.drawable.ic_launcher_round);

        buttonPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLastLocation != null)
                    getPesquisa(textPesquisa.getText().toString());
            }
        });

        if (!isGooglePlayServicesAvailable()) {
            onError("Não foi possível conexão com GooglePlayServices");
        } else {
            // Obtenha o SupportMapFragment e seja notificado quando o mapa estiver pronto para ser usado.
            initMapaLocalizacao();
        }
    }

    private void initMapaLocalizacao() {
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    private void initLocalizacao(Location location) {
        Log.e("locationPlaces", String.valueOf(location));
        if (location != null) {
            mLatitudePlaces = location.getLatitude();
            mLongitudePlaces = location.getLongitude();

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.2f));
        }

        getPesquisa(null);
    }

    private void getPesquisa(String pesquisa) {
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
            toPass[0] = mGoogleMap;
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
            toPass[0] = mGoogleMap;
            toPass[1] = googlePlacesUrl.toString();
            googlePlacesReadTask.execute(toPass);
        }
    }

    @Override
    protected void setUp() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.openMainActivity();
    }

    private void openMainActivity() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
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
    public Context getContextActivity() {
        return getContextActivity();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Geocoder geocoder = new Geocoder(MapaActivity.this);
        final MarkerOptions markerOptions = new MarkerOptions();

        String locality = null;
        String subLocality = null;
        String adminArea = null;

        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                locality = addressList.get(0).getLocality();
                subLocality = addressList.get(0).getSubLocality();
                adminArea = addressList.get(0).getAdminArea();
            }
            markerOptions.position(latLng);
            markerOptions.title("Você");
            markerOptions.snippet((subLocality != null ? subLocality + "\n" + (locality != null ? locality : "") + " - " + (adminArea != null ? adminArea : "") : "Local atual"));
            BitmapDescriptor markerIcon = getMarkerIconFromDrawable(iconeDrawable);
            if(markerIcon != null) {
                markerOptions.icon(markerIcon);
            }
            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                statusChangedMapa = true;
            }
        });

        if (!statusChangedMapa) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.5f));
            initLocalizacao(mLastLocation);
            statusChangedMapa = true;
        }

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final LatLng position = marker.getPosition();
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 12.2f), 1000, new GoogleMap.CancelableCallback() {

                    @Override
                    public void onFinish() {
                        Projection projection = mGoogleMap.getProjection();
                        Point point = projection.toScreenLocation(position);
                        point.x -= 100;
                        point.y -= 100;
                        LatLng offsetPosition = projection.fromScreenLocation(point);
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(offsetPosition), 300, null);
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                return true;
            }
        });

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker != null && !"Você".equals(marker.getTitle())) {
                    try {
                        //Navegar com Waze
                        String url = "waze://?ll=" + marker.getPosition().latitude + "," + marker.getPosition().longitude + "&navigate=yes";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Se o Waze não estiver instalado, abra-o no Google Play
                        Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + marker.getPosition().latitude + "," + marker.getPosition().longitude));
                        startActivity(navigation);
                    }
                }
            }
        });

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng position) {
                List<CameraUpdate> updates = new ArrayList<CameraUpdate>();
                CameraPosition.Builder builder = CameraPosition.builder();
                builder.target(position);
                updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
                builder.target(new LatLng(position.latitude + 20, position.longitude));
                updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
                builder.bearing(90);
                updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
                builder.target(new LatLng(position.latitude + 20, position.longitude + 40));
                updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
                builder.bearing(180);
                updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
                builder.target(new LatLng(position.latitude, position.longitude + 40));
                updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
                builder.bearing(270);
                updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
                builder.target(position);
                updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));

                loopAnimateCamera(updates);
            }
        });

        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng position) {
                mGoogleMap.stopAnimation();
            }
        });
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        int larguraIcone = 120;
        int alturaIcone = 120;
        Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, larguraIcone, alturaIcone, false);
        return BitmapDescriptorFactory.fromBitmap(smallMarker);
    }

    private void loopAnimateCamera(final List<CameraUpdate> updates) {
        CameraUpdate update = updates.remove(0);
        updates.add(update);
        mGoogleMap.animateCamera(update, 1000, new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {
                loopAnimateCamera(updates);
            }

            @Override
            public void onCancel() {
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
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

        //Inicializa o Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Permissão de localização já concedida
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Solicitar permissão de local
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //Parar as atualizações de localização quando a Atividade não estiver mais ativa
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permissão negada!", Toast.LENGTH_LONG).show();
                }
                return;
            }
            /* outras linhas de 'casos' para verificar outras permissões que esse aplicativo pode solicitar */
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Devemos mostrar uma explicação
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Mostra uma explicação para o usuário de forma assíncrona não bloqueie este
                // thread esperando pela resposta do usuário! Depois que o usuário vê a explicação,
                // tente novamente solicitar a permissão.
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_launcher_round)
                        .setTitle("Permissão de Localização")
                        .setMessage(getResources().getString(R.string.app_name) + " precisa da permissão de localização, por favor, confirme em aceitar para utilizar a funcionalidade de localização.")
                        .setPositiveButton("Aceitar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapaActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                // Nenhuma explicação necessária, podemos solicitar a permissão.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

}
