package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;


public class ProgressActivity extends AppCompatActivity{

    //code that sets the progress on the progress bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar pb=(ProgressBar) findViewById(R.id.pb);
        pb.setProgress(75);

    }


}
