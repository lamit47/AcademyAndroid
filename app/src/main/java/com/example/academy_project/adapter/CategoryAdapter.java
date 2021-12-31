package com.example.academy_project.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.academy_project.R;
import com.example.academy_project.entities.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private List<Category> listcateogy;

    public CategoryAdapter(List<Category> listcategory ) {
        this.listcateogy=listcategory;
    }


    public int getCount() {
        if (this.listcateogy == null) {
            return 0;
        }
        return this.listcateogy.size();
    }


    public Object getItem(int position) {
        return this.listcateogy.get(position);
    }

    public long getItemId(int position) {
        Category category = (Category) this.getItem(position);
        return category.getId();

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Category category = (Category) getItem(position);

        View viewCommet;
        if (convertView == null) {
            viewCommet = View.inflate(parent.getContext(), R.layout.spinner_item_layout_resource, null);
        } else {
            viewCommet = convertView;
        }
        TextView textViewItemName = (TextView) viewCommet.findViewById(R.id.textView_item_name);
        textViewItemName.setText(category.getName());

        return viewCommet;
    }

}
