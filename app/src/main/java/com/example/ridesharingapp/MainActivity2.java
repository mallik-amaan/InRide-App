package com.example.ridesharingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {
TextView login;
MaterialButton register;
EditText name;
EditText email;
EditText password,gender,phone;
FirebaseAuth mAuth=FirebaseAuth.getInstance();
LottieAnimationView animationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main2);
        name=findViewById(R.id.name);
        email=findViewById(R.id.emailaddress);
        register=findViewById(R.id.signup1);
        password=findViewById(R.id.password);
        login=findViewById(R.id.logintext);
        gender=findViewById(R.id.gender);
        phone=findViewById(R.id.phno);
        animationView=findViewById(R.id.searching);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationView.setVisibility(View.VISIBLE);
                animationView.playAnimation();
                String username = name.getText().toString();
                String Email = email.getText().toString();
                String pass = password.getText().toString();
                String gender1 = gender.getText().toString();
                String phoneno = phone.getText().toString();
                if (TextUtils.isEmpty(Email))
                {       Toast.makeText(MainActivity2.this, " ENTER EMAIL ADDRESS", Toast.LENGTH_SHORT).show();
                animationView.cancelAnimation();
                animationView.setVisibility(View.INVISIBLE);
            }else if (TextUtils.isEmpty(pass))
                {       Toast.makeText(MainActivity2.this, "ENTER PASSWORD", Toast.LENGTH_SHORT).show();
                animationView.cancelAnimation();
                animationView.setVisibility(View.INVISIBLE);
            }else if (TextUtils.isEmpty(username))
                {       animationView.cancelAnimation();
                animationView.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity2.this, "ENTER PASSWORD", Toast.LENGTH_SHORT).show();
            }else {
                    mAuth.createUserWithEmailAndPassword(Email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity2.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        uploadData(username,phoneno,gender1,Email);
                                        animationView.cancelAnimation();
                                        animationView.setVisibility(View.INVISIBLE);
                                        Intent intent=new Intent(MainActivity2.this,MapsActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.

                                    }
                                }
                            });
                    }

            }
        });
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    name.getText().clear();
            }
        });
        gender.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    gender.getText().clear();
            }
        });
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    phone.getText().clear();
            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    email.getText().clear();
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    password.getText().clear();
            }
        });
    }
    public void uploadData(String username,String Phone,String gender,String email)
    {
        ReadWriteData object=new ReadWriteData(Phone,username,gender,email);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String Uid=mAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(Uid).setValue(object).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity2.this, "DATA STORED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
            }
        });

    }
}