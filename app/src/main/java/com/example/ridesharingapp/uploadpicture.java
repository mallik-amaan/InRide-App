package com.example.ridesharingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class uploadpicture extends AppCompatActivity {
ImageView pic;
Button upload;
ImageButton filemanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadpicture);
        pic=findViewById(R.id.imageView2);
        upload=findViewById(R.id.button);
        filemanager=findViewById(R.id.choosefile);
        filemanager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}