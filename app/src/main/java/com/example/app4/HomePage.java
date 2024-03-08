package com.example.app4;

//...................................................................................
//................................Importing libraries................................
//...................................................................................

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomePage extends FragmentActivity implements OnMapReadyCallback {

//...................................................................................
//............................Declaring variables for map............................
//...................................................................................
    GoogleMap map;
    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;
    FloatingActionButton zoomIn, zoomOut, recenter1;
    ImageButton mapReturn, profile, history;
    LatLng latLng, latLng1, latLng2, latLng3, latLng4, latLng5;
    String locate;
    StationDetails stationDetails;
    MarkerOptions options1,options2,options3,options4,options5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    //...................................................................................
    //...............................Initializing variables..............................
    //...................................................................................
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFrag1);
    //initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this);
        mapFragment.getMapAsync(this);

    //HomePage buttons function variables
        zoomIn = findViewById(R.id.zoomIn);
        zoomOut = findViewById(R.id.zoomOut);
        profile = findViewById(R.id.profileBtn1);
        mapReturn = findViewById(R.id.mapButton1);
        locate = String.valueOf(latLng);
        recenter1 = findViewById(R.id.recenter1);
        history = findViewById(R.id.listBtn);




    //...................................................................................
    //.........................Checking if permission is granted.........................
    //...................................................................................
        if (ActivityCompat.checkSelfPermission(HomePage.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        //getting current location
            getCurrentLocation();
        }
        else {
        //asking permission for location(GPS) of user device
            ActivityCompat.requestPermissions(HomePage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    //...................................................................................
    //............................HomePage Buttons Functions.............................
    //...................................................................................
    //profile button
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,UserProfile.class));

            }
        });
    //Zoom in button function
        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });
    //Zoom out button function
        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.animateCamera(CameraUpdateFactory.zoomOut());

            }
        });
    //Return to map
        mapReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //reloading the activity
                finish();
                startActivity(new Intent(HomePage.this,HomePage.class));
            }
        });

        recenter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(HomePage.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    //getting current location
                    getCurrentLocation();
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18),1000,null);
                }
                else {
                    //asking permission for location(GPS) of user device
                    ActivityCompat.requestPermissions(HomePage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,HistoryPage.class));
            }
        });

    }

    private void getCurrentLocation() {
        @SuppressLint("MissingPermission") Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){

                    //sync map
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            latLng = new LatLng(location.getLatitude(),location.getLongitude());
                            latLng1 = new LatLng(18.47631051163299, 73.82269152992573);
                            latLng2 = new LatLng(18.45034642830959, 73.83401644298925);
                            latLng3 = new LatLng(18.446961324914785, 73.82061296592187);
                            latLng4 = new LatLng(18.514674752892, 73.84887474740029);
                            latLng5 = new LatLng(18.470434464081343, 73.82994996886168);


                            //set marker
                            MarkerOptions options = new MarkerOptions().position(latLng).title("Current Location")
                                    .icon(BitmapFromVector(getApplicationContext(), R.drawable.location1));
                            map = googleMap;
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14), 2000, null);
                            googleMap.addMarker(options);
                            map.getUiSettings().setRotateGesturesEnabled(false);

                            options1 = new MarkerOptions().position(latLng1).title("Ather Grid Charging Station").icon(BitmapFromVector(getApplicationContext(), R.drawable.station));
                            googleMap.addMarker(options1);

                            options2 = new MarkerOptions().position(latLng2).title("Arjun Engineering Services").icon(BitmapFromVector(getApplicationContext(), R.drawable.station));
                            googleMap.addMarker(options2);

                            options3 = new MarkerOptions().position(latLng3).title("Go Easy Smart Technology Pvt Ltd").icon(BitmapFromVector(getApplicationContext(), R.drawable.station));
                            googleMap.addMarker(options3);

                            options4 = new MarkerOptions().position(latLng4).title("Synergy Solutions AC Charging Station").icon(BitmapFromVector(getApplicationContext(), R.drawable.station));
                            googleMap.addMarker(options4);

                            options5 = new MarkerOptions().position(latLng5).title("Evigo Charge Charging Station").icon(BitmapFromVector(getApplicationContext(), R.drawable.station));
                            googleMap.addMarker(options5);

                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(@NonNull Marker marker) {
                                    String markerName = marker.getTitle();
                                    Intent intent = new Intent(getApplicationContext(), StationDetails.class);
                                    intent.putExtra("message", markerName);

                                    switch (markerName) {
                                        case "Evigo Charge Charging Station":
                                            intent.putExtra("status", "Open");
                                            intent.putExtra("ports", "3");
                                            intent.putExtra("link", getString(R.string.evigo));
                                            intent.putExtra("price","10");
                                            break;
                                        case "Go Easy Smart Technology Pvt Ltd":
                                            intent.putExtra("status", "Open");
                                            intent.putExtra("ports", "2");
                                            intent.putExtra("link", getString(R.string.gest));
                                            intent.putExtra("price","25");
                                            break;
                                        case "Synergy Solutions AC Charging Station":
                                            intent.putExtra("status", "Close");
                                            intent.putExtra("ports", "2");
                                            intent.putExtra("link", getString(R.string.synergy));
                                            intent.putExtra("price","15");
                                            break;
                                        case "Arjun Engineering Services":
                                            intent.putExtra("status", "Open");
                                            intent.putExtra("ports", "1");
                                            intent.putExtra("link", getString(R.string.arjun));
                                            intent.putExtra("price","15");
                                            break;
                                        case "Ather Grid Charging Station":
                                            intent.putExtra("status", "Close");
                                            intent.putExtra("ports", "2");
                                            intent.putExtra("link", getString(R.string.ather));
                                            intent.putExtra("price","18");
                                            break;
                                    }

                                    startActivity(intent);
                                    return false;
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
        builder.setMessage("Do you want to exit");
        builder.setTitle("Alert!");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (dialog, which) -> {
//            finish();
            this.finishAffinity();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

}