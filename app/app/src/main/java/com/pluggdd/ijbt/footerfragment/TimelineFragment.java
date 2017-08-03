package com.pluggdd.ijbt.footerfragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.adapter.TimerFollowerNotificationAdapter;
import com.pluggdd.ijbt.network.APIResponse;
import com.pluggdd.ijbt.network.IJBTResponseController;
import com.pluggdd.ijbt.util.ApplicationConstants;
import com.pluggdd.ijbt.util.CommonMethods;
import com.pluggdd.ijbt.util.GlobalConfig;
import com.pluggdd.ijbt.vo.GsonParseTimeLineFollowersNotification;
import com.quentindommerc.superlistview.SuperListview;

import java.io.UnsupportedEncodingException;

@SuppressLint("NewApi")
public class TimelineFragment extends Fragment
        implements OnClickListener, IJBTResponseController, OnRefreshListener {

    private Activity activity;
    private View view;
    private Button btn_follower_activity;
    private Button btn_your_activity;
    private LinearLayout top_follow_activity;
    private SuperListview lv_notificaiton;
    private SuperListview lv_following;
    private CommonMethods commonMethods;
    private String userid;
    private TimerFollowerNotificationAdapter timer_notification_adapter;
    private TimerFollowerNotificationAdapter timer_Follower_adapter;
    private boolean firstTimeService = true;
    protected int tasktype;
    private String notification = "follower";
    int int_total_page;
    private boolean loadingMore = false;
    private int pageNumberForService = 1;
    private GsonParseTimeLineFollowersNotification gsonParseTimeLineFollowersNotification;
    private GsonParseTimeLineFollowersNotification gsonParseTimeLineMyNotification;

    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
        return fragment;
    }

    public TimelineFragment() {
    }

    @Override
    public void onResume() {

        super.onResume();
        defaultActionBarProcess();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.timeline_fragment, container, false);

        setBodyUI();
        return view;
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
        Middle_text.setText("Notification");
        txt_reset.setVisibility(View.INVISIBLE);
        Middle_text.setVisibility(View.VISIBLE);
        txt_cancel.setVisibility(View.INVISIBLE);
        logo.setVisibility(View.VISIBLE);
        imgbtn_back_home.setVisibility(View.VISIBLE);
        imgbtn_notification.setVisibility(View.INVISIBLE);
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


    @SuppressLint("ResourceAsColor")
    private void setBodyUI() {
        activity = getActivity();
        commonMethods = new CommonMethods(activity, this);

        // commonMethods.actionBarProcess();
        userid = activity.getSharedPreferences("prefs_login", Activity.MODE_PRIVATE).getString("userid", "");
        // finding buttons
        btn_follower_activity = (Button) view.findViewById(R.id.btn_follower_activity);
        btn_your_activity = (Button) view.findViewById(R.id.btn_your_activity);
        top_follow_activity = (LinearLayout) view.findViewById(R.id.top_follow_activity);
        lv_following = (SuperListview) view.findViewById(R.id.lv_following);
        lv_following.setRefreshListener(this);

        // Wow so beautiful
        lv_following.setRefreshingColor(android.R.color.holo_blue_light, android.R.color.holo_blue_light,
                android.R.color.holo_blue_light, android.R.color.holo_blue_light);

        // method for paging

        lv_following.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                System.out.println("State Change is---" + scrollState);

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                System.out.println("scroll starting");

                int i = firstVisibleItem + visibleItemCount;

                System.out.println("scroll view item > " + i + " / " + totalItemCount + " / " + pageNumberForService
                        + " / " + int_total_page + " / " + loadingMore);

                if ((i == totalItemCount) && (!loadingMore)) {

                    System.out.println("scroll in condition");

                    System.out.println("scroll view load more data called 1");
                    if (pageNumberForService <= int_total_page) {
                        System.out.println("scroll view load more data called 2");
                        loadingMore = true;
                        callSecondTimeWebServices(pageNumberForService);
                    }
                }
                System.out.println("scroll out condition");
            }

            private void callSecondTimeWebServices(int pageNumberForService) {

                if (gsonParseTimeLineFollowersNotification != null) {
                    if (commonMethods.getConnectivityStatus()) {
                        try {
                            tasktype = ApplicationConstants.TaskType.OTHERACTIVITY;
                            commonMethods.GetTimelineFeedsFollowerNotification(commonMethods
                                    .getTimelineFollowersNotificationRequestParmas(userid, pageNumberForService));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
                    }
                }

            }

        });

        lv_notificaiton = (SuperListview) view.findViewById(R.id.lv_notificaiton);
        lv_notificaiton.setRefreshListener(this);

        // Wow so beautiful
        lv_notificaiton.setRefreshingColor(android.R.color.holo_blue_light, android.R.color.holo_blue_light,
                android.R.color.holo_blue_light, android.R.color.holo_blue_light);

        lv_notificaiton.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                System.out.println("State Change is---" + scrollState);

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                System.out.println("scroll starting");

                int i = firstVisibleItem + visibleItemCount;

                System.out.println("scroll view item > " + i + " / " + totalItemCount + " / " + pageNumberForService
                        + " / " + int_total_page + " / " + loadingMore);

                if ((i == totalItemCount) && (!loadingMore)) {

                    System.out.println("scroll in condition");

                    System.out.println("scroll view load more data called 1");
                    if (pageNumberForService <= int_total_page) {
                        System.out.println("scroll view load more data called 2");
                        loadingMore = true;
                        callSecondTimeWebServices(pageNumberForService);
                    }
                }
                System.out.println("scroll out condition");
            }

            private void callSecondTimeWebServices(int pageNumberForService) {

                if (gsonParseTimeLineMyNotification != null) {
                    if (commonMethods.getConnectivityStatus()) {
                        try {
                            tasktype = ApplicationConstants.TaskType.MYACTIVITY;
                            commonMethods.GetTimelineFeedsMyNotification(commonMethods
                                    .getTimelineFollowersNotificationRequestParmas(userid, pageNumberForService));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
                    }
                }

            }

        });

        btn_follower_activity.setOnClickListener(this);
        btn_your_activity.setOnClickListener(this);

        if (notification.equals("follower")) {
            if (commonMethods.getConnectivityStatus()) {
                try {
                    gsonParseTimeLineFollowersNotification = null;
                    tasktype = ApplicationConstants.TaskType.OTHERACTIVITY;
                    commonMethods.GetTimelineFeedsFollowerNotification(
                            commonMethods.getTimelineFollowersNotificationRequestParmas(userid, pageNumberForService));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
            }
        } else {
            if (commonMethods.getConnectivityStatus()) {
                try {
                    gsonParseTimeLineFollowersNotification = null;
                    tasktype = ApplicationConstants.TaskType.MYACTIVITY;
                    commonMethods.GetTimelineFeedsMyNotification(
                            commonMethods.getTimelineFollowersNotificationRequestParmas(userid, pageNumberForService));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
            }

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_follower_activity:
                notification = "follower";
                lv_following.setVisibility(View.VISIBLE);
                lv_notificaiton.setVisibility(View.GONE);
                // top_follow_activity.setBackgroundResource(R.drawable.activity_on);
                btn_follower_activity.setTextColor(Color.parseColor("#ffffff"));
                btn_your_activity.setTextColor(Color.parseColor("#80FFFFFF"));
                break;

            case R.id.btn_your_activity:
                notification = "mine";
                lv_following.setVisibility(View.GONE);
                lv_notificaiton.setVisibility(View.VISIBLE);
                // top_follow_activity.setBackgroundResource(R.drawable.activity_on);
                btn_follower_activity.setTextColor(Color.parseColor("#80FFFFFF"));
                btn_your_activity.setTextColor(Color.parseColor("#ffffff"));
                pageNumberForService = 1;
                if (firstTimeService) {
                    firstTimeService = false;
                    gsonParseTimeLineMyNotification = null;
                    if (commonMethods.getConnectivityStatus()) {
                        try {
                            tasktype = ApplicationConstants.TaskType.MYACTIVITY;
                            commonMethods.GetTimelineFeedsMyNotification(commonMethods
                                    .getTimelineFollowersNotificationRequestParmas(userid, pageNumberForService));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
                    }
                }
                break;

            default:
                break;
        }

    }

    @Override
    public void OnComplete(APIResponse apiResponse) {

        switch (tasktype) {
            case ApplicationConstants.TaskType.MYACTIVITY:

                System.out.println("Response timeline" + apiResponse.getResponse());
                if (apiResponse.getCode() == 200) {
                    Gson gson = new Gson();
                    GsonParseTimeLineFollowersNotification localGsonParseTimeLineFollowersNotification;
                    if (gsonParseTimeLineMyNotification == null) {
                        gsonParseTimeLineMyNotification = gson.fromJson(apiResponse.getResponse(),
                                GsonParseTimeLineFollowersNotification.class);
                        int_total_page = Integer.parseInt(gsonParseTimeLineMyNotification.getTotal_page());
                        if (gsonParseTimeLineMyNotification.getStatus().equals("true")) {
                            timer_notification_adapter = new TimerFollowerNotificationAdapter(activity,
                                    gsonParseTimeLineMyNotification.getNotifications());

                            lv_notificaiton.setAdapter(timer_notification_adapter);

                            // if (gsonParseTimeLineMyNotification.getApp_status()
                            // .getUser_status().equals("1")) {
                            // CommonMethods.SetPreferences(activity,
                            // "audio_status",
                            // gsonParseTimeLineMyNotification
                            // .getApp_status().getUser_status());
                            // CommonMethods.SetPreferences(activity,
                            // "video_status",
                            // gsonParseTimeLineMyNotification
                            // .getApp_status().getUser_status());
                            // CommonMethods.SetPreferences(activity,
                            // "audio_plans",
                            // gsonParseTimeLineMyNotification
                            // .getApp_status().getUser_status());
                            // CommonMethods.SetPreferences(activity,
                            // "video_plans",
                            // gsonParseTimeLineMyNotification
                            // .getApp_status().getUser_status());
                            // } else {
                            // commonMethods
                            // .popup("your account has been deleted or Deactivated
                            // by Admin/User.");
                            // }

                        } else {
                            GlobalConfig.showToast(activity, gsonParseTimeLineMyNotification.getMsg());
                        }

                        if (pageNumberForService <= int_total_page) {
                            pageNumberForService = (1 + pageNumberForService);
                        }
                        loadingMore = false;
                    } else {
                        localGsonParseTimeLineFollowersNotification = gson.fromJson(apiResponse.getResponse(),
                                GsonParseTimeLineFollowersNotification.class);
                        int_total_page = Integer.parseInt(localGsonParseTimeLineFollowersNotification.getTotal_page());
                        if (localGsonParseTimeLineFollowersNotification.getStatus().equals("true")) {
                            gsonParseTimeLineMyNotification.getNotifications()
                                    .addAll(localGsonParseTimeLineFollowersNotification.getNotifications());
                            timer_notification_adapter.setData(gsonParseTimeLineMyNotification.getNotifications());
                        } else {
                            GlobalConfig.showToast(activity, "Please try again");
                        }
                        if (pageNumberForService <= int_total_page) {
                            pageNumberForService = (1 + pageNumberForService);
                        }
                        loadingMore = false;
                    }

                } else {
                    GlobalConfig.showToast(activity, "Please try again");
                }

                break;

            case ApplicationConstants.TaskType.OTHERACTIVITY:

                System.out.println("Response timeline" + apiResponse.getResponse());
                if (apiResponse.getCode() == 200) {
                    Gson gson = new Gson();

                    GsonParseTimeLineFollowersNotification localGsonParseTimeLineFollowersNotification;

                    if (gsonParseTimeLineFollowersNotification == null) {
                        gsonParseTimeLineFollowersNotification = gson.fromJson(apiResponse.getResponse(),
                                GsonParseTimeLineFollowersNotification.class);
                        int_total_page = Integer.parseInt(gsonParseTimeLineFollowersNotification.getTotal_page());
                        if (gsonParseTimeLineFollowersNotification.getStatus().equals("true")) {
                            timer_Follower_adapter = new TimerFollowerNotificationAdapter(activity,
                                    gsonParseTimeLineFollowersNotification.getNotifications());

                            lv_following.setAdapter(timer_Follower_adapter);

                        } else {
                            GlobalConfig.showToast(activity, gsonParseTimeLineFollowersNotification.getMsg());
                        }
                        if (pageNumberForService <= int_total_page) {
                            pageNumberForService = (1 + pageNumberForService);
                        }
                        loadingMore = false;
                    } else {
                        localGsonParseTimeLineFollowersNotification = gson.fromJson(apiResponse.getResponse(),
                                GsonParseTimeLineFollowersNotification.class);
                        int_total_page = Integer.parseInt(localGsonParseTimeLineFollowersNotification.getTotal_page());
                        if (localGsonParseTimeLineFollowersNotification.getStatus().equals("true")) {
                            gsonParseTimeLineFollowersNotification.getNotifications()
                                    .addAll(localGsonParseTimeLineFollowersNotification.getNotifications());
                            timer_Follower_adapter.setData(gsonParseTimeLineFollowersNotification.getNotifications());
                        } else {
                            GlobalConfig.showToast(activity, "Please try again");
                        }
                        if (pageNumberForService <= int_total_page) {
                            pageNumberForService = (1 + pageNumberForService);
                        }
                        loadingMore = false;
                    }

                } else {
                    GlobalConfig.showToast(activity, "Please try again");
                }

                break;

            default:
                break;
        }

    }

    @Override
    public void onRefresh() {

        if (notification.equals("follower")) {
            if (commonMethods.getConnectivityStatus()) {
                try {
                    gsonParseTimeLineFollowersNotification = null;
                    pageNumberForService = 1;
                    tasktype = ApplicationConstants.TaskType.OTHERACTIVITY;
                    commonMethods.GetTimelineFeedsFollowerNotification(
                            commonMethods.getTimelineFollowersNotificationRequestParmas(userid, pageNumberForService));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
            }
        } else if (notification.equals("mine")) {
            if (commonMethods.getConnectivityStatus()) {
                try {
                    gsonParseTimeLineMyNotification = null;
                    pageNumberForService = 1;
                    tasktype = ApplicationConstants.TaskType.MYACTIVITY;
                    commonMethods.GetTimelineFeedsMyNotification(
                            commonMethods.getTimelineFollowersNotificationRequestParmas(userid, pageNumberForService));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
            }
        }

    }
}
