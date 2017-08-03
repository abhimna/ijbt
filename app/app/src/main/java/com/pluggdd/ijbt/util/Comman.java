package com.pluggdd.ijbt.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.pluggdd.ijbt.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Comman {

	public static String registeredID;
	// public static boolean flag_search = false;
	// public static boolean flag_planOnNewsfeed = false;
	// public static boolean flag_planOnNewsfeedUser = false;
	// public static boolean flag_onComment = false;
	public static boolean flag_change_password = false;
	public static boolean flag_show_dialog = true;
	public static boolean flag_twitter_share;
	// for action bar
	public static String forStoreHashTagName = "Hashtag";
	public static String forStoreHeaderFollow = "Follower";
	public static String forStoreHeaderInvite;

	// end action bar

	/********************* set shared preferences **************************/
	public static void SetPreferences(Context con, String key, String value) {
		// save the data
		SharedPreferences preferences = con.getSharedPreferences("prefs_login",
				0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/****************** get shared preferences *******************/
	public static String getPreferences(Context con, String key) {
		SharedPreferences sharedPreferences = con.getSharedPreferences(
				"prefs_login", 0);
		String value = sharedPreferences.getString(key, "0");
		return value;

	}

	/********************* set shared preferences **************************/
	public static void SetPreferencesBoolean(Context con, String key, boolean value) {
		// save the data
		SharedPreferences preferences = con.getSharedPreferences("prefs_login",
				0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/****************** get shared preferences *******************/
	public static boolean getPreferencesBoolean(Context con, String key) {
		SharedPreferences sharedPreferences = con.getSharedPreferences(
				"prefs_login", 0);
		boolean value = sharedPreferences.getBoolean(key, false);
		return value;

	}
	/********************** set shared preferences in int *********************************/
	public static void SetPreferencesInteger(Context con, String key, int value) {
		// save the data
		SharedPreferences preferences = con.getSharedPreferences("prefs_login",
				0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/********************* get shared preferences in int ***********************/
	public static int getPreferencesInteger(Context con, String key) {
		SharedPreferences sharedPreferences = con.getSharedPreferences(
				"prefs_login", 0);
		int value = sharedPreferences.getInt(key, 0);
		return value;

	}

	/********************** set shared preferences in Long *********************************/
	public static void SetPreferencesLong(Context con, String key, long value) {
		// save the data
		SharedPreferences preferences = con.getSharedPreferences("prefs_login",
				0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	/********************* get shared preferences in int ***********************/
	public static long getPreferencesLong(Context con, String key) {
		SharedPreferences sharedPreferences = con.getSharedPreferences(
				"prefs_login", 0);
		long value = sharedPreferences.getLong(key, 0);
		return value;

	}

	/********************* show toast message ***********************/
	public static void showToast(Context context, String message) {
		Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
	}

	/******* do animated activity ********/
	public static void doAnim(Activity act, String flag) {
		if (flag.equals("left")) {
			act.overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		} else if (flag.equals("right")) {
			act.overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		}
	}

	/********************** start activity **************************/
	public static void doStartActivityForFinishWithourFinish(Activity act,
			Class cls, String anim_left_right_no) {
		Intent intent = new Intent(act, cls);
		act.startActivity(intent);
		doAnim(act, anim_left_right_no);
	}

	/********************** start activity for finish **************************/
	public static void doStartActivityForFinish(Activity act, Class cls,
			String anim_left_right_no) {
		Intent intent = new Intent(act, cls);
		act.startActivity(intent);
		act.finish();
		doAnim(act, anim_left_right_no);
	}

	/********************** start activity for finish and send value like Class **************************/
	public static void doStartActivityForFinishAndValue(Activity act,
			Class cls, String anim_left_right_no, String key, String classname,
			String hash) {
		Intent intent = new Intent(act, cls);
		intent.putExtra(key, classname);
		intent.putExtra("HASH", hash);
		act.startActivity(intent);
		act.finish();
		doAnim(act, anim_left_right_no);
	}

	/********************** Hide keyboard **************************/
	public static void hideKeyboard(Activity _activity) {
		InputMethodManager inputManager = (InputMethodManager) _activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(_activity.getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/****************** Convert input stream into string **********************/

	public static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	public File String_to_File(String img_url) {
		File casted_image = null;
		try {
			File rootSdDirectory = Environment.getExternalStorageDirectory();

			casted_image = new File(rootSdDirectory, "attachment.jpg");
			if (casted_image.exists()) {
				casted_image.delete();
			}
			casted_image.createNewFile();

			FileOutputStream fos = new FileOutputStream(casted_image);

			URL url = new URL(img_url);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.connect();
			InputStream in = connection.getInputStream();

			byte[] buffer = new byte[1024];
			int size = 0;
			while ((size = in.read(buffer)) > 0) {
				fos.write(buffer, 0, size);
			}
			fos.close();
			return casted_image;

		} catch (Exception e) {

			System.out.print(e);

		}
		return casted_image;
	}

}
