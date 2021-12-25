package com.example.academy_project.apis;

import com.example.academy_project.entities.Login;
import com.example.academy_project.entities.Register;
import com.example.academy_project.entities.Token;
import com.example.academy_project.entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Currency;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://backend-academy.dongdev.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @POST("api/Auth/Token")
    Call<Token> getToken(@Body Login login);
    @POST("api/Auth/Refresh")
    Call<Token> refreshToken(@Body Token token);
    @POST("api/User/Register")
    Call<User> register(@Body Register register);
    @POST("api/User/Forgot")
    Call<Boolean> forgotPassword(@Body String str);
}
