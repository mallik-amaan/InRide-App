package com.example.ridesharingapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Ride {
    static int counter=0;
    String start_position;
    String destination;
    String phone;
    String Phoen;
    String Fare;
    String ridersuid;
    boolean rideavailable;
    public Ride() {
    }

    public Ride(String start_position, String destination, String phone,String Fare,String ridersuid) {
        this.start_position = start_position;
        this.destination = destination;
        this.phone = phone;
        this.Fare=Fare;
        this.ridersuid=ridersuid;
        rideavailable=false;
    }

    public boolean isRideavailable() {
        return rideavailable;
    }

    public void setRideavailable(boolean rideavailable) {
        this.rideavailable = rideavailable;
    }

    public static void setCounter(int counter) {
        Ride.counter = counter;
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

    public String getPhone() {
        return phone;
    }

    public String getRidersuid() {
        return ridersuid;
    }

    public void setRidersuid(String ridersuid) {
        this.ridersuid = ridersuid;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void create(String start, String destination,String Fare) {
        FirebaseAuth mAuth;
        final String phone_no, name;
        mAuth = FirebaseAuth.getInstance();
        String Uid = mAuth.getCurrentUser().getUid().toString();
        ridersuid=Uid;
        DatabaseReference userref = FirebaseDatabase.getInstance().getReference("Users");
        userref.child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("username").getValue(String.class);
                String phone_no = snapshot.child("phone_no").getValue(String.class);
                Ridecreation ride = new Ridecreation(start, destination, phone_no + "1", Fare, name + "1",Uid+"1");
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Ride").child(Uid).setValue(ride).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        counter = counter + 1;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Phoen="+92"+Phoen.substring(1,Phoen.length());

    }

    //METHOD FOR RETRIEVING RIDER'S PHONE NO----------------------------------------------------------------------------------------------------------
    public void getphoneno() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference();
        usersRef.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                ReadWriteData user = snapshot.getValue(ReadWriteData.class);
                String Number = new String(user.getPhone_no());
                Log.d(null, "onDataChange: Number"+Number);
                returnnumber(Number);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                }
        });

    }
    public void returnnumber(String number){
        Phoen = number;
    }
}

