package com.example.academy_project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.academy_project.R;
import com.example.academy_project.activity.RegisterActivity;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.entities.EditInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditInfoFragment extends Fragment {
    View view;
    EditText editTextFirstName;
    EditText editTextLastName;
    //EditText editTextEmail;
    Button btnSave;
//    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
//            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_editinfo, container, false);
        getUserInfo();
        return view;
    }

    public void getUserInfo() {
        editTextFirstName = view.findViewById(R.id.editTextFirstName);
        editTextLastName = view.findViewById(R.id.editTextLastName);
        //editTextEmail = view.findViewById(R.id.editTextEmail);
        btnSave = view.findViewById(R.id.btnSave);

        editTextFirstName.setText(getArguments().getString("firstName"));
        editTextLastName.setText(getArguments().getString("lastName"));
        //editTextEmail.setText(getArguments().getString("email"));

        btnSave.setOnClickListener(view -> {
            String firstName = editTextFirstName.getText().toString();
            String lastName = editTextLastName.getText().toString();
            //String email = editTextEmail.getText().toString();

            if (firstName == null || firstName.length() < 1) {
                Toast.makeText(getContext(), "Họ không được để trống!", Toast.LENGTH_SHORT).show();
                return;
            } else if (lastName == null || lastName.length() < 1) {
                Toast.makeText(getContext(), "Tên không được để trống!", Toast.LENGTH_SHORT).show();
                return;
            }
//            else if (!emailValidate(email)) {
//                Toast.makeText(getContext(), "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
//                return;
//            }

            EditInfo editInfo = new EditInfo(getArguments().getString("email"), firstName, lastName);
            editUserInfo(editInfo);
        });
    }

    private void editUserInfo(EditInfo editInfo) {
        RetrofitClient.getInstance()
                .create(ApiService.class)
                .putInfo(editInfo)
                .enqueue(new Callback<EditInfo>() {
                    @Override
                    public void onResponse(Call<EditInfo> call, Response<EditInfo> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Lưu thành công!", Toast.LENGTH_SHORT).show();
                            try {
                            Class fragmentClass = PersonalInfoFragment.class;
                            Fragment fragment = (Fragment) fragmentClass.newInstance();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getContext(), "Err!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EditInfo> call, Throwable t) {

                    }
                });
    }

//    public static boolean emailValidate(String emailStr) {
//        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
//        return matcher.find();
//    }
}
