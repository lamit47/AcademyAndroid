package com.example.academy_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.entities.Course;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursesFragment extends Fragment {
    List<String> listCourse = new ArrayList<>();
    ArrayAdapter<String> adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses, container, false);
        getListCourse();
        return view;
    adapter = new ArrayAdapter<>()

    }
    public void getListCourse(){
        RetrofitClient.getInstance().create(ApiService.class).getListCourse().enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful()){
                    List<Course> listcourse = response.body();
                    System.out.println(listcourse.size());
                    for (Course course:listcourse
                         ) {
                        System.out.printf(course.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                System.out.println("abc");
                System.out.println(t.toString());
            }
        });
    }
}
