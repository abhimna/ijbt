package com.pluggdd.ijbt.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pluggdd.ijbt.CommonFragmentActivity;
import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.footerfragment.ProfileFragment;
import com.pluggdd.ijbt.fragment.OtherUserProfileFragment;
import com.pluggdd.ijbt.util.URLFactory;
import com.pluggdd.ijbt.vo.CircleTransform;
import com.pluggdd.ijbt.vo.TimeLineAllUsersData;
import com.pluggdd.ijbt.vo.TimeLineMainData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TimerFollowerNotificationAdapter extends BaseAdapter {

    private Activity mactivity;
    // private ViewHolder holder = null;
    private String userid;
    private int mLastPosition;
    private ArrayList<TimeLineMainData> al_timeLineDataVOs_following = new ArrayList<TimeLineMainData>();

    public TimerFollowerNotificationAdapter(Activity activity, ArrayList<TimeLineMainData> arrayList) {
        this.mactivity = activity;
        this.al_timeLineDataVOs_following = arrayList;
        userid = activity.getSharedPreferences("prefs_login", Activity.MODE_PRIVATE).getString("userid", "");
    }

    class ViewHolder {
        private ImageView im_userImg;
        private ImageView iv_profile;
        private TextView tv_day_ago;
        private TextView tv_like_info;
    }

    @Override
    public int getCount() {

        return al_timeLineDataVOs_following.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        LayoutInflater inflater = (LayoutInflater) mactivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row_timeline, null);

            holder.tv_day_ago = (TextView) view.findViewById(R.id.tv_day_ago);
            holder.tv_like_info = (TextView) view.findViewById(R.id.tv_like_info);
            holder.im_userImg = (ImageView) view.findViewById(R.id.im_userImg);
            holder.iv_profile = (ImageView) view.findViewById(R.id.iv_profile);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (position != 0) {

            float initialTranslation = (mLastPosition <= position ? 500f : -500f);
            view.setTranslationY(initialTranslation);
            view.animate().setInterpolator(new DecelerateInterpolator(1.0f)).translationY(0f).setDuration(300l)
                    .setListener(null);

            mLastPosition = position;
        }
        holder.tv_day_ago.setText(al_timeLineDataVOs_following.get(position).getTime_ago());
        // holder.tv_like_info.setText(al_timeLineDataVOs_following.get(position).getMessage());
        applyColorAndClickOnText(al_timeLineDataVOs_following.get(position).getMessage(),
                al_timeLineDataVOs_following.get(position).getAll_users(), holder.tv_like_info, position);

        if (al_timeLineDataVOs_following.get(position).getType().equals("FOLLOW")
                || al_timeLineDataVOs_following.get(position).getType().equals("UNFOLLOW")
                || al_timeLineDataVOs_following.get(position).getType().equals("FACEBOOKJOIN")) {
            holder.im_userImg.setVisibility(View.GONE);
        } else {
            holder.im_userImg.setVisibility(View.VISIBLE);
            try {
                Picasso.with(mactivity)
                        .load(URLFactory.imageUrl
                                + al_timeLineDataVOs_following.get(position).getPost_detail().getThumb_image())
                        .placeholder(R.drawable.profile_squre_default).into(holder.im_userImg);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (al_timeLineDataVOs_following.get(position).getAll_users().size() > 0) {
            Picasso.with(mactivity)
                    .load(URLFactory.imageUrl
                            + al_timeLineDataVOs_following.get(position).getAll_users().get(0).getUser_thumbimage())
                    .placeholder(R.drawable.default_round_img_profile).transform(new CircleTransform())
                    .into(holder.iv_profile);
        }

        holder.iv_profile.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str_frndid = al_timeLineDataVOs_following.get(position).getAll_users().get(0).getUserid();
                if (str_frndid.equals(userid)) {
                    mactivity.getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim,
                                    R.animator.back_anim_start, R.animator.back_anim_end)
                            .add(R.id.container, ProfileFragment.newInstance((CommonFragmentActivity) mactivity))
                            .addToBackStack(null).commit();
                } else {
                    OtherUserProfileFragment fragment = new OtherUserProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("frndid", str_frndid);
                    fragment.setArguments(bundle);
                    mactivity.getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim,
                                    R.animator.back_anim_start, R.animator.back_anim_end)
                            .add(R.id.container, fragment).addToBackStack(null).commit();
                }

            }
        });

        holder.im_userImg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String strPostId = al_timeLineDataVOs_following.get(position).getPost_detail().getPost_id();

                Bundle bundle = new Bundle();
                bundle.putString("userid", userid);
                bundle.putString("postid", strPostId);

				/*SinglePostFragment fragment = new SinglePostFragment();
                fragment.setArguments(bundle);

				mactivity.getFragmentManager()
						.beginTransaction().setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim,
								R.animator.back_anim_start, R.animator.back_anim_end)
						.add(R.id.container, fragment).addToBackStack(null).commit();*/

            }
        });

        return view;

    }

    public void setData(ArrayList<TimeLineMainData> notifications) {
        this.al_timeLineDataVOs_following = notifications;
        notifyDataSetChanged();

    }

    public void applyColorAndClickOnText(String string, ArrayList<TimeLineAllUsersData> arrayList,
                                         TextView tv_like_info, final int position) {
        SpannableString sp = new SpannableString(string);
        if (arrayList.size() > 2) {
            for (int i = 0; i < 2; i++) {
                if (string.contains(arrayList.get(i).getUser_name())) {
                    final int j = i;

                    int index = string.indexOf(arrayList.get(i).getUser_name());
                    sp.setSpan(new ClickableSpan() {
                        @SuppressWarnings("static-access")
                        @Override
                        public void onClick(View textView) {

                            ForOpenProfilePage(position, j);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setUnderlineText(false);
                            ds.setColor(Color.parseColor("#5f7dc1"));
                        }

                    }, index, index + arrayList.get(i).getUser_name().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        } else {
            for (int i = 0; i < arrayList.size(); i++) {

                if (string.contains(arrayList.get(i).getUser_name())) {
                    final int k = i;

                    int index = string.indexOf(arrayList.get(i).getUser_name());

                    System.out.println("Your index is----" + index);

                    sp.setSpan(new ClickableSpan() {
                        @SuppressWarnings("static-access")
                        @Override
                        public void onClick(View textView) {
                            ForOpenProfilePage(position, k);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setUnderlineText(false);
                            ds.setColor(Color.parseColor("#5f7dc1"));
                        }

                    }, index, index + arrayList.get(i).getUser_name().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

            }
        }
        tv_like_info.setText(sp);
        tv_like_info.setMovementMethod(LinkMovementMethod.getInstance());

    }

    public void ForOpenProfilePage(int position, int innerPos) {
        String str_frndid = al_timeLineDataVOs_following.get(position).getAll_users().get(innerPos).getUserid();
        if (str_frndid.equals(userid)) {
            mactivity.getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim, R.animator.back_anim_start,
                            R.animator.back_anim_end)
                    .add(R.id.container, ProfileFragment.newInstance((CommonFragmentActivity) mactivity))
                    .addToBackStack(null).commit();
        } else {
            OtherUserProfileFragment fragment = new OtherUserProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putString("frndid", str_frndid);
            fragment.setArguments(bundle);
            mactivity.getFragmentManager()
                    .beginTransaction().setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim,
                    R.animator.back_anim_start, R.animator.back_anim_end)
                    .add(R.id.container, fragment).addToBackStack(null).commit();
        }
    }

}
