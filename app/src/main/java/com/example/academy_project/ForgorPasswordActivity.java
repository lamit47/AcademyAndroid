package com.example.academy_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.academy_project.apis.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgorPasswordActivity extends AppCompatActivity {
    EditText txtEmail;
    Button btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forgotpassword);

        txtEmail = findViewById(R.id.txtEmail);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener((view) -> {
            String email = txtEmail.getText().toString();
            if (!RegisterActivity.emailValidate(email)) {
                Toast.makeText(ForgorPasswordActivity.this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }
            requestNewPassword(email);
        });
    }

    private void requestNewPassword(String email) {
        AuthService.authService.forgotPassword(email).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Boolean bool = response.body();
                if (bool) {
                    Toast.makeText(ForgorPasswordActivity.this, "Mật khẩu mới đã được gửi về email của bạn!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgorPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ForgorPasswordActivity.this, "Email không tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }
}
