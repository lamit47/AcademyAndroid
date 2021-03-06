package com.example.academy_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.academy_project.R;
import com.example.academy_project.apis.AuthService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.entities.Register;
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
        setContentView(R.layout.activity_register);

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
                    Toast.makeText(RegisterActivity.this, "H??? kh??ng ???????c ????? tr???ng!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (lastName == null || lastName.length() < 1) {
                    Toast.makeText(RegisterActivity.this, "T??n kh??ng ???????c ????? tr???ng!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!emailValidate(email)) {
                    Toast.makeText(RegisterActivity.this, "Email kh??ng h???p l???!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password == null || password.length() < 8) {
                    Toast.makeText(RegisterActivity.this, "M???t kh???u c???n l???n h??n 8 k?? t???!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!password.equals(confirm)) {
                    Toast.makeText(RegisterActivity.this, "Hai m???t kh???u kh??ng kh???p!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Register register = new Register(firstName, lastName, email, password, confirm);
                registerAccount(register);
            }
        });
    }

    private void registerAccount(Register register) {
        RetrofitClient.getInstanceWithoutToken()
                .create(AuthService.class)
                .register(register)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "????ng k?? t??i kho???n th??nh c??ng!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Email t??i kho???n ???? t???n t???i!", Toast.LENGTH_SHORT).show();
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
