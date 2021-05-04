//Ask what everything does in this
package com.example.myapplication.ui.weekly;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;

import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.DB_Helper;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


public class WeeklyFragment extends Fragment {
    private WeeklyViewModel mViewModel;
    View rootView;
    ExpandableListView lv;

    //declaration of arrays
    HashMap<Integer, Integer> taskIdToIndex = new HashMap<Integer, Integer>();;
    private String[] groups;
    private String[][] children;
    String[] projection;
    String[] bind;
    Cursor taskCursor;
    Cursor goalCursor;
    int[] toGroup;
    int[] toItem;

    public WeeklyFragment() {

    }
    public String[] getTasks(){ // SAME AS GET TASKS IN DAILY, JUST 7 DAYS INSTEAD OF !
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.HOUR_OF_DAY, 0);
        calendar.set(calendar.MINUTE, 0);
        calendar.set(calendar.MILLISECOND, 0);


        long today = calendar.getTimeInMillis();
        calendar.add(calendar.DATE, 7); // ONLY CHANGE FROM DAILY
        long nextWeek = calendar.getTimeInMillis();
        DB_Helper dbHelper = new DB_Helper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT title FROM task WHERE datetime>"+today+" AND datetime<"+nextWeek+"", null);
        cursor.moveToFirst();
        ArrayList<String> tasks = new ArrayList<String>();
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
    public String[][] getGoals(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.HOUR_OF_DAY, 0);
        calendar.set(calendar.MINUTE, 0);
        calendar.set(calendar.MILLISECOND, 0);


        long today = calendar.getTimeInMillis();
        calendar.add(calendar.DATE, 7);
        long nextWeek = calendar.getTimeInMillis();
        DB_Helper dbHelper = new DB_Helper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT task_id FROM goal JOIN task ON goal.task_id=task._id WHERE datetime>"+today+" AND datetime<"+nextWeek+"", null);
        cursor.moveToFirst();

        cursor = db.rawQuery("SELECT title, _id FROM task WHERE datetime>"+today+" AND datetime<"+nextWeek+"", null);
        cursor.moveToFirst();
        int test = cursor.getCount();
        ArrayList<ArrayList<String>> goals = new ArrayList<ArrayList<String>>();
        for(int i = 0; i<cursor.getCount() ; i++){
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            taskIdToIndex.put(id,i);
            goals.add(new ArrayList<String>());
            cursor.moveToNext();
        }
        cursor = db.rawQuery("SELECT goal.title, task_id FROM goal JOIN task ON goal.task_id=task._id WHERE datetime>"+today+" AND datetime<"+nextWeek+"", null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            int task_num = cursor.getInt(cursor.getColumnIndex("task_id"));
            int id = taskIdToIndex.get(task_num);
            goals.get(goals.indexOf(cursor.getInt(cursor.getColumnIndex("task_id")))+id)
                    .add(cursor.getString(cursor.getColumnIndex("title")));
            cursor.moveToNext();
        }
        cursor.close();
        String[][] array = new String[goals.size()][];
        for (int i = 0; i < goals.size(); i++) {
            ArrayList<String> row = goals.get(i);
            array[i] = row.toArray(new String[row.size()]);
        }
        return array;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpandableListAdapter adapter = new ExpandableListAdapter(groups, children);
                adapter.notifyDataSetChanged();
            }
        };
        //adds the text into the arrays created above

        groups = getTasks();
        children = getGoals();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DB_Helper dbHelper = new DB_Helper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        rootView = inflater.inflate(R.layout.daily_fragment, container, false);

        ProgressBar pb=(ProgressBar) rootView.findViewById(R.id.pb);
        //TODO: Select # of Complete Activities for Day Out of # of Total Activities. Multiply by 100 and Floor Result. Set Result to pb.
        pb.setProgress(33);


        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv = (ExpandableListView) view.findViewById(R.id.expandableListView);
        lv.setAdapter(new ExpandableListAdapter(groups, children));
        lv.setGroupIndicator(null);
        getGoals();


    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {
// THIS IS THE SAME AS IN DAILY

        private final LayoutInflater inf;
        private String[] groups;
        private String[][] children;
        //creates expandable list adapter method
        public ExpandableListAdapter(String[] groups, String[][] children) {
            this.groups = groups;
            this.children = children;
            inf = LayoutInflater.from(getActivity());
        }

        @Override
        //get me
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            try { return children[groupPosition].length;}
            catch (Exception E){return 0;}

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
            ViewHolder holder;

            if (convertView == null) {
                convertView = inf.inflate(R.layout.list_group, parent, false);

                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.listTitle);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

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
        mViewModel = new ViewModelProvider(this).get(WeeklyViewModel.class);
        // TODO: Use the ViewModel
    }

}