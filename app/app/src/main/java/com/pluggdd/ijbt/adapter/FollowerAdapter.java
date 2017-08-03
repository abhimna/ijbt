package com.pluggdd.ijbt.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pluggdd.ijbt.CommonFragmentActivity;
import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.footerfragment.ProfileFragment;
import com.pluggdd.ijbt.fragment.OtherUserProfileFragment;
import com.pluggdd.ijbt.network.APIResponse;
import com.pluggdd.ijbt.network.IJBTResponseController;
import com.pluggdd.ijbt.util.CommonMethods;
import com.pluggdd.ijbt.util.GlobalConfig;
import com.pluggdd.ijbt.util.URLFactory;
import com.pluggdd.ijbt.vo.CircleTransform;
import com.pluggdd.ijbt.vo.FollowersListData;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

@SuppressLint("InflateParams")
public class FollowerAdapter extends BaseAdapter implements
        IJBTResponseController {
    private Activity mactivity;
    private ArrayList<FollowersListData> alFollowers = new ArrayList<FollowersListData>();
    private String str_status;
    private CommonMethods commonMethods;
    private String userid;
    private int pos;
    String followType;

    public FollowerAdapter(Activity _activity,
                           ArrayList<FollowersListData> arrayList, String followType) {
        mactivity = _activity;
        alFollowers = arrayList;
        this.followType = followType;
        commonMethods = new CommonMethods(mactivity, this);
        userid = mactivity.getSharedPreferences("prefs_login",
                Activity.MODE_PRIVATE).getString("userid", "");
    }

    class ViewHolder {
        private ImageView ivProfile;
        private ImageView imgview_following;
        private TextView tvUsername;
        public TextView tv_name;
    }

    @Override
    public int getCount() {
        return alFollowers.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    public void setData(ArrayList<FollowersListData> followers_list) {
        this.alFollowers = followers_list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        LayoutInflater inflater = (LayoutInflater) mactivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row_follower_following, null);

            holder.ivProfile = (ImageView) view.findViewById(R.id.iv_profile);
            holder.imgview_following = (ImageView) view
                    .findViewById(R.id.imgview_following);
            holder.tvUsername = (TextView) view.findViewById(R.id.tv_username);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Picasso.with(mactivity)
                .load(URLFactory.imageUrl
                        + alFollowers.get(position).getUser_thumbimage())
                .placeholder(R.drawable.default_round_img_profile)
                .transform(new CircleTransform()).into(holder.ivProfile);

        holder.tvUsername.setText(alFollowers.get(position).getUser_name());
        holder.tv_name.setText(alFollowers.get(position).getFull_name());
        String str_frndid = alFollowers.get(position).getUserid();
        if (str_frndid.equals(userid)) {
            holder.imgview_following.setVisibility(View.GONE);
        } else if (alFollowers.get(position).getIs_following().equals("0")) {
            holder.imgview_following
                    .setBackgroundResource(R.drawable.follower_add_frnd);
        } else if (alFollowers.get(position).getIs_following().equals("1")) {
            holder.imgview_following
                    .setBackgroundResource(R.drawable.following_request);
        } else {
            holder.imgview_following
                    .setBackgroundResource(R.drawable.l_pending_request);
        }

        holder.ivProfile.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str_frndid = alFollowers.get(position).getUserid();
                if (str_frndid.equals(userid)) {
                    mactivity
                            .getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.enter_anim,
                                    R.animator.exit_anim,
                                    R.animator.back_anim_start,
                                    R.animator.back_anim_end)
                            .add(R.id.container,
                                    ProfileFragment
                                            .newInstance((CommonFragmentActivity) mactivity))
                            .addToBackStack(null).commit();
                } else {
                    OtherUserProfileFragment fragment = new OtherUserProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("frndid", str_frndid);
                    fragment.setArguments(bundle);
                    mactivity
                            .getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.enter_anim,
                                    R.animator.exit_anim,
                                    R.animator.back_anim_start,
                                    R.animator.back_anim_end)
                            .add(R.id.container, fragment).addToBackStack(null)
                            .commit();
                }

            }
        });

        holder.tvUsername.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str_frndid = alFollowers.get(position).getUserid();
                if (str_frndid.equals(userid)) {
                    mactivity
                            .getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.enter_anim,
                                    R.animator.exit_anim,
                                    R.animator.back_anim_start,
                                    R.animator.back_anim_end)
                            .add(R.id.container,
                                    ProfileFragment
                                            .newInstance((CommonFragmentActivity) mactivity))
                            .addToBackStack(null).commit();
                } else {
                    OtherUserProfileFragment fragment = new OtherUserProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("frndid", str_frndid);
                    fragment.setArguments(bundle);
                    mactivity
                            .getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.enter_anim,
                                    R.animator.exit_anim,
                                    R.animator.back_anim_start,
                                    R.animator.back_anim_end)
                            .add(R.id.container, fragment).addToBackStack(null)
                            .commit();
                }

            }
        });

        if (followType.equals("Followers")) {
            holder.imgview_following.setVisibility(View.GONE);
        } else {
            holder.imgview_following.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alFollowers.get(position).getIs_following().equals("1")) {
                        str_status = "0";
                        holder.imgview_following
                                .setBackgroundResource(R.drawable.follower_add_frnd);
                        alFollowers.get(position).setIs_following(str_status);
                    } else if (alFollowers.get(position).getIs_following()
                            .equals("-1")) {
                        str_status = "0";
                        holder.imgview_following
                                .setBackgroundResource(R.drawable.follower_add_frnd);
                        alFollowers.get(position).setIs_following(str_status);
                    } else if (alFollowers.get(position).getIs_following()
                            .equals("0")) {
                        if (alFollowers.get(position).getAllow_follow().equals("1")) {
                            str_status = "-1";
                            holder.imgview_following
                                    .setBackgroundResource(R.drawable.l_pending_request);
                            alFollowers.get(position).setIs_following(str_status);
                        } else {
                            str_status = "1";
                            holder.imgview_following
                                    .setBackgroundResource(R.drawable.following_request);
                            alFollowers.get(position).setIs_following(str_status);
                        }
                    }
                    String other_userid = alFollowers.get(position).getUserid();
                    pos = position;
                    if (commonMethods.getConnectivityStatus()) {
                        try {
                            commonMethods.SetFollowRequestFeeds(commonMethods
                                    .setFollowUnfollowRequestParmas(userid,
                                            other_userid, str_status));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        GlobalConfig.showToast(mactivity, mactivity.getResources()
                                .getString(R.string.internet_error_message));
                    }
                }
            });
        }

        return view;

    }

    @Override
    public void OnComplete(APIResponse apiResponse) {
        System.out.println("Response of follow request"
                + apiResponse.getResponse());

        if (apiResponse.getCode() == 200) {

            try {
                JSONObject rootJsonObjObject = new JSONObject(
                        apiResponse.getResponse());
                String strMsg = rootJsonObjObject.getString("msg");
                String strStatus = rootJsonObjObject.getString("status");

                if (strStatus.equals("true")) {
                    notifyDataSetChanged();

                } else {
                    GlobalConfig.showToast(mactivity, strMsg);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            GlobalConfig.showToast(mactivity, "Please try again");
        }

    }
}