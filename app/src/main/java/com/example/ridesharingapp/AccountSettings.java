package com.example.ridesharingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountSettings extends AppCompatActivity {
    FirebaseAuth mAuth;
    String name;
    TextView namefield, logout, changepassword,phonenumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        namefield = findViewById(R.id.name);
        getusername();
        logout = findViewById(R.id.button2);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(AccountSettings.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        changepassword = findViewById(R.id.changepassword);
        phonenumber=findViewById(R.id.phoneno);
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResetPassword.class);
                startActivity(intent);
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_account);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_account:
                    return true;
                case R.id.give_ride:
                    try{
                    startActivity(new Intent(this, MapsActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
            }
                    catch(Exception e)
                    {
                        startActivity(new Intent(this, MapsActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                    }
            }
            return false;
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getusername() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference();
        usersRef.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteData userprofile = snapshot.getValue(ReadWriteData.class);
                if (userprofile == null)
                    Toast.makeText(AccountSettings.this, "Fail Retrieving Profile", Toast.LENGTH_SHORT).show();

                else {
                    namefield.setText(userprofile.getUsername());
                    phonenumber.setText(userprofile.getPhone_no());
                }
                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
