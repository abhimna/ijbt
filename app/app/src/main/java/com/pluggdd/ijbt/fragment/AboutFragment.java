package com.pluggdd.ijbt.fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.util.CommonMethods;

@SuppressLint("NewApi")
public class AboutFragment extends Fragment {

	private View view;
	private WebView webView;
	Activity activity;
	CommonMethods commonMethods;

	public static AboutFragment newInstance() {
		AboutFragment fragment = new AboutFragment();
		return fragment;
	}

	public AboutFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.web_view, container, false);

		setBodyUI();
		
		return view;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		defaultActionBarProcess();
	}
	private void defaultActionBarProcess() {

		ActionBar mActionBar = activity.getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(activity);
		View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);

		// content of action bar

		ImageView logo = (ImageView) mCustomView.findViewById(R.id.logo);
		ImageView imgbtn_back_home = (ImageView) mCustomView
				.findViewById(R.id.imgbtn_back_home);
		ImageView imgbtn_setting = (ImageView) mCustomView
				.findViewById(R.id.imgbtn_setting);
		ImageView imgbtn_notification = (ImageView) mCustomView
				.findViewById(R.id.imgbtn_notification);
		TextView Middle_text = (TextView) mCustomView
				.findViewById(R.id.Middle_text);
		TextView txt_reset = (TextView) mCustomView
				.findViewById(R.id.txt_reset);
		TextView txt_cancel = (TextView) mCustomView
				.findViewById(R.id.txt_cancel);
		Middle_text.setText("About Us");

		txt_reset.setVisibility(View.INVISIBLE);
		Middle_text.setVisibility(View.VISIBLE);
		txt_cancel.setVisibility(View.INVISIBLE);
		logo.setVisibility(View.GONE);
		imgbtn_back_home.setVisibility(View.VISIBLE);
		imgbtn_setting.setVisibility(View.INVISIBLE);
		imgbtn_notification.setVisibility(View.INVISIBLE);

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

		ll_right_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

	
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setBodyUI() {
		activity = getActivity();
		commonMethods = new CommonMethods(activity, this);
		//commonMethods.defaultActionBarProcess();
		defaultActionBarProcess();
		webView = (WebView) view.findViewById(R.id.webview);

		webView.loadUrl("http://52.36.237.68/app/services/pages/about.html");

	}

//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	@SuppressLint({ "InlinedApi", "InflateParams" })
//	public void defaultActionBarProcess() {
//		ActionBar mActionBar = activity.getActionBar();
//		mActionBar.setDisplayShowHomeEnabled(false);
//		mActionBar.setDisplayShowTitleEnabled(false);
//		LayoutInflater mInflater = LayoutInflater.from(activity);
//		View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
//
//		// content of action bar
//
//		ImageView logo = (ImageView) mCustomView.findViewById(R.id.logo);
//		ImageView imgbtn_back_home = (ImageView) mCustomView
//				.findViewById(R.id.imgbtn_back_home);
//		ImageView imgbtn_setting = (ImageView) mCustomView
//				.findViewById(R.id.imgbtn_setting);
//		TextView Middle_text = (TextView) mCustomView
//				.findViewById(R.id.Middle_text);
//		TextView txt_reset = (TextView) mCustomView
//				.findViewById(R.id.txt_reset);
//		TextView txt_cancel = (TextView) mCustomView
//				.findViewById(R.id.txt_cancel);
//
//		Middle_text.setText("About Lookagram");
//		txt_reset.setVisibility(View.INVISIBLE);
//		Middle_text.setVisibility(View.VISIBLE);
//		txt_cancel.setVisibility(View.INVISIBLE);
//		logo.setVisibility(View.GONE);
//		imgbtn_back_home.setVisibility(View.VISIBLE);
//		imgbtn_setting.setVisibility(View.INVISIBLE);
//
//		RelativeLayout ll_left_icon = (RelativeLayout) mCustomView
//				.findViewById(R.id.ll_left_icon);
//		RelativeLayout ll_right_icon = (RelativeLayout) mCustomView
//				.findViewById(R.id.ll_right_icon);
//
//		ll_left_icon.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				activity.onBackPressed();
//
//			}
//		});
//
//		ll_right_icon.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//			}
//		});
//
//		mActionBar.setCustomView(mCustomView);
//		mActionBar.setDisplayShowCustomEnabled(true);
//
//	}

}
