package com.example.academy_project.adapter;





import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;



import com.example.academy_project.R;
import com.example.academy_project.entities.Step;
import com.example.academy_project.entities.TrackStep;

import java.util.ArrayList;
import java.util.List;

public class TrackStepsAdapter extends BaseAdapter {
    final ArrayList<TrackStep> listTrackStep;


    public TrackStepsAdapter(ArrayList<TrackStep> listTrackStep) {
        this.listTrackStep = listTrackStep;
    }

    @Override
    public int getCount() {
        return listTrackStep.size();
    }

    @Override
    public Object getItem(int position) {
        return listTrackStep.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listTrackStep.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewTrackStep;
        if (convertView == null) {
            viewTrackStep = View.inflate(parent.getContext(), R.layout.view_tracksteps, null);
        } else {
            viewTrackStep = convertView;
        }

        TrackStep trackStep = (TrackStep) getItem(position);

        TextView txtTrackTitle =  viewTrackStep.findViewById(R.id.txtTrackTitle);
//        ListView lvSteps = viewTrackStep.findViewById(R.id.lvSteps);
        LinearLayout linearLayout = (LinearLayout) viewTrackStep.findViewById(R.id.LL1);
        List<Step> steps = trackStep.getSteps();
        for (Step t: steps
             ) {

           if(t.getTrackId()==listTrackStep.get(position).getId()){
               TextView rowTextView = new TextView(viewTrackStep.getContext());
               rowTextView.setId(t.getId());
               rowTextView.setText(t.getTitle());
               linearLayout.addView(rowTextView);
           }
        }



//        List<Step> steps = trackStep.getSteps();
//        StepsAdapter stepsAdapter = new StepsAdapter(new ArrayList<Step>(steps));

        txtTrackTitle.setText(trackStep.getTitle());
//       lvSteps.setAdapter(stepsAdapter);

        return viewTrackStep;
    }
}
