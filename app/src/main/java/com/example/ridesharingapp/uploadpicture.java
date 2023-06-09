package com.example.ridesharingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class uploadpicture extends AppCompatActivity {
    static String starting,ending;
    Button created;
    TextView start,end;
    EditText fare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadpicture);
        fare=findViewById(R.id.Fare);
        created=findViewById(R.id.creating);
        start=findViewById(R.id.Starting);
        end=findViewById(R.id.Ending);
        start.setText(starting);
        end.setText(ending);
        fare=findViewById(R.id.Fare);
        fare.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    fare.getText().clear();
            }
        });
        created.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fare.getText().equals("Set Fare") || !fare.getText().toString().matches("[0-9]+"))
                    Toast.makeText(uploadpicture.this, "Enter Valid Fare", Toast.LENGTH_SHORT).show();
                else{
                Ride ride=new Ride();
                ride.create(starting,ending,fare.getText().toString());
                Toast.makeText(uploadpicture.this, "Ride Created Successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(uploadpicture.this,lookingpage.class);
                startActivity(intent);

            }}
        });
    }public static void send(String start,String end)
    {
            starting=start;
            ending=end;
    }
    public void onBackPressed(){
        Intent intent=new Intent(uploadpicture.this,AccountSettings.class);
    startActivity(intent);
    finish();}
}