package com.pluggdd.ijbt.fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.util.Comman;
import com.pluggdd.ijbt.util.CommonMethods;


@SuppressLint("NewApi")
public class FindPeopleFragment extends Fragment implements OnClickListener {

	private View view;
	private Activity activity;
	private RelativeLayout rl_facebook;
	private RelativeLayout rl_contact;
	//private RelativeLayout rl_looka_suggestion;
	private RelativeLayout rl_invite_friend;
	//private static Facebook facebook;
	private CommonMethods commonMethods;

	public static FindPeopleFragment newInstance() {
		FindPeopleFragment fragment = new FindPeopleFragment();
		return fragment;
	}

	public FindPeopleFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.find_people, container, false);

		setBodyUI();

		return view;
	}

	@Override
	public void onResume() {

		super.onResume();
		defaultActionBarProcess();
		//commonMethods.ActionBarProcessForMiddletext("Find People");
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void setBodyUI() {
		activity = getActivity();
		//facebook = new Facebook(getResources().getString(R.string.fb_app_id));
		commonMethods = new CommonMethods(activity, this);
		defaultActionBarProcess();

		rl_facebook = (RelativeLayout) view.findViewById(R.id.rl_facebook);
		rl_contact = (RelativeLayout) view.findViewById(R.id.rl_contact);
		/*rl_looka_suggestion = (RelativeLayout) view
				.findViewById(R.id.rl_looka_suggestion);*/
		rl_invite_friend = (RelativeLayout) view
				.findViewById(R.id.rl_invite_friend);

		rl_facebook.setOnClickListener(this);
		rl_contact.setOnClickListener(this);
		//rl_looka_suggestion.setOnClickListener(this);
		rl_invite_friend.setOnClickListener(this);
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
		Middle_text.setText("Find People");
		txt_reset.setVisibility(View.INVISIBLE);
		Middle_text.setVisibility(View.VISIBLE);
		txt_cancel.setVisibility(View.INVISIBLE);
		logo.setVisibility(View.VISIBLE);
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

		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_facebook:
			Comman.forStoreHeaderInvite = "FACEBOOK";
			getFragmentManager()
					.beginTransaction()
					.setCustomAnimations(R.animator.enter_anim,
							R.animator.exit_anim, R.animator.back_anim_start,
							R.animator.back_anim_end)
					.add(R.id.container,
							InviteFragment
									.newInstance(Comman.forStoreHeaderInvite))
					.addToBackStack(null).commit();
			break;

		case R.id.rl_contact:
			contactDialog();
			break;

		/*case R.id.rl_looka_suggestion:
			Comman.forStoreHeaderInvite = "SUGGESTED";
			getFragmentManager()
					.beginTransaction()
					.setCustomAnimations(R.animator.enter_anim,
							R.animator.exit_anim, R.animator.back_anim_start,
							R.animator.back_anim_end)
					.add(R.id.container,
							InviteFragment
									.newInstance(Comman.forStoreHeaderInvite))
					.addToBackStack(null).commit();
			break;*/

		case R.id.rl_invite_friend:
			inviteFriends(activity);
			break;

		default:
			break;
		}

	}

	private void contactDialog() {
		new AlertDialog.Builder(this.activity)
				.setTitle("Search for Your Friends in Address Book?")
				.setMessage(
						"In order to find friends, we need to send address book information to PetSutra servers using a secure connection.")
				.setPositiveButton("Allow",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface paramAnonymousDialogInterface,
									int paramAnonymousInt) {
								Comman.forStoreHeaderInvite = "CONTACTS";
								FindPeopleFragment.this
										.getFragmentManager()
										.beginTransaction()
										.setCustomAnimations(
												R.animator.enter_anim,
												R.animator.exit_anim,
												R.animator.back_anim_start,
												R.animator.back_anim_end)
										.add(R.id.container,
												InviteFragment
														.newInstance(Comman.forStoreHeaderInvite))
										.addToBackStack(null).commit();
							}
						}).setNegativeButton("Cancel", null).show();
	}

	@SuppressWarnings("deprecation")
	public static void inviteFriends(final Context context) {
		try {
			Bundle params = new Bundle();
			// params.putString("title", "invite friends");
			params.putString("title", "FBinviteMessage");
			params.putString("message", "come join us!");
			/*facebook.dialog(context, "apprequests", params,
					new DialogListener() {
						@Override
						public void onComplete(Bundle values) {

							try {

								System.out.println("fb invite response "
										+ values.toString());
								Toast.makeText(context, "Request sent",
										Toast.LENGTH_SHORT).show();

							} catch (Exception e) {

							}

						}

						@Override
						public void onFacebookError(FacebookError error) {
						}

						@Override
						public void onCancel() {

						}

						@Override
						public void onError(DialogError e) {
							// TODO Auto-generated method stub

						}

					});*/
		} catch (Exception e) {

		}
	}

}
