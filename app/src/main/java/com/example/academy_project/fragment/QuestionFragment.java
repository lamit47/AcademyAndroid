package com.example.academy_project.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.academy_project.R;
import com.example.academy_project.adapter.CommentAdapter;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.entities.Comment;
import com.example.academy_project.entities.Question;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionFragment extends Fragment {
    private View view;
    private int questionId = 0;
    private CommentAdapter commentAdapter;
    private EditText txtContent;
    private Button btnSubmit;
    private Button btnCannel;
    private Button btnEdit;
    private Comment editComment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_question, container, false);

        questionId = this.getArguments().getInt("questionId");
        if (questionId == 0) {
            return view;
        }

        getQuestion();
        getComments();

        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnCannel = view.findViewById(R.id.btnCannel);
        btnEdit = view.findViewById(R.id.btnEdit);
        txtContent = view.findViewById(R.id.txtComment);

        btnSubmit.setOnClickListener(v -> {
            Comment comment = new Comment();
            comment.setQuestionId(questionId);
            comment.setContent(txtContent.getText().toString());

            postComment(comment);
        });
        btnEdit.setOnClickListener(v -> {
            editComment.setContent(txtContent.getText().toString());
            putComment(editComment);
        });
        btnCannel.setOnClickListener(v -> {
            txtContent.setText("");
            btnSubmit.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
            btnCannel.setVisibility(View.GONE);
        });

        return view;
    }

    private void postComment(Comment comment) {
        RetrofitClient.getInstance()
                .create(ApiService.class)
                .postComment(comment)
                .enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        if (response.isSuccessful()) {
                            getComments();
                            txtContent.setText("");
                        } else {
                            Toast.makeText(getActivity(), "Đăng bình luận thất bại, vui lòng kiểm tra lại nội dung!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
                        Toast.makeText(getActivity(), "Kết nối đến máy chủ thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void putComment(Comment comment) {
        RetrofitClient.getInstance()
                .create(ApiService.class)
                .putComment(String.valueOf(comment.getId()), comment)
                .enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        if (response.isSuccessful()) {
                            getComments();
                            txtContent.setText("");
                            btnSubmit.setVisibility(View.VISIBLE);
                            btnEdit.setVisibility(View.GONE);
                            btnCannel.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Sửa bình luận thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Sửa bình luận thất bại, vui lòng kiểm tra lại nội dung!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
                        Toast.makeText(getActivity(), "Kết nối đến máy chủ thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteComment(int commentId) {
        RetrofitClient.getInstance()
                .create(ApiService.class)
                .deleteComment(String.valueOf(commentId))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        getComments();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext().getApplicationContext(), "Bạn không thể xóa comment này!!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getQuestion() {
        RetrofitClient.getInstance()
                .create(ApiService.class)
                .getQuestion(String.valueOf(questionId))
                .enqueue(new Callback<Question>() {
                    @Override
                    public void onResponse(Call<Question> call, Response<Question> response) {
                        if (response.isSuccessful()) {
                            Question q = response.body();
                            TextView txtTitle = view.findViewById(R.id.txtTitle);
                            TextView txtContent = view.findViewById(R.id.txtContent);
                            txtTitle.setText(q.getTitle());
                            txtContent.setText(Html.fromHtml(q.getContent()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Question> call, Throwable t) {
                        Toast.makeText(getActivity(), "Kết nối đến máy chủ thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getComments() {
        RetrofitClient.getInstance()
                .create(ApiService.class)
                .getComments(String.valueOf(questionId))
                .enqueue(new Callback<List<Comment>>() {
                    @Override
                    public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                        List<Comment> listComment = response.body();
                        commentAdapter = new CommentAdapter(new ArrayList<Comment>(listComment));

                        ListView listComments = view.findViewById(R.id.lvComment);
                        listComments.setAdapter(commentAdapter);
                        registerForContextMenu(listComments);
                    }

                    @Override
                    public void onFailure(Call<List<Comment>> call, Throwable t) {
                        Toast.makeText(getActivity(), "Kết nối đến máy chủ thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.drawable_comment, menu);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Comment comment = (Comment) commentAdapter.getItem(info.position);

        if (getLoginUserId() != comment.getUserId()) {
            Toast.makeText(getContext(), "Bạn không có quyền để thao tác với bình luận này!", Toast.LENGTH_SHORT).show();
        }

        switch (item.getItemId()) {
            case R.id.optEdit:
                editComment = comment;
                txtContent.setText(comment.getContent());
                btnSubmit.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
                btnCannel.setVisibility(View.VISIBLE);
                return true;
            case R.id.optDelete:
                deleteComment((int) info.id);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private int getLoginUserId() {
        SharedPreferences shareRef = getContext().getSharedPreferences("user-info", getContext().MODE_PRIVATE);
        int id = shareRef.getInt("id", 0);
        return id;
    }
}
