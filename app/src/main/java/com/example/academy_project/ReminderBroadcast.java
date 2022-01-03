package com.example.academy_project;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.academy_project.activity.CourseStepActivity;

import java.util.Calendar;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

        String lastName = getLoginUseName(context);
        String title = "Hey " + lastName + ", đã đến giờ học bài rồi :)";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyCourse")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText("Hãy dành 30p phút mỗi ngày để học bài nhé!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat nmc = NotificationManagerCompat.from(context);
        nmc.notify(200, builder.build());

        try {
            String intentId = intent.getExtras().getString("Id");
            if (intentId.equals("notifyReminder"))
            {
                int[] config = getReminderConfig(context);
                setAlarm(context, config[0], config[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAlarm(Context context, int hour, int minute)
    {
        Intent intent = new Intent(context, ReminderBroadcast.class);
        intent.putExtra("Id", "notifyReminder");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar cal = Calendar.getInstance();
        if ((cal.get(Calendar.HOUR_OF_DAY) > hour) || (cal.get(Calendar.HOUR_OF_DAY) <= hour && cal.get(Calendar.MINUTE) >= minute)) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);

        long tomorrow = cal.getTimeInMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP, tomorrow, pendingIntent);
        saveReminderToSharedPerfs(context, hour, minute);
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        clearConfig(context);
    }

    public void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Course reminder";
            String description = "Course reminder channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyCourse", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void saveReminderToSharedPerfs(Context context, int hour, int minute) {
        SharedPreferences sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("ReminderHour", hour);
        editor.putInt("ReminderMinute", minute);
        editor.commit();
    }


    public int[] getReminderConfig(Context context) {
        int config[] = new int[2];
        SharedPreferences sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        config[0] = sharedPref.getInt("ReminderHour", 0);
        config[1] = sharedPref.getInt("ReminderMinute", 0);
        return config;
    }

    public void clearConfig(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        sharedPref.edit().clear().commit();
    }

    private String getLoginUseName(Context context) {
        SharedPreferences shareRef = context.getSharedPreferences("user-info", Context.MODE_PRIVATE);
        String lastName = shareRef.getString("lastName", null);
        return lastName;
    }
}
