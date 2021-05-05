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
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.Contract;
import com.example.myapplication.DB_Helper;
import com.example.myapplication.DailyActivity;
import com.example.myapplication.DatePickerFragment;
import com.example.myapplication.DatePickerFragment2;
import com.example.myapplication.R;
import com.example.myapplication.TimePickerFragment;
import com.example.myapplication.TimePickerFragment2;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.HashMap;

public class CreateFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private boolean startDateOrEndDate = true;
    private boolean startTimeOrEndTime = true;
    private CreateViewModel mViewModel;
    int startYear, startMonth, startDay, startHour, startMinute, startMillis;
    int endYear, endMonth, endDay, endHour, endMinute, endMillis;
    private AppBarConfiguration mAppBarConfiguration;
    HashMap<Integer, Integer> posToID = new HashMap<Integer, Integer>();
    EditText title;
    Spinner task;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Cursor taskCursor;
        DB_Helper dbHelper = new DB_Helper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // set values to get today in millis
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        long today = cal.getTimeInMillis();
        // select upcoming tasks and set results to the spinner
        taskCursor = db.rawQuery("SELECT _id, title FROM TASK WHERE datetime>"+today, null);
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, taskCursor, new String[]{"title"}, new int[]{android.R.id.text1});
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        View view = inflater.inflate(R.layout.fragment_create_goal, container, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setAdapter(mAdapter);
        // populate hashmap to correlate position id to task id
        int count = taskCursor.getCount();
        for (int i = 0; i<count; i++){
            posToID.put(i, taskCursor.getInt(taskCursor.getColumnIndex("_id")));
            taskCursor.moveToNext();
        }
        Button startDate, startTime, endDate, endTime, discard, save;

        startDate = view.findViewById(R.id.start_date);
        endDate = view.findViewById(R.id.end_date);
        startTime = view.findViewById(R.id.start_time);
        endTime = view.findViewById(R.id.end_time);
        discard = view.findViewById(R.id.discard);
        save = view.findViewById(R.id.save_goal);
        title = view.findViewById(R.id.title);
        task = view.findViewById(R.id.spinner);

        // set on click listeners
        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startDateOrEndDate = true; // variable to determine which global variables are set in on date set
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                DialogFragment startDateFragment = new DatePickerFragment(CreateFragment.this);
                startDateFragment.show(ft, "dialog");
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startDateOrEndDate = false; // variable to determine which global variables are set in on date set
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                DialogFragment endDateFragment = new DatePickerFragment2(CreateFragment.this);
                endDateFragment.show(ft, "dialog");
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startTimeOrEndTime = true;// variable to determine which global variables are set in on time set
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                DialogFragment startTimeFragment = new TimePickerFragment(CreateFragment.this);
                startTimeFragment.show(ft, "dialog");
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startTimeOrEndTime = false;// variable to determine which global variables are set in on time set
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                DialogFragment endTimeFragment = new TimePickerFragment2(CreateFragment.this);
                endTimeFragment.show(ft, "dialog");
            }
        });
        discard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) { // discard and navigate up backstack
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
            public void onClick(View v) { // save and navigate up backstack
                // Execute SQL insertion
                DB_Helper myDbHelper = new DB_Helper(getActivity().getApplicationContext());
                SQLiteDatabase db = myDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                // calculate start time in millis
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, startYear);
                cal.set(Calendar.MONTH, startMonth);
                cal.set(Calendar.DAY_OF_MONTH, startDay);
                cal.set(Calendar.HOUR_OF_DAY, startHour);
                cal.set(Calendar.MINUTE, startMinute);
                long millis = cal.getTimeInMillis();
                values.put(Contract.GoalEntry.COLUMN_NAME_START_DATE_TIME, millis);

                //end time in millis
                cal.set(Calendar.YEAR, endYear);
                cal.set(Calendar.MONTH, endMonth);
                cal.set(Calendar.DAY_OF_MONTH, endDay);
                cal.set(Calendar.HOUR_OF_DAY, endHour);
                cal.set(Calendar.MINUTE, endMinute);
                millis = cal.getTimeInMillis();
                values.put(Contract.GoalEntry.COLUMN_NAME_END_DATE_TIME, millis);

                // put title
                String str_title = title.getText().toString();
                values.put(Contract.GoalEntry.COLUMN_NAME_TITLE, str_title);

                // convert spinner location to task id and put
                int task_id = posToID.get(spinner.getSelectedItemPosition()); //CHANGE THIS TO NOT BE HARD CODED LATER
                values.put(Contract.GoalEntry.COLUMN_NAME_TASK_ID, task_id);

                // assume not complete
                int completed = 0;
                values.put(Contract.GoalEntry.COLUMN_NAME_COMPLETED, completed);


                long newRowId = db.insert(
                        Contract.GoalEntry.TABLE_NAME,  //table name for insert
                        null,  //null is all columns
                        values);  //values for the insert
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
        // store as global vars depending on start or end
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
        // store as global vars depending on start or end

        if(startTimeOrEndTime){
            startHour = hourOfDay;
            startMinute = minute;
        } else {
            endHour = hourOfDay;
            endMinute = minute;
        }
    }
}