package com.example.academy_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.academy_project.DownloadService;
import com.example.academy_project.R;
import com.example.academy_project.ReminderBroadcast;

public class SettingsFragment extends Fragment {
    View view;
    Button btnSave, btnCancel;
    static Button btnDownload, btnDownloadCancel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        btnSave = view.findViewById(R.id.btnSubmit);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnDownload = view.findViewById(R.id.btnDownload);
        btnDownloadCancel = view.findViewById(R.id.btnDownloadCancel);

        btnSave.setOnClickListener(v -> {
            startReminder();
        });
        btnCancel.setOnClickListener(v -> {
            stopReminder();
        });

        btnDownload.setOnClickListener(v -> {
            startService();
        });
        btnDownloadCancel.setOnClickListener(v -> {
            stopService();
        });

        return view;
    }

    public static void startDownload() {
        btnDownloadCancel.setVisibility(View.VISIBLE);
        btnDownload.setVisibility(View.GONE);
    }

    public static void stopDownload() {
        btnDownloadCancel.setVisibility(View.GONE);
        btnDownload.setVisibility(View.VISIBLE);
    }

    public void startReminder() {
        TimePicker timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        Toast.makeText(getContext(), "Đã đặt thời gian nhắc học!", Toast.LENGTH_SHORT).show();

        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        ReminderBroadcast reminderBroadcast = new ReminderBroadcast();
        reminderBroadcast.createNotificationChannel(getActivity());
        reminderBroadcast.setAlarm(getActivity(), hour, minute);
    }

    public void stopReminder() {
        Toast.makeText(getContext(), "Đã tắt nhắc học!", Toast.LENGTH_SHORT).show();
        ReminderBroadcast reminderBroadcast = new ReminderBroadcast();
        reminderBroadcast.createNotificationChannel(getActivity());
        reminderBroadcast.cancelAlarm(getActivity());
    }

    public void startService() {
//        Intent serviceIntent = new Intent(getActivity(), DownloadService.class);
//        serviceIntent.putExtra("inputExtra", "Tai khoa hoc de su dung khi offline");
//        ContextCompat.startForegroundService(getActivity(), serviceIntent);
        Intent intent = new Intent(getActivity(), DownloadService.class);
        getActivity().startService(intent);
    }
    public void stopService() {
        Intent serviceIntent = new Intent(getActivity(), DownloadService.class);
        getActivity().stopService(serviceIntent);
    }
}
