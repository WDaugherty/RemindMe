package com.example.myapplication;

import android.provider.BaseColumns;

public final class Contract {
    public Contract( ){

    }
    public static abstract class TaskEntry implements BaseColumns{
        public static final String TABLE_NAME ="task";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_START_DATE = "start_date";
        public static final String COLUMN_NAME_END_DATE = "end_date";
        public static final String COLUMN_NAME_END_TIME = "end_time";
    }

    public static abstract class GoalEntry implements BaseColumns{
        public static final String TABLE_NAME = "goal";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TASK = "task";
    }
}
