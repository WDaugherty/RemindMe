package com.example.myapplication.ui.daily;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;

import android.widget.ProgressBar;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.myapplication.DB_Helper;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class DailyFragment extends Fragment {
    private DailyViewModel mViewModel;
    View rootView;
    ExpandableListView lv;

    //hashmaps to track ID to index and Index to ID for task and goal
    HashMap<Integer, Integer> taskIdToIndex = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> indexToTaskId = new HashMap<Integer, Integer>();
    HashMap<int[], Integer> indexToGoalId = new HashMap<int[], Integer>();
    HashMap<Integer, int[]> goalIdToIndex = new HashMap<Integer, int[]>();

    public static String[] groups;
    public static String[][] children;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;

    public DailyFragment() {

    }
    public String[] getTasks(){ // This function queries the database and populates a list array. The list array is then converted to an array and stored in the groups variable
        // Set calendar values to calculate time in ms
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.HOUR_OF_DAY, 0);
        calendar.set(calendar.MINUTE, 0);
        calendar.set(calendar.MILLISECOND, 0);
        Bundle bundle = this.getArguments();
        if (bundle != null) { // if date came from monthly view
            int year = bundle.getInt("year");
            int month = bundle.getInt("month");
            int day = bundle.getInt("day");
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);
        }

        long today = calendar.getTimeInMillis(); // today in ms
        calendar.add(calendar.DATE, 1); // add one day
        long tomorrow = calendar.getTimeInMillis(); // tomorrow in ms
        DB_Helper dbHelper = new DB_Helper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // query for task titles between today and tomorrow
        Cursor cursor = db.rawQuery("SELECT title FROM task WHERE datetime>"+today+" AND datetime<"+tomorrow+" ORDER BY completed, datetime", null);
        cursor.moveToFirst();
        ArrayList<String> tasks = new ArrayList<String>();
        // populate list of tasks
        while(!cursor.isAfterLast()) {
            if(cursor!=null && cursor.getCount() > 0)
            {
            tasks.add(cursor.getString(cursor.getColumnIndex("title")));
            cursor.moveToNext();
            }
        }
        cursor.close();
        return tasks.toArray(new String[tasks.size()]);
    }
    public String[][] getGoals(){//populates nested array. For each task in date range, adds another array. Nested array contains goal info
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.HOUR_OF_DAY, 0);
        calendar.set(calendar.MINUTE, 0);
        calendar.set(calendar.MILLISECOND, 0);
        Bundle bundle = this.getArguments();
        if (bundle != null) { // if date came from monthly view
            int year = bundle.getInt("year");
            int month = bundle.getInt("month");
            int day = bundle.getInt("day");
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);
        }

        long today = calendar.getTimeInMillis(); // today in ms
        calendar.add(calendar.DATE, 1); // add a day
        long tomorrow = calendar.getTimeInMillis(); // tomorrow in ms
        DB_Helper dbHelper = new DB_Helper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // requery for task titles and ids between today and tomorrow
        Cursor cursor = db.rawQuery("SELECT title, _id FROM task WHERE datetime>"+today+" AND datetime<"+tomorrow+" ORDER BY completed, datetime", null);
        cursor.moveToFirst();
        ArrayList<ArrayList<String>> goals = new ArrayList<ArrayList<String>>();
        for(int i = 0; i<cursor.getCount() ; i++){ // populate goals 1d array with arrays. Hashmaps are used for querying with id
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            taskIdToIndex.put(id,i);
            indexToTaskId.put(i,id);
            goals.add(new ArrayList<String>());
            cursor.moveToNext();
        }
        // query for goal information, joined on task id
        cursor = db.rawQuery("SELECT goal.title, goal._id, task_id FROM goal JOIN task ON goal.task_id=task._id WHERE datetime>"+today+" AND datetime<"+tomorrow+" ORDER BY goal.completed", null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) { // this loop populates the nested array. It was also supposed to be able to gather the goal id so we could make the goal
            //options button work. This never happened :(
            int task_num = cursor.getInt(cursor.getColumnIndex("task_id"));
            int id = taskIdToIndex.get(task_num)+1;
            int firstPos = id;
            int secondPos = goals.indexOf(cursor.getInt(cursor.getColumnIndex("task_id")))+id;
            goals.get(secondPos)
                    .add(cursor.getString(cursor.getColumnIndex("title")));
            secondPos++;
            int[] pos = {firstPos, secondPos};
            indexToGoalId.put(pos, cursor.getInt(cursor.getColumnIndex("goal._id")));
            goalIdToIndex.put(cursor.getInt(cursor.getColumnIndex("goal._id")), pos);
            cursor.moveToNext();
        }
        cursor.close();
        String[][] array = new String[goals.size()][];// convert to nested array from arraylist
        for (int i = 0; i < goals.size(); i++) {
            ArrayList<String> row = goals.get(i);
            array[i] = row.toArray(new String[row.size()]);
        }
        return array;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //adds the text into the arrays created above

        groups = getTasks();
        children = getGoals();
    }
    @Override
    public void setMenuVisibility(boolean isvisible) { // this is overridden solely to refresh the list when its reopened from the backstack
        super.setMenuVisibility(isvisible);
        if (isvisible){
            groups = getTasks();
            children = getGoals();
        }else {
            groups = getTasks();
            children = getGoals();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DB_Helper dbHelper = new DB_Helper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        groups = getTasks();
        children = getGoals();
        rootView = inflater.inflate(R.layout.daily_fragment, container, false);
        // setting up navigation controller so the variable can be accessed later
        DrawerLayout drawer = rootView.findViewById(R.id.drawer_layout);
        NavigationView navigationView = rootView.findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_daily, R.id.nav_weekly, R.id.nav_monthly)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        ProgressBar pb=(ProgressBar) rootView.findViewById(R.id.pb);
        //TODO: Select # of Complete Activities for Day Out of # of Total Activities. Multiply by 100 and Floor Result. Set Result to pb.
        pb.setProgress(33);


        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv = (ExpandableListView) view.findViewById(R.id.expandableListView);
        lv.setAdapter(new ExpandableListAdapter(groups, children)); // set the adapter to the populated arrays
        lv.setGroupIndicator(null);



    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private final LayoutInflater inf;
        private String[] groups;
        private String[][] children;
        //expandable list adapter constructor
        public ExpandableListAdapter(String[] groups, String[][] children) {
            this.groups = groups;
            this.children = children;
            inf = LayoutInflater.from(getActivity());
        }

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return children[groupPosition].length;

        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return children[groupPosition][childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            // displays the child view information
            ViewHolder holder;
            if (convertView == null) {
                convertView = inf.inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder();

                holder.text = (TextView) convertView.findViewById(R.id.item1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.text.setText(getChild(groupPosition, childPosition).toString());

            return convertView;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            // displays group/parent informaion
            ViewHolder holder;

            if (convertView == null) {
                convertView = inf.inflate(R.layout.list_group, parent, false);

                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.listTitle);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Button options = (Button) convertView.findViewById(R.id.task_options);
            options.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) { // on options button click
                    final CharSequence[] items = {"View Task Details", "Mark Task Complete", "Edit Task Details", "Delete Task"};
                    // build an alert with the above options
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Select Option:");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            switch (item){
                                case 0: // view
                                    //navigate to view details activity. Pass a bundle with ID
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("id", indexToTaskId.get(groupPosition));
                                    navController.navigate(R.id.nav_task_details, bundle);
                                    break;
                                case 1: // completed
                                    // SQL query mark complete
                                    DB_Helper dbHelper = new DB_Helper(getActivity().getApplicationContext());
                                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                                    int id = indexToTaskId.get(groupPosition);
                                    db.execSQL("UPDATE task SET completed=1 WHERE _id="+id);
                                    // if task is complete, assume all associate goals are complete
                                    db.execSQL("UPDATE goal SET completed=1 WHERE task_id="+id);
                                    //update the arrays to update view
                                    groups = getTasks();
                                    children = getGoals();
                                    break;
                                case 2: // edit
                                    // navigate to edit activity. Pass a bundle with ID
                                    bundle = new Bundle();
                                    bundle.putInt("id", indexToTaskId.get(groupPosition));
                                    navController.navigate(R.id.nav_task_edit, bundle);
                                    break;
                                case 3: //delete
                                    final CharSequence[] yesNo = {"Yes", "No"};
                                    // build dialog to confirm delete
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Are you sure you want to delete?");
                                    builder.setItems(yesNo, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case 0: // yes
                                                    DB_Helper dbHelper = new DB_Helper(getActivity().getApplicationContext());
                                                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                                                    int id = indexToTaskId.get(groupPosition);
                                                    // delete all goals for task and delete task
                                                    db.execSQL("DELETE FROM goal WHERE task_id="+id);
                                                    db.execSQL("DELETE FROM task WHERE _id="+id);
                                                    // update arrays for view update
                                                    groups = getTasks();
                                                    children = getGoals();
                                                    break;
                                                case 1:
                                                    // do nothing
                                                default:
                                                    break;
                                            }
                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            holder.text.setText(getGroup(groupPosition).toString());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }



        private class ViewHolder {
            TextView text;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DailyViewModel.class);
        // TODO: Use the ViewModel
    }

}