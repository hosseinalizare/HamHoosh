package com.example.koohestantest1.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.koohestantest1.R;
import com.example.koohestantest1.databinding.ActivityMapsBinding;
import com.example.koohestantest1.fragments.CompanySettingDetailsFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastLocation;
    private ActivityMapsBinding binding;
    private MarkerOptions latestMarker;
    public static String INTENT_KEY_LAT = "lat";
    public static String INTENT_KEY_LONG = "long";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String SP_KEY_FIRST_TIME = "isfirsttime";
    private LocationManager locationManager;
    private double receivedLat;
    private double receivedLong;

    private boolean shouldUpdateLocation = true;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    @SuppressLint("MissingPermission")
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            mapUiOptions();
        } else
            Toast.makeText(this, "برای پیدا کردن موقعیت فعلی، نیاز به اجازه دسترسی داریم", Toast.LENGTH_LONG).show();
    });

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        sharedPreferences = getSharedPreferences("MapChecker", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //first time guide
        if (sharedPreferences.getBoolean(SP_KEY_FIRST_TIME, true)) {
            View view = getLayoutInflater().inflate(R.layout.view_guide_location, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setView(view)
                    .setPositiveButton("فهمیدم", (dialog, which) -> {
                        editor.putBoolean(SP_KEY_FIRST_TIME, false).apply();
                        dialog.dismiss();
                    })
                    .setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        binding.ivMapDone.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(INTENT_KEY_LAT, latestMarker.getPosition().latitude);
            intent.putExtra(INTENT_KEY_LONG, latestMarker.getPosition().longitude);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });

        Intent intent = getIntent();
        boolean companyMapState = intent.getBooleanExtra(CompanySettingDetailsFragment.KEY_INTENT_STATE_COMPANY_SETTING, false);
        receivedLat = intent.getDoubleExtra("resLat", -1);
        receivedLong = intent.getDoubleExtra("resLong", -1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(this);

        setUpMap();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }
        mapUiOptions();
    }

    @SuppressLint("MissingPermission")
    private void mapUiOptions() {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5 * 1000); // 5 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                Location location = locationResult.getLastLocation();
                lastLocation = location;
                placeMarkerOnMap(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        };

        mMap.setMyLocationEnabled(true);

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                lastLocation = location;
                LatLng currentLatLng;
                if (receivedLat != -1 && receivedLong != -1) {
                    currentLatLng = new LatLng(receivedLat, receivedLong);
                } else {
                    currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                }
                placeMarkerOnMap(currentLatLng);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f));
            }
        });


        mMap.setOnMyLocationButtonClickListener(() -> {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "لطفا gps  موبایل رو روشن کنید", Toast.LENGTH_SHORT).show();
            } else {
                if (lastLocation != null) {

                    LatLng currentLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    placeMarkerOnMap(currentLatLng);
                }
            }

            return false;
        });


        mMap.setOnMapClickListener(latLng -> {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);

            placeMarkerOnMap(latLng);
        });

    }

    private void placeMarkerOnMap(LatLng latLng) {
        mMap.clear();

        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        latestMarker = markerOptions;

        markerOptions.title("آدرس نهایی");

        mMap.addMarker(markerOptions);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Toast.makeText(this, "در حال دریافت موقعیت، چند ثانیه  صبر کنید...", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}