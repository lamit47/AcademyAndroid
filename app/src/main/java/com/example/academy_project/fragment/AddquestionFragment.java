package com.example.academy_project.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.academy_project.R;
import com.example.academy_project.activity.LoginActivity;
import com.example.academy_project.adapter.CategoryAdapter;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.entities.Category;
import com.example.academy_project.entities.Course;
import com.example.academy_project.entities.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddquestionFragment extends Fragment {
    View view;
    Spinner spinner;
    public static int categoryid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_addquestion, container, false);
        Button btnadd = view.findViewById(R.id.addquestion);
        getCategory();
        EditText txttieude = view.findViewById(R.id.txttieude);
        EditText txtnoidung = view.findViewById(R.id.txtnoidung);
        Activity activity = new Activity();
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question question = new Question();
                question.setTitle(txttieude.getText().toString());
                question.setContent(txtnoidung.getText().toString());
                question.setCategoryId(categoryid);
                RetrofitClient.getInstance().create(ApiService.class).postQuestion(question).enqueue(new Callback<Question>() {
                    @Override
                    public void onResponse(Call<Question> call, Response<Question> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(getActivity(), "tao cau hoi thanh cong!", Toast.LENGTH_SHORT).show();
                            try {
                                Class fragmentClass = QuestionsFragment.class;
                                Fragment fragment = (Fragment) fragmentClass.newInstance();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Question> call, Throwable t) {

                    }
                });
            }
        });
        return view;
    }
    public void getCategory() {
        RetrofitClient.getInstance().create(ApiService.class).getListCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()){
                    List<Category> listCategory = response.body();
                    System.out.println(listCategory.toString());
                    CategoryAdapter adapter = new CategoryAdapter(listCategory);
                    spinner =view.findViewById(R.id.list_category);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            categoryid=(int)id;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }


            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }
}
