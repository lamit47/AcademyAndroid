package com.example.academy_project.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.academy_project.R;
import com.example.academy_project.adapter.QuestionAdapter;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.entities.Question;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsFragment extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_questions, container, false);

        Button btnAddQuestion = view.findViewById(R.id.btnAddQuestion);
        ListView listView = view.findViewById(R.id.lvQuestion);
        if (!isNetworkAvailable()) {
            listView.setVisibility(View.GONE);
            btnAddQuestion.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Bạn đang offline!", Toast.LENGTH_SHORT).show();
            return view;
        }

        getListQuestion();
        btnAddQuestion.setOnClickListener((view) -> {
            try {
                Class fragmentClass = AddQuestionFragment.class;
                Fragment fragment = (Fragment) fragmentClass.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return view;

    }

    public void getListQuestion() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<Question>> call = apiService.getListQuestions();

        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful()) {
                    List<Question> listQuestion = response.body();
                    QuestionAdapter questionAdapter = new QuestionAdapter(new ArrayList<Question>(listQuestion));

                    ListView listView = view.findViewById(R.id.lvQuestion);
                    listView.setAdapter(questionAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                Bundle bundle = new Bundle();
                                bundle.putInt("questionId", (int) id);
                                QuestionFragment questionFragment = new QuestionFragment();
                                questionFragment.setArguments(bundle);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.flContent, questionFragment).addToBackStack(null).commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Toast.makeText(getActivity(), "Kết nối đến máy chủ thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
