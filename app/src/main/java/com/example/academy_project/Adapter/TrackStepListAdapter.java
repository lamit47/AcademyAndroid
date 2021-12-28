package com.example.academy_project.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.academy_project.R;
import com.example.academy_project.entities.Course;
import com.example.academy_project.entities.Step;
import com.example.academy_project.entities.TrackStep;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrackStepListAdapter extends BaseAdapter {
    //Dữ liệu liên kết bởi Adapter là một mảng các sản phẩm
    final ArrayList<TrackStep> listTrackStep;

    public TrackStepListAdapter(ArrayList<TrackStep> listTrackStep) {
        this.listTrackStep = listTrackStep;
    }

    @Override
    public int getCount() {
        //Trả về tổng số phần tử, nó được gọi bởi ListView
        return listTrackStep.size();
    }

    @Override
    public Object getItem(int position) {
        //Trả về dữ liệu ở vị trí position của Adapter, tương ứng là phần tử
        //có chỉ số position trong listProduct
        return listTrackStep.get(position);
    }

    @Override
    public long getItemId(int position) {
        //Trả về một ID của phần
        return listTrackStep.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView là View của phần tử ListView, nếu convertView != null nghĩa là
        //View này được sử dụng lại, chỉ việc cập nhật nội dung mới
        //Nếu null cần tạo mới

        View viewTrackStep;
        if (convertView == null) {
            viewTrackStep = View.inflate(parent.getContext(), R.layout.list_step, null);
        } else viewTrackStep = convertView;

        //Bind sữ liệu phần tử vào View
        TrackStep trackStep = (TrackStep) getItem(position);
        TextView txtTitleStep =  viewTrackStep.findViewById(R.id.txtTitleTrack);
        txtTitleStep.setText(String.format(trackStep.getTitle()));
        ListView listview =  viewTrackStep.findViewById(R.id.listViewStep);
//        ArrayList<Step> listStep = new ArrayList<Step>(trackStep.getSteps());



        return viewTrackStep;
    }
}
