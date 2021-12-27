package com.example.academy_project.apis;

import com.example.academy_project.entities.Course;
import com.example.academy_project.entities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/User/Me")
    Call<User> getUser();
    @GET("api/Course")
    Call<List<Course>> getListCourse();
}
