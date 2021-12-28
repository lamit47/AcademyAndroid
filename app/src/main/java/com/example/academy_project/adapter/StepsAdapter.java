package com.example.academy_project.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.academy_project.R;
import com.example.academy_project.entities.Step;
import com.example.academy_project.entities.TrackStep;

import java.util.ArrayList;

public class StepsAdapter extends BaseAdapter {
    final ArrayList<Step> steps;

    public StepsAdapter(ArrayList<Step> steps) {
        this.steps = steps;
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Override
    public Object getItem(int position) {
        return steps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return steps.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewTrackStep;
        if (convertView == null) {
            viewTrackStep = View.inflate(parent.getContext(), R.layout.view_steps, null);
        } else {
            viewTrackStep = convertView;
        }

        Step step = (Step) getItem(position);

        TextView txtStepTitle =  viewTrackStep.findViewById(R.id.txtStepTitle);
        txtStepTitle.setText(step.getTitle());

        return viewTrackStep;
    }
}
