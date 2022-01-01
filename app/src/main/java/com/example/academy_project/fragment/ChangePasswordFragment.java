package com.example.academy_project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.academy_project.R;

public class ChangePasswordFragment extends Fragment {
    View view;

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_changepassword, container, false);
        changePassword();
        return view;
    }

    public void changePassword() {

    }
}
