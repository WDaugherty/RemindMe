package com.example.myapplication.ui.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.Contract;
import com.example.myapplication.DB_Helper;
import com.example.myapplication.DatePickerFragment;
import com.example.myapplication.R;
import com.example.myapplication.TimePickerFragment;
import com.example.myapplication.ui.goal.CreateViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EditFragment extends Fragment {

    private EditViewModel mViewModel;
    private AppBarConfiguration mAppBarConfiguration;
    public static String str_location;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    private int year_g, month, day, hour, minute_g;
    public EditText title, location, description;
    PlacesClient placesClient;
    public static EditFragment newInstance() {
        return new EditFragment();
    }
    private int id;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        String apikey = "AIzaSyAptCM_9HCOYuXRMcXWO44qV0XA363b8OE";
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getInt("id"); // get id from daily fragment
        }
        View view = inflater.inflate(R.layout.edit_task_fragment, container, false);
        if (!Places.isInitialized()){

            Places.initialize(getActivity().getApplication(),apikey);
        }
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        DB_Helper dbHelper = new DB_Helper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // select selected task information
        Cursor cursor = db.rawQuery("SELECT * FROM task WHERE _id="+id,null);
        cursor.moveToFirst();

        title = (EditText) view.findViewById(R.id.title);
        // set fields with pre-existing data
        title.setText(cursor.getString(cursor.getColumnIndex("title")));

//        final AutocompleteSupportFragment autocompleteSupportFragment =
//                (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        location = (EditText) view.findViewById(R.id.location);
        location.setText(cursor.getString(cursor.getColumnIndex("location")));
        // Start the autocomplete intent.
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
        description.setText(cursor.getString(cursor.getColumnIndex("description")));
        Button startDate, startTime, endDate, endTime, discard, save;

        endDate = view.findViewById(R.id.date);
        endTime = view.findViewById(R.id.time);
        discard = view.findViewById(R.id.discard);
        save = view.findViewById(R.id.save_task);
        // button on click listeners, the same as in the creation, reference comments there as needed
        endDate.setOnClickListener(new View.OnClickListener() { // Fully functioning, no change needed

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                DialogFragment endDateFragment = new DatePickerFragment(EditFragment.this);
                endDateFragment.show(ft, "dialog");
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() { // Fully functioning, no change needed

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                DialogFragment endTimeFragment = new TimePickerFragment(EditFragment.this);
                endTimeFragment.show(ft, "dialog");
            }
        });
        discard.setOnClickListener(new View.OnClickListener() { // Fully functioning, no change needed

            @Override
            public void onClick(View v) {
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
            public void onClick(View v) {
                DB_Helper myDbHelper = new DB_Helper(getActivity().getApplicationContext());
                SQLiteDatabase db = myDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year_g);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, day);
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute_g);
                long millis = cal.getTimeInMillis();
                String str_title = title.getText().toString();
                String str_description =  description.getText().toString();
                String str_location = location.getText().toString();
                db.execSQL("UPDATE task SET title="+str_title+
                        " description="+str_description+
                        " location="+str_location+
                        " datetime="+millis+
                        "WHERE _id="+id);

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EditViewModel.class);
        // TODO: Use the ViewModel
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // Fully functioning, no change needed
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("PlacesApi", "Place: " + place.getName() + ", " + place.getId());
                str_location = place.getName() + ", " + place.getAddress();
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
}