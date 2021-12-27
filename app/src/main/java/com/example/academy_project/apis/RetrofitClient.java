package com.example.academy_project.apis;

import android.content.Context;

import com.example.academy_project.MainActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static Retrofit retrofitWithoutToken = null;
    private static String API_HOST = "https://backend-academy.dongdev.com";
    private static int CONNECT_TIMEOUT = 30;
    private static int READ_TIMEOUT = 15;
    private static int WRITE_TIMEOUT = 15;

    private RetrofitClient() {
    }

    public static Retrofit getInstance() {
        if (retrofit == null) {
            Context context = MainActivity.getContextOfApplication();

            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequests(1);

            TokenInterceptor interceptior = new TokenInterceptor(context);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(interceptior)
                    .dispatcher(dispatcher)
                    .build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();

            retrofit = new Retrofit.Builder().baseUrl(API_HOST)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }

        return retrofit;
    }

    public static Retrofit getInstanceWithoutToken() {
        if (retrofitWithoutToken == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .build();

            retrofitWithoutToken = new Retrofit.Builder().baseUrl(API_HOST)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofitWithoutToken;
    }
}
