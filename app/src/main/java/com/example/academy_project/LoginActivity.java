package com.example.academy_project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.academy_project.apis.ApiService;
import com.example.academy_project.entities.Login;
import com.example.academy_project.entities.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText editTextEmail;
    EditText editTextPassword;
    Button btnLogin;
    Token tokenResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        editTextEmail = findViewById(R.id.txtEmail);
        editTextPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener((view) -> {
             String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                // call api
                sendPosts(email, password);
                Toast.makeText(LoginActivity.this, tokenResult.toString(), Toast.LENGTH_SHORT).show();

        });
    }

    private void sendPosts(String email, String password) {
        Login login = new Login(email, password);
        ApiService.apiService.sendPosts(login).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                tokenResult = response.body();
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });
    }
}
