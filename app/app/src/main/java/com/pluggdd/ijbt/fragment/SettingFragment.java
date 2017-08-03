package com.pluggdd.ijbt.fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.pluggdd.ijbt.HomeActivity;
import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.SplashActivity;
import com.pluggdd.ijbt.network.APIResponse;
import com.pluggdd.ijbt.network.IJBTResponseController;
import com.pluggdd.ijbt.util.ApplicationConstants;
import com.pluggdd.ijbt.util.Comman;
import com.pluggdd.ijbt.util.CommonMethods;
import com.pluggdd.ijbt.util.GlobalConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

@SuppressLint("NewApi")
public class SettingFragment extends Fragment implements OnClickListener,
        IJBTResponseController {

    private View view;
    private Activity activity;
    //private LinearLayout btn_off_prvt_post;
    //private LinearLayout btn_on_prvt_post;
    //private LinearLayout ll_prvt_post;
    private RelativeLayout edit_profile;
    //private RelativeLayout ll_contextus;
    //private RelativeLayout ll_Report_Problems;
    //private RelativeLayout ll_Give_Us_Feedback;
    //private RelativeLayout ll_terms_servies;
    //private RelativeLayout ll_Privacy_Policy;
    //private RelativeLayout ll_aboutApp;
    //private RelativeLayout ll_changePassword;
    private RelativeLayout find_people;
    private RelativeLayout ll_logout;
    //private RelativeLayout ll_push_notification;
    private RelativeLayout llDeactivate;
    private RelativeLayout ll_invite_friends;
    private RelativeLayout ll_share_app;
    private Bundle bundle;
    private CommonMethods commonMethods;
    private FragmentManager fragmentManager;
    private String userid;
    private String userName;
    private String allow_follow;
    protected int tasktype;
    private static SharedPreferences mSharedPreferences;
    private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
    private static final String PREF_NAME = "sample_twitter_pref";

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    public SettingFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_on, container, false);

        setBodyUI();

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        defaultActionBarProcess();

    }


    public void inviteFriends() {
        CallbackManager sCallbackManager;
        String AppURl = "https://fb.me/381526528901255";
        String previewImageUrl = "http://someurl/13_dp.png";

        sCallbackManager = CallbackManager.Factory.create();

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(AppURl).setPreviewImageUrl(previewImageUrl)
                    .build();

            AppInviteDialog appInviteDialog = new AppInviteDialog(this);
            appInviteDialog.registerCallback(sCallbackManager,
                    new FacebookCallback<AppInviteDialog.Result>() {
                        @Override
                        public void onSuccess(AppInviteDialog.Result result) {
                            Log.d("Invitation", "Invitation Sent Successfully");
                        }

                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onError(FacebookException e) {
                            Log.d("Invitation", "Error Occured");
                        }
                    });

            appInviteDialog.show(content);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setBodyUI() {
        activity = getActivity();
        bundle = getArguments();

        userid = activity.getSharedPreferences("prefs_login",
                Activity.MODE_PRIVATE).getString("userid", "");
        userName = activity.getSharedPreferences("prefs_login",
                Activity.MODE_PRIVATE).getString("username", "");
        fragmentManager = getFragmentManager();
        commonMethods = new CommonMethods(activity, this);

        // finding LinearLayout
      /*  btn_off_prvt_post = (LinearLayout) view
                .findViewById(R.id.btn_off_prvt_post);
        btn_on_prvt_post = (LinearLayout) view
                .findViewById(R.id.btn_on_prvt_post);
        *//*ll_push_notification = (RelativeLayout) view
                .findViewById(R.id.ll_push_notification);*//*
        ll_prvt_post = (LinearLayout) view.findViewById(R.id.ll_prvt_post);*/
        edit_profile = (RelativeLayout) view.findViewById(R.id.edit_profile);
        /*ll_contextus = (RelativeLayout) view.findViewById(R.id.ll_contextus);
        ll_Report_Problems = (RelativeLayout) view
                .findViewById(R.id.ll_Report_Problems);
        ll_Give_Us_Feedback = (RelativeLayout) view
                .findViewById(R.id.ll_Give_Us_Feedback);
        ll_aboutApp = (RelativeLayout) view.findViewById(R.id.ll_aboutApp);
        ll_Privacy_Policy = (RelativeLayout) view
                .findViewById(R.id.ll_Privacy_Policy);
        ll_terms_servies = (RelativeLayout) view
                .findViewById(R.id.ll_terms_servies);*/
        //ll_changePassword = (RelativeLayout) view
        //		.findViewById(R.id.ll_changePassword);
        // ll_lookagram_plan = (RelativeLayout) view
        // .findViewById(R.id.ll_lookagram_plan);
        ll_share_app = (RelativeLayout) view
                .findViewById(R.id.ll_lookagram_share);
        ll_invite_friends = (RelativeLayout) view
                .findViewById(R.id.ll_invite_friends);
        find_people = (RelativeLayout) view.findViewById(R.id.find_people);
        ll_logout = (RelativeLayout) view.findViewById(R.id.ll_logout);
        llDeactivate = (RelativeLayout) view.findViewById(R.id.rlDeactivate);

        // on click listner LinearLayouts

        //btn_off_prvt_post.setOnClickListener(this);
        //btn_on_prvt_post.setOnClickListener(this);
        //ll_push_notification.setOnClickListener(this);
        edit_profile.setOnClickListener(this);
        //ll_contextus.setOnClickListener(this);
        //ll_Report_Problems.setOnClickListener(this);
        //ll_Give_Us_Feedback.setOnClickListener(this);
        //ll_aboutApp.setOnClickListener(this);
        //ll_Privacy_Policy.setOnClickListener(this);
        //ll_terms_servies.setOnClickListener(this);
        //ll_changePassword.setOnClickListener(this);
        find_people.setOnClickListener(this);
        ll_logout.setOnClickListener(this);
        llDeactivate.setOnClickListener(this);
        // ll_lookagram_plan.setOnClickListener(this);
        ll_share_app.setOnClickListener(this);
        ll_invite_friends.setOnClickListener(this);
        /*if (commonMethods.getConnectivityStatus()) {
            try {
                tasktype = ApplicationConstants.TaskType.GETALLOWFOLLOW;
                commonMethods.GetFollowPermission(commonMethods
                        .getLookagramPlanRequestParmas(userid));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            GlobalConfig.showToast(activity,
                    getResources().getString(R.string.internet_error_message));
        }*/

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
        Middle_text.setText("Settings");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_profile:
                EditProfileFragment fragment = new EditProfileFragment();
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.animator.enter_anim,
                                R.animator.exit_anim, R.animator.back_anim_start,
                                R.animator.back_anim_end)
                        .add(R.id.container, fragment).addToBackStack(null)
                        .commit();

                break;
            case R.id.find_people:
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.animator.enter_anim,
                                R.animator.exit_anim, R.animator.back_anim_start,
                                R.animator.back_anim_end)
                        .add(R.id.container, FindPeopleFragment.newInstance())
                        .addToBackStack(null).commit();
                break;
            case R.id.ll_logout:

                popupDelete();

                break;

            case R.id.ll_lookagram_share:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "IJBT");
                String sAux = "Hi, I would like to share this super cool app with you called IJBT. You can add pictures and comments. \n\n" ;
                sAux = sAux + "Link: https://play.google.com/store/apps/details?id=com.pluggdd.ijbt";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Share App"));
                break;

            case R.id.ll_invite_friends:
                inviteFriends();
                break;
            case R.id.rlDeactivate:

                popupDeactivate();

                break;

            default:
                break;
        }
    }

    private void popupDeactivate() {

        new AlertDialog.Builder(activity)

                .setMessage("Are you sure you want to DEACTIVATE Account?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (commonMethods.getConnectivityStatus()) {
                                    try {
                                        tasktype = ApplicationConstants.TaskType.SETDEACTIVATE;
                                        commonMethods.DeativateUser(commonMethods
                                                .getLookagramPlanRequestParmas(userid));
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    GlobalConfig
                                            .showToast(
                                                    activity,
                                                    getResources()
                                                            .getString(
                                                                    R.string.internet_error_message));
                                }

                            }

                        }).setNegativeButton("No", null).show();

    }

    private void popupDelete() {

        new AlertDialog.Builder(activity)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Comman.flag_show_dialog = true;

                                if (commonMethods.getConnectivityStatus()) {
                                    try {
                                        mSharedPreferences = activity
                                                .getSharedPreferences(
                                                        PREF_NAME, 0);
                                        SharedPreferences.Editor e = mSharedPreferences
                                                .edit();
                                        e.putBoolean(PREF_KEY_TWITTER_LOGIN,
                                                false);
                                        e.commit();
                                        tasktype = ApplicationConstants.TaskType.LOGOUT;
                                        commonMethods.LogoutUser(commonMethods
                                                .getlogoutRequestParmas(
                                                        userid,
                                                        SplashActivity.registrationId));
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    GlobalConfig
                                            .showToast(
                                                    activity,
                                                    getResources()
                                                            .getString(
                                                                    R.string.internet_error_message));
                                }

                            }

                        }).setNegativeButton("No", null).show();

    }

    @Override
    public void OnComplete(APIResponse apiResponse) {

        switch (tasktype) {
            case ApplicationConstants.TaskType.SETDEACTIVATE:
                System.out.println("Response deactivate user"
                        + apiResponse.getResponse());

                if (apiResponse.getCode() == 200) {

                    try {
                        JSONObject rootJsonObjObject = new JSONObject(
                                apiResponse.getResponse());
                        String strMsg = rootJsonObjObject.getString("msg");
                        String strStatus = rootJsonObjObject.getString("status");

                        if (strStatus.equals("true")) {
                            System.out.println("Response logout" + apiResponse.getResponse());
                            logoutFromFacebook(new LogoutFromFacebookListener() {
                                @Override
                                public void onLoggedOutFromFacebook() {
                                    activity.getSharedPreferences("prefs_login", Activity.MODE_PRIVATE)
                                            .edit().clear().commit();
                                    Intent intent = new Intent();
                                    activity.startActivity(intent
                                            .setClass(activity, HomeActivity.class));
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    activity.finish();
                                    activity.overridePendingTransition(0, 0);
                                }
                            });

                        } else {
                            GlobalConfig.showToast(activity, strMsg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    GlobalConfig.showToast(activity, "Please try again");
                }

                break;

            /*case ApplicationConstants.TaskType.GETALLOWFOLLOW:

                System.out.println("Response allow follow user"
                        + apiResponse.getResponse());

                if (apiResponse.getCode() == 200) {

                    try {
                        JSONObject rootJsonObjObject = new JSONObject(
                                apiResponse.getResponse());
                        String strMsg = rootJsonObjObject.getString("msg");
                        String strStatus = rootJsonObjObject.getString("status");

                        if (strStatus.equals("true")) {
                            String allow_follow = rootJsonObjObject
                                    .getString("allow_follow");
                            if (!(allow_follow.equals(null))) {
                                if (allow_follow.equals("1")) {
                                    ll_prvt_post
                                            .setBackgroundResource(R.drawable.set_toggle_on);
                                } else {
                                    ll_prvt_post
                                            .setBackgroundResource(R.drawable.set_toggle_off);
                                }
                            }
                        } else {
                            GlobalConfig.showToast(activity, strMsg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    GlobalConfig.showToast(activity, "Please try again");
                }

                break;*/

            case ApplicationConstants.TaskType.SETALLOWFOLLOW:

                System.out.println("Response set allow follow user"
                        + apiResponse.getResponse());

                break;

            case ApplicationConstants.TaskType.LOGOUT:
                System.out.println("Response logout" + apiResponse.getResponse());
                logoutFromFacebook(new LogoutFromFacebookListener() {
                    @Override
                    public void onLoggedOutFromFacebook() {
                        activity.getSharedPreferences("prefs_login", Activity.MODE_PRIVATE)
                                .edit().clear().commit();
                        Intent intent = new Intent();
                        activity.startActivity(intent
                                .setClass(activity, HomeActivity.class));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.finish();
                        activity.overridePendingTransition(0, 0);
                    }
                });
                break;

            default:
                break;
        }

    }

    public void logoutFromFacebook(final LogoutFromFacebookListener listener) {

        if (AccessToken.getCurrentAccessToken() == null) {
            // already logged out
            listener.onLoggedOutFromFacebook();
            return;
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
                listener.onLoggedOutFromFacebook();
            }
        }).executeAsync();
    }

    public interface LogoutFromFacebookListener {

        void onLoggedOutFromFacebook();
    }
}
