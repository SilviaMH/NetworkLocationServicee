package com.example.networklocationservicee;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener, View.OnClickListener {

    private LocationManager locationManager = null;
    private LatLng currentLocation = new LatLng(19.432608, -99.133208);
    private boolean moveCameraCurrentLocation = true;
    private static final int DEFAULT_ZOOM_LEVEL = 19;
    public int timeUpdateLocation = 2000;
    public float distanceUpateLocation = (float)0.05;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((EditText)findViewById(R.id.editTextDistance)).setText(String.valueOf(distanceUpateLocation));
        ((EditText)findViewById(R.id.editTextTime)).setText(String.valueOf(timeUpdateLocation));
        initGoogleMaps();
    }

    private void initGoogleMaps() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Log.e("initgoogle","initGoogleMaps Activity");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Add a marker in Mexico City and move the camera
        LatLng mexicoCity = new LatLng(19.432608, -99.133209);
        this.googleMap.addMarker(new MarkerOptions().position(mexicoCity).title("Marker in Sydney"));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(mexicoCity));
        //Log.e("mapready","onMapReady Activity");
        initLocationService();
    }

    private void initLocationService() {
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestPermissions(new String[] {
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET
                }, 10);
                //Log.e("initLocationService","requestPermisssions Activity");
            } else {
                //Log.e("initLocationService","noPermissionRequestNeeded Activity");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        timeUpdateLocation, distanceUpateLocation, this);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("onLocationChanged","onLocationChanged Activity");
        // This method is called when the location changes.
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        currentLocation = new LatLng(latitude, longitude);
        Log.d("onLocationChanged", "Latitud:" + latitude + " Longitud:" + longitude);
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title("Posición Actual")
                .icon(BitmapDescriptorFactory.defaultMarker(new Random().nextInt(360))));
        if(moveCameraCurrentLocation) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentLocation)
                    .zoom(DEFAULT_ZOOM_LEVEL)
                    .build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        //Toast toast2 = Toast.makeText(getApplicationContext(),
        //        timeUpdateLocation + " " + distanceUpateLocation, Toast.LENGTH_SHORT);
        //toast2.setGravity(Gravity.CENTER|Gravity.LEFT,0,0);
        //toast2.show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Log.i("onProviderDisabled","onProviderDisabled Activity");
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    timeUpdateLocation, distanceUpateLocation, this);
                //Log.e("onRequestPermissionsRes","onRequestPermissionsResult Activity");
                return;
        }
    }

    @Override
    public void onClick(View view) {
        int newTimeUpdateLocation;
        float newDistanceUpdateLocation;
        try {
            newTimeUpdateLocation = Integer.parseInt(((EditText)findViewById(R.id.editTextTime)).getText().toString());
            newDistanceUpdateLocation = Float.parseFloat(((EditText)findViewById(R.id.editTextDistance)).getText().toString());
        } catch (NumberFormatException e) {
            Toast toast2 = Toast.makeText(getApplicationContext(),
                    "Ingrese un valor numérico válido.", Toast.LENGTH_SHORT);
            toast2.setGravity(Gravity.BOTTOM|Gravity.CENTER,0,0);
            toast2.show();
            return;
        }

        if(newTimeUpdateLocation != timeUpdateLocation || newDistanceUpdateLocation != distanceUpateLocation) {
            timeUpdateLocation = newTimeUpdateLocation;
            distanceUpateLocation = newDistanceUpdateLocation;
            locationManager.removeUpdates(this);
            //Log.e("removeUpdates", LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    timeUpdateLocation, distanceUpateLocation, this);
        }
    }
}
