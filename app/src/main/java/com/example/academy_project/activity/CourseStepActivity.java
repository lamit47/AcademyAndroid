package com.example.academy_project.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.academy_project.R;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.database.CourseDB;
import com.example.academy_project.entities.CourseStep;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseStepActivity extends YouTubeBaseActivity {
    static final String API_KEY = "AIzaSyBv3JkI699bSuXB54-N46Hn8le9ElrBaSc";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.fragment_step);

        int stepId = getIntent().getExtras().getInt("stepId", 0);
        if (stepId == 0) {
            return;
        }

        if (isNetworkAvailable()) {
            getCourseStepFromAPI(stepId);
        } else {
            getCourseStepFromDB(stepId);
        }
    }

    private void getCourseStepFromDB(int stepId) {
        CourseStep step = CourseDB.getInstance(getApplicationContext()).courseStepDAO().selectCourseStep(stepId);
        setDataToView(step);
    }

    private void deleteCourseStep(int stepId) {
        CourseDB.getInstance(getApplicationContext()).courseStepDAO().deleteCourseStep(stepId);
    }

    private void addCourseStep(CourseStep step) {
        if (step.equals(null)) {
            return;
        }
        CourseDB.getInstance(getApplicationContext()).courseStepDAO().insertCourseStep(step);
    }

    private void getCourseStepFromAPI(int stepId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<CourseStep> call = apiService.getStep(String.valueOf(stepId));
        call.enqueue(new Callback<CourseStep>() {
            @Override
            public void onResponse(Call<CourseStep> call, Response<CourseStep> response) {
                if (response.isSuccessful()) {
                    CourseStep step = response.body();
                    setDataToView(step);

                    deleteCourseStep(stepId);
                    addCourseStep(step);
                }
            }

            @Override
            public void onFailure(Call<CourseStep> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi kết nối với máy chủ!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataToView(CourseStep step) {
        try {
            TextView txtTitle = findViewById(R.id.txtTitle);
            TextView txtContent = findViewById(R.id.txtContent);

            txtTitle.setText(step.getTitle());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txtContent.setText(Html.fromHtml(step.getContent(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                txtContent.setText(Html.fromHtml(step.getContent()));
            }

            if (isNetworkAvailable()) {
                String videoId = extractYTId(step.getEmbedLink());
                loadYTPlayer(videoId);
            } else {
                YouTubePlayerView yt = findViewById(R.id.ytPlayer);
                yt.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Bạn cần internet để tải bài học này!", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    private void loadYTPlayer(String videoId) {
        YouTubePlayerView yt = findViewById(R.id.ytPlayer);

        YouTubePlayer.OnInitializedListener onInitializedListener;
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(videoId);

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        yt.initialize(API_KEY, onInitializedListener);
    }

    private static String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()){
            vId = matcher.group(1);
        }
        return vId;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
