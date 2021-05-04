package com.example.myapplication.ui.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.myapplication.Contract;
import com.example.myapplication.DB_Helper;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;

public class DetailFragment extends Fragment {

    private DetailViewModel mViewModel;

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        int id = -1;
        if(bundle!=null){
            id=bundle.getInt("id"); // get id of task from daily fragment
        }
        View view = inflater.inflate(R.layout.detail_fragment, container, false);

        EditText title = view.findViewById(R.id.title);
        EditText description = view.findViewById(R.id.description);
        EditText location = view.findViewById(R.id.location);
        DB_Helper myDbHelper = new DB_Helper(getActivity().getApplicationContext());
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM task where _id="+id,null);
        cursor.moveToFirst();
        // set fields with pre-existing values
        title.setText(cursor.getString(cursor.getColumnIndex("title")));
        description.setText(cursor.getString(cursor.getColumnIndex("description")));
        location.setText(cursor.getString(cursor.getColumnIndex("location")));

        String[] projection = {
                Contract.GoalEntry.COLUMN_NAME_TITLE,
                Contract.GoalEntry.COLUMN_NAME_START_DATE_TIME,
                Contract.GoalEntry.COLUMN_NAME_END_DATE_TIME,
                Contract.GoalEntry.COLUMN_NAME_COMPLETED
        };
        // query for all goal fields
        cursor = db.rawQuery("SELECT * FROM goal WHERE task_id="+id,null);
        int[] to = new int[] {R.id.goal_title, R.id.goal_start, R.id.goal_end, R.id.goal_complete}; // set resources to bind results to
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.row_item, cursor, projection, to);
        final ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        // TODO: Use the ViewModel
    }

}