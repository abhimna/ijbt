package com.pluggdd.ijbt.footerfragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pluggdd.ijbt.CommonFragmentActivity;
import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.adapter.MyFavouriteLikeAdapter;
import com.pluggdd.ijbt.adapter.MyUploadsAdapter;
import com.pluggdd.ijbt.fragment.FollowerFollowingFragment;
import com.pluggdd.ijbt.fragment.ProductDetailsFragment;
import com.pluggdd.ijbt.network.APIResponse;
import com.pluggdd.ijbt.network.MyIJBTResponseController;
import com.pluggdd.ijbt.util.ApplicationConstants;
import com.pluggdd.ijbt.util.Comman;
import com.pluggdd.ijbt.util.CommonMethods;
import com.pluggdd.ijbt.util.DataPassed;
import com.pluggdd.ijbt.util.EndlessRecyclerOnScrollListener;
import com.pluggdd.ijbt.util.GlobalConfig;
import com.pluggdd.ijbt.util.OnProductDeletedListener;
import com.pluggdd.ijbt.util.RecyclerItemClickListener;
import com.pluggdd.ijbt.util.SelctedLineareLayout;
import com.pluggdd.ijbt.util.URLFactory;
import com.pluggdd.ijbt.vo.CircleTransform;
import com.pluggdd.ijbt.vo.GsonParseNewsFeed;
import com.pluggdd.ijbt.vo.GsonParseProfile;
import com.pluggdd.ijbt.vo.NewsFeedPostData;
import com.pluggdd.ijbt.vo.TouchImage;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/*
* This class is one of the footer class avaialble from the home screen.
* This class is a user profile screen where he/she can she the post they posted/liked/favourite added
* */
@SuppressLint({"NewApi", "InflateParams"})
public class ProfileFragment extends Fragment
        implements OnClickListener, MyIJBTResponseController, OnProductDeletedListener, SwipeRefreshLayout.OnRefreshListener {

    private static Context context;
    private View view;
    private Activity activity;
    private NestedScrollView root;
    private LinearLayout llfolower;
    private LinearLayout llfollowing;
    private String userid;
    private CommonMethods commonMethods;
    private TextView txt_username;
    private TextView tv_Follower;
    private TextView txt_Post;
    private TextView tv_Following;
    private TextView txt_username_down;

    private ImageView img_userprofile_pic;
    private MyUploadsAdapter myProfilePostsAdapter;
    private MyFavouriteLikeAdapter myLikesPostsAdapter;
    private MyFavouriteLikeAdapter myFavouritesPostsAdapter;
    private Bundle bundle;
    DataPassed dataPassed;
    private FragmentManager fragmentManager;
    GsonParseProfile gsonParseProfile;
    GsonParseNewsFeed gsonParseFavourites;
    GsonParseNewsFeed gsonParseLikes;
    int int_post_total_page;
    private int postPageNumberForService = 1;
    private int favPageNumberForService = 1;
    private int likePageNumberForService = 1;
    private String post_type = "audio";
    private LinearLayout ll_post;
    private RelativeLayout ll_root;
    private Display mDisplay;
    private RecyclerView recyclerViewPosts;
    private RecyclerView recyclerViewFavourites;
    private RecyclerView recyclerViewLikes;
    private TextView empty_view_post;
    private TextView empty_view_fav;
    private TextView empty_view_like;
    private ArrayList<GsonParseProfile.Userdetail.Posts.Post> profileDataSet;
    private LinearLayoutManager mLayoutManagerPost;
    private LinearLayoutManager mLayoutManagerFavourites;
    private LinearLayoutManager mLayoutManagerLikes;
    private ArrayList<NewsFeedPostData> favouriteDataSet;
    private int int_fav_total_page;
    private int int_like_total_page;
    private ArrayList<NewsFeedPostData> likesDataSet;
    private boolean shouldRefresh = false;
    SwipeRefreshLayout swipeRefresh;

    public static ProfileFragment newInstance(CommonFragmentActivity commonFragmentActivity) {
        ProfileFragment fragment = new ProfileFragment(commonFragmentActivity);
        context = commonFragmentActivity;
        return fragment;
    }

    public ProfileFragment(CommonFragmentActivity commonFragmentActivity) {
        this.activity = commonFragmentActivity;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        setBodyUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        commonMethods.ActionBarProcessForMiddletext("Profile");
        postPageNumberForService = 1;
        favPageNumberForService = 1;
        likePageNumberForService = 1;
        gsonParseProfile = null;
        gsonParseLikes = null;
        gsonParseFavourites = null;
        getProfileData();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setBodyUI() {
        activity = getActivity();
        bundle = new Bundle();
        fragmentManager = getFragmentManager();
        commonMethods = new CommonMethods(activity, this);
        // commonMethods.actionBarProcessProfile(bundle);
        userid = activity.getSharedPreferences("prefs_login", Activity.MODE_PRIVATE).getString("userid", "");

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);
        root = (NestedScrollView) view.findViewById(R.id.scroll);
        // find textview and imageview
        mDisplay = activity.getWindowManager().getDefaultDisplay();
        txt_username = (TextView) view.findViewById(R.id.txt_username);
        tv_Follower = (TextView) view.findViewById(R.id.tv_Follower);
        txt_Post = (TextView) view.findViewById(R.id.tv_post);

        tv_Following = (TextView) view.findViewById(R.id.tv_Following);
        // txt_username_down = (TextView) grid_header
        // .findViewById(R.id.txt_username_down);

        img_userprofile_pic = (ImageView) view.findViewById(R.id.img_userprofile_pic);

        // find linearlayout
        ll_post = (LinearLayout) view.findViewById(R.id.Post);
        llfolower = (LinearLayout) view.findViewById(R.id.llfolower);
        llfollowing = (LinearLayout) view.findViewById(R.id.llfollowing);
        ll_root = (RelativeLayout) view.findViewById(R.id.root);
        empty_view_post = (TextView) view.findViewById(R.id.empty_view_post);
        empty_view_fav = (TextView) view.findViewById(R.id.empty_view_fav);
        empty_view_like = (TextView) view.findViewById(R.id.empty_view_like);

        recyclerViewPosts = (RecyclerView) view.findViewById(R.id.post_recycler_view);
        recyclerViewFavourites = (RecyclerView) view.findViewById(R.id.favourites_recycler_view);
        recyclerViewLikes = (RecyclerView) view.findViewById(R.id.likes_recycler_view);

        img_userprofile_pic.setOnClickListener(this);
        ll_post.setOnClickListener(this);
        llfolower.setOnClickListener(this);
        llfollowing.setOnClickListener(this);
        /*getLikesData();
        getFavouritesData();*/
    }

    private void getFavouritesData() {
        mLayoutManagerFavourites
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFavourites.setLayoutManager(mLayoutManagerFavourites);
        recyclerViewFavourites.hasFixedSize();
        recyclerViewFavourites.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsFeedPostData newsFeedPostData = favouriteDataSet.get(position);
                activity.getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim,
                                R.animator.back_anim_start, R.animator.back_anim_end)
                        .replace(R.id.container, ProductDetailsFragment.newInstance(newsFeedPostData))
                        .addToBackStack(null).commit();
            }
        }));
        recyclerViewFavourites.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManagerFavourites) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (page < int_fav_total_page) {
                    callSecondTimeFavWebServices(favPageNumberForService);
                }
            }

            private void callSecondTimeFavWebServices(int postPageNumberForService) {
                if (commonMethods.getConnectivityStatus()) {
                    try {
                        commonMethods.GetFavouritesPost(
                                commonMethods.getProfileRequestParmas(userid, userid, "" + postPageNumberForService, post_type, ""),
                                false, ApplicationConstants.TaskType.GET_FAVOURITES_POST);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
                }
            }
        });

        if (commonMethods.getConnectivityStatus()) {
            try {
                gsonParseFavourites = null;
                commonMethods.GetFavouritesPost(
                        commonMethods.getProfileRequestParmas(userid, userid, "" + favPageNumberForService, post_type, ""),
                        false, ApplicationConstants.TaskType.GET_FAVOURITES_POST);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
        }
    }

    private void getLikesData() {
        mLayoutManagerLikes
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewLikes.setLayoutManager(mLayoutManagerLikes);
        recyclerViewLikes.hasFixedSize();
        recyclerViewLikes.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsFeedPostData newsFeedPostData = likesDataSet.get(position);
                activity.getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim,
                                R.animator.back_anim_start, R.animator.back_anim_end)
                        .replace(R.id.container, ProductDetailsFragment.newInstance(newsFeedPostData))
                        .addToBackStack(null).commit();
            }
        }));
        recyclerViewLikes.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManagerLikes) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (page < int_like_total_page) {
                    callSecondTimeLikeWebServices(likePageNumberForService);
                }
            }

            private void callSecondTimeLikeWebServices(int postPageNumberForService) {
                if (commonMethods.getConnectivityStatus()) {
                    try {
                        commonMethods.GetLikesPost(
                                commonMethods.getProfileRequestParmas(userid, userid, "" + postPageNumberForService, post_type, ""),
                                false, ApplicationConstants.TaskType.GET_LIKES_POST);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
                }
            }
        });

        if (commonMethods.getConnectivityStatus()) {
            try {
                gsonParseLikes = null;
                commonMethods.GetLikesPost(
                        commonMethods.getProfileRequestParmas(userid, userid, "" + likePageNumberForService, post_type, ""),
                        false, ApplicationConstants.TaskType.GET_LIKES_POST);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
        }
    }

    private void getProfileData() {
        mLayoutManagerPost
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPosts.setLayoutManager(mLayoutManagerPost);
        recyclerViewPosts.hasFixedSize();

        recyclerViewPosts.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManagerPost) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (page < int_post_total_page) {
                    callSecondTimePostWebServices(postPageNumberForService);
                }
            }

            private void callSecondTimePostWebServices(int postPageNumberForService) {
                if (commonMethods.getConnectivityStatus()) {
                    try {
                        commonMethods.GetProfileUser(
                                commonMethods.getProfileRequestParmas(userid, userid, "" + postPageNumberForService, post_type, ""),
                                false, ApplicationConstants.TaskType.GET_PROFILE_TASK);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
                }
            }
        });

        if (commonMethods.getConnectivityStatus()) {
            try {
                gsonParseProfile = null;
                commonMethods.GetProfileUser(
                        commonMethods.getProfileRequestParmas(userid, userid, "" + postPageNumberForService, post_type, ""),
                        false, ApplicationConstants.TaskType.GET_PROFILE_TASK);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
        }
    }

    @Override
    public void onClick(View v) {
        FollowerFollowingFragment fragment;
        switch (v.getId()) {

            case R.id.img_userprofile_pic:

                System.out.println("enter into Click" + R.id.img_userprofile_pic);
                final Dialog nagDialog = new Dialog(activity);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nagDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                nagDialog.setCancelable(false);
                nagDialog.setContentView(R.layout.preview_full_image);
                Button btnClose = (Button) nagDialog.findViewById(R.id.btnIvClose);
                TouchImage ivPreview = (TouchImage) nagDialog.findViewById(R.id.imageView1);
                final ProgressBar pb = (ProgressBar) nagDialog.findViewById(R.id.progress1);
                Picasso.with(activity).load(URLFactory.imageUrl + gsonParseProfile.getUserdetail().getUser_image())
                        .networkPolicy(NetworkPolicy.OFFLINE).error(R.drawable.user_pic_default).into(ivPreview, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        pb.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                    }
                });
                RelativeLayout rl = (RelativeLayout) nagDialog.findViewById(R.id.rl_root_preview);

                rl.getLayoutParams().width = mDisplay.getWidth();

                btnClose.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        nagDialog.dismiss();
                    }
                });

                ivPreview.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        nagDialog.dismiss();

                    }
                });
                nagDialog.show();

                break;

            case R.id.Post:
