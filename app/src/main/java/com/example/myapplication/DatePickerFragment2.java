package com.example.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

//picks a date for when a goal/task will be completed

public class DatePickerFragment2 extends DialogFragment {
    private Fragment mFragment;

    public DatePickerFragment2(Fragment callback) {
        mFragment = callback;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) mFragment, 1980, 7, 16);
    }
}
