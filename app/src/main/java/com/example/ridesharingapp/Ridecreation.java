package com.example.ridesharingapp;

import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Ridecreation {
    static int counter=0;
    String start_position;
    String destination;
    String Uid;

    public Ridecreation(String start_position, String destination ,String Uid) {
        this.start_position = start_position;
        this.destination = destination;
        this.Uid=Uid;
    }

    public Ridecreation() {
    }

    public String getStart_position() {
        return start_position;
    }

    public void setStart_position(String start_position) {
        this.start_position = start_position;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public static void create(String start, String destination) {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        String Uid = mAuth.getCurrentUser().getUid().toString();
        Ridecreation ride = new Ridecreation(start, destination,getusername());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("RideDetails").child(Integer.toString(counter)).setValue(ride).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        counter=counter+1;
    }
    public static String getusername() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String[] username = new String[1];
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference();
        usersRef.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteData userprofile = snapshot.getValue(ReadWriteData.class);
                if (userprofile == null);

                else {
                   username[0] =userprofile.getUsername();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return username[0];
    }
}