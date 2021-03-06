package com.example.myapplication;

import android.provider.BaseColumns;

public final class Contract {
    public Contract( ){

      
    }
    //creates an abstract class that stores all of the componenets of tasks
    
    public static abstract class TaskEntry implements BaseColumns{
        public static final String TABLE_NAME ="task";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_DATE_TIME = "datetime";
        public static final String COLUMN_NAME_COMPLETED = "completed";

    }
    //creates an abstract class that gives all of the components of goals
    
    public static abstract class GoalEntry implements BaseColumns{
        public static final String TABLE_NAME = "goal";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_START_DATE_TIME = "start_datetime";
        public static final String COLUMN_NAME_END_DATE_TIME = "end_datetime";
        public static final String COLUMN_NAME_TASK_ID = "task_id";
        public static final String COLUMN_NAME_COMPLETED = "completed";
    }
}
