package com.example.ridesharingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
EditText email;
Button send;
FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        send=findViewById(R.id.sendemail);
        email=findViewById(R.id.emailaddress);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailaddress=email.getText().toString();
                if(TextUtils.isEmpty(emailaddress))
                    Toast.makeText(ResetPassword.this,"Enter Email Address",Toast.LENGTH_SHORT).show();
                else if(!Patterns.EMAIL_ADDRESS.matcher(emailaddress).matches())
                    Toast.makeText(ResetPassword.this,"Enter Registered Email Address",Toast.LENGTH_SHORT).show();
                else
                    resetpassword(emailaddress);
            }
        });
    }

    private void resetpassword(String email) {
        auth=FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(ResetPassword.this,"Please check your inbox for password change link",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                }
                else
                    Toast.makeText(ResetPassword.this,"Something went wrong",Toast.LENGTH_SHORT).show();

            }
        });
    }
}