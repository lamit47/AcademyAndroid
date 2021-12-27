package com.example.academy_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.entities.User;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static Context context;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);

        if (isLogin() == false) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener((view) -> {
            getUser();
        });
    }

    public static Context getContextOfApplication(){
        return context;
    }

    public static boolean isLogin() {
        SharedPreferences sharedPref = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        String refreshToken = sharedPref.getString("refreshToken", null);
        long RTExpiresStr = sharedPref.getLong("RTExpires", 0);
        Date RTExpires = new Date(RTExpiresStr);
        Date now = new Date();

        if (refreshToken != null && now.before(RTExpires)) {
            return true;
        }
        return false;
    }

    private void getUser() {
        RetrofitClient.getInstance().create(ApiService.class).getUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();

                    System.out.println(user.toString());
                    Toast.makeText(MainActivity.this, user.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}