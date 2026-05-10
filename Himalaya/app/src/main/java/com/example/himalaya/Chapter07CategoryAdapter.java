package com.example.himalaya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Chapter07CategoryAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private String[] categories = new String[0];
    private int selectedPosition = 0;

    public Chapter07CategoryAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void submitCategories(String[] categories) {
        this.categories = categories;
        selectedPosition = 0;
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public String getSelectedCategory() {
        if (categories.length == 0) {
            return "";
        }
        return categories[selectedPosition];
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public Object getItem(int position) {
        return categories[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_chapter07_category, parent, false);
        }
        TextView nameView = view.findViewById(R.id.tv_category_name);
        nameView.setText(categories[position]);
        nameView.setSelected(position == selectedPosition);
        view.setActivated(position == selectedPosition);
        return view;
    }
}
