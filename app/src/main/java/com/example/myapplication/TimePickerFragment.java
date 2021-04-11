package com.example.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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

public class TimePickerFragment extends DialogFragment {
    private Fragment mFragment;

    public TimePickerFragment(Fragment callback) {
        mFragment = callback;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) mFragment, 12, 0, false);
    }
}