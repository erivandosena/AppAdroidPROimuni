package br.com.erivando.vacinaskids.ui.activity.mapa;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.erivando.vacinaskids.R;
import br.com.erivando.vacinaskids.mvp.base.BaseActivity;
import br.com.erivando.vacinaskids.ui.activity.main.MainActivity;
import br.com.erivando.vacinaskids.ui.application.AppAplicacao;
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
public class MapLocationActivity extends BaseActivity implements MapaMvpView, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final String GOOGLE_API_KEY = "AIzaSyDfjILD6c4LpiXBEHJrEvWdk726eXlyLZA";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private int PROXIMITY_RADIUS_1 = 5000;
    private int PROXIMITY_RADIUS_2 = 10000;

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private LocationManager locationManager;

    private double mLatitudePlaces;
    private double mLongitudePlaces;

    private boolean statusChangedMapa;

    @BindView(R.id.toolbar_mapa)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar_mapa)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.btn_pesquisa)
    Button buttonPesquisa;

    @BindView(R.id.text_pesquisa)
    TextView textPesquisa;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MapLocationActivity.class);
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

        buttonPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLastLocation != null)
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
            //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11.8f));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.2f));
        }

        getPesquisa(null);
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
        Geocoder geocoder = new Geocoder(MapLocationActivity.this);
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
            //Place current location marker
            markerOptions.position(latLng);
            markerOptions.title("Você");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round));
            markerOptions.snippet((subLocality != null ? subLocality + "\n" + (locality != null ? locality : "") + " - " + (adminArea != null ? adminArea : "") : "Local atual")).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round));
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

        if(!statusChangedMapa) {
            //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10.5f));
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

        /*
        final Map<Marker, Map<String, Object>> markers = new HashMap<>();
        Map<String, Object> dataModel = new HashMap<>();

        dataModel.put("locality", locality);
        dataModel.put("subLocality", subLocality);
        dataModel.put("adminArea", adminArea);
        dataModel.put("latitude", latLng.latitude);
        dataModel.put("longitude", latLng.longitude);

        Marker marker = mGoogleMap.addMarker(markerOptions);
        markers.put(marker, dataModel);
        */


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
                Context context = MapLocationActivity.this;

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

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
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
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissão negada!", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_launcher_round)
                        .setTitle("Permissão de Localização")
                        .setMessage(getResources().getString(R.string.app_name)+" precisa da permissão de localização, por favor, confirme em aceitar para utilizar a funcionalidade de localização.")
                        .setPositiveButton("Aceitar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapLocationActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
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
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
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
