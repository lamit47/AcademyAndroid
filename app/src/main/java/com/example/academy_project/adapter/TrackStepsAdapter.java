package com.example.academy_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.academy_project.R;
import com.example.academy_project.entities.TrackStep;

import java.util.ArrayList;

public class TrackStepsAdapter extends BaseExpandableListAdapter {
    final ArrayList<TrackStep> listTrackStep;

    public TrackStepsAdapter(ArrayList<TrackStep> listTrackStep) {
        this.listTrackStep = listTrackStep;
    }

    @Override
    public int getGroupCount() {
        return listTrackStep.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listTrackStep.get(i).getSteps().size();
    }

    @Override
    public Object getGroup(int i) {
        return listTrackStep.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listTrackStep.get(i).getSteps();
    }

    @Override
    public long getGroupId(int i) {
        return listTrackStep.get(i).getId();
    }

    @Override
    public long getChildId(int i, int i1) {
        return listTrackStep.get(i).getSteps().get(i1).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
            view = li.inflate(R.layout.listview_header, viewGroup, false);
        }

        TextView tvHeader = (TextView) view.findViewById(R.id.txtLVHeader);
        tvHeader.setText(listTrackStep.get(i).getTitle());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
            view = li.inflate(R.layout.listview_item, viewGroup, false);
        }

        TextView item = (TextView) view.findViewById(R.id.txtLVItem);
        item.setText(listTrackStep.get(i).getSteps().get(i1).getTitle());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}