package com.example.myapplication.ui.monthly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.ui.daily.DailyFragment;
import com.google.android.material.navigation.NavigationView;

public class MonthlyFragment extends Fragment {

    private MonthlyViewModel mViewModel;
    CalendarView calendar;
    TextView dateView;
    int year_g, month_g, day_g;
    public static MonthlyFragment newInstance() {
        return new MonthlyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.monthly_fragment, container, false);
        Button tasks = view.findViewById(R.id.tasks);
        calendar = view.findViewById(R.id.calender);
        dateView = view.findViewById(R.id.dateView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() { // set date as variables when selected
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //sets the date
                year_g = year;
                month_g = month;
                day_g = dayOfMonth;
                String Date = dayOfMonth + "-" + (month + 1) + "-" + year;
                dateView.setText(Date); // set string above calendar
            }
        });
        tasks.setOnClickListener(new View.OnClickListener() { // when click "view tasks"

            @Override
            public void onClick(View v) { // store variables in a bundle
                Bundle bundle = new Bundle();
                bundle.putInt("year",year_g);
                bundle.putInt("month", month_g);
                bundle.putInt("day", day_g);
                AppBarConfiguration mAppBarConfiguration;
                DrawerLayout drawer = v.findViewById(R.id.drawer_layout);
                NavigationView navigationView = v.findViewById(R.id.nav_view);
                // Passing each menu ID as a set of Ids because each
                // menu should be considered as top level destinations.
                mAppBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_daily, R.id.nav_weekly, R.id.nav_monthly)
                        .setDrawerLayout(drawer)
                        .build();
                NavController navController = Navigation.findNavController((AppCompatActivity)getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_daily,bundle); // navigate to daily and send bundle

            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MonthlyViewModel.class);
        // TODO: Use the ViewModel
    }

}
