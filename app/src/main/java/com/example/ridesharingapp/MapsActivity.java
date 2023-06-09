package com.example.ridesharingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ridesharingapp.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private final int permission = 1;
    Location CurrentLocation;
    double latitude, longitude;
    ImageView imageviewsearch1, imageviewsearch2;
    EditText inputlocation, destinationlocation;
    Marker previousmarker, previousmarker2;
    Button create;
    Button search;
    LatLng start, end;
    String address;
    private int counter=0;
    private final int code = 100;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private PopupWindow popup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_maps);
        getLocation();
        //---------------------------------------------FUNCTIONING BUTTONS AND TEXTFIELD--------------------------------------------------------------------------------
        imageviewsearch1 = findViewById(R.id.imageViewsearch4);
        imageviewsearch2 = findViewById(R.id.imageViewsearch);
        inputlocation = findViewById(R.id.inputlocation);
        inputlocation.setText(address);
        destinationlocation = findViewById(R.id.destinationlocation);
        create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(destinationlocation.getText().toString().equals("Enter Destination")|| destinationlocation.getText().toString().equals(""))
                    Toast.makeText(MapsActivity.this, "Enter Destination Location", Toast.LENGTH_SHORT).show();
                else{
                uploadpicture.send(inputlocation.getText().toString(),destinationlocation.getText().toString());
                Intent intent=new Intent(MapsActivity.this,uploadpicture.class);
                startActivity(intent);
                finish();
            }}
        });
        search = findViewById(R.id.searchride);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(destinationlocation.getText().toString().equals("Enter Destination")  || destinationlocation.getText().toString().equals(""))
                    Toast.makeText(MapsActivity.this, "Enter Destination Location", Toast.LENGTH_SHORT).show();
                else{
                searchride(inputlocation.getText().toString(),destinationlocation.getText().toString());
              }}
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
        inputlocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    inputlocation.getText().clear();
            }
        });
        destinationlocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    destinationlocation.getText().clear();
            }
        });
        //---------------------------------------------------------------------------BOTTOM NAVIGATION-----------------------------------------------------------------
        BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.give_ride);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.give_ride:
                    return true;
                case R.id.bottom_account:
                    startActivity(new Intent(this, AccountSettings.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    return true;

            }
            return false;
        });

        //-------------------------------------- Obtain the SupportMapFragment and get notified when the map is ready to be used.--------------------------------------------
        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.container, supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);
    }

    public void getLocation() {
       if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                       // Toast.makeText(MapsActivity.this,"Location is not null",Toast.LENGTH_SHORT).show();
                        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            latitude = addresses.get(0).getLatitude();
                            longitude = addresses.get(0).getLongitude();
                            onMapReady(mMap);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });
        }
        else
            askpermission();
    }
    public void askpermission() {
        ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==code){
            if(grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                getLocation();
            else
                Toast.makeText(MapsActivity.this,"PERMISSION REQUIRED",Toast.LENGTH_SHORT).show();
        }



        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //----------------------------------------------------------------------------iMPLEMENTATION OF GOOGLE MAPS--------------------------------------------------------------

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        // Check for permission to access the user's location
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Enable the My Location button and the corresponding functionality
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
           // mMap.getUiSettings().setMyLocationButtonEnabled(true);

            // Get the user's location and update the UI
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.mipmap.maplocation_foreground);
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        mMap.addMarker(new MarkerOptions().position(latLng).icon(markerIcon).title("Your Location"));
                        // Get the user's location address
                        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            if (addresses != null && addresses.size() > 0) {
                                 address = addresses.get(0).getAddressLine(0);
                                //String addressText = String.format("%s, %s, %s", address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "", address.getLocality(), address.getCountryName());
                                // Update the UI with the user's location address
                                setAddress(address);
                            }
                        } catch (IOException e) {
                            Toast.makeText(MapsActivity.this, "Error getting address", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });

        } else {
            // Ask for permission to access the user's location
            askpermission();
        }
    }


    //----------------------------------------------------------Current Location-------------------------------------------------------
    public void currentlocation() {
        LatLng cp=new LatLng(latitude,longitude);
        MarkerOptions markerOptions=new MarkerOptions().position(cp);
        previousmarker=mMap.addMarker(markerOptions);
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
    public void setAddress(String address)
    {
        if(counter<1)
        {
            inputlocation.setText(address);
            counter++;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        counter=0;
    }
    public void searchride(String searchedridestart,String searchedridedestination) {
        //  Toast.makeText(this, "Search Started", Toast.LENGTH_SHORT).show();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Assuming you have already set up Firebase and obtained a reference to your database
        DatabaseReference rideDetailsRef = FirebaseDatabase.getInstance().getReference("Ride");
        rideDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(MapsActivity.this, "Searching", Toast.LENGTH_SHORT).show();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String rideId = childSnapshot.getKey();
                    String start = childSnapshot.child("start_position").getValue(String.class);
                    String end = childSnapshot.child("destination").getValue(String.class);
                    String phone_no_of_rider=childSnapshot.child("phone").getValue(String.class);
                    String fare=childSnapshot.child("fare").getValue(String.class);
                    String name=childSnapshot.child("name").getValue(String.class);
                    String phonenumber=childSnapshot.child("phone_no").getValue(String.class);
                    String uid=childSnapshot.child("ridersuid").getValue(String.class);
                    boolean rideavailable=childSnapshot.child("rideavailable").getValue(Boolean.class);
                    uid=uid.substring(0,uid.length()-1);
                    phonenumber=phonenumber.substring(0,phonenumber.length()-1);
                    MapsActivity2.Phone=phonenumber;
                    MapsActivity2.uids=uid;
                    name=name.substring(0,name.length()-1);
                    Log.e(null, "onDataChange: "+start+" "+end );
                    // Do whatever you want with the start and end values
                    if(start==null || end==null)
                        Toast.makeText(MapsActivity.this, "Nothing Found", Toast.LENGTH_SHORT).show();
                    if(start.equalsIgnoreCase(searchedridestart)||end.equalsIgnoreCase(searchedridedestination)) {
                        Toast.makeText(MapsActivity.this, "Ride Found", Toast.LENGTH_SHORT).show();
                        Ridecreation rides=childSnapshot.getValue(Ridecreation.class);
                        boolean conti= popupcreator(name,fare,rides);
                        if (!conti)
                            break;
                        else
                            continue;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this,"DataBase Error",Toast.LENGTH_SHORT).show();
                Log.d(null,"Error retrieving data: " + databaseError.getMessage());
            }
        });
    }
    public boolean popupcreator(String start,String end,Ridecreation ridechange) {
        Button getride, cancel;
        int[] count = {0};
        TextView starting, ending;
        View popupview = getLayoutInflater().inflate(R.layout.popup, null);
        starting = popupview.findViewById(R.id.Startinglocation);
        ending = popupview.findViewById(R.id.Destination);
        starting.setText(start);
        ending.setText(end);
        popup = new PopupWindow(popupview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popup.showAtLocation(popupview, Gravity.CENTER, 0, 0);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        getride = popupview.findViewById(R.id.button_getride);
        cancel = popupview.findViewById(R.id.cancel);
        getride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ridechange.setRideavailable(true);
                DatabaseReference userref = FirebaseDatabase.getInstance().getReference("Ride");
                userref.child(ridechange.getRidersuid().substring(0, ridechange.getRidersuid().length() - 1)).setValue(ridechange);
                Intent intent=new Intent(MapsActivity.this,MapsActivity2.class);
                startActivity(intent);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                count[0] = 2;
            }

        });
        if (count[0] == 2)
            return true;
        else
            return false;
    }
}
