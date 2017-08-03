package com.pluggdd.ijbt.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.pluggdd.ijbt.R;

public class ViewFavouritesDialog extends Dialog {

    private Button btn_yes;
    private Button btn_no;
    Context context;

    OnViewFavourites onViewFavourites;
    private String code;

    public ViewFavouritesDialog(Activity activity, OnViewFavourites onViewFavourites) {
        super(activity, R.style.DialogStyle);
        this.context = activity;
        this.onViewFavourites = onViewFavourites;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.prompt_favourites_dialog);
        findViews();
    }

    private void findViews() {
        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_no = (Button) findViewById(R.id.btn_no);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewFavourites.onShouldShowFavourites(true);
                dismiss();
            }

        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewFavourites.onShouldShowFavourites(false);
                dismiss();
            }
        });
    }
}