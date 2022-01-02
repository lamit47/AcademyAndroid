package com.example.academy_project.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.academy_project.MainActivity;
import com.example.academy_project.R;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.AuthService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.entities.Login;
import com.example.academy_project.entities.Token;
import com.example.academy_project.entities.User;
import com.example.academy_project.fragment.ChangePasswordFragment;
import com.example.academy_project.fragment.EditInfoFragment;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText editTextEmail;
    EditText editTextPassword;
    Button btnLogin;
    TextView tvToRegister;
    TextView tvToForgotPW;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Context context = getApplicationContext();
        sharedPref = context.getSharedPreferences("login", Context.MODE_PRIVATE);

        editTextEmail = findViewById(R.id.txtEmail);
        editTextPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvToRegister = findViewById(R.id.tvToRegister);
        tvToForgotPW = findViewById(R.id.tvToForgotPW);

        btnLogin.setOnClickListener((view) -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            // call api
            getToken(email, password);
        });

        tvToRegister.setOnClickListener((view) -> {
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        });

        tvToForgotPW.setOnClickListener((view) -> {
            Intent forgotIntent = new Intent(LoginActivity.this, ForgorPasswordActivity.class);
            startActivity(forgotIntent);
        });
    }

    private void getToken(String email, String password) {
        Login login = new Login(email, password);

        RetrofitClient.getInstanceWithoutToken()
                .create(AuthService.class)
                .getToken(login)
                .enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        if (response.isSuccessful()) {
                            Token token = response.body();
                            editor.putString("accessToken", token.getAccessToken());
                            editor.putLong("ATExpires", token.getAccessTokenExpires().getTime());
                            editor.putString("refreshToken", token.getRefreshToken());
                            editor.putLong("RTExpires", token.getRefreshTokenExpires().getTime());
                            editor.commit();

                            saveUserInfo();
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "2 - Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserInfo() {
        RetrofitClient.getInstance()
                .create(ApiService.class)
                .getUser()
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User user = response.body();

                            SharedPreferences shareRef = getSharedPreferences("user-info", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = shareRef.edit();
                            editor.putInt("id", user.getId());
                            editor.putString("firstName", user.getFirstName());
                            editor.putString("lastName", user.getLastName());
                            editor.putString("email", user.getEmail());
                            editor.putString("picturePath", user.getPicture());
                            editor.commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                    }
                });
    }
}
