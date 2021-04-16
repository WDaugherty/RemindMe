package com.example.myapplication.ui.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

<<<<<<< HEAD
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
=======
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
>>>>>>> bf6f4794179236a71d4cb1b449662d6c453938cd
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

<<<<<<< HEAD
import android.util.Log;
=======
>>>>>>> bf6f4794179236a71d4cb1b449662d6c453938cd
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
<<<<<<< HEAD
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
=======
import android.widget.TimePicker;
>>>>>>> bf6f4794179236a71d4cb1b449662d6c453938cd

import com.example.myapplication.DatePickerFragment;
import com.example.myapplication.R;
import com.example.myapplication.TimePickerFragment;
import com.example.myapplication.ui.goal.CreateViewModel;
<<<<<<< HEAD
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.List;

public class CreateFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private CreateViewModel mViewModel;
    PlacesClient placesClient;
    int startYear, startMonth, startDay;
    int endYear, endMonth, endDay;
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
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        // Start the autocomplete intent.


        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        EditText location = (EditText) view.findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(getContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }


        });
        Button startDate, startTime, endDate, endTime, discard, save;
=======
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
        View view = inflater.inflate(R.layout.fragment_create_task, container, false);
>>>>>>> bf6f4794179236a71d4cb1b449662d6c453938cd
        endDate = view.findViewById(R.id.date);
        endTime = view.findViewById(R.id.time);
        discard = view.findViewById(R.id.discard);
        save = view.findViewById(R.id.save_task);

        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment endDateFragment = new DatePickerFragment(CreateFragment.this);
                endDateFragment.show(ft, "dialog");
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
<<<<<<< HEAD
                NavController navController = Navigation.findNavController((AppCompatActivity) getActivity(), R.id.nav_host_fragment);
=======
                NavController navController = Navigation.findNavController((AppCompatActivity)getActivity(), R.id.nav_host_fragment);
>>>>>>> bf6f4794179236a71d4cb1b449662d6c453938cd
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
<<<<<<< HEAD

=======
>>>>>>> bf6f4794179236a71d4cb1b449662d6c453938cd
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        // do stuff with the date the user selected
    }
<<<<<<< HEAD

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // do stuff with the time the user selected
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("PlacesApi", "Place: " + place.getName() + ", " + place.getId());
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

=======
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // do stuff with the time the user selected
    }
}
>>>>>>> bf6f4794179236a71d4cb1b449662d6c453938cd
