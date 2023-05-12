package com.example.ridesharingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

public class MainActivity3 extends AppCompatActivity {
Button ride;
  TextView starting;
  TextView destinate;
CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        cardView=findViewById(R.id.container);
        ride=findViewById(R.id.getride);
        starting=findViewById(R.id.start);
        destinate=findViewById(R.id.destination);
        BottomNavigationView bottomNavigationView=findViewById(R.id.BottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.ride);
        bottomNavigationView.setOnItemSelectedListener(item ->{
            switch(item.getItemId())
            {
                case R.id.ride:return true;
                case R.id.bottom_account:
                    startActivity(new Intent(this,AccountSettings.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    return true;
                case R.id.give_ride:
                    startActivity(new Intent(this,MapsActivity.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    return true;
            }
            return false;
        });
    }
public void searchride(String searchedridestart,String searchedridedestination)
{
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference();
        usersRef.child("RideDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<Ridecreation>> t = new GenericTypeIndicator<ArrayList<Ridecreation>>() {};
                ArrayList<Ridecreation> ride= snapshot.getValue(t);
                for (Ridecreation rides:ride) {
                    Log.d(null,  rides.toString());
                }

                if (ride == null);
                  //  Toast.makeText(MainActivity3.this, "Fail Retrieving Ride Details", Toast.LENGTH_SHORT).show();

                else {
                    for (Ridecreation rideobjects:ride) {
                        if(searchedridestart==rideobjects.getStart_position() || searchedridedestination==rideobjects.getDestination())
                        {
                            Log.d(null, "onDataChange: not found");
                            starting.setText(rideobjects.getStart_position());
                            destinate.setText(rideobjects.getDestination());
                        }
                        else
                            Log.d(null, "onDataChange: fuck");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
