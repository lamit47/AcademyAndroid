package com.example.academy_project.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.example.academy_project.entities.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditQuestionFragment extends Fragment {
    View view;
    public static int categoryId = 0;
    Question question;
    EditText txtTitle;
    EditText txtContent;
    Button btnSubmit;
    Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_addquestion, container, false);

        int questionId = this.getArguments().getInt("questionId");
        if (questionId == 0) {
            return view;
        }

        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setText("Sửa");
        spinner = view.findViewById(R.id.spCategory);
        txtTitle = view.findViewById(R.id.txtTitle);
        txtContent = view.findViewById(R.id.txtContent);
        getQuestion(questionId);
        getCategory();

        btnSubmit.setOnClickListener(v -> {
            question.setTitle(txtTitle.getText().toString());
            question.setContent(txtContent.getText().toString());
            question.setCategoryId(categoryId);

            putQuestion(question);
        });
        return view;
    }

    private void putQuestion(Question q) {
        String id = String.valueOf(q.getId());
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<Question> call = apiService.putQuestion(id, q);
        call.enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Sửa câu hỏi thành công!", Toast.LENGTH_SHORT).show();
                    try {
                        Bundle bundle = new Bundle();
                        bundle.putInt("questionId", question.getId());
                        QuestionFragment questionFragment = new QuestionFragment();
                        questionFragment.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, questionFragment).addToBackStack(null).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Sửa câu hỏi thất bại, vui lòng kiểm tra lại nội dung!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                Toast.makeText(getActivity(), "Kết nối đến máy chủ thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCategory() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<Category>> call = apiService.getListCategory();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> listCategory = response.body();
                    CategoryAdapter adapter = new CategoryAdapter(listCategory);

                    spinner.setAdapter(adapter);
                    if (listCategory.size() > 0) {
                        int pos = getSpinnerIndex(spinner, question.getCategoryId());
                        categoryId = question.getCategoryId();
                        spinner.setSelection(pos);
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

    private int getSpinnerIndex(Spinner spinner, int mySetId)
    {
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemIdAtPosition(i)==mySetId){
                index = i;
                i = spinner.getCount();
            }
        }
        return index;
    }

    private void getQuestion(int questionId) {
        RetrofitClient.getInstance()
                .create(ApiService.class)
                .getQuestion(String.valueOf(questionId))
                .enqueue(new Callback<Question>() {
                    @Override
                    public void onResponse(Call<Question> call, Response<Question> response) {
                        if (response.isSuccessful()) {
                            question = response.body();
                            txtTitle.setText(question.getTitle());
                            txtContent.setText(question.getContent());

                        }
                    }

                    @Override
                    public void onFailure(Call<Question> call, Throwable t) {
                        Toast.makeText(getActivity(), "Kết nối đến máy chủ thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
