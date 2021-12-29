package com.example.academy_project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.academy_project.R;
import com.example.academy_project.adapter.TrackStepsAdapter;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.entities.Course;
import com.example.academy_project.entities.TrackStep;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackStepsFragment extends Fragment {
    static int courseId = 0;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tracksteps, container, false);
        if (courseId != 0) {
            getTrackSteps(courseId);
        }
        return view;
    }

    public void getTrackSteps(int courseId) {
        RetrofitClient.getInstance()
                .create(ApiService.class)
                .getTrackStep(String.valueOf(courseId))
                .enqueue(new Callback<List<TrackStep>>() {
                    @Override
                    public void onResponse(Call<List<TrackStep>> call, Response<List<TrackStep>> response) {
                        if (response.isSuccessful()) {
                            List<TrackStep> trackSteps = response.body();

                            TrackStepsAdapter trackStepsAdapter = new TrackStepsAdapter(new ArrayList<TrackStep>(trackSteps));

                            ExpandableListView lvTrackSteps = view.findViewById(R.id.lvTrackSteps);
                            lvTrackSteps.setAdapter(trackStepsAdapter);

                            lvTrackSteps.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                                @Override
                                public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                                    long id = trackStepsAdapter.getChildId(i, i1);

                                    Toast.makeText(getActivity().getApplicationContext(), String.valueOf(id), Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TrackStep>> call, Throwable t) {
                        System.out.println(t.toString());
                    }
                });
    }

    public static void setCourseId(int id) {
        courseId = id;
    }
}