/*
            int index = gv_images.getFirstVisiblePosition();

			System.out.println("index====>" + index);

			gv_images.smoothScrollToPosition(index + 20);*/

                break;
            case R.id.llfolower:

                if (gsonParseProfile != null) {
                    if (Integer.parseInt(gsonParseProfile.getUserdetail().getTotalfollower()) > 0) {
                        Comman.forStoreHeaderFollow = "Followers";
                        bundle.putString("userid", userid);
                        fragment = new FollowerFollowingFragment(Comman.forStoreHeaderFollow);
                        fragment.setArguments(bundle);
                        fragmentManager.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
                    }
                }

                break;

            case R.id.llfollowing:
                if (gsonParseProfile != null) {
                    if (Integer.parseInt(gsonParseProfile.getUserdetail().getTotalfollowing()) > 0) {
                        Comman.forStoreHeaderFollow = "Following";
                        bundle.putString("userid", userid);
                        fragment = new FollowerFollowingFragment(Comman.forStoreHeaderFollow);
                        fragment.setArguments(bundle);
                        fragmentManager.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
                    }
                }
                break;

            /*case R.id.ll_images:

                if (ll_images_video.getselected() == 1) {
                    pageNumberForService = 1;
                    ll_images_video.setselected(0);
                    post_type = "audio";
                    if (commonMethods.getConnectivityStatus()) {
                        try {
                            gsonParseProfile = null;
                            commonMethods.GetProfileUser(commonMethods.getProfileRequestParmas(userid, userid,
                                    "" + pageNumberForService, post_type, ""), true);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
                    }
                }

                break;

            case R.id.ll_video:
                if (ll_images_video.getselected() == 0) {
                    ll_images_video.setselected(1);
                    post_type = "video";
                    pageNumberForService = 1;
                    if (commonMethods.getConnectivityStatus()) {
                        try {
                            gsonParseProfile = null;
                            commonMethods.GetProfileUser(commonMethods.getProfileRequestParmas(userid, userid,
                                    "" + pageNumberForService, post_type, ""), true);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
                    }
                }
                break;*/


            default:

        }

    }

    // catches all the webservice response which has been called from this class and handles them according to the tasktype
    @Override
    public void OnComplete(APIResponse apiResponse, int TaskType) {
        switch (TaskType) {
            case ApplicationConstants.TaskType.GET_PROFILE_TASK:
                System.out.println("Response get login profile" + apiResponse.getResponse());
                if (apiResponse.getCode() == 200) {

                    Gson gson = new Gson();

                    if (gsonParseProfile == null) {

                        gsonParseProfile = gson.fromJson(apiResponse.getResponse(), GsonParseProfile.class);
                        int_post_total_page = Integer.parseInt(gsonParseProfile.getUserdetail().getPosts().getTotal_page());

                        if (gsonParseProfile.getStatus().equals("true")) {
                            root.setVisibility(View.VISIBLE);
                            activity.findViewById(R.id.llfooter).setVisibility(View.VISIBLE);
                            txt_username.setText(gsonParseProfile.getUserdetail().getUser_name());
                            tv_Follower.setText(gsonParseProfile.getUserdetail().getTotalfollower() + " " + "Follower");
                            tv_Following.setText(gsonParseProfile.getUserdetail().getTotalfollowing() + " " + "Following");

                            int total_post;
                            total_post = Integer.parseInt(gsonParseProfile.getUserdetail().getPosts().getTotal_image_data())
                                    + Integer.parseInt(gsonParseProfile.getUserdetail().getPosts().getTotal_video_data());

                            txt_Post.setText(total_post + " " + "Post");

                            Picasso.with(activity).load(URLFactory.imageUrl + gsonParseProfile.getUserdetail().getUser_image())
                                    .placeholder(R.drawable.profile_user_img_default).transform(new CircleTransform())
                                    .into(img_userprofile_pic);
                            profileDataSet = gsonParseProfile.getUserdetail().getPosts().getData();
                            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                            int width = windowManager.getDefaultDisplay().getWidth();
                            width = width * 80 / 100;
                            myProfilePostsAdapter = new MyUploadsAdapter(activity,
                                    profileDataSet, width, this);
                            if (profileDataSet.size() < 1) {
                                setPostEmptyData();
                            }

                            recyclerViewPosts.setVisibility(View.VISIBLE);

                            recyclerViewPosts.setAdapter(myProfilePostsAdapter);
                            mLayoutManagerPost.scrollToPosition(Integer.MAX_VALUE / 2);

                            if (postPageNumberForService < int_post_total_page) {
                                postPageNumberForService = (1 + postPageNumberForService);
                            }
                            getFavouritesData();
                        } else {
                            GlobalConfig.showToast(context, "Please try again");
                            setPostEmptyData();
                        }

                    } else {
                        GsonParseProfile localGsonParseProfile = gson.fromJson(apiResponse.getResponse(),
                                GsonParseProfile.class);
                        int_post_total_page = Integer.parseInt(localGsonParseProfile.getUserdetail().getPosts().getTotal_page());
                        if (localGsonParseProfile.getStatus().equals("true")) {
                            myProfilePostsAdapter.setData(profileDataSet);
                            if (postPageNumberForService < int_post_total_page) {
                                postPageNumberForService = (1 + postPageNumberForService);
                            }
                        } else {
                            GlobalConfig.showToast(context, "Please try again");
                            setPostEmptyData();
                        }
                    }
                } else {
                    GlobalConfig.showToast(this.activity, "Please try again");
                    setPostEmptyData();
                    if (swipeRefresh.isRefreshing()) {
                        swipeRefresh.setRefreshing(false);
                    }
                }
                break;
            case ApplicationConstants.TaskType.GET_FAVOURITES_POST:
                System.out.println("Response get login profile" + apiResponse.getResponse());
                if (apiResponse.getCode() == 200) {

                    Gson gson = new Gson();

                    if (gsonParseFavourites == null) {

                        gsonParseFavourites = gson.fromJson(apiResponse.getResponse(), GsonParseNewsFeed.class);
                        int_fav_total_page = Integer.parseInt(gsonParseFavourites.getTotal_page());

                        if (gsonParseFavourites.getStatus().equals("true")) {
                            favouriteDataSet = gsonParseFavourites.getPosts();
                            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                            int width = windowManager.getDefaultDisplay().getWidth();
                            width = width * 80 / 100;
                            myFavouritesPostsAdapter = new MyFavouriteLikeAdapter(activity,
                                    favouriteDataSet, width);
                            if (favouriteDataSet.size() < 1) {
                                setFavEmptyStatus();
                            }
                            recyclerViewFavourites.setVisibility(View.VISIBLE);
                            recyclerViewFavourites.setAdapter(myFavouritesPostsAdapter);

                            if (favPageNumberForService < int_fav_total_page) {
                                favPageNumberForService = (1 + favPageNumberForService);
                            }
                            getLikesData();
                        } else {
                            GlobalConfig.showToast(context, "Please try again");
                            setFavEmptyStatus();
                        }
                    } else {
                        GsonParseNewsFeed localGsonParseNewsFeed = gson.fromJson(apiResponse.getResponse(),
                                GsonParseNewsFeed.class);
                        if (localGsonParseNewsFeed.getStatus().equals("true")) {
                            favouriteDataSet
                                    .addAll(localGsonParseNewsFeed.getPosts());
                            myFavouritesPostsAdapter.setData(favouriteDataSet);

                            if (favPageNumberForService < int_fav_total_page) {
                                favPageNumberForService = (1 + favPageNumberForService);
                            }
                        } else {
                            GlobalConfig.showToast(context, "Please try again");
                            setFavEmptyStatus();
                        }
                    }
                } else {
                    GlobalConfig.showToast(this.activity, "Please try again");
                    setFavEmptyStatus();
                    if (swipeRefresh.isRefreshing()) {
                        swipeRefresh.setRefreshing(false);
                    }
                }
                break;
            case ApplicationConstants.TaskType.GET_LIKES_POST:
                System.out.println("Response get login profile" + apiResponse.getResponse());
                if (apiResponse.getCode() == 200) {

                    Gson gson = new Gson();

                    if (gsonParseLikes == null) {

                        gsonParseLikes = gson.fromJson(apiResponse.getResponse(), GsonParseNewsFeed.class);
                        int_like_total_page = Integer.parseInt(gsonParseLikes.getTotal_page());

                        if (gsonParseLikes.getStatus().equals("true")) {
                            likesDataSet = gsonParseLikes.getPosts();
                            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                            int width = windowManager.getDefaultDisplay().getWidth();
                            width = width * 80 / 100;
                            myLikesPostsAdapter = new MyFavouriteLikeAdapter(activity,
                                    likesDataSet, width);
                            if (likesDataSet.size() < 1) {
                                setLikeEmptyStatus();
                            }
                            recyclerViewLikes.setVisibility(View.VISIBLE);
                            recyclerViewLikes.setAdapter(myLikesPostsAdapter);

                            if (likePageNumberForService < int_like_total_page) {
                                likePageNumberForService = (1 + likePageNumberForService);
                            }
                        } else {
                            GlobalConfig.showToast(context, "Please try again");
                            setLikeEmptyStatus();
                        }
                    } else {
                        GsonParseNewsFeed localGsonParseNewsFeed = gson.fromJson(apiResponse.getResponse(),
                                GsonParseNewsFeed.class);
                        if (localGsonParseNewsFeed.getStatus().equals("true")) {
                            likesDataSet
                                    .addAll(localGsonParseNewsFeed.getPosts());
                            myLikesPostsAdapter.setData(likesDataSet);

                            if (likePageNumberForService < int_like_total_page) {
                                likePageNumberForService = (1 + likePageNumberForService);
                            }
                        } else {
                            GlobalConfig.showToast(context, "Please try again");
                            setLikeEmptyStatus();
                        }
                    }
                } else {
                    GlobalConfig.showToast(this.activity, "Please try again");
                    setLikeEmptyStatus();
                }
                if (swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                }
                break;
        }
    }

    private void setPostEmptyData() {
        recyclerViewPosts.setVisibility(View.GONE);
        empty_view_post.setVisibility(View.VISIBLE);
    }

    private void setLikeEmptyStatus() {
        recyclerViewLikes.setVisibility(View.GONE);
        empty_view_like.setVisibility(View.VISIBLE);
    }

    private void setFavEmptyStatus() {
        recyclerViewFavourites.setVisibility(View.GONE);
        empty_view_fav.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProductDeleted() {
        int total_post;
        total_post = Integer.parseInt(gsonParseProfile.getUserdetail().getPosts().getTotal_image_data())
                + Integer.parseInt(gsonParseProfile.getUserdetail().getPosts().getTotal_video_data());
        total_post = total_post - 1;
        txt_Post.setText(total_post + " " + "Post");
    }

    @Override
    public void onRefresh() {
        postPageNumberForService = 1;
        favPageNumberForService = 1;
        likePageNumberForService = 1;
        gsonParseProfile = null;
        gsonParseLikes = null;
        gsonParseFavourites = null;
        getProfileData();
    }
}

