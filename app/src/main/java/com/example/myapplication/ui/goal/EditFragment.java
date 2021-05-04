package com.example.myapplication.ui.goal;

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
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.Contract;
import com.example.myapplication.DB_Helper;
import com.example.myapplication.DatePickerFragment;
import com.example.myapplication.DatePickerFragment2;
import com.example.myapplication.R;
import com.example.myapplication.TimePickerFragment;
import com.example.myapplication.TimePickerFragment2;
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
    int startYear, startMonth, startDay, startHour, startMinute, startMillis;
    int endYear, endMonth, endDay, endHour, endMinute, endMillis;
    private int year_g, month, day, hour, minute_g;
    public EditText title, location, description;
    PlacesClient placesClient;

    public static EditFragment newInstance() {
        return new EditFragment();
    }
    private int id;
    boolean startDateOrEndDate = true;
    boolean startTimeOrEndTime = true;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_goal_fragment, container, false);

        Button startDate, startTime, endDate, endTime, discard, save;

        startDate = view.findViewById(R.id.start_date);
        endDate = view.findViewById(R.id.end_date);
        startTime = view.findViewById(R.id.start_time);
        endTime = view.findViewById(R.id.end_time);
        discard = view.findViewById(R.id.discard);
        save = view.findViewById(R.id.save_goal);
        title = view.findViewById(R.id.title);
        // set click listeners, same as create fragment. Reference there as needed
        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startDateOrEndDate = true;
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                DialogFragment startDateFragment = new DatePickerFragment(EditFragment.this);
                startDateFragment.show(ft, "dialog");
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startDateOrEndDate = false;
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                DialogFragment endDateFragment = new DatePickerFragment2(EditFragment.this);
                endDateFragment.show(ft, "dialog");
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startTimeOrEndTime = true;
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                DialogFragment startTimeFragment = new TimePickerFragment(EditFragment.this);
                startTimeFragment.show(ft, "dialog");
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startTimeOrEndTime = false;
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                DialogFragment endTimeFragment = new TimePickerFragment2(EditFragment.this);
                endTimeFragment.show(ft, "dialog");
            }
        });
        discard.setOnClickListener(new View.OnClickListener() {

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
                // Execute SQL insertion
                DB_Helper myDbHelper = new DB_Helper(getActivity().getApplicationContext());
                SQLiteDatabase db = myDbHelper.getWritableDatabase();

                Calendar cal = Calendar.getInstance();
                // start time in millis
                cal.set(Calendar.YEAR, startYear);
                cal.set(Calendar.MONTH, startMonth);
                cal.set(Calendar.DAY_OF_MONTH, startDay);
                cal.set(Calendar.HOUR_OF_DAY, startHour);
                cal.set(Calendar.MINUTE, startMinute);
                long start_millis = cal.getTimeInMillis();
                // end time in millis
                cal.set(Calendar.YEAR, endYear);
                cal.set(Calendar.MONTH, endMonth);
                cal.set(Calendar.DAY_OF_MONTH, endDay);
                cal.set(Calendar.HOUR_OF_DAY, endHour);
                cal.set(Calendar.MINUTE, endMinute);
                long end_millis = cal.getTimeInMillis();

                String str_title = title.getText().toString();
                // update the goal record with new values
                db.execSQL("UPDATE goal SET title="+str_title+
                            " start_datetime="+start_millis+
                            " end_datetime="+end_millis,null);

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
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        // store in global vars based on start or end
        if(startDateOrEndDate){
            startDay = dayOfMonth;
            startMonth = monthOfYear;
            startYear = year;
        } else {
            endDay = dayOfMonth;
            endMonth = monthOfYear;
            endYear = year;
        }
    }
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // store in global vars based on start or end
        if(startTimeOrEndTime){
            startHour = hourOfDay;
            startMinute = minute;
        } else {
            endHour = hourOfDay;
            endMinute = minute;
        }
    }
}