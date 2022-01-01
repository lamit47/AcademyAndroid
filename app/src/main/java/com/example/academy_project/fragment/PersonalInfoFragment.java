package com.example.academy_project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.academy_project.R;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.entities.User;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalInfoFragment extends Fragment {
    View view;
    TextView txtFirstname;
    TextView txtLastname;
    TextView txtEmail;
    ImageView imgAvatar;
    User user;
    String imageUri = "https://www.stregasystem.com/img/users/user.png";
    Button btnEdit;
    Button btnChangePassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personalinfo, container, false);
        getUserInfo();
        return view;
    }

    public void getUserInfo() {
        RetrofitClient.getInstance()
                .create(ApiService.class)
                .getUser()
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            user = response.body();

                            txtFirstname = (TextView) view.findViewById(R.id.txtFirstName);
                            txtLastname = (TextView) view.findViewById(R.id.txtLastName);
                            txtEmail = (TextView) view.findViewById(R.id.txtEmail);
                            imgAvatar = (ImageView) view.findViewById(R.id.imgAvatar);
                            btnEdit = (Button) view.findViewById(R.id.btnEdit);
                            btnChangePassword = (Button) view.findViewById(R.id.btnChangePassword);

                            txtFirstname.setText(user.getFirstName());
                            txtLastname.setText(user.getLastName());
                            txtEmail.setText(user.getEmail());
                            if (user.getPicture() != null & !user.getPicture().equals("/")) {
                                Picasso.get().load(user.getPicture()).into(imgAvatar);
                                System.out.println(user.getPicture());
                            } else {
                                Picasso.get().load(imageUri).into(imgAvatar);
                            }

                            btnEdit.setOnClickListener(view -> {
                                    try {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("firstName", user.getFirstName());
                                        bundle.putString("lastName", user.getLastName());
                                        bundle.putString("email", user.getEmail());
                                        bundle.putString("picture", user.getPicture());

                                        Class fragmentClass = EditInfoFragment.class;
                                        Fragment fragment = (Fragment) fragmentClass.newInstance();
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();

                                        fragment.setArguments(bundle);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                             });

                            btnChangePassword.setOnClickListener(view -> {
                                try {
                                    Class fragmentClass = ChangePasswordFragment.class;
                                    Fragment fragment = (Fragment) fragmentClass.newInstance();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });

                        }

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
    }
}
