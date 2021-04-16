package com.example.myapplication.ui.goal;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.example.myapplication.DailyActivity;
import com.example.myapplication.DatePickerFragment;
import com.example.myapplication.R;
import com.example.myapplication.TimePickerFragment;
import com.google.android.material.navigation.NavigationView;

public class CreateFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private CreateViewModel mViewModel;
    int startYear, startMonth, startDay;
    int endYear, endMonth, endDay;
    private AppBarConfiguration mAppBarConfiguration;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Button startDate, startTime, endDate, endTime, discard, save;
        View view = inflater.inflate(R.layout.fragment_create_goal, container, false);
        startDate = view.findViewById(R.id.start_date);
        endDate = view.findViewById(R.id.end_date);
        startTime = view.findViewById(R.id.start_time);
        endTime = view.findViewById(R.id.end_time);
        discard = view.findViewById(R.id.discard);
        save = view.findViewById(R.id.save_goal);
        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment startDateFragment = new DatePickerFragment(CreateFragment.this);
                startDateFragment.show(ft, "dialog");
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment endDateFragment = new DatePickerFragment(CreateFragment.this);
                endDateFragment.show(ft, "dialog");
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment startTimeFragment = new TimePickerFragment(CreateFragment.this);
                startTimeFragment.show(ft, "dialog");
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment endTimeFragment = new TimePickerFragment(CreateFragment.this);
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
                // Verify forms are filled
                // Execute SQL insertion
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CreateViewModel.class);
        // TODO: Use the ViewModel
    }
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        // do stuff with the date the user selected
    }
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // do stuff with the time the user selected
    }
}