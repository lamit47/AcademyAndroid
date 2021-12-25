package com.example.academy_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.academy_project.apis.ApiService;
import com.example.academy_project.entities.Register;
import com.example.academy_project.entities.Token;
import com.example.academy_project.entities.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtEmail;
    EditText txtPassword;
    EditText txtConfirm;
    Button btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirm = findViewById(R.id.txtPasswordConfirm);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = txtFirstName.getText().toString();
                String lastName = txtLastName.getText().toString();
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                String confirm = txtConfirm.getText().toString();

                if (firstName == null || firstName.length() < 1) {
                    Toast.makeText(RegisterActivity.this, "Họ không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (lastName == null || lastName.length() < 1) {
                    Toast.makeText(RegisterActivity.this, "Tên không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!emailValidate(email)) {
                    Toast.makeText(RegisterActivity.this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password == null || password.length() < 8) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu cần lớn hơn 8 ký tự!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!password.equals(confirm)) {
                    Toast.makeText(RegisterActivity.this, "Hai mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Register register = new Register(firstName, lastName, email, password, confirm);
                registerAccount(register);
            }
        });
    }

    private void registerAccount(Register register) {
        ApiService.apiService.register(register).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Email tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public static boolean emailValidate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
