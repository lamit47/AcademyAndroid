package com.example.academy_project.adapter;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.academy_project.R;
import com.example.academy_project.entities.Course;
import com.example.academy_project.entities.Question;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class QuestionAdapter extends BaseAdapter {

    final ArrayList<Question> listQuestion;

    public QuestionAdapter(ArrayList<Question> listQuestion) {
        this.listQuestion = listQuestion;
    }

    @Override
    public int getCount() {
        //Trả về tổng số phần tử, nó được gọi bởi ListView
        return listQuestion.size();
    }

    @Override
    public Object getItem(int position) {
        //Trả về dữ liệu ở vị trí position của Adapter, tương ứng là phần tử
        //có chỉ số position trong listProduct
        return listQuestion.get(position);
    }

    @Override
    public long getItemId(int position) {
        //Trả về một ID của phần
        return listQuestion.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView là View của phần tử ListView, nếu convertView != null nghĩa là
        //View này được sử dụng lại, chỉ việc cập nhật nội dung mới
        //Nếu null cần tạo mới

        View viewQuestion;
        if (convertView == null) {
            viewQuestion = View.inflate(parent.getContext(), R.layout.view_question, null);
        } else {
            viewQuestion = convertView;
        }

        //Bind sữ liệu phần tử vào View
        Question q = (Question) getItem(position);
        ((TextView) viewQuestion.findViewById(R.id.txtTitle)).setText(String.format(q.getTitle()));

        ((TextView) viewQuestion.findViewById(R.id.txtContent)).setText(Html.fromHtml(String.format(q.getContent())));

        return viewQuestion;
    }
}
