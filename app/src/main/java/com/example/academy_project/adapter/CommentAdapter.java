package com.example.academy_project.adapter;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.academy_project.R;
import com.example.academy_project.entities.Comment;
import com.example.academy_project.entities.Course;
import com.example.academy_project.entities.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
    final ArrayList<Comment> listComment;

    public CommentAdapter(ArrayList<Comment> listComment) {
        this.listComment = listComment;
    }

    @Override
    public int getCount() {
        //Trả về tổng số phần tử, nó được gọi bởi ListView
        return listComment.size();
    }

    @Override
    public Object getItem(int position) {
        //Trả về dữ liệu ở vị trí position của Adapter, tương ứng là phần tử
        //có chỉ số position trong listProduct
        return listComment.get(position);
    }

    @Override
    public long getItemId(int position) {
        //Trả về một ID của phần
        return listComment.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView là View của phần tử ListView, nếu convertView != null nghĩa là
        //View này được sử dụng lại, chỉ việc cập nhật nội dung mới
        //Nếu null cần tạo mới

        View viewCommet;
        if (convertView == null) {
            viewCommet = View.inflate(parent.getContext(), R.layout.view_comment, null);
        } else {
            viewCommet = convertView;
        }

        //Bind sữ liệu phần tử vào View
        Comment c = (Comment) getItem(position);
        User u = c.getUser();
        String nameUser = u.getFirstName() +" "+ u.getLastName();
        ((TextView) viewCommet.findViewById(R.id.txtNameUser)).setText(String.format(nameUser));
        ((TextView) viewCommet.findViewById(R.id.txtContent)).setText(Html.fromHtml(String.format(c.getContent())));

        return viewCommet;
    }
}
