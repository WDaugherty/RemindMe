package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.Contract;
import com.example.myapplication.DB_Helper;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import static android.R.id.list;

public class DB_Display extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_display_db);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //instead of array and preferences have the DB stuff here

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), com.example.myapplication.ui.task.CreateFragment.class);
                startActivity(intent);
            }
        });

        DB_Helper dbHelper = new DB_Helper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //
        //out of dataset which columns to use projection

        String[] projection = {
                Contract.TaskEntry.COLUMN_NAME_TITLE
        };

        String[] bind = {
                Contract.TaskEntry.TABLE_NAME,
                Contract.TaskEntry.COLUMN_NAME_TITLE,
                Contract.TaskEntry.COLUMN_NAME_DESCRIPTION,
                Contract.TaskEntry.COLUMN_NAME_LOCATION,
                Contract.TaskEntry.COLUMN_NAME_START_TIME,
                Contract.TaskEntry.COLUMN_NAME_START_DATE,
                Contract.TaskEntry.COLUMN_NAME_END_DATE,
                Contract.TaskEntry.COLUMN_NAME_END_TIME,
        };

        //now going to call method to return cursor

        Cursor cursor = db.query(Contract.TaskEntry.TABLE_NAME, //table to query
                bind,
                null, //columns for where, Null will return all rows
                null, //values for where
                null, //Group By, null is no group by
                null, //Having, null says return all rows
                Contract.TaskEntry.COLUMN_NAME_END_DATE + " ASC" //names in alpabetical order
        );


        //the list items from the layout, will find these in the row_item,
        //these are the 4 fields being displayed
        int[] to = new int[]{
                //R.id.first,  R.id.last, R.id.phone, R.id.email
        };

        //create the adapter
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.row_item, cursor, projection, to, 0);

        //set the list to the adapter
        final ListView listView = (ListView) findViewById(list);
        listView.setAdapter(adapter);

        //set up for the empty non data messaged
        TextView emptyView = (TextView) findViewById(android.R.id.empty);
        listView.setEmptyView(emptyView);

        //need to set the On Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                //this is returning a cursor this time, so need to get the string out of the cursor
                String selectedItem = (String) cursor.getString(cursor.getColumnIndex(Contract.TaskEntry.COLUMN_NAME_TITLE));
                //Used a Snackbar instead of a toast, just something different
                Snackbar.make(view, selectedItem, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }


        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_display_db, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.enterValues) {
//            Intent intent = new Intent(getApplicationContext(), AddUserActivity.class);
//            startActivity(intent);
//            return true;
//        }


        return super.onOptionsItemSelected(item);
    }

}