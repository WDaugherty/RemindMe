/*
* Things to work on 4/24:
* This File:
*   * On Date Set Function
*   * On Time Set Function
*   * On Save Click Function
*
* Contract File:
*   * Verify Table Columns are With Correct Entry (Some were mixed up, I made changes)
*   * Decide on TEXT or INTEGER for DateTime column. I suggest INTEGER. Methods are psuedo-coded for each below
    UPDATE: Verified table columns and decieded on text for datetime, this is subject to change.
*
* DB Helper:
*   * Verify that all needed SQL statements exist.
*
* Stretch Goals:
*   * Test DB Retrieval, Set Adapter to Expandable List View to Test Daily View
* */
package com.example.myapplication.ui.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import android.widget.TimePicker;

import com.example.myapplication.ui.daily.DailyFragment;
import com.example.myapplication.Contract;
import com.example.myapplication.DB_Helper;
import com.example.myapplication.DatePickerFragment;
import com.example.myapplication.R;
import com.example.myapplication.TimePickerFragment;
import com.example.myapplication.ui.daily.DailyFragment;
import com.example.myapplication.ui.goal.CreateViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

public class CreateFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static String str_title;
    public static String str_description;
    public static String str_location;
    public static String str_endDate;
    public static Timestamp unix;
    public static String str_endTime;
    public static String JDBC;
    private CreateViewModel mViewModel;
    private int year_g, month, day, hour, minute_g;
    public EditText title, location, description;
    PlacesClient placesClient;
    public int startYear, startMonth, startDay;
    public int endYear, endMonth, endDay;
    private AppBarConfiguration mAppBarConfiguration;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        String apikey = "AIzaSyAptCM_9HCOYuXRMcXWO44qV0XA363b8OE";
        View view = inflater.inflate(R.layout.fragment_create_task, container, false);
        if (!Places.isInitialized()){

            Places.initialize(getActivity().getApplication(),apikey);
        }
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        title = (EditText) view.findViewById(R.id.title);
        // Start the autocomplete intent on location field click
        location = (EditText) view.findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() { // On click of location field
            @Override
            public void onClick(View v) { // Fully functioning, no change needed
                // Launch the Places API via an intent using OVERLAY mode
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .build(getContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }


        });
        description = (EditText) view.findViewById(R.id.description);

        Button endDate, endTime, discard, save;

        endDate = view.findViewById(R.id.date);
        endTime = view.findViewById(R.id.time);
        discard = view.findViewById(R.id.discard);
        save = view.findViewById(R.id.save_task);
        // set on click listeners for each button
        endDate.setOnClickListener(new View.OnClickListener() { // Fully functioning, no change needed

            @Override
            public void onClick(View v) { // use datepicker for end date
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                DialogFragment endDateFragment = new DatePickerFragment(CreateFragment.this);
                endDateFragment.show(ft, "dialog");
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() { // Fully functioning, no change needed

            @Override
            public void onClick(View v) { // use time picker for end time
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                DialogFragment endTimeFragment = new TimePickerFragment(CreateFragment.this);
                endTimeFragment.show(ft, "dialog");
            }
        });
        discard.setOnClickListener(new View.OnClickListener() { // Fully functioning, no change needed

            @Override
            public void onClick(View v) { // discard new goal, navigate up backstack
                DrawerLayout drawer = v.findViewById(R.id.drawer_layout);
                NavigationView navigationView = v.findViewById(R.id.nav_view);
                // Passing each menu ID as a set of Ids because each
                // menu should be considered as top level destinations.
                mAppBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_daily, R.id.nav_weekly, R.id.nav_monthly)
                        .setDrawerLayout(drawer)
                        .build();
                NavController navController = Navigation.findNavController((AppCompatActivity)getActivity(), R.id.nav_host_fragment);


                NavigationUI.navigateUp(navController, mAppBarConfiguration);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) { // insert and navigate up backstack
                                        DB_Helper myDbHelper = new DB_Helper(getActivity().getApplicationContext());
                                        SQLiteDatabase db = myDbHelper.getWritableDatabase();
                                        ContentValues values = new ContentValues();


                                        // set calendar values to get time as millis for db
                                        Calendar cal = Calendar.getInstance();
                                        cal.set(Calendar.YEAR, year_g);
                                        cal.set(Calendar.MONTH, month);
                                        cal.set(Calendar.DAY_OF_MONTH, day);
                                        cal.set(Calendar.HOUR_OF_DAY, hour);
                                        cal.set(Calendar.MINUTE, minute_g);
                                        long millis = cal.getTimeInMillis();
                                        // get title and desc values
                                        String str_title = title.getText().toString();
                                        String str_description =  description.getText().toString();
                                        //store in values
                                        values.put(Contract.TaskEntry.COLUMN_NAME_TITLE, str_title);
                                        values.put(Contract.TaskEntry.COLUMN_NAME_DESCRIPTION, str_description);
                                        // this is from global variable
                                        values.put(Contract.TaskEntry.COLUMN_NAME_LOCATION, str_location);
                                        // set time in millis
                                        values.put(Contract.TaskEntry.COLUMN_NAME_DATE_TIME, millis);
                                        // assume a new task is not complete
                                        values.put(Contract.TaskEntry.COLUMN_NAME_COMPLETED, 0);

                                        long newRowId = db.insert(
                                                Contract.TaskEntry.TABLE_NAME,  //table name for insert
                                                null,  //null is all columns
                                                values);  //values for the insert
                                        DrawerLayout drawer = v.findViewById(R.id.drawer_layout);
                                        NavigationView navigationView = v.findViewById(R.id.nav_view);
                                        // Passing each menu ID as a set of Ids because each
                                        // menu should be considered as top level destinations.
                                        mAppBarConfiguration = new AppBarConfiguration.Builder(
                                                R.id.nav_daily, R.id.nav_weekly, R.id.nav_monthly)
                                                .setDrawerLayout(drawer)
                                                .build();
                                        NavController navController = Navigation.findNavController((AppCompatActivity)getActivity(), R.id.nav_host_fragment);


                                        NavigationUI.navigateUp(navController, mAppBarConfiguration);
                                    }
                                });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) { // Fully functioning, no change needed
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CreateViewModel.class);
        // TODO: Use the ViewModel
    }
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        // do stuff with the date the user selected
        
        //Store date values in global variables
        year_g = year;
        month = monthOfYear;
        day = dayOfMonth;

    }
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // do stuff with the time the user selected

        //Store time values in global variables
        hour = hourOfDay;
        minute_g = minute;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // Fully functioning, no change needed. Activity result for API intent
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("PlacesApi", "Place: " + place.getName() + ", " + place.getId());
                str_location = place.getName() + ", " + place.getAddress(); // set location variable and text
                location.setText(place.getName() + ", " + place.getAddress());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("PlacesApi", status.getStatusMessage());
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}
