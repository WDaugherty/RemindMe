package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

<<<<<<< HEAD
public class MainActivity extends AppCompatActivity {
=======

import com.google.android.libraries.places.api.net.PlacesClient;

public class MainActivity extends AppCompatActivity {
    PlacesClient placesClient;
>>>>>>> 187dc0dbf157f80bef57a8f8f2d5a499070dcf1a

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}