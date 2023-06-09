package com.example.ridesharingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class lookingpage extends AppCompatActivity {

    private ValueEventListener rideAvailableListener;
    private DatabaseReference userref;
    private FirebaseAuth mAuth;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookingpage);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        userref = FirebaseDatabase.getInstance().getReference("Ride");
        rideAvailableListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Ridecreation ride = snapshot.getValue(Ridecreation.class);
                Boolean rideavailable = ride.isRideavailable();
                if (rideavailable) {
                    changescreen();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        };

        startListening();
    }

    private void startListening() {
        userref.child(uid).addValueEventListener(rideAvailableListener);
    }

    private void stopListening() {
        userref.child(uid).removeEventListener(rideAvailableListener);
    }

    private void changescreen() {
        stopListening();
        Intent view = new Intent(lookingpage.this, RidersMap.class);
        startActivity(view);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopListening();
    }
}
