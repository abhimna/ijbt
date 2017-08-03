package com.pluggdd.ijbt.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.network.APIResponse;
import com.pluggdd.ijbt.network.IJBTResponseController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class InfluencerCodePromptDialog extends Dialog implements IJBTResponseController {

    private EditText editTextCode;
    private Button btn_submit;
    private Button btn_continue;
    private View rootView;
    Context context;

    OnCodeEntered onCodeEntered;
    private CommonMethods commonMethods;
    private String code;

    public InfluencerCodePromptDialog(Activity activity, OnCodeEntered onCodeEntered) {
        super(activity, R.style.DialogStyle);
        this.context = activity;
        this.onCodeEntered = onCodeEntered;
        commonMethods = new CommonMethods(activity, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.prompt_influencer_dialog);
        findViews();
    }

    private void findViews() {
        rootView = findViewById(android.R.id.content).getRootView();
        editTextCode = (EditText) findViewById(R.id.editTextCode);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = editTextCode.getText().toString();
                if (code != null && !code.equalsIgnoreCase("")) {
                    try {
                        commonMethods.validateInfluencerCode(commonMethods.getInfluencerParams(code));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    onCodeEntered.onNormalUserLoggedIn();
                }
            }

        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCodeEntered.onNormalUserLoggedIn();
            }
        });
    }

    @Override
    public void OnComplete(APIResponse apiResponse) {
                System.out.println(
                        (new StringBuilder("Resonse of facebook sig up")).append(apiResponse.getResponse()).toString());
                if (apiResponse.getCode() == 200) {
                    try {
                        JSONObject res = new JSONObject(apiResponse.getResponse());
                        String strMsg = res.getString("msg");
                        String strStatus = res.getString("status");
                        if (strStatus.equalsIgnoreCase("1")) {
                            onCodeEntered.onInfluencerCodeValidated(code);
                        } else {
                            onCodeEntered.onInfluencerCodeValidated(null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    onCodeEntered.onInfluencerCodeValidated(null);
                }
    }
}