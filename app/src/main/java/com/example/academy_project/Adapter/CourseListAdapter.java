package com.example.academy_project.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.academy_project.R;
import com.example.academy_project.entities.Course;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CourseListAdapter extends BaseAdapter {
    //Dữ liệu liên kết bởi Adapter là một mảng các sản phẩm
    final ArrayList<Course> listCourse;

    public CourseListAdapter(ArrayList<Course> listCourse) {
        this.listCourse = listCourse;
    }

    @Override
    public int getCount() {
        //Trả về tổng số phần tử, nó được gọi bởi ListView
        return listCourse.size();
    }

    @Override
    public Object getItem(int position) {
        //Trả về dữ liệu ở vị trí position của Adapter, tương ứng là phần tử
        //có chỉ số position trong listProduct
        return listCourse.get(position);
    }

    @Override
    public long getItemId(int position) {
        //Trả về một ID của phần
        return listCourse.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView là View của phần tử ListView, nếu convertView != null nghĩa là
        //View này được sử dụng lại, chỉ việc cập nhật nội dung mới
        //Nếu null cần tạo mới

        View viewCourse;
        if (convertView == null) {
            viewCourse = View.inflate(parent.getContext(), R.layout.cousre_view, null);
        } else viewCourse = convertView;

        //Bind sữ liệu phần tử vào View
        Course c = (Course) getItem(position);
        ImageView imageView  = viewCourse.findViewById(R.id.imageCourse);
        Picasso.get().load(c.getPicturePath()).into(imageView);
        ((TextView) viewCourse.findViewById(R.id.nameCourse)).setText(String.format(c.getTitle()));


        return viewCourse;
    }

}
