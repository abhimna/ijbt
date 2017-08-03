package com.pluggdd.ijbt.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.adapter.ProfileImagesVideosAdapter;
import com.pluggdd.ijbt.network.APIResponse;
import com.pluggdd.ijbt.network.IJBTResponseController;
import com.pluggdd.ijbt.util.ApplicationConstants;
import com.pluggdd.ijbt.util.Comman;
import com.pluggdd.ijbt.util.CommonMethods;
import com.pluggdd.ijbt.util.DataPassed;
import com.pluggdd.ijbt.util.GlobalConfig;
import com.pluggdd.ijbt.util.GridViewWithHeader;
import com.pluggdd.ijbt.util.SelctedLineareLayout;
import com.pluggdd.ijbt.util.URLFactory;
import com.pluggdd.ijbt.vo.CircleTransform;
import com.pluggdd.ijbt.vo.GsonParseProfile;
import com.pluggdd.ijbt.vo.TouchImage;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

@SuppressLint("NewApi")
public class OtherUserProfileFragment extends Fragment implements
		OnClickListener, IJBTResponseController {

	private View view;
	private Activity activity;
	private RelativeLayout root;
	private LinearLayout llfolower;
	private LinearLayout llfollowing;
	private LinearLayout ll_images;
	private LinearLayout ll_video;
	private SelctedLineareLayout ll_images_video;
	private LinearLayout ll_user_status;
	private ImageView ll_more;
	private GridViewWithHeader gv_images;
	private String userid;
	private CommonMethods commonMethods;
	private TextView txt_username;
	private TextView tv_Follower;
	private TextView txt_Post;
	private TextView tv_Following;
	private TextView txt_username_down;
	private TextView txt_email;
	private TextView txt_images;
	private TextView txt_video;
	private ImageView txt_user_status;
	private ImageView img_userprofile_pic;
	private ProfileImagesVideosAdapter exploreAdapter_images;
	private Bundle bundle;
	DataPassed dataPassed;
	private FragmentManager fragmentManager;
	// private String str_follow;
	private String frndid;
	private View grid_header;
	protected int tasktype;
	GsonParseProfile gsonParseProfile;
	String str_status;
	String block_status;
	int int_total_page;
	private boolean loadingMore = false;
	private int pageNumberForService = 1;
	private String post_type = "audio";
	private Display mDisplay;
	public static OtherUserProfileFragment newInstance() {
		OtherUserProfileFragment fragment = new OtherUserProfileFragment();
		return fragment;
	}

	public OtherUserProfileFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.other_user_profile, container, false);
		grid_header = inflater
				.inflate(R.layout.header_other_user_profile, null);
		setBodyUI();
		gv_images.addHeaderView(grid_header);
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		commonMethods.ActionBarProcessForMiddletext("Profile");

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setBodyUI() {
		activity = getActivity();
		bundle = getArguments();
		frndid = bundle.getString("frndid");
		System.out.println("frndid" + frndid);
		fragmentManager = getFragmentManager();
		commonMethods = new CommonMethods(activity, this);
		userid = activity.getSharedPreferences("prefs_login",
				Activity.MODE_PRIVATE).getString("userid", "");
		String username = activity.getSharedPreferences("prefs_login",
				Activity.MODE_PRIVATE).getString("username", "");
		System.out.println("usernameloginuser" + username);
		root = (RelativeLayout) grid_header.findViewById(R.id.root);
		mDisplay=activity.getWindowManager().getDefaultDisplay();
		// find textview and imageview

		txt_username = (TextView) grid_header.findViewById(R.id.txt_username);
		tv_Follower = (TextView) grid_header.findViewById(R.id.tv_Follower);
		tv_Following = (TextView) grid_header.findViewById(R.id.tv_Following);
		// txt_username_down = (TextView) grid_header
		// .findViewById(R.id.txt_username_down);
		txt_email = (TextView) grid_header.findViewById(R.id.txt_email);
		txt_images = (TextView) grid_header.findViewById(R.id.txt_images);
		txt_video = (TextView) grid_header.findViewById(R.id.txt_video);
		txt_user_status = (ImageView) grid_header
				.findViewById(R.id.txt_user_status);
		img_userprofile_pic = (ImageView) grid_header
				.findViewById(R.id.img_userprofile_pic);
		txt_Post = (TextView) grid_header.findViewById(R.id.tv_post);
		// find linearlayout

		llfolower = (LinearLayout) grid_header.findViewById(R.id.llfolower);
		llfollowing = (LinearLayout) grid_header.findViewById(R.id.llfollowing);
		ll_images = (LinearLayout) grid_header.findViewById(R.id.ll_images);
		ll_video = (LinearLayout) grid_header.findViewById(R.id.ll_video);
		ll_images_video = (SelctedLineareLayout) grid_header
				.findViewById(R.id.ll_images_video);
		// ll_user_status = (LinearLayout) grid_header
		// .findViewById(R.id.ll_user_status);
		ll_more = (ImageView) grid_header.findViewById(R.id.ll_more);
		ll_images_video.setselected(0);
		img_userprofile_pic.setOnClickListener(this);
		llfolower.setOnClickListener(this);
		llfollowing.setOnClickListener(this);
		ll_images.setOnClickListener(this);
		ll_video.setOnClickListener(this);
		txt_user_status.setOnClickListener(this);
		// ll_user_status.setOnClickListener(this);
		ll_more.setOnClickListener(this);
		gv_images = (GridViewWithHeader) view.findViewById(R.id.gv_images);

		gv_images.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				System.out.println("State Change is---" + scrollState);

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int i = firstVisibleItem + visibleItemCount;
				System.out.println("scroll view item > " + i + " / "
						+ totalItemCount + " / " + pageNumberForService + " / "
						+ int_total_page + " / " + loadingMore);
				if ((i == totalItemCount) && (!loadingMore)) {
					System.out.println("scroll view load more data called 1");
					if (pageNumberForService <= int_total_page) {
						System.out
								.println("scroll view load more data called 2");
						loadingMore = true;
						callSecondTimeWebServices(pageNumberForService);
					}
				}

			}

			private void callSecondTimeWebServices(int pageNumberForService) {
				if (frndid.equals("")) {
					GlobalConfig.showToast(activity, "Invalid Tag");
					// activity.onBackPressed();

				} else {
					if (frndid.matches("[0-9]+")) {

						if (commonMethods.getConnectivityStatus()) {
							try {
								tasktype = ApplicationConstants.TaskType.GETOTHERUSERPROFILE;
								commonMethods.GetProfileUser(commonMethods
										.getProfileRequestParmas(userid,
												frndid, ""
														+ pageNumberForService,
												post_type, ""), true);
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

					} else {
						if (commonMethods.getConnectivityStatus()) {
							try {
								tasktype = ApplicationConstants.TaskType.GETOTHERUSERPROFILE;
								commonMethods.GetProfileUser(commonMethods
										.getProfileRequestParmas(userid,
												frndid, ""
														+ pageNumberForService,
												post_type, "STRING"), true);
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
				}

			}
		});

		if (frndid.equals("")) {
			GlobalConfig.showToast(activity, "Invalid Tag");
			// activity.onBackPressed();
		} else {
			if (frndid.matches("[0-9]+")) {
				if (commonMethods.getConnectivityStatus()) {
					try {
						gsonParseProfile = null;
						tasktype = ApplicationConstants.TaskType.GETOTHERUSERPROFILE;
						commonMethods.GetProfileUser(commonMethods
								.getProfileRequestParmas(userid, frndid, ""
										+ pageNumberForService, post_type, ""),
								true);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					GlobalConfig.showToast(
							activity,
							getResources().getString(
									R.string.internet_error_message));
				}
			} else {
				if (commonMethods.getConnectivityStatus()) {
					try {
						gsonParseProfile = null;
						tasktype = ApplicationConstants.TaskType.GETOTHERUSERPROFILE;
						commonMethods.GetProfileUser(commonMethods
								.getProfileRequestParmas(userid, frndid, ""
										+ pageNumberForService, post_type,
										"STRING"), true);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					GlobalConfig.showToast(
							activity,
							getResources().getString(
									R.string.internet_error_message));
				}
			}
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
			TouchImage ivPreview = (TouchImage) nagDialog
					.findViewById(R.id.imageView1);
			// ivPreview.setBackgroundDrawable(dd);
			RelativeLayout rl = (RelativeLayout) nagDialog
					.findViewById(R.id.rl_root_preview);
			final ProgressBar progress1 = (ProgressBar) nagDialog
					.findViewById(R.id.progress1);
			String imageUrl = URLFactory.imageUrl
					+ gsonParseProfile.getUserdetail().getUser_image();
			rl.getLayoutParams().width = mDisplay
					.getWidth();
			Picasso.with(activity)
					.load(imageUrl)
					.into(ivPreview, new com.squareup.picasso.Callback() {
						@Override
						public void onSuccess() {
							progress1.setVisibility(View.GONE);
						}

						@Override
						public void onError() {
							progress1.setVisibility(View.GONE);
						}
					});

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

		
		

		case R.id.llfolower:
			if (Integer.parseInt(gsonParseProfile.getUserdetail()
					.getTotalfollower()) > 0) {
				Comman.forStoreHeaderFollow = "Followers";

				bundle.putString("userid", frndid);
				fragment = new FollowerFollowingFragment(
						Comman.forStoreHeaderFollow);
				fragment.setArguments(bundle);
				fragmentManager
						.beginTransaction()
						.setCustomAnimations(R.animator.enter_anim,
								R.animator.exit_anim,
								R.animator.back_anim_start,
								R.animator.back_anim_end)
						.add(R.id.container, fragment).addToBackStack(null)
						.commit();
			}

			break;

		case R.id.llfollowing:
			if (Integer.parseInt(gsonParseProfile.getUserdetail()
					.getTotalfollowing()) > 0) {
				Comman.forStoreHeaderFollow = "Followings";
				bundle.putString("userid", frndid);
				fragment = new FollowerFollowingFragment(
						Comman.forStoreHeaderFollow);
				fragment.setArguments(bundle);
				fragmentManager
						.beginTransaction()
						.setCustomAnimations(R.animator.enter_anim,
								R.animator.exit_anim,
								R.animator.back_anim_start,
								R.animator.back_anim_end)
						.add(R.id.container, fragment).addToBackStack(null)
						.commit();
			}
			break;

		case R.id.ll_images:
			ll_images_video.setselected(0);
			post_type = "audio";
			pageNumberForService = 1;
			if (frndid.matches("[0-9]+")) {
				if (commonMethods.getConnectivityStatus()) {
					try {
						tasktype = ApplicationConstants.TaskType.GETOTHERUSERPROFILE;
						gsonParseProfile = null;
						commonMethods.GetProfileUser(commonMethods
								.getProfileRequestParmas(userid, frndid, ""
										+ pageNumberForService, post_type, ""),
								true);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					GlobalConfig.showToast(
							activity,
							getResources().getString(
									R.string.internet_error_message));
				}
			} else {
				if (commonMethods.getConnectivityStatus()) {
					try {
						tasktype = ApplicationConstants.TaskType.GETOTHERUSERPROFILE;
						gsonParseProfile = null;
						commonMethods.GetProfileUser(commonMethods
								.getProfileRequestParmas(userid, frndid, ""
										+ pageNumberForService, post_type,
										"STRING"), true);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					GlobalConfig.showToast(
							activity,
							getResources().getString(
									R.string.internet_error_message));
				}
			}

			break;

		case R.id.ll_video:
			ll_images_video.setselected(1);
			post_type = "video";
			pageNumberForService = 1;
			if (frndid.matches("[0-9]+")) {
				if (commonMethods.getConnectivityStatus()) {
					try {
						tasktype = ApplicationConstants.TaskType.GETOTHERUSERPROFILE;
						gsonParseProfile = null;
						commonMethods.GetProfileUser(commonMethods
								.getProfileRequestParmas(userid, frndid, ""
										+ pageNumberForService, post_type, ""),
								true);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					GlobalConfig.showToast(
							activity,
							getResources().getString(
									R.string.internet_error_message));
				}
			} else {
				if (commonMethods.getConnectivityStatus()) {
					try {
						tasktype = ApplicationConstants.TaskType.GETOTHERUSERPROFILE;
						gsonParseProfile = null;
						commonMethods.GetProfileUser(commonMethods
								.getProfileRequestParmas(userid, frndid, ""
										+ pageNumberForService, post_type,
										"STRING"), true);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					GlobalConfig.showToast(
							activity,
							getResources().getString(
									R.string.internet_error_message));
				}
			}
			break;

		case R.id.txt_user_status:

			System.out.println("Clcik" + R.id.txt_user_status);
			if (gsonParseProfile.getUserdetail().getIs_blocked().equals("0")) {
				if (gsonParseProfile.getUserdetail().getIs_following()
						.equals("1")) {

					str_status = "0";
					// ll_user_status.setBackgroundColor(Color
					// .parseColor("#515151"));

					txt_user_status
							.setBackgroundResource(R.drawable.follower_add_frnd);
					// /txt_user_status.setTextColor(Color.parseColor("#ffffff"));
				} else if (gsonParseProfile.getUserdetail().getIs_following()
						.equals("-1")) {
					str_status = "0";
					// ll_user_status.setBackgroundColor(Color
					// .parseColor("#515151"));
					txt_user_status
							.setBackgroundResource(R.drawable.follower_add_frnd);
					// txt_user_status.setTextColor(Color.parseColor("#ffffff"));
				}

				else if (gsonParseProfile.getUserdetail().getIs_following()
						.equals("0")) {
					if (gsonParseProfile.getUserdetail().getAllow_follow()
							.equals("1")) {
						str_status = "-1";
						// ll_user_status
						// .setBackgroundResource(R.drawable.box_follow_follower);
						txt_user_status
								.setBackgroundResource(R.drawable.l_pending_request);
						// txt_user_status.setTextColor(Color
						// .parseColor("#6F7179"));
					} else {
						str_status = "1";
						// ll_user_status.setBackgroundColor(Color
						// .parseColor("#2dbcff"));
						txt_user_status
								.setBackgroundResource(R.drawable.following_request);
						// txt_user_status.setTextColor(Color
						// .parseColor("#ffffff"));
					}
				}

				if (commonMethods.getConnectivityStatus()) {
					try {
						tasktype = ApplicationConstants.TaskType.SETFOLLOWREQUEST;
						commonMethods.SetFollowRequestFeeds(commonMethods
								.setFollowUnfollowRequestParmas(userid,
										gsonParseProfile.getUserdetail()
												.getUserid(), str_status));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					GlobalConfig.showToast(
							activity,
							getResources().getString(
									R.string.internet_error_message));
				}

			} else {
				GlobalConfig.showToast(activity, "User is blocked");
			}

			break;

		case R.id.ll_more:
			if (gsonParseProfile.getUserdetail().getIs_blocked().equals("0")) {
				openDialogWithMoreApp("Block User");
			} else {
				openDialogWithMoreApp("Unblock User");
			}

			break;

		default:
			break;
		}

	}

	private void openDialogWithMoreApp(final String string) {
		final Dialog dialogMapMain = new Dialog(activity);
		dialogMapMain.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dialogMapMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogMapMain.setContentView(R.layout.dialog_more_option);
		dialogMapMain.getWindow().setGravity(Gravity.BOTTOM);

		dialogMapMain.setCanceledOnTouchOutside(true);
		LinearLayout ll_delete = (LinearLayout) dialogMapMain
				.findViewById(R.id.ll_delete);
		LinearLayout ll_sharefb = (LinearLayout) dialogMapMain
				.findViewById(R.id.ll_sharefb);
		LinearLayout ll_addPhoto = (LinearLayout) dialogMapMain
				.findViewById(R.id.ll_addPhoto);
		/*LinearLayout ll_sharetwitter = (LinearLayout) dialogMapMain
				.findViewById(R.id.ll_sharetwitter);*/

		LinearLayout ll_cancel = (LinearLayout) dialogMapMain
				.findViewById(R.id.ll_cancel);
		TextView txt_savetogallery = (TextView) dialogMapMain
				.findViewById(R.id.txt_savetogallery);

		ll_delete.setVisibility(View.GONE);
		ll_sharefb.setVisibility(View.GONE);
		//ll_sharetwitter.setVisibility(View.GONE);
		txt_savetogallery.setText(string);

		ll_addPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (string.equals("Block User")) {
					block_status = "1";
				} else {
					block_status = "0";
				}
				if (commonMethods.getConnectivityStatus()) {
					try {
						tasktype = ApplicationConstants.TaskType.SETBLOCK;
						commonMethods.SetBlockUser(commonMethods
								.setBlockUserRequestParmas(userid, frndid,
										block_status));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					GlobalConfig.showToast(
							activity,
							getResources().getString(
									R.string.internet_error_message));
				}

				dialogMapMain.dismiss();
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

	@Override
	public void OnComplete(APIResponse apiResponse) {

		switch (tasktype) {
		case ApplicationConstants.TaskType.GETOTHERUSERPROFILE:

			System.out.println("Response get other profile"
					+ apiResponse.getResponse());

			// if (post_type.equals("audio")) {
			// ll_images_video.setselected(0);
			// } else {
			// ll_images_video.setselected(1);
			// }

			if (apiResponse.getCode() == 200) {

				JSONObject rootJsonObjObject;

				try {
					rootJsonObjObject = new JSONObject(
							apiResponse.getResponse());
					String strMsg = rootJsonObjObject.getString("message");
					String strStatus = rootJsonObjObject.getString("status");
					if (strStatus.equals("true")) {

						Gson gson = new Gson();

						if (gsonParseProfile == null) {

							gsonParseProfile = gson.fromJson(
									apiResponse.getResponse(),
									GsonParseProfile.class);
							System.out.println("Message"
									+ gsonParseProfile.getMessage());
							if (gsonParseProfile.getStatus().equals("true")) {

								System.out.println("enter into gson111111111");
								int_total_page = Integer
										.parseInt(gsonParseProfile
												.getUserdetail().getPosts()
												.getTotal_page());

								frndid = gsonParseProfile.getUserdetail()
										.getUserid();
								root.setVisibility(View.VISIBLE);
								activity.findViewById(R.id.llfooter)
										.setVisibility(View.VISIBLE);
								// txt_username_down.setText(gsonParseProfile
								// .getUserdetail().getUser_name());
								txt_username.setText(gsonParseProfile
										.getUserdetail().getFname());
								txt_email.setText(gsonParseProfile
										.getUserdetail().getEmail());
								tv_Follower.setText(gsonParseProfile
										.getUserdetail().getTotalfollower()
										+ " " + "Follower");
								tv_Following.setText(gsonParseProfile
										.getUserdetail().getTotalfollowing()
										+ " " + "Following");
								txt_images.setText(gsonParseProfile
										.getUserdetail().getPosts()
										.getTotal_image_data()
										+ " " + "Images");
								txt_video.setText(gsonParseProfile
										.getUserdetail().getPosts()
										.getTotal_video_data()
										+ " " + "Videos");
								if (gsonParseProfile.getUserdetail()
										.getIs_following().equals("0")) {
									txt_user_status
											.setBackgroundResource(R.drawable.follower_add_frnd);
								} else if (gsonParseProfile.getUserdetail()
										.getIs_following().equals("1")) {
									txt_user_status
											.setBackgroundResource(R.drawable.following_request);
								} else {
									txt_user_status
											.setBackgroundResource(R.drawable.l_pending_request);
								}

								int total_post;
								total_post = Integer.parseInt(gsonParseProfile
										.getUserdetail().getPosts()
										.getTotal_image_data())
										+ Integer.parseInt(gsonParseProfile
												.getUserdetail().getPosts()
												.getTotal_video_data());

								txt_Post.setText(total_post + " " + "Post");

								// if
								// (gsonParseProfile.getUserdetail().getIs_following()
								// .equals("1")) {
								// // ll_user_status.setBackgroundColor(Color
								// // .parseColor("#2dbcff"));
								// txt_user_status
								// .setBackgroundResource(R.drawable.following_request);
								// // txt_user_status.setTextColor(Color
								// // .parseColor("#ffffff"));
								// } else if (gsonParseProfile.getUserdetail()
								// .getIs_following().equals("-1")) {
								// txt_user_status
								// .setBackgroundResource(R.drawable.follower_add_frnd);
								// // txt_user_status.setText("Requested");
								// // txt_user_status.setTextColor(Color
								// // .parseColor("#6F7179"));
								// } else {
								// // ll_user_status.setBackgroundColor(Color
								// // .parseColor("#515151"));
								// txt_user_status
								// .setBackgroundResource(R.drawable.follower_add_frnd);
								// // txt_user_status.setTextColor(Color
								// // .parseColor("#ffffff"));
								// }
								block_status = gsonParseProfile.getUserdetail()
										.getIs_blocked();
								Picasso.with(activity)
										.load(URLFactory.imageUrl
												+ gsonParseProfile
														.getUserdetail()
														.getUser_image())
										.placeholder(
												R.drawable.profile_user_img_default)
										.transform(new CircleTransform())
										.into(img_userprofile_pic);

								exploreAdapter_images = new ProfileImagesVideosAdapter(
										activity, gsonParseProfile
												.getUserdetail().getPosts()
												.getData());

								gv_images.setAdapter(exploreAdapter_images);
								if (pageNumberForService <= int_total_page) {
									pageNumberForService = (1 + pageNumberForService);
								}
								loadingMore = false;
							} else {
								GlobalConfig.showToast(activity,
										gsonParseProfile.getMessage());
							}

						} else {
							GsonParseProfile localGsonParseProfile = gson
									.fromJson(apiResponse.getResponse(),
											GsonParseProfile.class);
							System.out
									.println("enter into gson1111111112222222222");
							if (localGsonParseProfile.getStatus()
									.equals("true")) {
								int_total_page = Integer
										.parseInt(localGsonParseProfile
												.getUserdetail().getPosts()
												.getTotal_page());
								gsonParseProfile
										.getUserdetail()
										.getPosts()
										.getData()
										.addAll(localGsonParseProfile
												.getUserdetail().getPosts()
												.getData());
								if (gsonParseProfile.getUserdetail()
										.getIs_following().equals("1")) {
									txt_user_status
											.setBackgroundResource(R.drawable.following_request);
								} else if (gsonParseProfile.getUserdetail()
										.getIs_following().equals("-1")) {
									// txt_user_status.setText("Requested");
								} else {

									txt_user_status
											.setBackgroundResource(R.drawable.follower_add_frnd);

								}

								exploreAdapter_images.setData(gsonParseProfile
										.getUserdetail().getPosts().getData());
								if (pageNumberForService <= int_total_page) {
									pageNumberForService = (1 + pageNumberForService);
								}
								loadingMore = false;
							} else {
								GlobalConfig.showToast(activity,
										localGsonParseProfile.getMessage());
							}

						}

					}

					else {
						GlobalConfig.showToast(activity, strMsg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			break;

		case ApplicationConstants.TaskType.SETFOLLOWREQUEST:

			System.out.println("Result of set follow request"
					+ apiResponse.getResponse());

			if (apiResponse.getCode() == 200) {

				try {
					JSONObject rootJsonObjObject = new JSONObject(
							apiResponse.getResponse());
					String strMsg = rootJsonObjObject.getString("msg");
					String strStatus = rootJsonObjObject.getString("status");

					if (strStatus.equals("true")) {

						String strfollower = rootJsonObjObject
								.getString("follower_count");
						String strfollowing = rootJsonObjObject
								.getString("following_count");

						if (str_status.equals("1")) {
							// txt_user_status.setText("Following");
							gsonParseProfile.getUserdetail().setIs_following(
									str_status);
						} else if (str_status.equals("-1")) {
							// txt_user_status.setText("Requested");
							gsonParseProfile.getUserdetail().setIs_following(
									str_status);
						} else {
							// txt_user_status.setText("Follow");
							gsonParseProfile.getUserdetail().setIs_following(
									str_status);
						}

						tv_Follower.setText(strfollower + " " + "Follower");
						tv_Following.setText(strfollowing + " " + "Following");

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

		case ApplicationConstants.TaskType.SETBLOCK:

			System.out.println("Response of block user"
					+ apiResponse.getResponse());
			if (apiResponse.getCode() == 200) {

				try {
					JSONObject rootJsonObjObject = new JSONObject(
							apiResponse.getResponse());
					String strMsg = rootJsonObjObject.getString("msg");
					String strStatus = rootJsonObjObject.getString("status");

					if (strStatus.equals("true")) {
						gsonParseProfile.getUserdetail().setIs_blocked(
								block_status);
						GlobalConfig.showToast(activity, strMsg);
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

		default:
			break;
		}

	}
}
