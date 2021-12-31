package com.example.academy_project.fragment;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.academy_project.R;
import com.example.academy_project.ReminderBroadcast;

import java.util.Calendar;

public class SettingsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button btnSave = view.findViewById(R.id.btnSubmit);
        Button btnCannel = view.findViewById(R.id.btnCannel);
        TimePicker timePicker = (TimePicker) view.findViewById(R.id.timePicker);

        btnSave.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đã đặt thời gian nhắc học!", Toast.LENGTH_SHORT).show();

            int hour = timePicker.getCurrentHour();
            int minute = timePicker.getCurrentMinute();

            ReminderBroadcast reminderBroadcast = new ReminderBroadcast();
            reminderBroadcast.createNotificationChannel(getActivity());
            reminderBroadcast.setAlarm(getActivity(), hour, minute);
        });
        btnCannel.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đã tắt nhắc học!", Toast.LENGTH_SHORT).show();

            ReminderBroadcast reminderBroadcast = new ReminderBroadcast();
            reminderBroadcast.createNotificationChannel(getActivity());
            reminderBroadcast.cancelAlarm(getActivity());
        });
        return view;
    }
}
