package com.example.ridesharingapp;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ridesharingapp.databinding.ActivityMain3Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class MainActivity3 extends AppCompatActivity {
    Button ride;
    EditText starting;
    EditText destinate;
    CardView cardView;
    ConstraintLayout layout;
    String start;
    String ending;
    PopupWindow popup;
    private int previousPopupHeight = 0;
    public MainActivity3(){};
    public MainActivity3(String Start,String end)
    {
        start=Start;
        ending=end;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main3);
        ActivityMain3Binding binding;
        View popupview=getLayoutInflater().inflate(R.layout.popup,null);
        popup=new PopupWindow(popupview,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popup.showAtLocation(popupview, Gravity.CENTER, 0, 0);
    }
    public void searchride(String searchedridestart, String searchedridedestination) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Assuming you have already set up Firebase and obtained a reference to your database
        DatabaseReference rideDetailsRef = FirebaseDatabase.getInstance().getReference("RideDetails");
        rideDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean rideFound = false;  // Variable to track if a ride has been found

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String rideId = childSnapshot.getKey();
                    String start = childSnapshot.child("start_position").getValue(String.class);
                    String end = childSnapshot.child("destination").getValue(String.class);
                    String phone_no_of_rider = childSnapshot.child("phone").getValue(String.class);
                    Log.e(null, "onDataChange: " + start + " " + end);

                    if (start.equals(null) || end.equals(null)) {
                        Toast.makeText(MainActivity3.this, "Nothing Found", Toast.LENGTH_SHORT).show();
                    } else if (start.equalsIgnoreCase(searchedridestart) || end.equalsIgnoreCase(searchedridedestination)) {
                        popupcreator(searchedridestart,searchedridedestination);
                        Toast.makeText(MainActivity3.this, "Ride Found", Toast.LENGTH_SHORT).show();
                        rideFound = true;
                        break;
                    }
                }

                if (!rideFound) {
                    Toast.makeText(MainActivity3.this, "No matching ride found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity3.this, "Database Error", Toast.LENGTH_SHORT).show();
                Log.d(null, "Error retrieving data: " + databaseError.getMessage());
            }
        });
    }
public void popupcreator(String start,String end)
{
    TextView starting, ending;
    View popupview = getLayoutInflater().inflate(R.layout.popup, null);
    starting = popupview.findViewById(R.id.Startinglocation);
    ending = popupview.findViewById(R.id.Destination);
    starting.setText(start);
    ending.setText(end);
    popup = new PopupWindow(popupview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
    popup.showAtLocation(popupview, Gravity.CENTER, 0, 0);
}
}

