package com.example.academy_project.apis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.academy_project.LoginActivity;
import com.example.academy_project.entities.Token;

import java.io.IOException;
import java.util.Date;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class TokenInterceptor implements Interceptor {
    Context ctx;
    SharedPreferences mPrefs;

    public TokenInterceptor(Context ctx) {
        this.ctx = ctx;
        this.mPrefs= PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request newRequest = chain.request();

        mPrefs = ctx.getSharedPreferences("login", Context.MODE_PRIVATE);

        long RTExpiresStr = mPrefs.getLong("RTExpires", 0);
        Date RTExpires = new Date(RTExpiresStr);
        Date now = new Date();
        String accessToken = mPrefs.getString("accessToken", null);
        String refreshToken = mPrefs.getString("refreshToken", null);

        //Nếu refresh token hết hạn chuyển về trang đăng nhập
        if (refreshToken == null && now.after(RTExpires)) {
            System.out.println("Refresh token expires");
            Intent intent = new Intent(ctx, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }

        long ATExpiresStr = mPrefs.getLong("ATExpires", 0);
        Date ATExpires = new Date(ATExpiresStr);
        // Nếu access token hết hạn làm mới AC bằng refresh token
        if (refreshToken != null && now.after(ATExpires)) {
            System.out.println("Access token refresh");
            refeshToken(refreshToken);
        }

        newRequest  = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        return chain.proceed(newRequest);
    }

    private void refeshToken(String refreshToken) {
        Token token = new Token();
        token.setRefreshToken(refreshToken);

        AuthService.authService.refreshToken(token).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, retrofit2.Response<Token> response) {
                SharedPreferences.Editor editor = mPrefs.edit();

                if (response.isSuccessful()) {
                    Token token = response.body();

                    editor.putString("accessToken", token.getAccessToken());
                    editor.putLong("ATExpires", token.getAccessTokenExpires().getTime());
                    editor.commit();
                } else {
                    Intent intent = new Intent(ctx, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Intent intent = new Intent(ctx, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });
    }
}