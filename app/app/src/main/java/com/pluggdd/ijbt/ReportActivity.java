package com.pluggdd.ijbt;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pluggdd.ijbt.network.APIResponse;
import com.pluggdd.ijbt.network.IJBTResponseController;
import com.pluggdd.ijbt.util.CommonMethods;
import com.pluggdd.ijbt.util.GlobalConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ReportActivity extends Activity implements OnClickListener,
        IJBTResponseController {

    private Activity activity;
    private Intent intent;
    private String userid;
    private String postid;
    private TextView txt_one;
    private TextView txt_two;
    private TextView txt_three;
    private TextView txt_four;
    private TextView txt_five;
    private TextView txt_six;
    private TextView txt_seven;
    private TextView txt_eight;
    private TextView txt_nine;
    private String message;
    private CommonMethods commonMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.report_inapropreate);

        setBodyUI();
    }

    private void setBodyUI() {

        activity = this;
        intent = new Intent();
        commonMethods = new CommonMethods(activity, this);
        //commonMethods.ActionBarProcessForMiddletext("Report");

        //defaultactionbar();
        userid = getIntent().getStringExtra("userid");
        postid = getIntent().getStringExtra("postid");

        findViewById(R.id.ll_one).setOnClickListener(this);
        findViewById(R.id.ll_two).setOnClickListener(this);
        findViewById(R.id.ll_three).setOnClickListener(this);
        findViewById(R.id.ll_four).setOnClickListener(this);
        findViewById(R.id.ll_five).setOnClickListener(this);
        findViewById(R.id.ll_six).setOnClickListener(this);
        findViewById(R.id.ll_seven).setOnClickListener(this);
        findViewById(R.id.ll_eight).setOnClickListener(this);
        findViewById(R.id.ll_nine).setOnClickListener(this);
        findViewById(R.id.ll_left_icon).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onBackPressed();

                    }
                });

        txt_one = (TextView) findViewById(R.id.txt_one);
        txt_two = (TextView) findViewById(R.id.txt_two);
        txt_three = (TextView) findViewById(R.id.txt_three);
        txt_four = (TextView) findViewById(R.id.txt_four);
        txt_five = (TextView) findViewById(R.id.txt_five);
        txt_six = (TextView) findViewById(R.id.txt_six);
        txt_seven = (TextView) findViewById(R.id.txt_seven);
        txt_eight = (TextView) findViewById(R.id.txt_eight);
        txt_nine = (TextView) findViewById(R.id.txt_nine);

    }

    private void defaultactionbar() {
        ActionBar mActionBar = activity.getActionBar();
        //mActionBar.setDisplayShowHomeEnabled(false);
        //mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(activity);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);

        // content of action bar

        ImageView logo = (ImageView) mCustomView.findViewById(R.id.logo);
        ImageView imgbtn_back_home = (ImageView) mCustomView
                .findViewById(R.id.imgbtn_back_home);
        ImageView imgbtn_setting = (ImageView) mCustomView
                .findViewById(R.id.imgbtn_setting);
        TextView Middle_text = (TextView) mCustomView
                .findViewById(R.id.Middle_text);
        TextView txt_reset = (TextView) mCustomView
                .findViewById(R.id.txt_reset);
        TextView txt_cancel = (TextView) mCustomView
                .findViewById(R.id.txt_cancel);
        Middle_text.setText("Report");
        txt_reset.setVisibility(View.INVISIBLE);
        Middle_text.setVisibility(View.VISIBLE);
        txt_cancel.setVisibility(View.INVISIBLE);
        logo.setVisibility(View.VISIBLE);
        imgbtn_back_home.setVisibility(View.VISIBLE);
        imgbtn_setting.setVisibility(View.INVISIBLE);

        RelativeLayout ll_left_icon = (RelativeLayout) mCustomView
                .findViewById(R.id.ll_left_icon);
        RelativeLayout ll_right_icon = (RelativeLayout) mCustomView
                .findViewById(R.id.ll_right_icon);

        ll_left_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                activity.onBackPressed();

            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_one:
                message = txt_one.getText().toString();
                report(message);

                break;

            case R.id.ll_two:
                message = txt_two.getText().toString();
                report(message);
                break;

            case R.id.ll_three:
                message = txt_three.getText().toString();
                report(message);
                break;

            case R.id.ll_four:
                message = txt_four.getText().toString();
                report(message);
                break;

            case R.id.ll_five:
                message = txt_five.getText().toString();
                report(message);
                break;

            case R.id.ll_six:
                message = txt_six.getText().toString();
                report(message);
                break;

            case R.id.ll_seven:
                message = txt_seven.getText().toString();
                report(message);
                break;

            case R.id.ll_eight:
                message = txt_eight.getText().toString();
                report(message);
                break;

            case R.id.ll_nine:

                messageDialog();

                break;

            default:
                break;
        }

    }

    private void messageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Add a Report");
        final EditText input = new EditText(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setBackgroundColor(Color.TRANSPARENT);
        input.setHint("Report here...");
        builder.setView(input); // uncomment this line
        builder.setPositiveButton("Submit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        message = input.getText().toString();
                        report(message);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void report(String message2) {
        if (commonMethods.getConnectivityStatus()) {
            try {
                commonMethods.ReportonSinglePost(commonMethods
                        .setReportRequestParmas(userid, postid, message2));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            GlobalConfig.showToast(
                    activity,
                    activity.getResources().getString(
                            R.string.internet_error_message));
        }
    }

    @Override
    public void OnComplete(APIResponse apiResponse) {
        System.out.println("Response of report " + apiResponse.getResponse());
        if (apiResponse.getCode() == 200) {

            try {
                JSONObject rootJsonObjObject = new JSONObject(
                        apiResponse.getResponse());
                String strMsg = rootJsonObjObject.getString("msg");
                String strStatus = rootJsonObjObject.getString("status");

                if (strStatus.equals("true")) {
                    finish();
                    GlobalConfig.showToast(activity, strMsg);
                } else {
                    GlobalConfig.showToast(activity, strMsg);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            GlobalConfig.showToast(activity, "Please try again");
        }
    }

}
