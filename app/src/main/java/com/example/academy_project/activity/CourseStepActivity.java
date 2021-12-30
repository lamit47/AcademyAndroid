package com.example.academy_project.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.example.academy_project.R;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
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
    static int stepId = 0;
    static final String API_KEY = "AIzaSyBv3JkI699bSuXB54-N46Hn8le9ElrBaSc";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.fragment_step);

        if (stepId != 0) {
            getStep();
        } else {
            //TODO
        }
    }

    public void getStep() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<CourseStep> call = apiService.getStep(String.valueOf(stepId));
        call.enqueue(new Callback<CourseStep>() {
            @Override
            public void onResponse(Call<CourseStep> call, Response<CourseStep> response) {
                if (response.isSuccessful()) {
                    CourseStep step = response.body();

                    TextView txtTitle = findViewById(R.id.txtTitle);
                    TextView txtContent = findViewById(R.id.txtContent);

                    txtTitle.setText(step.getTitle());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        txtContent.setText(Html.fromHtml(step.getContent(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        txtContent.setText(Html.fromHtml(step.getContent()));
                    }

                    String videoId = extractYTId(step.getEmbedLink());
                    loadYTPlayer(videoId);
                }
            }

            @Override
            public void onFailure(Call<CourseStep> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void loadYTPlayer(String videoId) {
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

    public static String extractYTId(String ytUrl) {
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

    public static void setStepId(int id) {
        stepId = id;
    }
}
