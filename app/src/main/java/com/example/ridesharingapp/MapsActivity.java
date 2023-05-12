package com.example.ridesharingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.ridesharingapp.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.EncodedPolyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private final int permission = 1;
    Location CurrentLocation;
    double latitude,longitude;
    ImageView imageviewsearch1,imageviewsearch2;
    EditText inputlocation,destinationlocation;
    Marker previousmarker,previousmarker2;
    MaterialButton create;
    Button search;
    LatLng start,end;
    private FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_maps);
        //---------------------------------------------FUNCTIONING BUTTONS AND TEXTFIELD--------------------------------------------------------------------------------
        imageviewsearch1 =findViewById(R.id.imageViewsearch4);
        imageviewsearch2=findViewById(R.id.imageViewsearch);
        inputlocation=findViewById(R.id.inputlocation);
        destinationlocation=findViewById(R.id.destinationlocation);
        create=findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ridecreation.create(inputlocation.getText().toString(),destinationlocation.getText().toString());
                Toast.makeText(MapsActivity.this, "Ride Created Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        search=findViewById(R.id.searchride);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity3 m3=new MainActivity3();
                m3.searchride(inputlocation.getText().toString(),destinationlocation.getText().toString());
                Toast.makeText(MapsActivity.this, "Ride Searched Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        //-------------------------------------------------------SETTING ONCLICK LISTNERS--------------------------------------------------------------------------------
        imageviewsearch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               searchLocation(destinationlocation);
            }
        });
        imageviewsearch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               searchLocation(inputlocation);
            }
        });
      /*  create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawpath(start,end);
            }
        });*/

         inputlocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                inputlocation.getText().clear();
            }
        });
        destinationlocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                destinationlocation.getText().clear();
            }
        });
        //---------------------------------------------------------------------------BOTTOM NAVIGATION-----------------------------------------------------------------
        BottomNavigationView bottomNavigationView=findViewById(R.id.BottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.give_ride);
        bottomNavigationView.setOnItemSelectedListener(item ->{
            switch(item.getItemId())
            {
                case R.id.give_ride:return true;
                case R.id.bottom_account:
                    startActivity(new Intent(this,AccountSettings.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    return true;
                case R.id.ride:
                    startActivity(new Intent(getApplicationContext(),MainActivity3.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    return true;
            }
            return false;
        });

        //-------------------------------------- Obtain the SupportMapFragment and get notified when the map is ready to be used.--------------------------------------------
        SupportMapFragment supportMapFragment= SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.container,supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);


    }
    //----------------------------------------------------------------------------iMPLEMENTATION OF GOOGLE MAPS--------------------------------------------------------------
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(33.642987,72.99);
        MarkerOptions markerOptions = new MarkerOptions().position(sydney).title("New Marker");
        previousmarker = mMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(sydney,20);
        mMap.animateCamera(cameraUpdate);
    }
    //----------------------------------------------------------------------------SEARCHING OF LOCATIONS--------------------------------------------------------------------
    private void searchLocation(EditText locationEditText) {

        String location = locationEditText.getText().toString();
        if (TextUtils.isEmpty(location)) {
            Toast.makeText(MapsActivity.this, "TYPE A LOCATION", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
            try {
                List<Address> list_address = geocoder.getFromLocationName(location, 100);
                if (list_address.size() > 0) {
                    LatLng latLng = new LatLng(list_address.get(0).getLatitude(), list_address.get(0).getLongitude());
                    if(latLng==null)
                        Toast.makeText(MapsActivity.this,"NULL",Toast.LENGTH_SHORT).show();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title("My Position");
                    markerOptions.position(latLng);
                    addOrUpdateMarker(markerOptions, locationEditText.getId());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 20);
                    mMap.animateCamera(cameraUpdate);
                    //return latLng;
                } else {
                    Toast.makeText(MapsActivity.this, "NO ADDRESS FOUND", Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //----------------------------------------------------------------FUNCTION FOR ADDING MARKER---------------------------------------------------------------------------
    private void addOrUpdateMarker(MarkerOptions markerOptions, int id) {
        if (id == R.id.inputlocation) {
            if (previousmarker != null) {
                previousmarker.remove();
            }
            previousmarker = mMap.addMarker(markerOptions);
        } else if (id == R.id.destinationlocation) {
            if (previousmarker2 != null) {
                previousmarker2.remove();
            }
            previousmarker2 = mMap.addMarker(markerOptions);
        }
    }
    //-------------------------------------------------------------------------DRAWPATH--------------------------------------------------------------------------------------
    private void drawpath(LatLng inputlocation, LatLng destinationlocation) {
        // Define a list to store the locations for the polyline, as well as the polyline options
        List<LatLng> path = new ArrayList<>();
        PolylineOptions polylineOptions = new PolylineOptions();

        // Create a new DirectionsApiRequest
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyCsmJMzznmZllkkYqoQY4mJeoliyhxKz3U")
                .build();
        DirectionsApiRequest request = DirectionsApi.getDirections(context, inputlocation.latitude + "," + inputlocation.longitude, destinationlocation.latitude + "," + destinationlocation.longitude);

        try {
            // Execute the request and get the response
            DirectionsResult directionsResult = request.await();

            // Extract the polyline from the response
            EncodedPolyline encodedPolyline = directionsResult.routes[0].overviewPolyline;
            List<com.google.maps.model.LatLng> decodedPath = encodedPolyline.decodePath();

            // Convert the polyline's latlngs to our list of latlngs
            for (com.google.maps.model.LatLng latLng : decodedPath) {
                path.add(new LatLng(latLng.lat, latLng.lng));
            }

            // Add the polyline to the map
            polylineOptions.addAll(path);
            polylineOptions.width(10);
            polylineOptions.color(Color.BLUE);
            mMap.addPolyline(polylineOptions);
        } catch (Exception ex) {
            Log.e(null, "Error while getting directions: " + ex.getMessage());
        }
    }

}
