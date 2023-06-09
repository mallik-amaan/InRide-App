package com.example.ridesharingapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.ridesharingapp.databinding.ActivityMaps2Binding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mMap;
    private ActivityMaps2Binding binding;
    ImageButton callbutton, messagebutton, back;
    static String Phone, Name, uids;
    double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        callbutton = findViewById(R.id.caller);
        messagebutton = findViewById(R.id.messager);
        back = findViewById(R.id.back);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callrider(Phone);
            }
        });

        messagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagerider(Phone);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.container, supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);
        getlocation();
     //   riderslocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.mipmap.riderpic_foreground);

        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney)).setTitle("Your Location");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
        mMap.addMarker(new MarkerOptions().position(sydney).icon(markerIcon).title("rider's location"));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }

    public void callrider(String phoneno) {
        Intent dialerIntent = new Intent(Intent.ACTION_DIAL);
        dialerIntent.setData(Uri.parse("tel:" + phoneno));
        startActivity(dialerIntent);
    }

    public void messagerider(String phoneno) {
        String message = "Type your message here"; // replace with your message
        Uri uri = Uri.parse("smsto:" + phoneno);
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
        smsIntent.putExtra("sms_body", message);
        startActivity(smsIntent);
    }

    public void rider_no(String phone_no_of_rider) {
        Phone = phone_no_of_rider;
    }

    public void getlocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Toast.makeText(MapsActivity.this,"Location is not null",Toast.LENGTH_SHORT).show();
                        Geocoder geocoder = new Geocoder(MapsActivity2.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            latitude = addresses.get(0).getLatitude();
                            longitude = addresses.get(0).getLongitude();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });
        }
    }

    public void riderslocation() {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        String Uid = mAuth.getCurrentUser().getUid().toString();
        Uid = Uid.substring(0, Uid.length() - 1);
        DatabaseReference userref = FirebaseDatabase.getInstance().getReference("RiderLocation");
        userref.child(uids).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double latitudes = snapshot.child("latitude").getValue(Double.class);
                double longitudes = snapshot.child("longitude").getValue(Double.class);
                LatLng riderposition = new LatLng(longitudes, latitudes);
                if (ActivityCompat.checkSelfPermission(MapsActivity2.this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                        (MapsActivity2.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Log.d(null, "onDataChange: "+latitudes+"    "  +longitudes);
                if(longitudes==0.00 || latitudes==0.00)
                    Toast.makeText(MapsActivity2.this, "empty location", Toast.LENGTH_SHORT).show();
                //BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.mipmap.riderpic_foreground);
                //mMap.addMarker(new MarkerOptions()
                //        .position(riderposition)
                //        .icon(markerIcon)
                 //       .title("Rider's Location"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(riderposition,15));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}