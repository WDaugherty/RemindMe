package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.ui.task.CreateFragment;

public class DB_Helper extends SQLiteOpenHelper {
    //Defines the database
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "remindme.db";
    private static final String COMMA_SEP =",";
    private static final String TEXT_TYPE = " TEXT";
    private static final String SQL_CREATE_TASK = "CREATE TABLE " + Contract.GoalEntry.TABLE_NAME + " (" +
            Contract.TaskEntry._ID + "INTEGER PRIMARY KEY" + COMMA_SEP+
            Contract.TaskEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP+
            Contract.TaskEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP+
            Contract.TaskEntry.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP+
            Contract.TaskEntry.COLUMN_NAME_DATE_TIME + " INTEGER" + COMMA_SEP +
            Contract.TaskEntry.COLUMN_NAME_COMPLETED + " INTEGER" +
            ")";
    private static final String SQL_CREATE_GOAL = "CREATE TABLE " + Contract.TaskEntry.TABLE_NAME + " (" +
            Contract.GoalEntry._ID + "INTEGER PRIMARY KEY" + COMMA_SEP+
            Contract.GoalEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP+
            Contract.GoalEntry.COLUMN_NAME_START_DATE_TIME + "INTEGER" + COMMA_SEP +
            Contract.GoalEntry.COLUMN_NAME_END_DATE_TIME + " INTEGER" + COMMA_SEP +
            Contract.GoalEntry.COLUMN_NAME_COMPLETED + " INTEGER" +
            Contract.GoalEntry.COLUMN_NAME_TASK_ID + " INTEGER,"+ " FOREIGN KEY (task) REFERENCES task (_ID)" +

            ")";
    private static final String SQL_DROP_TASK = "DROP TABLE IF EXISTS " + Contract.TaskEntry.TABLE_NAME;
    private static final String SQL_DROP_GOAL = "DROP TABLE IF EXISTS " + Contract.GoalEntry.TABLE_NAME;
    private static final String SQL_INSERT_TASK = "INSERT INTO " + Contract.TaskEntry.TABLE_NAME +
            "(" +
            Contract.TaskEntry.COLUMN_NAME_TITLE+
            COMMA_SEP + Contract.TaskEntry.COLUMN_NAME_DESCRIPTION +
            COMMA_SEP + Contract.TaskEntry.COLUMN_NAME_LOCATION +
            COMMA_SEP + Contract.TaskEntry.COLUMN_NAME_DATE_TIME +
            COMMA_SEP + Contract.TaskEntry.COLUMN_NAME_COMPLETED +
            ")" +
            " VALUES (" +
            CreateFragment.str_title + COMMA_SEP +
            CreateFragment.str_description + COMMA_SEP +
            CreateFragment.str_location + COMMA_SEP +
            CreateFragment.unix + COMMA_SEP +
            0 +
            ")";

    public DB_Helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //need constructor with just context...
    public DB_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TASK);
        db.execSQL(SQL_CREATE_GOAL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TASK);
        db.execSQL(SQL_DROP_GOAL);
        onCreate(db);
    }


}

