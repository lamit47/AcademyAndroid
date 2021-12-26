package com.example.academy_project.apis;

import android.content.Context;

import com.example.academy_project.MainActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static String host = "https://backend-academy.dongdev.com";
    private static Retrofit retrofit = null;

    private RetrofitClient() {
    }

    public static Retrofit getInstance() {
        if (retrofit == null) {
            Context context = MainActivity.getContextOfApplication();

            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequests(1);

            TokenInterceptor interceptior = new TokenInterceptor(context);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptior)
                    .dispatcher(dispatcher)
                    .build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();

            retrofit = new Retrofit.Builder().baseUrl(host)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }

        return retrofit;
    }
}
