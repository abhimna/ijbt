package com.pluggdd.ijbt.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pluggdd.ijbt.CommonFragmentActivity;
import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.footerfragment.ProfileFragment;
import com.pluggdd.ijbt.network.APIResponse;
import com.pluggdd.ijbt.network.IJBTResponseController;
import com.pluggdd.ijbt.util.ApplicationConstants;
import com.pluggdd.ijbt.util.CommonMethods;
import com.pluggdd.ijbt.util.GlobalConfig;
import com.pluggdd.ijbt.util.SignatureCommonMenthods;
import com.pluggdd.ijbt.util.URLFactory;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProductDetailsFragment extends Fragment implements IJBTResponseController {

    private View view;
    private Activity activity;
    private CommonMethods commonMethods;
    private NewsFeedPostData newsFeedPostData;
    private GsonParseProfile.Userdetail.Posts.Post postData;
    private TextView username;
    private TextView txt_msg;
    private TextView tv_agoday;
    private RelativeLayout rl_post_image;
    private LinearLayout ll_more;
    private CircleImageView img_user_pic;
    private ImageView img_favourite;
    private LinearLayout ll_fav;
    private TextView tv_price;
    private TextView tv_location;
    private TextView txtViewProgressLike;
    private TextView txtViewProgressDisLike;
    private ImageView img_like_double, img_post_image, img_type;
    ProgressBar progressbar;
    LinearLayout like_dislike_layout;
    RelativeLayout like_comment_layout;
    private String userid, userName;
    private LinearLayout ll_app_share;
    ImageView image_user_status;
    TextView txt_user_status;
    private LinearLayout ll_user_status;

    public static ProductDetailsFragment newInstance(NewsFeedPostData newsFeedPostData) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", newsFeedPostData);
        fragment.setArguments(bundle);
        return fragment;
    }


    public static ProductDetailsFragment newInstance(GsonParseProfile.Userdetail.Posts.Post postData) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", postData);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ProductDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.product_details_fragment, container, false);
        findViews();
        if (getArguments().getParcelable("data") instanceof NewsFeedPostData) {
            newsFeedPostData = getArguments().getParcelable("data");
            setBodyUI();
        } else {
            postData = getArguments().getParcelable("data");
            setBodyUIPost();
        }
        return view;
    }

    private void findViews() {
        activity = getActivity();
        commonMethods = new CommonMethods(activity, this);
        defaultActionBarProcess();
        userid = activity.getSharedPreferences("prefs_login", Activity.MODE_PRIVATE).getString("userid", "");
        userName = activity.getSharedPreferences("prefs_login", Activity.MODE_PRIVATE).getString("username", "");

        img_like_double = (ImageView) view.findViewById(R.id.img_like_double);
        img_post_image = (ImageView) view.findViewById(R.id.img_post_image);
        img_type = (ImageView) view.findViewById(R.id.img_type);
        progressbar = (ProgressBar) view.findViewById(R.id.progress);
        username = (TextView) view.findViewById(R.id.username);
        txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        tv_agoday = (TextView) view.findViewById(R.id.tv_agoday);
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        tv_price = (TextView) view.findViewById(R.id.tv_price);
        ll_fav = (LinearLayout) view.findViewById(R.id.ll_fav);
        ll_app_share = (LinearLayout) view.findViewById(R.id.ll_share);
        rl_post_image = (RelativeLayout) view.findViewById(R.id.rl_post_image);
        ll_more = (LinearLayout) view.findViewById(R.id.ll_more);
        img_user_pic = (CircleImageView) view.findViewById(R.id.img_user_pic);
        img_favourite = (ImageView) view.findViewById(R.id.img_favourite);
        like_dislike_layout = (LinearLayout) view.findViewById(R.id.like_dislike_layout);
        like_comment_layout = (RelativeLayout) view.findViewById(R.id.like_comment_layout);
        txtViewProgressLike = (TextView) view.findViewById(R.id.txtViewProgressLike);
        txtViewProgressDisLike = (TextView) view.findViewById(R.id.txtViewProgressDisLike);
        image_user_status = (ImageView) view.findViewById(R.id.image_user_status);
        txt_user_status = (TextView) view.findViewById(R.id.txt_user_status);
        ll_user_status = (LinearLayout) view.findViewById(R.id.ll_user_status);

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setBodyUI() {
        username.setText(newsFeedPostData.getUser_name());
        tv_price.setText(newsFeedPostData.getPrice() + " " + newsFeedPostData.getCurrency());
        tv_location.setText(newsFeedPostData.getPlacebought());
        tv_agoday.setText(newsFeedPostData.getTime_ago());
        if (newsFeedPostData.getIs_post_favourate().equalsIgnoreCase("1")) {
            img_favourite.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart));
        } else {
            img_favourite.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart_temp));
        }
        ll_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newsFeedPostData.getIs_post_favourate().equalsIgnoreCase("0")) {
                    setFavouritePost(userid, newsFeedPostData.getPost_id(), 1);
                    newsFeedPostData.setIs_post_favourate("1");
                    img_favourite.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart));
                } else {
                    setFavouritePost(userid, newsFeedPostData.getPost_id(), 0);
                    newsFeedPostData.setIs_post_favourate("0");
                    img_favourite.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart_temp));
                }
            }
        });
        ll_user_status.setVisibility(View.VISIBLE);
        if (newsFeedPostData.getFollow_status().equalsIgnoreCase("1")) {
            txt_user_status.setText("UnFollow");
            image_user_status.setImageDrawable(getResources().getDrawable(R.drawable.following_request));
        } else {
            txt_user_status.setText("Follow");
            image_user_status.setImageDrawable(getResources().getDrawable(R.drawable.follower_add_frnd));
        }
        ll_user_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newsFeedPostData.getFollow_status().equalsIgnoreCase("0")) {
                    txt_user_status.setText("UnFollow");
                    image_user_status.setImageDrawable(getResources().getDrawable(R.drawable.following_request));
                    newsFeedPostData.setFollow_status("1");
                    setFollowRequest(newsFeedPostData.getUser_id(), "1");
                } else {
                    txt_user_status.setText("Follow");
                    image_user_status.setImageDrawable(getResources().getDrawable(R.drawable.follower_add_frnd));
                    newsFeedPostData.setFollow_status("0");
                    setFollowRequest(newsFeedPostData.getUser_id(), "0");
                }
            }
        });

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
            like_dislike_layout.setGravity(Gravity.CENTER);
        }

        Picasso.with(activity).load(URLFactory.imageUrl + newsFeedPostData.getUser_image())
                .placeholder(R.drawable.profile_squre_default).into(img_user_pic);

        ll_app_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newsFeedPostData.getData_type().equals("video")) {
                    shareVideo(newsFeedPostData);
                } else if (newsFeedPostData.getData_type().equals("audio")) {
                    shareImage(newsFeedPostData);
                }
            }
        });

        ll_more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (newsFeedPostData.getData_type().equals("video")) {

                    if (newsFeedPostData.getUser_id().equals(userid)) {
                        openDialogWithMoreApp("Delete", null, rl_post_image);
                    } else {
                        openDialogWithMoreApp("Report inappropriate", null, rl_post_image);
                    }

                } else if (newsFeedPostData.getData_type().equals("audio")) {
                    if (newsFeedPostData.getData_url().equals("")) {

                        if (newsFeedPostData.getUser_id().equals(userid)) {
                            openDialogWithMoreApp("Delete", "Save to Gallery", rl_post_image);
                        } else {
                            openDialogWithMoreApp("Report inappropriate", "Save to Gallery",
                                    rl_post_image);
                        }

                    } else {

                        if (newsFeedPostData.getUser_id().equals(userid)) {
                            openDialogWithMoreApp("Delete", null, rl_post_image);
                        } else {
                            openDialogWithMoreApp("Report inappropriate", null, rl_post_image);
                        }
                    }

                }

            }
        });

        username.setOnClickListener(new View.OnClickListener() {

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
        img_user_pic.setOnClickListener(new View.OnClickListener() {

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

        if (newsFeedPostData.getData_type().equals("audio")) {

            if (newsFeedPostData.getData_url().equals("")) {
                img_type.setVisibility(View.INVISIBLE);
            } else {
                img_type.setVisibility(View.VISIBLE);
                img_type.setBackgroundResource(R.drawable.play_phone);
            }

            Picasso.with(activity).load(URLFactory.imageUrl + newsFeedPostData.getImage())
                    // .placeholder(R.drawable.placeholder_looka)
                    .into(img_post_image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressbar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                        }
                    });
        } else if (newsFeedPostData.getData_type().equals("video")) {
            /*Bitmap bm = ThumbnailUtils.createVideoThumbnail(URLFactory.imageUrl + newsFeedPostData.getImage(), MediaStore.Images.Thumbnails.MINI_KIND);
            img_post_image.setImageBitmap(bm);
            img_type.setVisibility(View.VISIBLE);
            img_type.setBackgroundResource(R.drawable.play_oval);*/
            Picasso.with(activity).load(URLFactory.imageUrl + newsFeedPostData.getImage())
                    // .placeholder(R.drawable.placeholder_looka)
                    .into(img_post_image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            img_type.setVisibility(View.VISIBLE);
                            img_type.setBackgroundResource(R.drawable.play_oval);
                            progressbar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                        }
                    });
        } else {
            Picasso.with(activity).load(URLFactory.imageUrl + newsFeedPostData.getImage())
                    // .placeholder(R.drawable.placeholder_looka)
                    .into(img_post_image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressbar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                        }
                    });
        }
        img_type.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                performAction(newsFeedPostData);
            }
        });
    }

    private void setFollowRequest(String other_userid, String str_status) {
        if (commonMethods.getConnectivityStatus()) {
            try {
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

    void setFavouritePost(String userid, String post_id, int fav_value) {
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

    @SuppressLint("SetJavaScriptEnabled")
    private void setBodyUIPost() {

        username.setText(postData.getUser_name());
        tv_price.setText(postData.getPrice() + " " + postData.getCurrency());
        tv_location.setText(postData.getPlacebought());
        tv_agoday.setText(postData.getTime_ago());
        ll_user_status.setVisibility(View.GONE);
        ll_fav.setVisibility(View.GONE);
        if (postData.getIs_post_favourate().equalsIgnoreCase("1")) {
            img_favourite.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart));
        } else {
            img_favourite.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart_temp));
        }
        if (postData.getFollow_status().equalsIgnoreCase("1")) {
            txt_user_status.setText("UnFollow");
            image_user_status.setImageDrawable(getResources().getDrawable(R.drawable.following_request));
        } else {
            txt_user_status.setText("Follow");
            image_user_status.setImageDrawable(getResources().getDrawable(R.drawable.follower_add_frnd));
        }

        String totallikes = postData.getPost_total_like();
        String totalDislikes = postData.getPost_total_dislike();
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
        }

        Picasso.with(activity).load(URLFactory.imageUrl + postData.getUser_image())
                .placeholder(R.drawable.profile_squre_default).into(img_user_pic);


        ll_app_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postData.getData_type().equals("video")) {
                    //shareTOFbVideo(URLFactory.imageUrl + postData.getThumb_image(), URLFactory.imageUrl + postData.getImage());
                    shareVideo(postData);
                } else if (postData.getData_type().equals("audio")) {
                    //share(URLFactory.imageUrl + postData.getImage());
                    shareImage(postData);
                }
            }
        });

        ll_more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (postData.getData_type().equals("video")) {

                    if (postData.getUser_id().equals(userid)) {
                        openDialogWithMoreApp("Delete", null, rl_post_image);
                    } else {
                        openDialogWithMoreApp("Report inappropriate", null, rl_post_image);
                    }

                } else if (postData.getData_type().equals("audio")) {
                    if (postData.getData_url().equals("")) {

                        if (postData.getUser_id().equals(userid)) {
                            openDialogWithMoreApp("Delete", "Save to Gallery", rl_post_image);
                        } else {
                            openDialogWithMoreApp("Report inappropriate", "Save to Gallery",
                                    rl_post_image);
                        }

                    } else {

                        if (postData.getUser_id().equals(userid)) {
                            openDialogWithMoreApp("Delete", null, rl_post_image);
                        } else {
                            openDialogWithMoreApp("Report inappropriate", null, rl_post_image);
                        }
                    }

                }

            }
        });

        username.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String str_frndid = postData.getUser_id();
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
        img_user_pic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String str_frndid = postData.getUser_id();
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

        if (postData.getData_type().equals("audio")) {

            if (postData.getData_url().equals("")) {
                img_type.setVisibility(View.INVISIBLE);
            } else {
                img_type.setVisibility(View.VISIBLE);
                img_type.setBackgroundResource(R.drawable.play_phone);
            }

            Picasso.with(activity).load(URLFactory.imageUrl + postData.getImage())
                    // .placeholder(R.drawable.placeholder_looka)
                    .into(img_post_image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressbar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                        }
                    });
        } else if (postData.getData_type().equals("video")) {
            /*Bitmap bm = ThumbnailUtils.createVideoThumbnail(URLFactory.imageUrl + newsFeedPostData.getImage(), MediaStore.Images.Thumbnails.MINI_KIND);
            img_post_image.setImageBitmap(bm);
            img_type.setVisibility(View.VISIBLE);
            img_type.setBackgroundResource(R.drawable.play_oval);*/
            Picasso.with(activity).load(URLFactory.imageUrl + postData.getImage())
                    // .placeholder(R.drawable.placeholder_looka)
                    .into(img_post_image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            img_type.setVisibility(View.VISIBLE);
                            img_type.setBackgroundResource(R.drawable.play_oval);
                            progressbar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                        }
                    });
        } else {
            Picasso.with(activity).load(URLFactory.imageUrl + postData.getImage())
                    // .placeholder(R.drawable.placeholder_looka)
                    .into(img_post_image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressbar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                        }
                    });
        }
        img_type.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                performAction(newsFeedPostData);
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

    private void shareVideo(GsonParseProfile.Userdetail.Posts.Post newsFeedPostData) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "IJBT - check what your friend just bought \n\n" +
                URLFactory.imageUrl + newsFeedPostData.getData_url() + "\n" + "\n" + " App Link: https://play.google.com/store/apps/details?id=com.pluggdd.ijbt");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent,
                "Share Video URL"));
    }

    public void shareImage(GsonParseProfile.Userdetail.Posts.Post newsFeedPostData) {
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

    private void openDialogWithMoreApp(String string, String string2,
                                       final RelativeLayout rl_post_image) {

        final Dialog dialogMapMain = new Dialog(activity);
        dialogMapMain.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialogMapMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMapMain.setContentView(R.layout.dialog_more_option);
        dialogMapMain.getWindow().setGravity(Gravity.BOTTOM);
        dialogMapMain.setCanceledOnTouchOutside(true);

        LinearLayout ll_delete = (LinearLayout) dialogMapMain.findViewById(R.id.ll_delete);
        LinearLayout ll_sharefb = (LinearLayout) dialogMapMain.findViewById(R.id.ll_sharefb);
        LinearLayout ll_cancel = (LinearLayout) dialogMapMain.findViewById(R.id.ll_cancel);
        TextView txtDelete = (TextView) dialogMapMain.findViewById(R.id.txtDelete);
        //LinearLayout ll_share = (LinearLayout) dialogMapMain.findViewById(R.id.ll_sharetwitter);
        // LinearLayout ll_shareTwitter = (LinearLayout) dialogMapMain
        // .findViewById(R.id.ll_sharetwitter);
        LinearLayout ll_addPhoto = (LinearLayout) dialogMapMain.findViewById(R.id.ll_addPhoto);
        TextView txt_savetogallery = (TextView) dialogMapMain.findViewById(R.id.txt_savetogallery);

        if (newsFeedPostData!=null) {
            newsFeedPostData = getArguments().getParcelable("data");
            txtDelete.setVisibility(View.GONE);
            if (newsFeedPostData.getData_type().equals("video")) {
                txtDelete.setVisibility(View.GONE);
                ll_sharefb.setVisibility(View.GONE);
            } else if (newsFeedPostData.getData_type().equals("audio")) {
                txtDelete.setVisibility(View.GONE);
                ll_sharefb.setVisibility(View.GONE);
            }
        } else {
            postData = getArguments().getParcelable("data");
            txtDelete.setVisibility(View.GONE);
            ll_sharefb.setVisibility(View.GONE);
        }

        ll_addPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (newsFeedPostData != null) {
                    if (newsFeedPostData.getData_type().equals("video")) {
                        file_download(URLFactory.imageUrl + newsFeedPostData.getImage());
                        GlobalConfig.showToast(activity, "Photo Saved successfully");
                        dialogMapMain.dismiss();
                    } else if (newsFeedPostData.getData_type().equals("audio")) {
                        file_download(URLFactory.imageUrl + newsFeedPostData.getImage());
                        GlobalConfig.showToast(activity, "Photo Saved successfully");
                        dialogMapMain.dismiss();
                    }
                } else {
                    if (postData.getData_type().equals("video")) {
                        file_download(URLFactory.imageUrl + postData.getImage());
                        GlobalConfig.showToast(activity, "Photo Saved successfully");
                        dialogMapMain.dismiss();
                    } else if (postData.getData_type().equals("audio")) {
                        file_download(URLFactory.imageUrl + postData.getImage());
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

        ll_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogMapMain.dismiss();
            }
        });

        dialogMapMain.show();
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
        Middle_text.setText("Product Details");

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

    @Override
    public void OnComplete(APIResponse apiResponse) {
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

    }

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
}
