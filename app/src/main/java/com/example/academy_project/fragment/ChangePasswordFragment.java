package com.example.academy_project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.academy_project.R;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.entities.ChangePassword;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {
    View view;
    EditText editTextOld;
    EditText editTextNew;
    EditText editTextConfirm;
    Button btnSave;

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_changepassword, container, false);
        changePassword();
        return view;
    }

    public void changePassword() {
        editTextOld = view.findViewById(R.id.editTextOld);
        editTextNew = view.findViewById(R.id.editTextNew);
        editTextConfirm = view.findViewById(R.id.editTextConfirm);
        btnSave = view.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(view -> {
            String oldPassword = editTextOld.getText().toString();
            String newPassword = editTextNew.getText().toString();
            String confirmPassword = editTextConfirm.getText().toString();

            //validation
            if (oldPassword == null || oldPassword.length() < 1 && newPassword == null || newPassword.length() < 1 && confirmPassword == null || confirmPassword.length() < 1) {
                Toast.makeText(getContext(), "Không được để trống!", Toast.LENGTH_SHORT).show();
                return;
            } else if (newPassword.length() < 8 && !isValidPassword(newPassword)){
                Toast.makeText(getContext(), "Mật khẩu không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();
                return;
            }
            ChangePassword changePassword = new ChangePassword(oldPassword, newPassword);
            postNewPassword(changePassword);
        });
    }

    private void postNewPassword(ChangePassword changePassword) {
        RetrofitClient.getInstance()
                .create(ApiService.class)
                .postNewPassword(changePassword)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Thay đổi thành công!", Toast.LENGTH_SHORT).show();
                            try {
                                Class fragmentClass = PersonalInfoFragment.class;
                                Fragment fragment = (Fragment) fragmentClass.newInstance();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getContext(), "Sai mật khẩu cũ!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ChangePassword> call, Response<ResponseBody> response) {
//                        if (response.isSuccessful()) {
//                            Toast.makeText(getContext(), "Thay đổi thành công!", Toast.LENGTH_SHORT).show();
//                            try {
//                                Class fragmentClass = PersonalInfoFragment.class;
//                                Fragment fragment = (Fragment) fragmentClass.newInstance();
//                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            Toast.makeText(getContext(), "Sai mật khẩu cũ!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ChangePassword> call, Throwable t) {
//
//                    }
//                });
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
