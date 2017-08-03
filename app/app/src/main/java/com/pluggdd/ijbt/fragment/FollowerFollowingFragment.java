package com.pluggdd.ijbt.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.adapter.FollowerAdapter;
import com.pluggdd.ijbt.network.APIResponse;
import com.pluggdd.ijbt.network.IJBTResponseController;
import com.pluggdd.ijbt.util.CommonMethods;
import com.pluggdd.ijbt.util.GlobalConfig;
import com.pluggdd.ijbt.vo.GsonParseFollower;

import java.io.UnsupportedEncodingException;

@SuppressLint("NewApi")
public class FollowerFollowingFragment extends Fragment implements
		IJBTResponseController, OnRefreshListener {

	private View view;
	private Activity activity;
	private ListView lstview_cmnt;
	private FollowerAdapter followerAdapter;
	private CommonMethods commonMethods;
	private GsonParseFollower gsonParseFollower;
	private String str_follow;
	private String userid;
	private String frndid;
	int pageNumberForService = 1;
	int int_total_page;
	private boolean loadingMore = false;
	private SwipeRefreshLayout swipeLayout;

	public static FollowerFollowingFragment newInstance(String str_follow) {
		FollowerFollowingFragment fragment = new FollowerFollowingFragment(
				str_follow);
		return fragment;
	}

	public FollowerFollowingFragment(String str_follow) {
		this.str_follow = str_follow;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.follwing_xml, container, false);

		setBodyUI();

		return view;
	}
		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			defaultActionBarProcess();
		}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint({"InlinedApi", "InflateParams"})
	public void defaultActionBarProcess() {
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
		if (str_follow.equals("Followers")) {
			Middle_text.setText("Follower");
		}
		else {
			Middle_text.setText("Following");
		}

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

		ll_left_icon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.onBackPressed();

			}
		});

		ll_right_icon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void setBodyUI() {
		activity = getActivity();

		swipeLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_blue_light,
				android.R.color.holo_blue_light,
				android.R.color.holo_blue_light);

		userid = activity.getSharedPreferences("prefs_login",
				Activity.MODE_PRIVATE).getString("userid", "");
		frndid = getArguments().getString("userid");
		commonMethods = new CommonMethods(activity, this);
		lstview_cmnt = (ListView) view.findViewById(R.id.folowing_listview);

		lstview_cmnt.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				System.out.println("State Change is---" + scrollState);

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				System.out.println("scroll starting");

				int i = firstVisibleItem + visibleItemCount;

				System.out.println("scroll view item > " + i + " / "
						+ totalItemCount + " / " + pageNumberForService + " / "
						+ int_total_page + " / " + loadingMore);

				if ((i == totalItemCount) && (!loadingMore)) {

					System.out.println("scroll in condition");

					System.out.println("scroll view load more data called 1");
					if (pageNumberForService <= int_total_page) {
						System.out
								.println("scroll view load more data called 2");
						loadingMore = true;
						callSecondTimeWebServices(pageNumberForService);
					}
				}
				System.out.println("scroll out condition");
			}

			private void callSecondTimeWebServices(int pageNumberForService) {

				if (gsonParseFollower != null) {

					if (commonMethods.getConnectivityStatus()) {
						try {
							commonMethods.GetFollowers(commonMethods
									.getFollowerRequestParmas(userid, frndid,
											"" + pageNumberForService),
									str_follow);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					} else {
						GlobalConfig.showToast(activity, getResources()
								.getString(R.string.internet_error_message));
					}
				}
			}
		});

		if (commonMethods.getConnectivityStatus()) {
			try {
				gsonParseFollower = null;
				commonMethods.GetFollowers(commonMethods
						.getFollowerRequestParmas(userid, frndid, ""
								+ pageNumberForService), str_follow);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			GlobalConfig.showToast(activity,
					getResources().getString(R.string.internet_error_message));
		}

	}

	@Override
	public void OnComplete(APIResponse apiResponse) {
		System.out.println("Response of get follower"
				+ apiResponse.getResponse());
		swipeLayout.setRefreshing(false);
		if (apiResponse.getCode() == 200) {
			Gson gson = new Gson();
			GsonParseFollower localgsonParseFollower;

			if (gsonParseFollower == null) {
				gsonParseFollower = gson.fromJson(apiResponse.getResponse(),
						GsonParseFollower.class);
				int_total_page = Integer.parseInt(gsonParseFollower
						.getTotal_page());
				if (pageNumberForService <= int_total_page) {
					pageNumberForService = (1 + pageNumberForService);
				}
				loadingMore = false;
				if (gsonParseFollower.getStatus().equals("true")) {
					followerAdapter = new FollowerAdapter(activity,
							gsonParseFollower.getFollowers_list(),str_follow);
					lstview_cmnt.setAdapter(followerAdapter);
				} else {
					GlobalConfig.showToast(activity, "Please try again");
				}
			} else {
				localgsonParseFollower = gson.fromJson(
						apiResponse.getResponse(), GsonParseFollower.class);
				int_total_page = Integer.parseInt(localgsonParseFollower
						.getTotal_page());

				if (localgsonParseFollower.getStatus().equals("true")) {
					if (pageNumberForService <= int_total_page) {
						pageNumberForService = (1 + pageNumberForService);
					}
					loadingMore = false;
					gsonParseFollower.getFollowers_list().addAll(
							localgsonParseFollower.getFollowers_list());
					followerAdapter.setData(gsonParseFollower
							.getFollowers_list());
				} else {
					GlobalConfig.showToast(activity, "Please try again");
				}
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener#onRefresh
	 * ()
	 */
	@Override
	public void onRefresh() {
		if (commonMethods.getConnectivityStatus()) {
			try {
				pageNumberForService = 1;
				gsonParseFollower = null;
				commonMethods.GetFollowers(commonMethods
						.getFollowerRequestParmas(userid, frndid, ""
								+ pageNumberForService), str_follow);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			GlobalConfig.showToast(activity,
					getResources().getString(R.string.internet_error_message));
		}

	}

}
