package com.example.academy_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.academy_project.apis.ApiService;
import com.example.academy_project.entities.Login;
import com.example.academy_project.entities.Token;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

//        Context context = getApplicationContext();
//        sharedPref = context.getSharedPreferences("login", Context.MODE_PRIVATE);
//
//        long RTExpiresStr = sharedPref.getLong("RTExpires", 0);
//        Date RTExpires = new Date(RTExpiresStr);
//        Date now = new Date();
//        String accessToken = sharedPref.getString("accessToken", null);
//
//        //Nếu refresh token hết hạn chuyển về trang đăng nhập
//        if (now.after(RTExpires)) {
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(intent);
//            return;
//        }
//        long ATExpiresStr = sharedPref.getLong("ATExpires", 0);
//        Date ATExpires = new Date(ATExpiresStr);
//        // Nếu access token hết hạn làm mới AC bằng refresh token
//        if (now.after(ATExpires)) {
//            Toast.makeText(MainActivity.this, "Làm mới token!", Toast.LENGTH_LONG).show();
//            String refreshToken = sharedPref.getString("refreshToken", null);
//            refeshToken(refreshToken);
//        }

    }

    private void refeshToken(String refreshToken) {
        Token token = new Token();
        token.setRefreshToken(refreshToken);
        ApiService.apiService.refreshToken(token).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                SharedPreferences.Editor editor = sharedPref.edit();

                if (response.isSuccessful()) {
                    Token token = response.body();

                    System.out.println(token.toString());
                    editor.putString("accessToken", token.getAccessToken());
                    editor.putLong("ATExpires", token.getAccessTokenExpires().getTime());
                    editor.commit();
                } else {
                    Toast.makeText(MainActivity.this, "Hết hạn đăng nhập!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(MainActivity.this, "2 - Hết hạn đăng nhập!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}