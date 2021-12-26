package com.example.academy_project.apis;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.academy_project.MainActivity;
import com.example.academy_project.entities.Login;
import com.example.academy_project.entities.Register;
import com.example.academy_project.entities.Token;
import com.example.academy_project.entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    String host = "https://backend-academy.dongdev.com";
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Context context = MainActivity.getContextOfApplication();
            SharedPreferences sharedPref = context.getSharedPreferences("login", Context.MODE_PRIVATE);
            String accessToken = sharedPref.getString("accessToken", null);
            Request newRequest  = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();
            return chain.proceed(newRequest);
        }
    }).build();

    ApiService apiService = new Retrofit.Builder()
            .client(client)
            .baseUrl(host)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @POST("api/Auth/Token")
    Call<Token> getToken(@Body Login login);
    @GET("api/User/Me")
    Call<User> getUser();
    @POST("api/Auth/Refresh")
    Call<Token> refreshToken(@Body Token token);
    @POST("api/User/Register")
    Call<User> register(@Body Register register);
    @POST("api/User/Forgot")
    Call<Boolean> forgotPassword(@Body String str);
}
