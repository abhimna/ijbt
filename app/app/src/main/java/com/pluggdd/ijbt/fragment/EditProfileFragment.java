package com.pluggdd.ijbt.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.network.APIResponse;
import com.pluggdd.ijbt.network.IJBTResponseController;
import com.pluggdd.ijbt.util.ApplicationConstants;
import com.pluggdd.ijbt.util.CommonMethods;
import com.pluggdd.ijbt.util.GlobalConfig;
import com.pluggdd.ijbt.util.URLFactory;
import com.pluggdd.ijbt.vo.CircleTransform;
import com.pluggdd.ijbt.vo.GsonParseProfile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("NewApi")
public class EditProfileFragment extends Fragment implements
		IJBTResponseController, OnClickListener {

	private View view;
	private Activity activity;
	private String userid;
	private CommonMethods commonMethods;
	private RelativeLayout root;
	private EditText et_username;
	private EditText et_firstname;
	private EditText et_about;
	private EditText et_email;
	private EditText et_mobileno;
	private ImageView iv_userimage;
	private LinearLayout ll_profile;
	private String str_username;
	private String str_fname;
	private String str_email;
	private String str_user_bio;
	private String str_phone;
	private String str_is_private;

	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE1 = 200;
	private static Uri pickedImage;
	private static Bitmap bitmap;
	protected int TaskType;
	GsonParseProfile gsonParseProfile;
	static EditProfileFragment fragment;

	public static EditProfileFragment newInstance() {
		fragment = new EditProfileFragment();
		return fragment;
	}

	public EditProfileFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.edit_profile, container, false);

		setBodyUI();

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		actionBarProcessForMiddleText("Cancel", "Edit Profile", "Save");
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setBodyUI() {
		activity = getActivity();
		commonMethods = new CommonMethods(activity, this);
		actionBarProcessForMiddleText("Cancel", "EDIT PROFILE", "Save");
		getFragmentManager();
		root = (RelativeLayout) view.findViewById(R.id.root);
		userid = activity.getSharedPreferences("prefs_login",
				Activity.MODE_PRIVATE).getString("userid", "");

		et_username = (EditText) view.findViewById(R.id.et_username);
		et_firstname = (EditText) view.findViewById(R.id.et_firstname);
		// et_lastname = (EditText) view.findViewById(R.id.et_lastname);
		et_about = (EditText) view.findViewById(R.id.et_about);
		et_email = (EditText) view.findViewById(R.id.et_email);
		et_mobileno = (EditText) view.findViewById(R.id.et_mobileno);
		// male_female_text = (TextView)
		// view.findViewById(R.id.male_female_text);
		// et_zipcode = (EditText) view.findViewById(R.id.et_zipcod);
		// male_female_btn = (LinearLayout) view
		// .findViewById(R.id.male_female_btn);
		iv_userimage = (ImageView) view.findViewById(R.id.iv_userimage);
		//
		// male = (LinearLayout) view.findViewById(R.id.male);
		// female = (LinearLayout) view.findViewById(R.id.female);
		/*ll_prvt_post = (LinearLayout) view.findViewById(R.id.ll_prvt_post);
		btn_off_prvt_post = (LinearLayout) view
				.findViewById(R.id.btn_off_prvt_post);
		btn_on_prvt_post = (LinearLayout) view
				.findViewById(R.id.btn_on_prvt_post);*/
		// change_pswd = (LinearLayout) view.findViewById(R.id.change_pswd);
		ll_profile = (LinearLayout) view.findViewById(R.id.ll_profile);
		
		ll_profile.setOnClickListener(this);
		// change_pswd.setOnClickListener(this);
		// male.setOnClickListener(this);
		// female.setOnClickListener(this);
		/*btn_off_prvt_post.setOnClickListener(this);
		btn_on_prvt_post.setOnClickListener(this);*/

		if (commonMethods.getConnectivityStatus()) {
			try {
				TaskType = ApplicationConstants.TaskType.GET_PROFILE_TASK;
				commonMethods.GetProfileUser(commonMethods
						.getProfileRequestParmas(userid, userid, "", "", ""),
						true);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			GlobalConfig.showToast(activity,
					getResources().getString(R.string.internet_error_message));
		}
	}

	// Mathod is used for custom action bar

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint({ "InlinedApi", "InflateParams" })
	public void actionBarProcessForMiddleText(String cancel, String edit,
			String save) {
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

		logo.setVisibility(View.GONE);

		imgbtn_back_home.setVisibility(View.GONE);

		imgbtn_setting.setVisibility(View.GONE);
		imgbtn_notification.setVisibility(View.GONE);

		Middle_text.setVisibility(View.VISIBLE);
		Middle_text.setText(edit);

		txt_reset.setVisibility(View.VISIBLE);
		txt_reset.setText(save);

		txt_cancel.setVisibility(View.VISIBLE);
		txt_cancel.setText(cancel);

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

		ll_right_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkValidation();
			}

			private void checkValidation() {

				str_username = et_username.getText().toString();
				str_fname = et_firstname.getText().toString();
				// str_lname = et_lastname.getText().toString();
				str_email = et_email.getText().toString();
				// str_gender = male_female_text.getText().toString();
				str_user_bio = et_about.getText().toString();
				str_phone = et_mobileno.getText().toString();
				// str_location = et_zipcode.getText().toString();

				if (et_firstname.getText().toString().equals("")) {
					showToastMsg("Please enter name");
				} else if (!(validateName(et_firstname.getText().toString()))) {
					showToastMsg("Please enter valid  name");
				}

				// else if (et_lastname.getText().toString().equals("")) {
				// showToastMsg("Please enter last name");
				// }
				// else if (!(validateName(et_lastname.getText().toString()))) {
				// showToastMsg("Please enter valid last name");
				//
				// }
				else if ((!(et_email.getText().toString().equals("")))
						&& (!(isValidEmail(et_email.getText().toString())))) {
					showToastMsg("Please enter valid Email Address");
				}
				// else if (et_mobileno.getText().toString().equals("")) {
				// showToastMsg("Please enter mobile number");
				// }

				// else if (et_zipcode.getText().toString().equals("")) {
				// showToastMsg("Please enter location");
				// }
				//
				//
				else {
					if (commonMethods.getConnectivityStatus()) {
						try {

							if (bitmap != null) {
								File F = new File(getPath(pickedImage));
								TaskType = ApplicationConstants.TaskType.EDIT_PROFILE_TASK;
								commonMethods.EditProfileUser(commonMethods
										.getEditProfileRequestParmas(
												str_username, str_fname,
												str_email, str_user_bio,
												str_phone, userid,
												str_is_private, F));
							} else {
								TaskType = ApplicationConstants.TaskType.EDIT_PROFILE_TASK;
								commonMethods.EditProfileUser(commonMethods
										.getEditProfileRequestParmas(
												str_username, str_fname,
												str_email, str_user_bio,
												str_phone, userid,
												str_is_private, null));
							}

						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						GlobalConfig.showToast(activity, getResources()
								.getString(R.string.internet_error_message));
					}
				}

			}

			@SuppressWarnings("deprecation")
			public String getPath(Uri uri) {
				String[] projection = { Images.Media.DATA };
				Cursor cursor = activity.managedQuery(uri, projection, null,
						null, null);
				activity.startManagingCursor(cursor);
				int column_index = cursor
						.getColumnIndexOrThrow(Images.Media.DATA);
				cursor.moveToFirst();
				return cursor.getString(column_index);
			}

			public boolean isValidEmail(CharSequence target) {
				if (target == null) {
					return false;
				} else {
					return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
							.matches();
				}
			}

			public boolean validateName(final String username) {
				Pattern pattern = Pattern
						.compile("^[A-Za-z0-9]+(?:[ _-][A-Za-z0-9]+)*$");
				Matcher matcher = pattern.matcher(username);
				return matcher.matches();

			}

			public void showToastMsg(String str) {
				Toast.makeText(activity, "" + str, Toast.LENGTH_SHORT).show();
			}
		});

		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

	}

	@Override
	public void OnComplete(APIResponse apiResponse) {

		switch (TaskType) {
		case ApplicationConstants.TaskType.GET_PROFILE_TASK:
			System.out.println("Response edit profile get"
					+ apiResponse.getResponse());

			if (apiResponse.getCode() == 200) {
				Gson gson = new Gson();

				gsonParseProfile = gson.fromJson(apiResponse.getResponse(),
						GsonParseProfile.class);
				if (gsonParseProfile.getStatus().equals("true")) {
					root.setVisibility(View.VISIBLE);
					Picasso.with(activity)
							.load(URLFactory.imageUrl
									+ gsonParseProfile.getUserdetail()
											.getUser_image())
							.placeholder(R.drawable.profile_user_img_default)
							.transform(new CircleTransform())
							.into(iv_userimage);
					et_username.setText(gsonParseProfile.getUserdetail()
							.getUser_name());

					et_firstname.setText(gsonParseProfile.getUserdetail()
							.getFname());
					// et_lastname.setText(gsonParseProfile.getUserdetail()
					// .getLname());
					et_email.setText(gsonParseProfile.getUserdetail()
							.getEmail());
					et_about.setText(gsonParseProfile.getUserdetail()
							.getUser_bio());
					et_mobileno.setText(gsonParseProfile.getUserdetail()
							.getPhone());
					// et_zipcode.setText(gsonParseProfile.getUserdetail()
					// .getLocation());
					// male_female_text.setText(gsonParseProfile.getUserdetail()
					// .getGender());
					str_is_private = gsonParseProfile.getUserdetail()
							.getIs_private();
					//
					// if (gsonParseProfile.getUserdetail().getGender()
					// .equals("Female")) {
					// // male_female_btn
					// // .setBackgroundResource(R.drawable.reg_female_tab);
					// } else {
					// male_female_btn
					// .setBackgroundResource(R.drawable.reg_male_tab);
					// }
/*
					if (str_is_private.equals("1")) {
						ll_prvt_post
								.setBackgroundResource(R.drawable.set_toggle_on);
					} else {
						ll_prvt_post
								.setBackgroundResource(R.drawable.set_toggle_off);
					}*/

				} else {
					GlobalConfig.showToast(activity,
							gsonParseProfile.getMessage());
				}

			} else {
				GlobalConfig.showToast(activity, "Please try again");
			}
			break;

		case ApplicationConstants.TaskType.EDIT_PROFILE_TASK:
			System.out.println("Response edit profile edit"
					+ apiResponse.getResponse());
			if (apiResponse.getCode() == 200) {
				try {
					JSONObject rootJsonObjObject = new JSONObject(
							apiResponse.getResponse());
					String strMsg = rootJsonObjObject.getString("message");
					String strStatus = rootJsonObjObject.getString("status");

					if (strStatus.equals("true")) {
						GlobalConfig.showToast(activity, strMsg);
						activity.onBackPressed();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		// case R.id.change_pswd:
		// Comman.flag_change_password = true;
		// fragmentManager
		// .beginTransaction()
		// .setCustomAnimations(R.animator.enter_anim,
		// R.animator.exit_anim, R.animator.back_anim_start,
		// R.animator.back_anim_end)
		// .replace(R.id.container,
		// ChangePasswordFragment.newInstance()).commit();
		// break;

		// case R.id.male:
		// male_female_btn.setBackgroundResource(R.drawable.reg_male_tab);
		// male_female_text.setText("Male");
		//
		// break;
		// case R.id.female:
		// male_female_btn.setBackgroundResource(R.drawable.reg_female_tab);
		// male_female_text.setText("Female");
		// break;

		/*case R.id.btn_off_prvt_post:
			str_is_private = "0";
			ll_prvt_post.setBackgroundResource(R.drawable.set_toggle_off);
			break;

		case R.id.btn_on_prvt_post:
			str_is_private = "1";
			ll_prvt_post.setBackgroundResource(R.drawable.set_toggle_on);
			break;*/

		case R.id.ll_profile:
			shareProcess();
			break;

		default:
			break;

		}

	}

	private void shareProcess() {
		final Dialog dialogMapMain = new Dialog(activity);
		dialogMapMain.getWindow().setBackgroundDrawable(new ColorDrawable());
		dialogMapMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogMapMain.setContentView(R.layout.popup_uploadpic);
		dialogMapMain.getWindow().setGravity(Gravity.BOTTOM);

		dialogMapMain.setCancelable(true);
		dialogMapMain.setCanceledOnTouchOutside(true);
		dialogMapMain.show();

		LinearLayout btn_takepic = (LinearLayout) dialogMapMain
				.findViewById(R.id.llTakePic);

		btn_takepic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogMapMain.dismiss();
				Intent cameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent,
						CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
			}
		});

		LinearLayout btn_picgallery = (LinearLayout) dialogMapMain
				.findViewById(R.id.llPicGallery);

		btn_picgallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogMapMain.dismiss();
				Intent i = new Intent(
						Intent.ACTION_PICK,
						Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, CAMERA_CAPTURE_IMAGE_REQUEST_CODE1);

			}

		});

	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "Title", null);
		System.out.println("urioncanecl" + "path" + Uri.parse(path));
		return Uri.parse(path);
	}

	/********************************************************************/
	/********************************************************************/

	/*
	 * Capturing Camera Image will lauch camera app requrest image capture
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != 0) {

			if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE
					&& resultCode == Activity.RESULT_OK && data != null) {
				bitmap = (Bitmap) data.getExtras().get("data");
				pickedImage = getImageUri(activity, bitmap);
				System.out.println("urioncanecl" + pickedImage);
				// edit_profile_pic.setImageBitmap(bitmap);
			} else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE1
					&& resultCode == Activity.RESULT_OK && data != null) {

				// Here we need to check if the activity that was triggers was
				// the Image Gallery.
				// If it is the requestCode will match the LOAD_IMAGE_RESULTS
				// value.
				// If the resultCode is RESULT_OK and there is some data we know
				// that an image was picked.
				// if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE1
				// && resultCode == RESULT_OK && data != null) {
				// Let's read picked image data - its URI
				pickedImage = data.getData();

				String[] filePathColumn = { Images.Media.DATA };
				Cursor cursor = activity.getContentResolver().query(
						pickedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				BitmapFactory.Options options = new BitmapFactory.Options();

				// downsizing image as it throws OutOfMemory Exception for
				// larger
				// images
				options.inSampleSize = 8;

				bitmap = BitmapFactory.decodeFile(picturePath, options);

				Bitmap sourceBitmap = bitmap;

				ExifInterface exif = null;
				try {
					exif = new ExifInterface(picturePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
				int orientation = exif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION, 1);
				System.out.println("in camera rotate back and ori"
						+ orientation);
				if (orientation == 6) {
					Matrix matrix = new Matrix();
					matrix.postRotate(90);
					bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
							sourceBitmap.getWidth(), sourceBitmap.getHeight(),
							matrix, true);
				} else if (orientation == 8) {
					Matrix matrix = new Matrix();
					matrix.postRotate(270);
					bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
							sourceBitmap.getWidth(), sourceBitmap.getHeight(),
							matrix, true);
				} else {
					bitmap = sourceBitmap;
				}

			} else if (requestCode == 0000) {
				Intent intenttt = new Intent();
				bitmap = (Bitmap) intenttt.getParcelableExtra("bitmap_round");

			}

		}
		try {
			Picasso.with(activity).load(pickedImage).placeholder(R.drawable.default_round_img_profile)
					.transform(new CircleTransform()).into(iv_userimage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			bitmap = BitmapFactory.decodeStream(input);
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/***********************************************************/
	/***********************************************************/
}
