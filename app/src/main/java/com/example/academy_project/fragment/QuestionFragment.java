package com.example.academy_project.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.RoundedCorner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.academy_project.R;
import com.example.academy_project.adapter.CommentsAdapter;
import com.example.academy_project.adapter.CoursesAdapter;
import com.example.academy_project.adapter.QuestionAdapter;
import com.example.academy_project.adapter.TrackStepsAdapter;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.entities.Comment;
import com.example.academy_project.entities.Course;
import com.example.academy_project.entities.Question;
import com.example.academy_project.entities.TrackStep;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionFragment extends Fragment {
    static int Id = 0;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_question, container, false);
        if (Id != 0) {
            getQuestion(Id);
            getComments(Id);
        }
        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        EditText editText =  view.findViewById(R.id.txtComment);
        btnSubmit.setOnClickListener(view ->{
            Comment comment = new Comment();
            comment.setQuestionId(Id);
            comment.setContent(editText.getText().toString());
            RetrofitClient.getInstance().create(ApiService.class).postComent(comment).enqueue(new Callback<Comment>() {
                @Override
                public void onResponse(Call<Comment> call, Response<Comment> response) {
                    if(response.isSuccessful()){
                        getComments(Id);
                    }else
                    {
                        System.out.println(comment.getContent());
                    }
                }

                @Override
                public void onFailure(Call<Comment> call, Throwable t) {

                }
            });
        });

        return view;
    }

    public void getQuestion(int Id) {
        RetrofitClient.getInstance().create(ApiService.class).getQuestion(String.valueOf(Id)).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.isSuccessful()) {
                        Question q = response.body();
                    TextView txtTitle1 =  view.findViewById(R.id.txtTitle);
                    TextView txtContent1 = view.findViewById(R.id.txtContent);
                    txtTitle1.setText(q.getTitle());
                    txtContent1.setText(Html.fromHtml(q.getContent()));
                        }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }
    public void getComments(int Id){
        RetrofitClient.getInstance().create(ApiService.class).getComments(String.valueOf(Id)).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                List<Comment> listComment = response.body();
                CommentsAdapter commentsAdapter = new CommentsAdapter(new ArrayList<Comment>(listComment));

                ListView listComments = view.findViewById(R.id.lvComment);
                listComments.setAdapter(commentsAdapter);
                registerForContextMenu(listComments);

            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });
    }

    public static void setId(int id) {
        Id = id;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.drawable_comment,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.optEdit:

                return true;
            case R.id.optDeleted:
               
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
