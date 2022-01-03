package com.example.academy_project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.database.CourseDB;
import com.example.academy_project.entities.Course;
import com.example.academy_project.entities.CourseStep;
import com.example.academy_project.entities.Step;
import com.example.academy_project.entities.TrackStep;
import com.example.academy_project.fragment.SettingsFragment;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadService extends Service {
    public static final String CHANNEL_ID = "DownloadServiceChannel";
    int lastStepId = 0;
    int lastCourseId = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getListCourseFromAPI();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Bắt đầu tải dữ liệu......", Toast.LENGTH_SHORT).show();
        SettingsFragment.startDownload();

        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Đang tải dữ liệu......")
                .setContentText("Quá trình này có thể mất nhiều thời gian.")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_graduation_cap)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // Download step
    private void deleteCourseStep(int stepId) {
        CourseDB.getInstance(this).courseStepDAO().deleteCourseStep(stepId);
    }

    private void addCourseStep(CourseStep step) {
        if (step.equals(null)) {
            return;
        }
        CourseDB.getInstance(this).courseStepDAO().insertCourseStep(step);
    }

    private void getCourseStepFromAPI(int stepId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<CourseStep> call = apiService.getStep(String.valueOf(stepId));
        call.enqueue(new Callback<CourseStep>() {
            @Override
            public void onResponse(Call<CourseStep> call, Response<CourseStep> response) {
                if (response.isSuccessful()) {
                    CourseStep step = response.body();

                    deleteCourseStep(step.getId());
                    addCourseStep(step);
                    if (lastStepId == step.getId()) {
                        Toast.makeText(getApplicationContext(), "Tải dữ liệu hoàn tất!", Toast.LENGTH_SHORT).show();
                        SettingsFragment.stopDownload();
                        stopSelf();
                    }
                }
            }

            @Override
            public void onFailure(Call<CourseStep> call, Throwable t) {
            }
        });
    }

    // Download trackstep
    private void deleteTrackStepByCourseId(int courseId) {
        CourseDB.getInstance(this).trackStepDAO().deleteTrackStepByCourseId(courseId);
    }

    private void addTrackStep(TrackStep trackStep) {
        if (trackStep.equals(null)) {
            return;
        }
        CourseDB.getInstance(this).trackStepDAO().insertTrackStep(trackStep);
    }

    private void getTrackStepsFromAPI(int courseId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<TrackStep>> call = apiService.getTrackStep(String.valueOf(courseId));
        call.enqueue(new Callback<List<TrackStep>>() {
            @Override
            public void onResponse(Call<List<TrackStep>> call, Response<List<TrackStep>> response) {
                if (response.isSuccessful()) {
                    List<TrackStep> trackSteps = response.body();

                    if (lastCourseId == courseId) {
                        List<Step> listStep = trackSteps.get(trackSteps.size() - 1).getSteps();
                        lastStepId = listStep.get(listStep.size() - 1).getId();
                    }

                    deleteTrackStepByCourseId(courseId);
                    for (TrackStep ts : trackSteps) {
                        addTrackStep(ts);
                        for (Step step : ts.getSteps()) {
                            getCourseStepFromAPI(step.getId());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TrackStep>> call, Throwable t) {
            }
        });
    }

    // Download course
    private void nukeCourses() {
        CourseDB.getInstance(this).courseDAO().nukeCourses();
    }

    private void addCourse(Course course) {
        if (course.getId() == 0 || TextUtils.isEmpty(course.getTitle()) || TextUtils.isEmpty(course.getPicturePath())) {
            return;
        }
        CourseDB.getInstance(this).courseDAO().insertCourse(course);
    }

    private void getListCourseFromAPI() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<Course>> call = apiService.getListCourse();
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful()) {
                    List<Course> listcourse = response.body();

                    nukeCourses();
                    lastCourseId = listcourse.get(listcourse.size() - 1).getId();
                    for (Course c : listcourse) {
                        addCourse(c);
                        getTrackStepsFromAPI(c.getId());
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Download Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
