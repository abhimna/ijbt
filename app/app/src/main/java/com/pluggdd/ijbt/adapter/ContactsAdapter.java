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
import android.widget.LinearLayout;
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
import com.pluggdd.ijbt.vo.GsonParseContacts;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

@SuppressLint({ "InflateParams" })
public class ContactsAdapter extends BaseAdapter implements
		IJBTResponseController {
	private ArrayList<GsonParseContacts.Contacts> alSuggestions = new ArrayList();
	private CommonMethods commonMethods;
	private Activity mactivity;
	private int pos;
	private String str_status;
	private String userid;
	private ViewHolder localViewHolder = null;

	public ContactsAdapter(Activity paramActivity,
			ArrayList<GsonParseContacts.Contacts> paramArrayList) {
		this.mactivity = paramActivity;
		this.alSuggestions = paramArrayList;
		this.commonMethods = new CommonMethods(this.mactivity, this);
		this.userid = this.mactivity.getSharedPreferences("prefs_login", 0)
				.getString("userid", "");
	}

	public void OnComplete(APIResponse paramAPIResponse) {
		System.out.println("Response of follow request"
				+ paramAPIResponse.getResponse());

		if (paramAPIResponse.getCode() == 200) {

			try {
				JSONObject rootJsonObjObject = new JSONObject(
						paramAPIResponse.getResponse());
				String strMsg = rootJsonObjObject.getString("msg");
				String strStatus = rootJsonObjObject.getString("status");

				if (strStatus.equals("true")) {

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

	public int getCount() {
		return this.alSuggestions.size();
	}

	public Object getItem(int paramInt) {
		return Integer.valueOf(paramInt);
	}

	public long getItemId(int paramInt) {
		return 0L;
	}

	public View getView(final int paramInt, View paramView,
			ViewGroup paramViewGroup) {
		LayoutInflater localLayoutInflater = (LayoutInflater) mactivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (paramView == null) {
			localViewHolder = new ViewHolder();
			paramView = localLayoutInflater.inflate(R.layout.row_invite, null);
			localViewHolder.ivProfile = ((ImageView) paramView
					.findViewById(R.id.iv_profile));
			localViewHolder.tvUsername = ((TextView) paramView
					.findViewById(R.id.tv_username));
			localViewHolder.img_user_status = ((ImageView) paramView
					.findViewById(R.id.img_user_status));
			localViewHolder.ll_user_status = (LinearLayout) paramView
					.findViewById(R.id.ll_user_status);
			paramView.setTag(localViewHolder);
		} else {
			localViewHolder = (ViewHolder) paramView.getTag();
		}
		Picasso.with(this.mactivity)
				.load(URLFactory.imageUrl
						+ ((GsonParseContacts.Contacts) this.alSuggestions
								.get(paramInt)).getUser_thumbimage())
				.placeholder(R.drawable.default_round_img_profile).transform(new CircleTransform())
				.into(localViewHolder.ivProfile);
		localViewHolder.tvUsername
				.setText(((GsonParseContacts.Contacts) this.alSuggestions
						.get(paramInt)).getUser_name());
		localViewHolder.ivProfile
				.setOnClickListener(new OnClickListener() {
					public void onClick(View paramAnonymousView) {
						String str = ((GsonParseContacts.Contacts) ContactsAdapter.this.alSuggestions
								.get(paramInt)).getUserid();
						if (str.equals(ContactsAdapter.this.userid)) {
							ContactsAdapter.this.mactivity
									.getFragmentManager()
									.beginTransaction()
									.setCustomAnimations(R.animator.enter_anim,
											R.animator.exit_anim,
											R.animator.back_anim_start,
											R.animator.back_anim_end)
									.setCustomAnimations(R.animator.enter_anim,
											R.animator.exit_anim)
									.add(R.id.container,
											ProfileFragment
													.newInstance((CommonFragmentActivity) ContactsAdapter.this.mactivity))
									.addToBackStack(null).commit();
							return;
						}
						OtherUserProfileFragment localOtherUserProfileFragment = new OtherUserProfileFragment();
						Bundle localBundle = new Bundle();
						localBundle.putString("frndid", str);
						localOtherUserProfileFragment.setArguments(localBundle);
						ContactsAdapter.this.mactivity
								.getFragmentManager()
								.beginTransaction()
								.setCustomAnimations(R.animator.enter_anim,
										R.animator.exit_anim,
										R.animator.back_anim_start,
										R.animator.back_anim_end)
								.setCustomAnimations(R.animator.enter_anim,
										R.animator.exit_anim)
								.add(R.id.container,
										localOtherUserProfileFragment)
								.addToBackStack(null).commit();
					}
				});

		localViewHolder.ll_user_status
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (alSuggestions.get(paramInt).getFollow_status()
								.equals("1")) {

							// localViewHolder.txt_user_status.setText("Follow");
							// localViewHolder.ll_user_status
							// .setBackgroundColor(Color
							// .parseColor("#515151"));
							// localViewHolder.txt_user_status.setTextColor(Color
							// .parseColor("#ffffff"));
							str_status = "0";
							localViewHolder.img_user_status
									.setImageDrawable(mactivity.getResources().getDrawable(R.drawable.follower_add_frnd));
							alSuggestions.get(paramInt).setFollow_status(
									str_status);
						} else if (alSuggestions.get(paramInt)
								.getFollow_status().equals("-1")) {
							str_status = "0";
							localViewHolder.img_user_status
									.setImageDrawable(mactivity.getResources().getDrawable(R.drawable.follower_add_frnd));
							// localViewHolder.txt_user_status.setText("Follow");
							// localViewHolder.ll_user_status
							// .setBackgroundColor(Color
							// .parseColor("#515151"));
							// localViewHolder.txt_user_status.setTextColor(Color
							// .parseColor("#ffffff"));
							alSuggestions.get(paramInt).setFollow_status(
									str_status);
						}

						else if (alSuggestions.get(paramInt).getFollow_status()
								.equals("0")) {
							if (alSuggestions.get(paramInt).getAllow_follow()
									.equals("1")) {
								str_status = "-1";

								localViewHolder.img_user_status
										.setImageDrawable(mactivity.getResources().getDrawable(R.drawable.l_pending_request));
								// localViewHolder.txt_user_status
								// .setText("Requested");
								// localViewHolder.ll_user_status
								// .setBackgroundResource(R.drawable.box_follow_follower);
								// localViewHolder.txt_user_status
								// .setTextColor(Color
								// .parseColor("#6F7179"));
								alSuggestions.get(paramInt).setFollow_status(
										str_status);
							} else {
								str_status = "1";
								localViewHolder.img_user_status
										.setImageDrawable(mactivity.getResources().getDrawable(R.drawable.following_request));
								// localViewHolder.txt_user_status
								// .setText("Following");
								// localViewHolder.ll_user_status
								// .setBackgroundColor(Color
								// .parseColor("#2dbcff"));
								// localViewHolder.txt_user_status
								// .setTextColor(Color
								// .parseColor("#ffffff"));
								alSuggestions.get(paramInt).setFollow_status(
										str_status);
							}
						}

						String other_userid = alSuggestions.get(paramInt)
								.getUserid();
						pos = paramInt;
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
									mactivity,
									mactivity.getResources().getString(
											R.string.internet_error_message));
						}
						notifyDataSetChanged();
					}
				});

		localViewHolder.tvUsername
				.setOnClickListener(new OnClickListener() {
					public void onClick(View paramAnonymousView) {
						String str = ((GsonParseContacts.Contacts) ContactsAdapter.this.alSuggestions
								.get(paramInt)).getUserid();
						if (str.equals(ContactsAdapter.this.userid)) {
							ContactsAdapter.this.mactivity
									.getFragmentManager()
									.beginTransaction()
									.setCustomAnimations(R.animator.enter_anim,
											R.animator.exit_anim,
											R.animator.back_anim_start,
											R.animator.back_anim_end)
									.setCustomAnimations(R.animator.enter_anim,
											R.animator.exit_anim)
									.add(R.id.container,
											ProfileFragment
													.newInstance((CommonFragmentActivity) ContactsAdapter.this.mactivity))
									.addToBackStack(null).commit();
							return;
						}
						OtherUserProfileFragment localOtherUserProfileFragment = new OtherUserProfileFragment();
						Bundle localBundle = new Bundle();
						localBundle.putString("frndid", str);
						localOtherUserProfileFragment.setArguments(localBundle);
						ContactsAdapter.this.mactivity
								.getFragmentManager()
								.beginTransaction()
								.setCustomAnimations(R.animator.enter_anim,
										R.animator.exit_anim,
										R.animator.back_anim_start,
										R.animator.back_anim_end)
								.setCustomAnimations(R.animator.enter_anim,
										R.animator.exit_anim)
								.add(R.id.container,
										localOtherUserProfileFragment)
								.addToBackStack(null).commit();
					}
				});

		if (alSuggestions.get(paramInt).getFollow_status().equals("0")) {

			localViewHolder.img_user_status
					.setImageDrawable(mactivity.getResources().getDrawable(R.drawable.follower_add_frnd));

		} else if (alSuggestions.get(paramInt).getFollow_status().equals("1")) {
			localViewHolder.img_user_status
					.setImageDrawable(mactivity.getResources().getDrawable(R.drawable.following_request));
		} else {
			localViewHolder.img_user_status
					.setImageDrawable(mactivity.getResources().getDrawable(R.drawable.l_pending_request));
		}
		return paramView;

	}

	class ViewHolder {
		private ImageView ivProfile;
		private LinearLayout ll_user_status;
		private TextView tvUsername;
		private ImageView img_user_status;

	}
}
