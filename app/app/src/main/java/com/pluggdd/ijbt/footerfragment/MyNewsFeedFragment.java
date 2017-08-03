package com.pluggdd.ijbt.footerfragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daprlabs.cardstack.SwipeDeck;
import com.daprlabs.cardstack.SwipeFrameLayout;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pluggdd.ijbt.CommonFragmentActivity;
import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.ReportActivity;
import com.pluggdd.ijbt.adapter.PorfileCardAdapter;
import com.pluggdd.ijbt.fragment.OtherUserProfileFragment;
import com.pluggdd.ijbt.network.APIResponse;
import com.pluggdd.ijbt.network.IJBTResponseController;
import com.pluggdd.ijbt.util.ApplicationConstants;
import com.pluggdd.ijbt.util.CardTypeListener;
import com.pluggdd.ijbt.util.Comman;
import com.pluggdd.ijbt.util.CommonMethods;
import com.pluggdd.ijbt.util.GlobalConfig;
import com.pluggdd.ijbt.util.OnViewFavourites;
import com.pluggdd.ijbt.util.RefreshListener;
import com.pluggdd.ijbt.util.SignatureCommonMenthods;
import com.pluggdd.ijbt.util.URLFactory;
import com.pluggdd.ijbt.util.ViewFavouritesDialog;
import com.pluggdd.ijbt.vo.GsonParseCommentList;
import com.pluggdd.ijbt.vo.GsonParseNewsFeed;
import com.pluggdd.ijbt.vo.GsonParseProfile;
import com.pluggdd.ijbt.vo.NewsFeedPostData;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/*
* This class is one of the footer class avaialble from the home screen.
* This class is a feed screen which the user gets the feed of the posts which can be visible to him/herxw
* */
@SuppressLint({"NewApi", "ResourceAsColor"})
public class MyNewsFeedFragment extends Fragment
        implements IJBTResponseController, CardTypeListener, SwipeRefreshLayout.OnRefreshListener, RefreshListener {

    private View view;
    private String userid, userName;
    private Activity activity;
    private CommonMethods commonMethods;
    // private AdView ADVIEW;
    private GsonParseNewsFeed gsonParseNewsFeed;
    protected int TaskType;
    int int_total_page;
    private int pageNumberForService = 1;
    public static int commentCountPostion;
    private String sharePostId;
    SharedPreferences pref;
    GsonParseProfile gsonParseProfile;
    private SwipeFrameLayout swipeFrameLayout;
    private SwipeDeck cardStack;
    private LinearLayout llPostImage;
    private TextView username;
    private TextView tv_agoday;
    private RelativeLayout rl_post_image;
    private RelativeLayout rl_loading_layout;
    private RelativeLayout rl_main_layout;
    private LinearLayout ll_more;
    private CircleImageView img_user_pic;
    private ImageView img_favourite;
    private LinearLayout ll_fav;
    private LinearLayout ll_share;
    private LinearLayout ll_user_status;
    private TextView tv_price;
    private TextView tv_location;
    private static MyNewsFeedFragment fragment;
    private Animation animation;
    String like_value;
    private ArrayList<NewsFeedPostData> data;
    private int pos;
    private boolean loadMoreStack = true;
    private int queriedPosts = 0;
    ImageView image_user_status;
    TextView txt_user_status;
    private TextView txtViewProgressLike, txtViewProgressDisLike;
    LinearLayout like_dislike_layout;
    RelativeLayout like_comment_layout;
    RelativeLayout tutorial_layout;
    TextView textViewSkip;
    private RelativeLayout notAvailableLayout;
    boolean shouldRefresh = false;
    boolean isFirstTime = false;
    ImageView imgbtn_refresh;
    private int maxCallInt = 1;
    //SwipeRefreshLayout swipeRefresh;

    public static MyNewsFeedFragment newInstance() {
        fragment = new MyNewsFeedFragment();
        return fragment;
    }

    public MyNewsFeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.swipeable_feed_fragment, container, false);
        setBodyUI();
        isFirstTime = true;
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().findViewById(R.id.footer_layout).setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        imgbtn_refresh = commonMethods.ActionBarProcessFeed("News Feed", this);
        getActivity().findViewById(R.id.llfooter).setVisibility(View.VISIBLE);
        refreshData();
    }

    @Override
    public void onPause() {
        super.onPause();
        shouldRefresh = true;
    }

    //Initializing all ui elements and its events
    @SuppressLint("SetJavaScriptEnabled")
    private void setBodyUI() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    "com.pluggdd.ijbt",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        findViews();

        //swipeRefresh.setOnRefreshListener(this);

        activity = getActivity();
        commonMethods = new CommonMethods(activity, this);
        commonMethods.ActionBarProcessForMiddletext("NewsFeed");

        pref = activity.getPreferences(Context.MODE_PRIVATE);
        pref = activity.getPreferences(0);

        userid = activity.getSharedPreferences("prefs_login", Activity.MODE_PRIVATE).getString("userid", "");

        userName = activity.getSharedPreferences("prefs_login", Activity.MODE_PRIVATE).getString("username", "");

        System.out.println("usernameloginuser" + userName);
        System.out.println("usernameloginuser--id" + userid);

        cardStack.setHardwareAccelerationEnabled(true);
        cardStack.invalidate();

        ll_share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsFeedPostData newsFeedPostData = data.get(pos);
                if (newsFeedPostData.getData_type().equals("video")) {
                    shareVideo(newsFeedPostData);
                } else if (newsFeedPostData.getData_type().equals("audio")) {
                    shareImage(newsFeedPostData);
                }
            }
        });

        ll_fav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsFeedPostData newsFeedPostData = data.get(pos);
                if (newsFeedPostData.getIs_post_favourate().equalsIgnoreCase("0")) {
                    setFavouritePost(userid, newsFeedPostData.getPost_id(), 1, pos);
                    newsFeedPostData.setIs_post_favourate("1");
                    img_favourite.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart));
                } else {
                    setFavouritePost(userid, newsFeedPostData.getPost_id(), 0, pos);
                    newsFeedPostData.setIs_post_favourate("0");
                    img_favourite.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart_temp));
                }
            }
        });


        ll_user_status.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsFeedPostData newsFeedPostData = data.get(pos);
                if (newsFeedPostData.getFollow_status().equalsIgnoreCase("0")) {
                    txt_user_status.setText("UnFollow");
                    for (int i = 0; i < data.size(); i++) {
                        if (newsFeedPostData.getUser_id().equals(data.get(i).getUser_id())) {
                            data.get(i).setFollow_status("1");
                        }
                    }
                    image_user_status.setImageDrawable(getResources().getDrawable(R.drawable.following_request));
                    setFollowRequest(newsFeedPostData.getUser_id(), "1");
                } else {
                    txt_user_status.setText("Follow");
                    for (int i = 0; i < data.size(); i++) {
                        if (newsFeedPostData.getUser_id().equals(data.get(i).getUser_id())) {
                            data.get(i).setFollow_status("0");
                        }
                    }
                    image_user_status.setImageDrawable(getResources().getDrawable(R.drawable.follower_add_frnd));
                    setFollowRequest(newsFeedPostData.getUser_id(), "0");
                }
            }
        });

        textViewSkip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Comman.SetPreferencesBoolean(activity, "is_not_first_time", true);
                tutorial_layout.setVisibility(View.GONE);
            }
        });
    }

    // refreshing the feed data when the user backs to this screen or refresh clicked
    private void refreshData() {
        pageNumberForService = 1;
        queriedPosts = 0;
        gsonParseNewsFeed = null;
        PorfileCardAdapter adapter = new PorfileCardAdapter(new ArrayList<NewsFeedPostData>(), getActivity(), this);
        cardStack.setAdapter(adapter);
        llPostImage.post(new Runnable() {
            @Override
            public void run() {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) cardStack.getLayoutParams();
                layoutParams.topMargin = llPostImage.getTop();
                layoutParams.leftMargin = llPostImage.getLeft();
                int height = llPostImage.getMeasuredHeight();
                int width = llPostImage.getMeasuredWidth();

                int size = 0;
                if (width > height) {
                    size = height;
                } else {
                    size = width;
                }
                layoutParams.height = size;
                layoutParams.width = size;
                cardStack.setLayoutParams(layoutParams);
                cardStack.invalidate();
                cardStack.setLeftImage(R.id.left_image);
                cardStack.setRightImage(R.id.right_image);
                swipeFrameLayout.updateViewLayout(cardStack, layoutParams);
                if (commonMethods.getConnectivityStatus()) {
                    getNewsFeeds(pageNumberForService);
                } else {
                    GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
                }
            }
        });
    }

    private void shareVideo(NewsFeedPostData newsFeedPostData) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "IJBT - check what your friend just bought \n\n" +
                URLFactory.imageUrl + newsFeedPostData.getData_url() + "\n" + "\n" + " App Link: https://play.google.com/store/apps/details?id=com.pluggdd.ijbt");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent,
                "Share Video URL"));
    }

    public void shareImage(final NewsFeedPostData newsFeedPostData) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "IJBT - check what your friend just bought \n\n" +
                URLFactory.imageUrl + newsFeedPostData.getImage() + "\n" + "\n" + "App Link: https://play.google.com/store/apps/details?id=com.pluggdd.ijbt");
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share Product"));
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    private void setFollowRequest(String other_userid, String str_status) {
        if (commonMethods.getConnectivityStatus()) {
            try {
                TaskType = ApplicationConstants.TaskType.SETFOLLOWREQUEST;
                commonMethods.SetFollowRequestFeeds(commonMethods
                        .setFollowUnfollowRequestParmas(userid,
                                other_userid, str_status));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            GlobalConfig.showToast(
                    getActivity(),
                    getActivity().getResources().getString(
                            R.string.internet_error_message));
        }
    }

    private void getNewsFeeds(int pageNumberForService) {
        notAvailableLayout.setVisibility(View.GONE);
        rl_main_layout.setVisibility(View.GONE);
        rl_loading_layout.setVisibility(View.VISIBLE);
        try {
            TaskType = ApplicationConstants.TaskType.GETNEWSFEED;
            commonMethods.GetCardNewsFeeds(commonMethods.getNewsFeedRequestParmas(userid, "" + pageNumberForService));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void findViews() {
        swipeFrameLayout = (SwipeFrameLayout) view.findViewById(R.id.swipeLayout);
        cardStack = (SwipeDeck) view.findViewById(R.id.swipe_deck);
        llPostImage = (LinearLayout) view.findViewById(R.id.ll_post_image);
        username = (TextView) view.findViewById(R.id.username);
        tv_agoday = (TextView) view.findViewById(R.id.tv_agoday);
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        tv_price = (TextView) view.findViewById(R.id.tv_price);
        ll_fav = (LinearLayout) view.findViewById(R.id.ll_fav);
        ll_share = (LinearLayout) view.findViewById(R.id.ll_share);
        rl_post_image = (RelativeLayout) view.findViewById(R.id.rl_post_image);
        rl_loading_layout = (RelativeLayout) view.findViewById(R.id.loadingLayout);
        notAvailableLayout = (RelativeLayout) view.findViewById(R.id.notAvailableLayout);

        rl_main_layout = (RelativeLayout) view.findViewById(R.id.originalLayout);
        ll_more = (LinearLayout) view.findViewById(R.id.ll_more);
        img_user_pic = (CircleImageView) view.findViewById(R.id.img_user_pic);
        img_favourite = (ImageView) view.findViewById(R.id.img_favourite);
        ll_user_status = (LinearLayout) view.findViewById(R.id.ll_user_status);
        image_user_status = (ImageView) view.findViewById(R.id.image_user_status);
        txt_user_status = (TextView) view.findViewById(R.id.txt_user_status);
        like_dislike_layout = (LinearLayout) view.findViewById(R.id.like_dislike_layout);
        like_comment_layout = (RelativeLayout) view.findViewById(R.id.like_comment_layout);
        txtViewProgressLike = (TextView) view.findViewById(R.id.txtViewProgressLike);
        txtViewProgressDisLike = (TextView) view.findViewById(R.id.txtViewProgressDisLike);
        tutorial_layout = (RelativeLayout) view.findViewById(R.id.tutorial_layout);
        textViewSkip = (TextView) view.findViewById(R.id.textViewSkip);

        //swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        //progressbar = (ProgressBar) view.findViewById(R.id.progress);
    }

    private void populateCardStack() {
        PorfileCardAdapter adapter = new PorfileCardAdapter(data, getActivity(), this);
        cardStack.setAdapter(adapter);
        setupCurrentStackItems(0);

        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                increaseDisLike(position);
                setupCurrentStackItems(position + 1);
                if (position == data.size() - 1) {
                    if (loadMoreStack) {
                        queriedPosts=0;
                        pageNumberForService = 1;
                        getNewsFeeds(pageNumberForService);
                    } else {
                        pageNumberForService = 1;
                        queriedPosts = 0;
                        gsonParseNewsFeed = null;
                        PorfileCardAdapter adapter = new PorfileCardAdapter(new ArrayList<NewsFeedPostData>(), getActivity(), MyNewsFeedFragment.this);
                        cardStack.setAdapter(adapter);
                        ViewFavouritesDialog viewFavouritesDialog = new ViewFavouritesDialog(getActivity(), new OnViewFavourites() {
                            @Override
                            public void onShouldShowFavourites(boolean shouldShowFav) {
                                if (shouldShowFav) {
                                    ((CommonFragmentActivity) getActivity()).goToProfilePage();
                                } else {
                                    rl_loading_layout.setVisibility(View.GONE);
                                    rl_main_layout.setVisibility(View.GONE);
                                    notAvailableLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        viewFavouritesDialog.show();
                    }
                }
            }

            @Override
            public void cardSwipedRight(int position) {
                increaseLike(position);
                setupCurrentStackItems(position + 1);
                if (position == data.size() - 1) {
                    if (loadMoreStack) {
                        queriedPosts=0;
                        pageNumberForService = 1;
                        getNewsFeeds(pageNumberForService);
                    } else {
                        pageNumberForService = 1;
                        queriedPosts = 0;
                        gsonParseNewsFeed = null;
                        PorfileCardAdapter adapter = new PorfileCardAdapter(new ArrayList<NewsFeedPostData>(), getActivity(), MyNewsFeedFragment.this);
                        cardStack.setAdapter(adapter);
                        ViewFavouritesDialog viewFavouritesDialog = new ViewFavouritesDialog(getActivity(), new OnViewFavourites() {
                            @Override
                            public void onShouldShowFavourites(boolean shouldShowFav) {
                                if (shouldShowFav) {
                                    ((CommonFragmentActivity) getActivity()).goToProfilePage();
                                } else {
                                    rl_loading_layout.setVisibility(View.GONE);
                                    rl_main_layout.setVisibility(View.GONE);
                                    notAvailableLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        viewFavouritesDialog.show();
                    }
                }
            }

            @Override
            public void cardsDepleted() {
                Log.i("Feed Fragment", "no more cards");
            }

            @Override
            public void cardActionDown() {
                Log.i("Feed Fragment", "cardActionDown");
            }

            @Override
            public void cardActionUp() {
                Log.i("Feed Fragment", "cardActionUp");
            }

        });
    }

    private void increaseDisLike(int position) {
        NewsFeedPostData newsFeedPostData = data.get(position);
        if (newsFeedPostData.getIs_post_liked().equals("1")) {
            newsFeedPostData.setIs_post_liked("0");
            like_value = "0";
            String noLikes = newsFeedPostData.getPost_total_like();
            newsFeedPostData.setPost_total_like(String.valueOf(Integer.parseInt(noLikes) - 1));

        }
        String post_id = newsFeedPostData.getPost_id();
        setLikePost(userid, post_id, "-1", position);
    }

    private void increaseLike(int position) {
        NewsFeedPostData newsFeedPostData = data.get(position);
        if (newsFeedPostData.getIs_post_liked().equals("0")) {
            newsFeedPostData.setIs_post_liked("1");
            like_value = "1";
            String noLikes = newsFeedPostData.getPost_total_like();
            newsFeedPostData.setPost_total_like(String.valueOf(Integer.parseInt(noLikes) + 1));
        }
        String post_id = newsFeedPostData.getPost_id();
        setLikePost(userid, post_id, "1", position);

    }

    public void setupCurrentStackItems(final int position) {
        if (position == 0) {
            if (!Comman.getPreferencesBoolean(activity, "is_not_first_time")) {
                tutorial_layout.setVisibility(View.VISIBLE);
            } else {
                tutorial_layout.setVisibility(View.GONE);
            }
        }
        pos = position;
        if (position < data.size()) {
            final NewsFeedPostData newsFeedPostData = data.get(position);
            username.setText(newsFeedPostData.getUser_name());
            tv_price.setText(newsFeedPostData.getPrice() + " " + newsFeedPostData.getCurrency());
            tv_location.setText(newsFeedPostData.getPlacebought());
            tv_agoday.setText(newsFeedPostData.getTime_ago());
            if (newsFeedPostData.getIs_post_favourate().equalsIgnoreCase("1")) {
                img_favourite.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart));
            } else {
                img_favourite.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart_temp));
            }
            if (newsFeedPostData.getFollow_status().equalsIgnoreCase("1")) {
                txt_user_status.setText("UnFollow");
                image_user_status.setImageDrawable(getResources().getDrawable(R.drawable.following_request));
            } else {
                txt_user_status.setText("Follow");
                image_user_status.setImageDrawable(getResources().getDrawable(R.drawable.follower_add_frnd));
            }

            String totallikes = newsFeedPostData.getPost_total_like();
            String totalDislikes = newsFeedPostData.getPost_total_dislike();
            float likeCount = 0;
            float dislikeCount = 0;
            float totalCount = 0;
            try {
                likeCount = Integer.parseInt(totallikes);
                dislikeCount = Integer.parseInt(totalDislikes);
                totalCount = likeCount + dislikeCount;
            } catch (Exception e) {

            }
            if (totalCount > 0) {
                float likePercentage = (likeCount * 100) / totalCount;
                float disLikePercentage = (dislikeCount * 100) / totalCount;

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, Float.parseFloat(String.valueOf(likePercentage)));
                params.gravity = Gravity.CENTER;
                txtViewProgressLike.setLayoutParams(params);
                txtViewProgressLike.setText(Math.round(likePercentage) + " %");
                params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, Float.parseFloat(String.valueOf(disLikePercentage)));
                params.gravity = Gravity.CENTER;
                txtViewProgressDisLike.setLayoutParams(params);
                txtViewProgressDisLike.setText(Math.round(disLikePercentage) + " %");
                /*like_dislike_layout.removeView(txtViewProgressLike);
                like_dislike_layout.removeView(txtViewProgressDisLike);
                like_dislike_layout.addView(txtViewProgressDisLike,params);
                like_dislike_layout.addView(txtViewProgressDisLike,params);*/
                like_dislike_layout.setGravity(Gravity.CENTER);
                //like_comment_layout.setLayoutParams(relativeLayoutParams);
            } else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, Float.parseFloat(String.valueOf(100)));
                params.gravity = Gravity.CENTER;
                txtViewProgressLike.setLayoutParams(params);
                txtViewProgressLike.setText("0%");
                /*params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, Float.parseFloat(String.valueOf(0)));
                params.gravity = Gravity.CENTER;
                txtViewProgressDisLike.setLayoutParams(params);
                txtViewProgressDisLike.setText(Math.round(disLikePercentage) + " %");*/
                /*like_dislike_layout.removeView(txtViewProgressLike);
                like_dislike_layout.removeView(txtViewProgressDisLike);
                like_dislike_layout.addView(txtViewProgressDisLike,params);
                like_dislike_layout.addView(txtViewProgressDisLike,params);*/
                like_dislike_layout.setGravity(Gravity.CENTER);
                //like_comment_layout.setLayoutParams(relativeLayoutParams);
            }

            Picasso.with(activity).load(URLFactory.imageUrl + newsFeedPostData.getUser_image())
                    .placeholder(R.drawable.profile_squre_default).into(img_user_pic);

            ll_more.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (newsFeedPostData.getData_type().equals("video")) {

                        if (newsFeedPostData.getUser_id().equals(userid)) {
                            openDialogWithMoreApp("Delete", null, position, rl_post_image);
                        } else {
                            openDialogWithMoreApp("Report inappropriate", null, position, rl_post_image);
                        }

                    } else if (newsFeedPostData.getData_type().equals("audio")) {
                        if (newsFeedPostData.getData_url().equals("")) {

                            if (newsFeedPostData.getUser_id().equals(userid)) {
                                openDialogWithMoreApp("Delete", "Save to Gallery", position, rl_post_image);
                            } else {
                                openDialogWithMoreApp("Report inappropriate", "Save to Gallery", position,
                                        rl_post_image);
                            }

                        } else {

                            if (newsFeedPostData.getUser_id().equals(userid)) {
                                openDialogWithMoreApp("Delete", null, position, rl_post_image);
                            } else {
                                openDialogWithMoreApp("Report inappropriate", null, position, rl_post_image);
                            }
                        }

                    }

                }
            });

            username.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String str_frndid = newsFeedPostData.getUser_id();
                    if (str_frndid.equals(userid)) {
                        activity.getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim,
                                        R.animator.back_anim_start, R.animator.back_anim_end)
                                .add(R.id.container, ProfileFragment.newInstance((CommonFragmentActivity) activity))
                                .addToBackStack(null).commit();
                    } else {
                        OtherUserProfileFragment fragment = new OtherUserProfileFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("frndid", str_frndid);
                        fragment.setArguments(bundle);
                        activity.getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim,
                                        R.animator.back_anim_start, R.animator.back_anim_end)
                                .add(R.id.container, fragment).addToBackStack(null).commit();
                    }

                }
            });
            img_user_pic.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String str_frndid = newsFeedPostData.getUser_id();
                    if (str_frndid.equals(userid)) {
                        activity.getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim,
                                        R.animator.back_anim_start, R.animator.back_anim_end)
                                .add(R.id.container, ProfileFragment.newInstance((CommonFragmentActivity) activity))
                                .addToBackStack(null).commit();
                    } else {
                        OtherUserProfileFragment fragment = new OtherUserProfileFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("frndid", str_frndid);
                        fragment.setArguments(bundle);
                        activity.getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim,
                                        R.animator.back_anim_start, R.animator.back_anim_end)
                                .add(R.id.container, fragment).addToBackStack(null).commit();
                    }

                }
            });
        }
    }

    //method to set the post as user's to favourite
    void setFavouritePost(String userid, String post_id, int fav_value, final int position) {
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings.add(ApplicationConstants.GetPostLikeAPIKeys.API_TYPE);
        al_signature_strings.add(ApplicationConstants.GetPostLikeAPIKeys.USERID_KEY);
        al_signature_strings.add(ApplicationConstants.GetPostLikeAPIKeys.POST_ID_KEY);
        al_signature_strings.add(ApplicationConstants.GetPostLikeAPIKeys.STATUS_KEY);
        al_signature_strings.add(ApplicationConstants.GetPostLikeAPIKeys.STATUS_FAVOURITE);

        String str_signature = SignatureCommonMenthods.getSignatureForAPI(al_signature_strings);
        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(ApplicationConstants.GetPostLikeAPIKeys.USERID_KEY, userid);
            rootJsonObj.put(ApplicationConstants.GetPostLikeAPIKeys.POST_ID_KEY, post_id);
            rootJsonObj.put(ApplicationConstants.GetPostLikeAPIKeys.STATUS_FAVOURITE, fav_value);
            rootJsonArr.put(rootJsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = null;
        try {
            url = URLFactory.GetPostLikeUrl() + "&signature=" + str_signature + "&data="
                    + URLEncoder.encode(rootJsonArr.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(6000);
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {

                    try {
                        JSONObject rootJsonObjObject = new JSONObject(response);
                        String strMsg = rootJsonObjObject.getString("msg");
                        String strStatus = rootJsonObjObject.getString("status");

                        if (strStatus.equals("true")) {
                            String like_status = rootJsonObjObject.getString("like_status");
                            String total_votes = rootJsonObjObject.getString("total_votes");
                            String is_vote = rootJsonObjObject.getString("is_vote");

                            System.out.println("position of row" + position);

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

            @SuppressWarnings("deprecation")
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
                super.onFailure(statusCode, headers, error, content);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

        });
    }

    //method to set the post like with user's id
    void setLikePost(String userid, String post_id, String like_value, final int position) {
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings.add(ApplicationConstants.GetPostLikeAPIKeys.API_TYPE);
        al_signature_strings.add(ApplicationConstants.GetPostLikeAPIKeys.USERID_KEY);
        al_signature_strings.add(ApplicationConstants.GetPostLikeAPIKeys.POST_ID_KEY);
        al_signature_strings.add(ApplicationConstants.GetPostLikeAPIKeys.STATUS_KEY);

        String str_signature = SignatureCommonMenthods.getSignatureForAPI(al_signature_strings);
        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(ApplicationConstants.GetPostLikeAPIKeys.USERID_KEY, userid);
            rootJsonObj.put(ApplicationConstants.GetPostLikeAPIKeys.POST_ID_KEY, post_id);
            rootJsonObj.put(ApplicationConstants.GetPostLikeAPIKeys.STATUS_KEY, like_value);
            rootJsonArr.put(rootJsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = null;
        try {
            url = URLFactory.GetPostLikeUrl() + "&signature=" + str_signature + "&data="
                    + URLEncoder.encode(rootJsonArr.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(6000);
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {

                    try {
                        JSONObject rootJsonObjObject = new JSONObject(response);
                        String strMsg = rootJsonObjObject.getString("msg");
                        String strStatus = rootJsonObjObject.getString("status");

                        if (strStatus.equals("true")) {

                            String like_status = rootJsonObjObject.getString("like_status");
                            String total_votes = rootJsonObjObject.getString("total_votes");
                            String is_vote = rootJsonObjObject.getString("is_vote");

                            System.out.println("position of row" + position);

                            // al.get(local_position).setIs_post_liked(
                            // like_status);
                            // al.get(local_position).setPost_total_like(
                            // total_votes);
                            // notifyDataSetChanged();

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

            @SuppressWarnings("deprecation")
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
                super.onFailure(statusCode, headers, error, content);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

        });
    }

    private void openDialogWithMoreApp(String string, String string2, final int position,
                                       final RelativeLayout rl_post_image) {

        pos = position;
        System.out.println("pos2222" + pos);
        final Dialog dialogMapMain = new Dialog(activity);
        dialogMapMain.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialogMapMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMapMain.setContentView(R.layout.dialog_more_option);
        dialogMapMain.getWindow().setGravity(Gravity.BOTTOM);
        dialogMapMain.setCanceledOnTouchOutside(true);

        LinearLayout ll_delete = (LinearLayout) dialogMapMain.findViewById(R.id.ll_delete);
        LinearLayout ll_sharefb = (LinearLayout) dialogMapMain.findViewById(R.id.ll_sharefb);
        LinearLayout ll_addPhoto = (LinearLayout) dialogMapMain.findViewById(R.id.ll_addPhoto);
        LinearLayout ll_cancel = (LinearLayout) dialogMapMain.findViewById(R.id.ll_cancel);
        TextView txtDelete = (TextView) dialogMapMain.findViewById(R.id.txtDelete);
        TextView txt_savetogallery = (TextView) dialogMapMain.findViewById(R.id.txt_savetogallery);

        ll_sharefb.setVisibility(View.GONE);

        if (data.get(position).getData_type().equals("video")) {
            ll_share.setVisibility(View.GONE);
            ll_sharefb.setVisibility(View.VISIBLE);
            if (data.get(position).getUser_id().equals(userid)) {
                txtDelete.setText(string);
            }
        } else if (data.get(position).getData_type().equals("audio")) {
            if (data.get(position).getData_url().equals("")) {
                if (data.get(position).getUser_id().equals(userid)) {
                    txtDelete.setText(string);
                    txt_savetogallery.setText(string2);
                } else {
                    txtDelete.setText(string);
                    txt_savetogallery.setText(string2);
                }
            } else {

                if (data.get(position).getUser_id().equals(userid)) {
                    txtDelete.setText(string);
                    ll_addPhoto.setVisibility(View.GONE);
                } else {
                    txtDelete.setText(string);
                    ll_addPhoto.setVisibility(View.GONE);
                }
            }
        }

			/*
             *
			 * Share single post to share activity page that redirect to
			 * facebook, twitter, flick, tumbler,,
			 */

        ll_share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                NewsFeedPostData newsFeedPostData = data.get(pos);
                if (newsFeedPostData.getData_type().equals("video")) {
                    shareVideo(newsFeedPostData);
                } else if (newsFeedPostData.getData_type().equals("audio")) {
                    shareImage(newsFeedPostData);
                }
                dialogMapMain.dismiss();

            }
        });

        ll_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String strUserId = data.get(pos).getUser_id();
                String strPostId = data.get(pos).getPost_id();

                if (data.get(pos).getData_type().equals("video")) {

                    if (data.get(pos).getUser_id().equals(userid)) {
                        if (commonMethods.getConnectivityStatus()) {
                            try {
                                commonMethods.DeleteSinglePost(
                                        commonMethods.getSinglePostRequestParmas(strUserId, strPostId));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            GlobalConfig.showToast(activity,
                                    activity.getResources().getString(R.string.internet_error_message));
                        }
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("userid", strUserId);
                        intent.putExtra("postid", strPostId);
                        intent.setClass(activity, ReportActivity.class);
                        fragment.startActivity(intent);

                    }
                } else if (data.get(pos).getData_type().equals("audio")) {
                    if (data.get(pos).getData_url().equals("")) {

                        if (data.get(pos).getUser_id().equals(userid)) {

                            if (commonMethods.getConnectivityStatus()) {
                                try {
                                    commonMethods.DeleteSinglePost(
                                            commonMethods.getSinglePostRequestParmas(strUserId, strPostId));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                GlobalConfig.showToast(activity,
                                        activity.getResources().getString(R.string.internet_error_message));
                            }
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra("userid", strUserId);
                            intent.putExtra("postid", strPostId);
                            intent.setClass(activity, ReportActivity.class);
                            fragment.startActivity(intent);

                        }

                    } else {

                        if (data.get(pos).getUser_id().equals(userid)) {
                            if (commonMethods.getConnectivityStatus()) {
                                try {
                                    commonMethods.DeleteSinglePost(
                                            commonMethods.getSinglePostRequestParmas(strUserId, strPostId));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                GlobalConfig.showToast(activity,
                                        activity.getResources().getString(R.string.internet_error_message));
                            }
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra("userid", strUserId);
                            intent.putExtra("postid", strPostId);
                            intent.setClass(activity, ReportActivity.class);
                            fragment.startActivity(intent);

                        }
                    }

                }

                dialogMapMain.dismiss();
            }
        });


        ll_addPhoto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (data.get(pos).getData_type().equalsIgnoreCase("audio")
                        || data.get(pos).getData_type().equalsIgnoreCase("video")) {

                    {

                        file_download(URLFactory.imageUrl + data.get(pos).getImage());
                        GlobalConfig.showToast(activity, "Photo Saved successfully");
                        dialogMapMain.dismiss();

                    }

                }

            }

            public void file_download(String uRl) {
                File direct = new File(Environment.getExternalStorageDirectory() + "/PetSutra");

                if (!direct.exists()) {
                    direct.mkdirs();
                }

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                DownloadManager mgr = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);

                Uri downloadUri = Uri.parse(uRl);
                DownloadManager.Request request = new DownloadManager.Request(downloadUri);

                request.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false).setTitle("PetSutra").setDescription("Wait Sometime.")
                        .setDestinationInExternalPublicDir("/PetSutra", timeStamp + ".jpg");

                mgr.enqueue(request);

            }
        });

        ll_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogMapMain.dismiss();
            }
        });

        dialogMapMain.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 2) && (resultCode == Activity.RESULT_OK) && (data != null)) {
            String str1 = data.getStringExtra("comment_count");
            String str2 = data.getStringExtra("comment");
            gsonParseNewsFeed.getPosts().get(commentCountPostion).setPost_total_comment(str1);

            System.out.println("rewsult............" + str2);

            Gson gson = new Gson();
            GsonParseCommentList gsonParseCommentList = gson.fromJson(str2.toString(), GsonParseCommentList.class);

            if (gsonParseCommentList.getComments().size() <= 3) {
                gsonParseNewsFeed.getPosts().get(commentCountPostion).getLast_three_comments().clear();
                gsonParseNewsFeed.getPosts().get(commentCountPostion).getLast_three_comments()
                        .addAll(gsonParseCommentList.getComments());
            } else {

                gsonParseNewsFeed.getPosts().get(commentCountPostion).getLast_three_comments().clear();
                for (int i = gsonParseCommentList.getComments().size() - 3; i < gsonParseCommentList.getComments()
                        .size(); i++) {

                    gsonParseNewsFeed.getPosts().get(commentCountPostion).getLast_three_comments()
                            .add(gsonParseCommentList.getComments().get(i));
                }

            }
            //this.newsfeedAdapter.notifyDataSetChanged();
        }

    }

    public void removeNotAvailbleLayout() {
        rl_loading_layout.setVisibility(View.GONE);
        notAvailableLayout.setVisibility(View.GONE);
    }


    // catches all the webservice response which has been called from this class and handles them according to the tasktype
    @Override
    public void OnComplete(APIResponse apiResponse) {
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            switch (TaskType) {
                case ApplicationConstants.TaskType.GETNEWSFEED:
                    System.out.println("Response NewsFeed" + apiResponse.getResponse());
                    if (apiResponse.getCode() == 200) {
                        rl_loading_layout.setVisibility(View.GONE);
                        notAvailableLayout.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        GsonParseNewsFeed localGsonParseNewsFeed;
                        if (gsonParseNewsFeed == null) {
                            gsonParseNewsFeed = gson.fromJson(apiResponse.getResponse(), GsonParseNewsFeed.class);
                            int_total_page = Integer.parseInt(gsonParseNewsFeed.getTotal_page());
                            if (gsonParseNewsFeed.getStatus().equals("true")) {
                                if (gsonParseNewsFeed.getPosts().size() > 0) {
                                    data = gsonParseNewsFeed.getPosts();
                                }
                                else {
                                    data = null;
                                }
                                if (data != null && data.size() > 0) {
                                    queriedPosts += 1;
                                    populateCardStack();
                                    if (pageNumberForService <= int_total_page) {
                                        pageNumberForService = (1 + pageNumberForService);
                                    }
                                    if (isFirstTime) {
                                        rl_main_layout.setVisibility(View.VISIBLE);
                                        onStartRefresh();
                                        isFirstTime = false;
                                    } else {
                                        rl_main_layout.setVisibility(View.VISIBLE);
                                        int totalRecords = Integer.parseInt(gsonParseNewsFeed.getTotal_records());
                                        double maxCall = totalRecords * 1.0 / 5;
                                        maxCallInt = (int) Math.ceil(maxCall);

                                        if (queriedPosts >= maxCallInt) {
                                            loadMoreStack = false;
                                        }
                                        imgbtn_refresh.clearAnimation();
                                    }
                                } else {
                                    notAvailableLayout.setVisibility(View.VISIBLE);
                                }
                            } else {
                                notAvailableLayout.setVisibility(View.VISIBLE);
                                GlobalConfig.showToast(activity, "Please try again");
                            }
                        } else {
                            localGsonParseNewsFeed = gson.fromJson(apiResponse.getResponse(), GsonParseNewsFeed.class);
                            int_total_page = Integer.parseInt(localGsonParseNewsFeed.getTotal_page());

                            if (localGsonParseNewsFeed.getStatus().equals("true")) {
                                gsonParseNewsFeed.getPosts().addAll(localGsonParseNewsFeed.getPosts());
                                //newsfeedAdapter.setData(gsonParseNewsFeed.getPosts());
                                data = localGsonParseNewsFeed.getPosts();
                                if (data.size() > 0) {
                                    queriedPosts += 1;
                                    populateCardStack();
                                    if (pageNumberForService <= int_total_page) {
                                        pageNumberForService = (1 + pageNumberForService);
                                    }
                                    int totalRecords = Integer.parseInt(localGsonParseNewsFeed.getTotal_records());
                                    double maxCall = totalRecords * 1.0 / 5;
                                    maxCallInt = (int) Math.ceil(maxCall);
                                    if (queriedPosts >= maxCallInt) {
                                        loadMoreStack = false;
                                    }
                                    rl_main_layout.setVisibility(View.VISIBLE);
                                }
                            } else {
                                GlobalConfig.showToast(activity, "Please try again");
                                imgbtn_refresh.clearAnimation();
                            }

                        }
                    }

                    imgbtn_refresh.clearAnimation();
                /*if (swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                }*/
                    break;

                case ApplicationConstants.TaskType.SETFOLLOWREQUEST:
                    if (apiResponse.getCode() == 200) {

                        try {
                            JSONObject rootJsonObjObject = new JSONObject(
                                    apiResponse.getResponse());
                            String strMsg = rootJsonObjObject.getString("msg");
                            String strStatus = rootJsonObjObject.getString("status");

                            if (strStatus.equals("true")) {

                            } else {
                                GlobalConfig.showToast(getActivity(), strMsg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        GlobalConfig.showToast(getActivity(), "Please try again");
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void performAction(NewsFeedPostData newsFeedPostData) {
        if (newsFeedPostData.getData_type().equals("audio")) {
            String videoPath = URLFactory.imageUrl + newsFeedPostData.getData_url();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoPath));
            intent.setDataAndType(Uri.parse(videoPath), "audio/*");
            activity.startActivity(intent);
        } else {
            String videoPath = URLFactory.imageUrl + newsFeedPostData.getData_url();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoPath));
            intent.setDataAndType(Uri.parse(videoPath), "video/*");
            activity.startActivity(intent);
        }
    }

    // posting picture to the user's FB account
    private void share(final String string) {
        if (AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired()) {

            Bundle params = new Bundle();
            params.putString("message", "￼‘IJBT – Do you like this ?\n" +
                    "Let me know what you think of my recent buy’");
            params.putString("picture", string);
            params.putString("link", string);
            GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(),
                    "/me/feed", params, HttpMethod.POST,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse graphResponse) {
                            if (graphResponse.getError() == null) {
                                GlobalConfig.showToast(getActivity(), " Successfully Posted on Facebook...");
                            } else {
                                if (graphResponse.getError().getErrorCode() == 200) {
                                    LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile,user_friends,publish_actions"));
                                } else {
                                    GlobalConfig.showToast(getActivity(), "Failed to post on Facebook..." + graphResponse.getRawResponse());
                                }
                            }
                        }
                    });
            request.executeAsync();

        } else
            LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile,user_friends,publish_actions"));
    }

    // method for caputre video facebook sharing
    private void shareTOFbVideo(final String string, final String videopath) {
        if (AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired()) {
            Bundle params = new Bundle();
            params.putString("message", "￼‘IJBT – Do you like this ?\n" +
                    "Let me know what you think of my recent buy’");
            params.putString("picture", string);
            params.putString("link", videopath);

            GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(),
                    "/me/feed", params, HttpMethod.POST,
                    new GraphRequest.Callback() {
                        Intent intent = new Intent();

                        @Override
                        public void onCompleted(GraphResponse graphResponse) {

                            if (graphResponse.getError() == null) {
                                GlobalConfig.showToast(getActivity(), " Successfully Posted on Facebook...");
                                intent.setClass(getActivity(), CommonFragmentActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                getActivity().finish();
                                getActivity().overridePendingTransition(0, 0);
                            } else {
                                if (graphResponse.getError().getErrorCode() == 200) {
                                    LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile,user_friends,publish_actions"));
                                } else {
                                    GlobalConfig.showToast(getActivity(), "Failed to post on Facebook...");
                                }
                            }

                        }
                    });
            request.executeAsync();

        } else {
            LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile,user_friends,publish_actions"));
        }

    }

    @Override
    public void onStartRefresh() {
        pageNumberForService = 1;
        queriedPosts = 0;
        gsonParseNewsFeed = null;
        PorfileCardAdapter adapter = new PorfileCardAdapter(new ArrayList<NewsFeedPostData>(), getActivity(), this);
        cardStack.setAdapter(adapter);
        llPostImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llPostImage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) cardStack.getLayoutParams();
                layoutParams.topMargin = llPostImage.getTop();
                layoutParams.leftMargin = llPostImage.getLeft();
                int height = llPostImage.getMeasuredHeight();
                int width = llPostImage.getMeasuredWidth();
                int size = 0;
                if (width > height) {
                    size = height;
                } else {
                    size = width;
                }
                layoutParams.height = size;
                layoutParams.width = size;
                cardStack.setLayoutParams(layoutParams);
                cardStack.invalidate();
                cardStack.setLeftImage(R.id.left_image);
                cardStack.setRightImage(R.id.right_image);
                swipeFrameLayout.updateViewLayout(cardStack, layoutParams);
                if (commonMethods.getConnectivityStatus()) {
                    getNewsFeeds(pageNumberForService);
                } else {
                    GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        pageNumberForService = 1;
        queriedPosts = 0;
        PorfileCardAdapter adapter = new PorfileCardAdapter(new ArrayList<NewsFeedPostData>(), getActivity(), this);
        cardStack.setAdapter(adapter);
        llPostImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llPostImage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) cardStack.getLayoutParams();
                layoutParams.topMargin = llPostImage.getTop();
                layoutParams.leftMargin = llPostImage.getLeft();
                int height = llPostImage.getMeasuredHeight();
                int width = llPostImage.getMeasuredWidth();
                int size = 0;
                if (width > height) {
                    size = height;
                } else {
                    size = width;
                }
                layoutParams.height = size;
                layoutParams.width = size;
                cardStack.setLayoutParams(layoutParams);
                cardStack.invalidate();
                cardStack.setLeftImage(R.id.left_image);
                cardStack.setRightImage(R.id.right_image);
                swipeFrameLayout.updateViewLayout(cardStack, layoutParams);
                if (commonMethods.getConnectivityStatus()) {
                    getNewsFeeds(pageNumberForService);
                } else {
                    GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
                }
            }
        });
    }
}
