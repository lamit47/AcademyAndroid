package com.example.academy_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.academy_project.Adapter.CourseListAdapter;
import com.example.academy_project.Adapter.TrackStepListAdapter;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.entities.Course;
import com.example.academy_project.entities.TrackStep;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseFragment extends Fragment {

    ArrayList<TrackStep> listTrackStep;
    TrackStepListAdapter TrackStepListAdapter;
    ListView listViewTrackStep;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.course, container, false);
        getCourse();
        return view;
    }
    public void getCourse(){
        RetrofitClient.getInstance().create(ApiService.class).getTrackStep("1").enqueue(new Callback<List<TrackStep>>() {
            @Override
            public void onResponse(Call<List<TrackStep>> call, Response<List<TrackStep>> response) {
                if(response.isSuccessful()){
                    List<TrackStep> listTracStep =  response.body();
                    System.out.println("Anh nam"+listTracStep.size());
                    TrackStepListAdapter = new TrackStepListAdapter(new ArrayList<TrackStep>(listTracStep));

                    listViewTrackStep = view.findViewById(R.id.listTrackStep);
                    listViewTrackStep.setAdapter(TrackStepListAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<TrackStep>> call, Throwable t) {

            }
        });
    }
}
