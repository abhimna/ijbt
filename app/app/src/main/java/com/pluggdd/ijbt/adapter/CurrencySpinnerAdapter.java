package com.pluggdd.ijbt.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.model.Currency;

import java.util.List;

/*
*  Adapter class for the list of Currency list
* */
public class CurrencySpinnerAdapter extends BaseAdapter {

    Context context;
    List<Currency> spinnerItems;

    public CurrencySpinnerAdapter(Context context,
                                  List<Currency> spinnerItems) {
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
        Currency currency = spinnerItems.get(position);
        textView.setText(currency.getIsoCode()  + " " + Html.fromHtml(currency.getSymbol()));
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
        Currency currency = spinnerItems.get(position);
        textView.setText(currency.getIsoCode()  + " " + Html.fromHtml(currency.getSymbol()));
        return row;
    }
}