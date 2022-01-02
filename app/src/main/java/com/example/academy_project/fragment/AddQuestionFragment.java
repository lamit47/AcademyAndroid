package com.example.academy_project.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.academy_project.R;
import com.example.academy_project.adapter.CategoryAdapter;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.entities.Category;
import com.example.academy_project.entities.CourseStep;
import com.example.academy_project.entities.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddQuestionFragment extends Fragment {
    View view;
    public static int categoryId = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_addquestion, container, false);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);

        getCategory();
        EditText txtTitle = view.findViewById(R.id.txtTitle);
        EditText txtContent = view.findViewById(R.id.txtContent);

        btnSubmit.setOnClickListener(v -> {
            Question question = new Question();
            String title = txtTitle.getText().toString();
            String content = txtContent.getText().toString();
            question.setTitle(title);
            question.setContent(content);
            question.setCategoryId(categoryId);

            ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
            Call<Question> call = apiService.postQuestion(question);
            call.enqueue(new Callback<Question>() {
                @Override
                public void onResponse(Call<Question> call, Response<Question> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), "Đăng câu hỏi thành công!", Toast.LENGTH_SHORT).show();
                        Question q = response.body();
                        try {
                            Bundle bundle = new Bundle();
                            bundle.putInt("questionId", q.getId());
                            QuestionFragment questionFragment = new QuestionFragment();
                            questionFragment.setArguments(bundle);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.flContent, questionFragment).addToBackStack(null).commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Đăng câu hỏi thất bại, vui lòng kiểm tra lại nội dung!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Question> call, Throwable t) {
                    Toast.makeText(getActivity(), "Kết nối đến máy chủ thất bại!", Toast.LENGTH_SHORT).show();
                }
            });
        });
        return view;
    }

    public void getCategory() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<Category>> call = apiService.getListCategory();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> listCategory = response.body();
                    CategoryAdapter adapter = new CategoryAdapter(listCategory);
                    Spinner spinner = view.findViewById(R.id.spCategory);
                    spinner.setAdapter(adapter);
                    if (listCategory.size() > 0) {
                        categoryId = listCategory.get(0).getId();
                    }

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            categoryId = (int) id;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(getActivity(), "Kết nối đến máy chủ thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
