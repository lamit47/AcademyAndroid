package com.example.academy_project.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.academy_project.R;
import com.example.academy_project.activity.CourseStepActivity;
import com.example.academy_project.adapter.TrackStepsAdapter;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.database.CourseDB;
import com.example.academy_project.entities.Course;
import com.example.academy_project.entities.TrackStep;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackStepsFragment extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tracksteps, container, false);

        int courseId = this.getArguments().getInt("courseId");
        if (courseId == 0) {
            return view;
        }

        if (isNetworkAvailable()) {
            getTrackStepsFromAPI(courseId);
        } else {
            getTrackStepsFromDB(courseId);
        }

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTrackStepsFromAPI(courseId);
                if (srl.isRefreshing()) {
                    srl.setRefreshing(false);
                }
            }
        });
        return view;
    }

    private void getTrackStepsFromDB(int courseId) {
        List<TrackStep> list = CourseDB.getInstance(getActivity()).trackStepDAO().getListTrackStep(courseId);
        setDataToAdapter(list);
    }

    private void deleteTrackStepByCourseId(int courseId) {
        CourseDB.getInstance(getActivity()).trackStepDAO().deleteTrackStepByCourseId(courseId);
    }

    private void addTrackStep(TrackStep trackStep) {
        if (trackStep.equals(null)) {
            return;
        }
        CourseDB.getInstance(getActivity()).trackStepDAO().insertTrackStep(trackStep);
    }

    private void getTrackStepsFromAPI(int courseId) {
        RetrofitClient.getInstance()
                .create(ApiService.class)
                .getTrackStep(String.valueOf(courseId))
                .enqueue(new Callback<List<TrackStep>>() {
                    @Override
                    public void onResponse(Call<List<TrackStep>> call, Response<List<TrackStep>> response) {
                        if (response.isSuccessful()) {
                            List<TrackStep> trackSteps = response.body();
                            setDataToAdapter(trackSteps);

                            deleteTrackStepByCourseId(courseId);
                            for (TrackStep ts : trackSteps) {
                                addTrackStep(ts);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TrackStep>> call, Throwable t) {
                        System.out.println(t.toString());
                    }
                });
    }

    private void setDataToAdapter(List<TrackStep> trackSteps) {
        if (trackSteps.size() < 1) {
            Toast.makeText(getActivity(), "Danh sách bài học trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        TrackStepsAdapter trackStepsAdapter = new TrackStepsAdapter(new ArrayList<TrackStep>(trackSteps));

        ExpandableListView lvTrackSteps = view.findViewById(R.id.lvTrackSteps);
        lvTrackSteps.setAdapter(trackStepsAdapter);

        lvTrackSteps.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                long id = trackStepsAdapter.getChildId(i, i1);

                try {
                    Intent intent = new Intent(getActivity(), CourseStepActivity.class);
                    intent.putExtra("stepId", (int) id);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
