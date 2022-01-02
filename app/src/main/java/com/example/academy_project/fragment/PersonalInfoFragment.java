package com.example.academy_project.fragment;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.academy_project.R;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.entities.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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

    private static final int INTENT_REQUEST_CODE = 100;
    Button btnSelect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personalinfo, container, false);
        if (checkInternet()) {
            getUserInfo();
        } else {
            btnSelect = view.findViewById(R.id.btnSelect);
            btnChangePassword = view.findViewById(R.id.btnChangePassword);
            btnEdit = view.findViewById(R.id.btnEdit);
            btnSelect.setVisibility(View.INVISIBLE);
            btnChangePassword.setVisibility(View.INVISIBLE);
            btnEdit.setVisibility(View.INVISIBLE);
            getUserInfoOffline();
        }
        handleUploadImage();
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

                            SharedPreferences shareRef = getContext().getSharedPreferences(
                                    "user-info", getContext().MODE_PRIVATE);
                            SharedPreferences.Editor info = shareRef.edit();
                            info.putInt("id", user.getId());
                            info.putString("firstName", user.getFirstName());
                            info.putString("lastName", user.getLastName());
                            info.putString("email", user.getEmail());
                            info.putString("picturePath", user.getPicture());
                            info.apply();

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
                                    //bundle.putString("picture", user.getPicture());

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

    public boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }

    public void getUserInfoOffline() {
        txtFirstname = (TextView) view.findViewById(R.id.txtFirstName);
        txtLastname = (TextView) view.findViewById(R.id.txtLastName);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        imgAvatar = (ImageView) view.findViewById(R.id.imgAvatar);
        btnEdit = (Button) view.findViewById(R.id.btnEdit);
        btnChangePassword = (Button) view.findViewById(R.id.btnChangePassword);

        SharedPreferences shareRef = getContext().getSharedPreferences(
                "user-info", getContext().MODE_PRIVATE);
        String firstName = shareRef.getString("firstName", "");
        String lastName = shareRef.getString("lastName", "");
        String email = shareRef.getString("email", "");
        String picture = shareRef.getString("picturePath", "");

        txtFirstname.setText(firstName);
        txtLastname.setText(lastName);
        txtEmail.setText(email);

        if (picture != null & !picture.equals("/")) {
            Picasso.get().load(picture).into(imgAvatar);
        } else {
            Picasso.get().load(imageUri).into(imgAvatar);
        }

        btnEdit.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Mất kết nối!", Toast.LENGTH_SHORT).show();
        });

        btnChangePassword.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Mất kết nối!", Toast.LENGTH_SHORT).show();
        });
    }

    public void handleUploadImage() {
        btnSelect = (Button) view.findViewById(R.id.btnSelect);

        btnSelect.setOnClickListener(view -> {
            if (checkInternet()) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");

                try {
                    startActivityForResult(intent, INTENT_REQUEST_CODE);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "Mất kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_REQUEST_CODE && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    InputStream is = getActivity().getContentResolver().openInputStream(data.getData());
                    uploadImage(getBytes(is));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
        int buffSize = 1024;
        byte[] buff = new byte[buffSize];
        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }
        return byteBuff.toByteArray();
    }

    private void uploadImage(byte[] imageBytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", "image.jpg", requestFile);

        RetrofitClient.getInstance()
                .create(ApiService.class)
                .uploadImage(body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(getContext(), "Lưu ảnh đại diện thành công!", Toast.LENGTH_SHORT).show();
                        try {
                            Class fragmentClass = PersonalInfoFragment.class;
                            Fragment fragment = (Fragment) fragmentClass.newInstance();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                    }
                });
    }
}
