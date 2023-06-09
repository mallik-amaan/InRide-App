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

public class Ridecreation {
    static int counter = 0;
    String start_position;
    String destination;
    String phone;
    String name;
    String Fare;
    String ridersuid;
    boolean rideavailable;
    //CONSTRUCTERS AND GETTERS,SETTERS-------------------------------------------------------------------------------------------------------------
    public Ridecreation(String start_position, String destination, String phone,String Fare,String name,String ridersuid) {
        this.start_position = start_position;
        this.destination = destination;
        this.phone = phone;
        this.name=name;
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

    public String getFare() {
        return Fare;
    }

    public void setFare(String fare) {
        Fare = fare;
    }

    public void setPhone_no(String phone) {
        this.phone = phone;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ridecreation() {
    }

    public void setStart_position(String start_position) {
        this.start_position = start_position;
    }

    public String getStart_position() {
        return start_position;
    }

    public String getDestination() {
        return destination;
    }

    public String getPhone_no() {
        return phone;
    }

    public String getRidersuid() {
        return ridersuid;
    }

    public void setRidersuid(String ridersuid) {
        this.ridersuid = ridersuid;
    }
}
//------------------------------------------------------------------------------------------------------------------------------------------------







//METHOD FOR CREATING A RIDE IN DATA BASE---------------------------------------------------------------------------------------------------------
