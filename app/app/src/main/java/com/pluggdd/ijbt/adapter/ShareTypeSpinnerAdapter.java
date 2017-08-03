package com.pluggdd.ijbt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pluggdd.ijbt.R;

import java.util.List;


public class ShareTypeSpinnerAdapter extends BaseAdapter {

    Context context;
    List<String> spinnerItems;

    public ShareTypeSpinnerAdapter(Context context,
                                   List<String> spinnerItems) {
        this.context = context;
        this.spinnerItems = spinnerItems;
    }

    @Override
    public int getCount() {
        return spinnerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return spinnerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(context).inflate(R.layout.spinner_item_root, parent, false);
        TextView textView = (TextView) row.findViewById(R.id.textViewCategoryName);
        textView.setText(spinnerItems.get(position));
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View row = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        TextView textView = (TextView) row.findViewById(R.id.textViewCategoryName);
        if ((position + 2) % 2 == 0) {
            row.setBackgroundColor(context.getResources().getColor(R.color.app_black_50_percent));
            textView.setTextColor(context.getResources().getColor(R.color.app_white));
        } else {
            row.setBackgroundColor(context.getResources().getColor(R.color.app_black_50_percent));
            textView.setTextColor(context.getResources().getColor(R.color.app_white));
        }
        textView.setText(spinnerItems.get(position));
        return row;
    }
}