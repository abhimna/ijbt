package com.pluggdd.ijbt.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.loopj.android.http.RequestParams;
import com.pluggdd.ijbt.HomeActivity;
import com.pluggdd.ijbt.R;
import com.pluggdd.ijbt.footerfragment.TimelineFragment;
import com.pluggdd.ijbt.fragment.SettingFragment;
import com.pluggdd.ijbt.network.IJBTConnectionProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class CommonMethods {

    Activity activity;
    Intent intent = new Intent();
    Object listener;
    ProgressDialog pd;
    @SuppressLint("NewApi")
    private FragmentManager fragmentManager;

    public CommonMethods(Activity activity, Object listener) {
        this.activity = activity;
        this.listener = listener;
        fragmentManager = activity.getFragmentManager();
    }

    public void showProgressDialog() {
        pd = new ProgressDialog(activity);
        pd.setMessage("loading");
        pd.show();
    }

    public void dismissProgressDialog() {
        pd.dismiss();
    }

    /*********************
     * set shared preferences
     **************************/
    public static void SetPreferences(Context con, String key, String value) {
        // save the data
        SharedPreferences preferences = con.getSharedPreferences("prefs_login",
                0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /******************
     * get shared preferences
     *******************/
    public static String getPreferences(Context con, String key) {
        SharedPreferences sharedPreferences = con.getSharedPreferences(
                "prefs_login", 0);
        String value = sharedPreferences.getString(key, "0");
        return value;

    }

    /**********************
     * set shared preferences in int
     *********************************/
    public static void SetPreferencesInteger(Context con, String key, int value) {
        // save the data
        SharedPreferences preferences = con.getSharedPreferences("prefs_login",
                0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /*********************
     * get shared preferences in int
     ***********************/
    public static int getPreferencesInteger(Context con, String key) {
        SharedPreferences sharedPreferences = con.getSharedPreferences(
                "prefs_login", 0);
        int value = sharedPreferences.getInt(key, 0);
        return value;

    }

    /**
     * method used for getting the request parameters for the Registration web
     * service with the given parameter
     *
     * @return The complete varify json request parameter
     */

    public String getVarifyRequestParmas(String email, String username) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj
                    .put(ApplicationConstants.VarifyAPIKeys.EMAIL_KEY, email);
            rootJsonObj.put(ApplicationConstants.VarifyAPIKeys.USERNAME_KEY,
                    username);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the Registration web
     * service with the given parameter
     *
     * @param str_email
     * @param str_firstname
     * @param str_username
     * @param str_passowrd
     * @param registrationId
     * @param file
     * @param string
     * @return The complete Registration json request parameter
     * @throws FileNotFoundException
     */

    public RequestParams getRegistrationRequestParmas(String str_username,
                                                      String str_firstname, String str_email, String str_passowrd,
                                                      String registrationId, File file, String string)
            throws FileNotFoundException {

        RequestParams params = new RequestParams();

        params.put(ApplicationConstants.RegistrationAPIKeys.USERNAME_KEY,
                str_username);
        params.put(ApplicationConstants.RegistrationAPIKeys.FIRST_NAME_KEY,
                str_firstname);
        params.put(ApplicationConstants.RegistrationAPIKeys.EMAIL_KEY,
                str_email);
        params.put(ApplicationConstants.RegistrationAPIKeys.PASSWORD_KEY,
                str_passowrd);

        params.put(ApplicationConstants.RegistrationAPIKeys.DEVICE_TOKEN_KEY,
                registrationId);

        params.put(ApplicationConstants.RegistrationAPIKeys.PHOTO_KEY, file);
        params.put(ApplicationConstants.RegistrationAPIKeys.DEVICE_TYPE_KEY,
                string);
        return params;

    }

    /**
     * method used for getting the request parameters for the posting image and
     * video web service with the given parameter
     *
     * @param file
     * @param tagged_user
     * @param hastags
     * @param data_type
     * @param title
     * @param userid
     * @return The complete Registration json request parameter
     * @throws FileNotFoundException
     */
    public RequestParams postImageRequestParmas(String userid, String title,
                                                String data_type, String hastags, String tagged_user, String placebought, String currency, String price, String category, String comment, File file, String userType)
            throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put(ApplicationConstants.PostImageAPIKeys.USERID_KEY, userid);
        params.put(ApplicationConstants.PostImageAPIKeys.TITLE_KEY, title);
        params.put(ApplicationConstants.PostImageAPIKeys.DATA_TYPE_KEY,
                data_type);
        params.put(ApplicationConstants.PostImageAPIKeys.HASHTAG_KEY, hastags);
        params.put(ApplicationConstants.PostImageAPIKeys.TAGGED_USER_KEY,
                tagged_user);
        params.put(ApplicationConstants.PostImageAPIKeys.PLACE_BOUGHT, placebought);
        params.put(ApplicationConstants.PostImageAPIKeys.CURRENCY, currency);
        params.put(ApplicationConstants.PostImageAPIKeys.PRICE, price);
        params.put(ApplicationConstants.PostImageAPIKeys.CATEGORY, category);
        params.put(ApplicationConstants.PostImageAPIKeys.COMMENT, comment);
        params.put(ApplicationConstants.PostImageAPIKeys.IMAGE_KEY, file);
        params.put(ApplicationConstants.PostImageAPIKeys.USER_TYPE, userType);

        return params;
    }

    public RequestParams postAudioRequestParmas(String userid, String title,
                                                String data_type, String hastags, String tagged_user, String placebought, String currency, String price, String category, String comment, File file1, File file2, String userType)
            throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put(ApplicationConstants.PostImageAPIKeys.USERID_KEY, userid);
        params.put(ApplicationConstants.PostImageAPIKeys.TITLE_KEY, title);
        params.put(ApplicationConstants.PostImageAPIKeys.DATA_TYPE_KEY,
                data_type);
        params.put(ApplicationConstants.PostImageAPIKeys.HASHTAG_KEY, hastags);
        params.put(ApplicationConstants.PostImageAPIKeys.TAGGED_USER_KEY,
                tagged_user);
        params.put(ApplicationConstants.PostImageAPIKeys.PLACE_BOUGHT, placebought);
        params.put(ApplicationConstants.PostImageAPIKeys.CURRENCY, currency);
        params.put(ApplicationConstants.PostImageAPIKeys.PRICE, price);
        params.put(ApplicationConstants.PostImageAPIKeys.CATEGORY, category);
        params.put(ApplicationConstants.PostImageAPIKeys.COMMENT, comment);
        params.put(ApplicationConstants.PostImageAPIKeys.IMAGE_KEY, file1);
        params.put(ApplicationConstants.PostImageOrAudioAPIKeys.DATA_KEY, file2);
        params.put(ApplicationConstants.PostImageOrAudioAPIKeys.USER_TYPE, userType);
        return params;

    }

    public RequestParams postAudioCommentRequestParmas(String userid,
                                                       String postid, File file) throws FileNotFoundException {

        RequestParams params = new RequestParams();

        params.put("userid", userid);
        params.put("post_id", postid);
        params.put("audio", file);
        return params;

    }

    public RequestParams setAllowRequestParmas(String userid, String postid)
            throws FileNotFoundException {

        RequestParams params = new RequestParams();

        params.put("userid", userid);
        params.put("users", postid);
        return params;

    }

    /**
     * method used for getting the request parameters for the posting image and
     * video web service with the given parameter
     *
     * @param thumbImage
     * @param tagged_user
     * @param hastags
     * @param data_type
     * @param title
     * @param userid
     * @param device_type
     * @return The complete Registration json request parameter
     * @throws FileNotFoundException
     */

    public RequestParams postVideoRequestParmas(String userid, String title,
                                                String data_type, String hastags, String tagged_user, String data,
                                                File thumbImage, String str, String device_type)
            throws FileNotFoundException {

        RequestParams params = new RequestParams();

        params.put(ApplicationConstants.PostImageAPIKeys.USERID_KEY, userid);
        params.put(ApplicationConstants.PostImageAPIKeys.TITLE_KEY, title);
        params.put(ApplicationConstants.PostImageAPIKeys.DATA_TYPE_KEY,
                data_type);
        params.put(ApplicationConstants.PostImageAPIKeys.HASHTAG_KEY, hastags);
        params.put(ApplicationConstants.PostImageAPIKeys.TAGGED_USER_KEY,
                tagged_user);
        // params.put(ApplicationConstants.PostImageAPIKeys.DATA_KEY, data);
        params.put(ApplicationConstants.PostImageAPIKeys.IMAGE_KEY, thumbImage);
        params.put(ApplicationConstants.PostImageAPIKeys.VIDEO_PATH_KEY, str);
        params.put(ApplicationConstants.PostImageAPIKeys.DEVICE_TYPE_KEY,
                device_type);

        params.put(ApplicationConstants.PostImageAPIKeys.USERID_KEY, userid);
        params.put(ApplicationConstants.PostImageAPIKeys.TITLE_KEY, title);
        params.put(ApplicationConstants.PostImageAPIKeys.DATA_TYPE_KEY,
                data_type);
        params.put(ApplicationConstants.PostImageAPIKeys.HASHTAG_KEY, hastags);
        params.put(ApplicationConstants.PostImageAPIKeys.TAGGED_USER_KEY,
                tagged_user);

        return params;

    }

    public RequestParams postVideoRequestParmas(String userid, String title,
                                                String data_type, String hastags, String tagged_user,
                                                File thumbImage, String videoPathKey, String device_type, String placebought, String currency, String price, String category, String comment, String userType)
            throws FileNotFoundException {

        RequestParams params = new RequestParams();

        params.put(ApplicationConstants.PostImageAPIKeys.USERID_KEY, userid);
        params.put(ApplicationConstants.PostImageAPIKeys.TITLE_KEY, title);
        params.put(ApplicationConstants.PostImageAPIKeys.DATA_TYPE_KEY,
                data_type);
        params.put(ApplicationConstants.PostImageAPIKeys.HASHTAG_KEY, hastags);
        params.put(ApplicationConstants.PostImageAPIKeys.TAGGED_USER_KEY,
                tagged_user);
        // params.put(ApplicationConstants.PostImageAPIKeys.DATA_KEY, data);
        params.put(ApplicationConstants.PostImageAPIKeys.IMAGE_KEY, thumbImage);
        params.put(ApplicationConstants.PostImageAPIKeys.VIDEO_PATH_KEY, videoPathKey);
        params.put(ApplicationConstants.PostImageAPIKeys.DEVICE_TYPE_KEY,
                device_type);
        params.put(ApplicationConstants.PostImageAPIKeys.PLACE_BOUGHT, placebought);
        params.put(ApplicationConstants.PostImageAPIKeys.CURRENCY, currency);
        params.put(ApplicationConstants.PostImageAPIKeys.PRICE, price);
        params.put(ApplicationConstants.PostImageAPIKeys.CATEGORY, category);
        params.put(ApplicationConstants.PostImageAPIKeys.COMMENT, comment);
        params.put(ApplicationConstants.PostImageAPIKeys.USER_TYPE, userType);
        return params;

    }

    /**
     * method used for getting the request parameters for the Edit profile web
     * service with the given parameter
     *
     * @param str_phone
     * @param str_user_bio
     * @param str_email
     * @param str_fname
     * @param str_username
     * @param userid
     * @param str_is_private
     * @param f
     * @return The complete Edit profile json request parameter
     * @throws FileNotFoundException
     */

    public RequestParams getEditProfileRequestParmas(String str_username,
                                                     String str_fname, String str_email, String str_user_bio,
                                                     String str_phone, String userid, String str_is_private, File f)
            throws FileNotFoundException {

        RequestParams params = new RequestParams();

        params.put(ApplicationConstants.EditProfileAPIKeys.USERNAME_KEY,
                str_username);
        params.put(ApplicationConstants.EditProfileAPIKeys.FIRST_NAME_KEY,
                str_fname);
        params.put(ApplicationConstants.EditProfileAPIKeys.EMAIL_KEY, str_email);
        params.put(ApplicationConstants.EditProfileAPIKeys.USER_BIO_KEY,
                str_user_bio);
        params.put(ApplicationConstants.EditProfileAPIKeys.PHONE_KEY, str_phone);

        params.put(ApplicationConstants.EditProfileAPIKeys.USERID_KEY, userid);
        params.put(ApplicationConstants.EditProfileAPIKeys.PHOTO_KEY, f);
        params.put(ApplicationConstants.EditProfileAPIKeys.IS_PRIVATE_KEY,
                str_is_private);
        return params;

    }

    /**
     * method used for getting the request parameters for the Registration web
     * service with the given parameter
     *
     * @param deviceid
     * @param string
     * @return The complete login json request parameter
     */

    public String getLoginRequestParmas(String deviceid, String username,
                                        String password, String string) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(ApplicationConstants.LoginAPIKeys.DEVICE_TOKEN_KEY,
                    deviceid);
            rootJsonObj.put(ApplicationConstants.LoginAPIKeys.USERNAME_KEY,
                    username);
            rootJsonObj.put(ApplicationConstants.LoginAPIKeys.PASSWORD_KEY,
                    password);
            rootJsonObj.put(ApplicationConstants.LoginAPIKeys.DEVICE_TYPE_KEY,
                    string);
            rootJsonArr.put(rootJsonObj);

            System.out.println("login request : " + rootJsonArr.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the Registration web
     * service with the given parameter
     *
     * @return The complete forget password json request parameter
     */

    public String forgetPasswordRequestParmas(String email) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj
                    .put(ApplicationConstants.ForgotPasswordAPIKeys.EMAIL_KEY,
                            email);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the Registration web
     * service with the given parameter
     *
     * @param change_password_str
     * @param old_password_str
     * @return The complete change password json request parameter
     */

    public String changePasswordRequestParmas(String userid,
                                              String old_password_str, String change_password_str) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj
                    .put(ApplicationConstants.ChangePasswordAPIKeys.OLD_PASSWORD_KEY,
                            old_password_str);
            rootJsonObj
                    .put(ApplicationConstants.ChangePasswordAPIKeys.NEW_PASSWORD_KEY,
                            change_password_str);
            rootJsonObj.put(
                    ApplicationConstants.ChangePasswordAPIKeys.USERID_KEY,
                    userid);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the get profile web
     * service with the given parameter
     *
     * @param frndid
     * @param string
     * @param string2
     * @param string3
     * @return The complete get profile json request parameter
     */

    public String getProfileRequestParmas(String userid, String frndid,
                                          String string, String string2, String string3) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(ApplicationConstants.GetProfileAPIKeys.USERID_KEY,
                    userid);
            rootJsonObj.put(ApplicationConstants.GetProfileAPIKeys.FRNDID_KEY,
                    frndid);
            rootJsonObj.put(ApplicationConstants.GetProfileAPIKeys.PAGE_KEY,
                    string);
            rootJsonObj.put(ApplicationConstants.GetProfileAPIKeys.TYPE_KEY,
                    string2);
            rootJsonObj.put("strUserType", string3);

            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the block user web
     * service with the given parameter
     *
     * @param frndid
     * @param status
     * @return The complete get profile json request parameter
     */

    public String setBlockUserRequestParmas(String userid, String frndid,
                                            String status) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj
                    .put(ApplicationConstants.setBlockUserAPIKeys.USERID_KEY,
                            userid);
            rootJsonObj
                    .put(ApplicationConstants.setBlockUserAPIKeys.FRNDID_KEY,
                            frndid);
            rootJsonObj
                    .put(ApplicationConstants.setBlockUserAPIKeys.STATUS_KEY,
                            status);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the get lookagram plan
     * web service with the given parameter
     *
     * @param userid
     * @return The complete get profile json request parameter
     */

    public String getLookagramPlanRequestParmas(String userid) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(
                    ApplicationConstants.GetLookagramPlanAPIKeys.USERID_KEY,
                    userid);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    public RequestParams getInfluencerParams(String influencerCode) {
        RequestParams requestparams = new RequestParams();
        requestparams.put(ApplicationConstants.GetInfluencerAPIKeys.INFLUENCER_CODE, influencerCode);
        return requestparams;
    }

    public String getNotificationStatusRequestParmas(String userid) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(
                    ApplicationConstants.GetLookagramPlanAPIKeys.USERID_KEY,
                    userid);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    public String setNotificationStatusRequestParmas(String userid,
                                                     String strType, String strValue) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj
                    .put(ApplicationConstants.SetNotificationStatusAPIKeys.USERID_KEY,
                            userid);
            rootJsonObj
                    .put(ApplicationConstants.SetNotificationStatusAPIKeys.STR_TYPE_KEY,
                            strType);
            rootJsonObj
                    .put(ApplicationConstants.SetNotificationStatusAPIKeys.STR_VALUE_KEY,
                            strValue);

            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the get hashtag web
     * service with the given parameter
     *
     * @param userid
     * @return The complete get profile json request parameter
     */

    public String getHashtagRequestParmas(String userid) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(
                    ApplicationConstants.GetLookagramPlanAPIKeys.USERID_KEY,
                    userid);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    public String getlogoutRequestParmas(String userid, String registrationId) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(
                    ApplicationConstants.GetLookagramPlanAPIKeys.USERID_KEY,
                    userid);
            rootJsonObj.put("device_token", registrationId);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the set allow follow
     * web service with the given parameter
     *
     * @param userid
     * @param allow_follow2
     * @return The complete get profile json request parameter
     */

    public String setAllowFollowRequestParmas(String userid,
                                              String allow_follow2) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(
                    ApplicationConstants.GetLookagramPlanAPIKeys.USERID_KEY,
                    userid);
            rootJsonObj.put("allow_follow", allow_follow2);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the search web service
     * with the given parameter
     *
     * @param search_key
     * @param searchType
     * @return The complete search request parameter
     */

    public String getSearchRequestParmas(String userid, String searchType,
                                         String search_key) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(ApplicationConstants.GetSearchAPIKeys.USERID_KEY,
                    userid);
            rootJsonObj.put(
                    ApplicationConstants.GetSearchAPIKeys.SEARCH_TYPE_KEY,
                    searchType);
            rootJsonObj.put(ApplicationConstants.GetSearchAPIKeys.KEYWORD_KEY,
                    search_key);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the get CommentList
     * web service with the given parameter
     *
     * @return The complete get profile json request parameter
     */

    public String getCommentListRequestParmas(String userid, String postid) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(
                    ApplicationConstants.GetCommentListAPIKeys.USERID_KEY,
                    userid);
            rootJsonObj.put(
                    ApplicationConstants.GetCommentListAPIKeys.POSTID_KEY,
                    postid);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the get CommentList
     * web service with the given parameter
     *
     * @param string
     * @return The complete get profile json request parameter
     */

    public String getLikeListRequestParmas(String userid, String postid,
                                           String string) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(ApplicationConstants.GetLikeListAPIKeys.USERID_KEY,
                    userid);
            rootJsonObj.put(ApplicationConstants.GetLikeListAPIKeys.POSTID_KEY,
                    postid);
            rootJsonObj.put(ApplicationConstants.GetLikeListAPIKeys.PAGE_KEY,
                    string);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the get single post
     * web service with the given parameter
     *
     * @return The complete get profile json request parameter
     */

    public String getSinglePostRequestParmas(String userid, String postid) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(
                    ApplicationConstants.GetSinglePostAPIKeys.USERID_KEY,
                    userid);
            rootJsonObj.put(
                    ApplicationConstants.GetSinglePostAPIKeys.POSTID_KEY,
                    postid);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the set report web
     * service with the given parameter
     *
     * @param message
     * @return The complete get profile json request parameter
     */

    public String setReportRequestParmas(String userid, String postid,
                                         String message) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(
                    ApplicationConstants.GetSinglePostAPIKeys.USERID_KEY,
                    userid);
            rootJsonObj.put(
                    ApplicationConstants.GetSinglePostAPIKeys.POSTID_KEY,
                    postid);
            rootJsonObj.put("text", message);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the get post comment
     * web service with the given parameter
     *
     * @param msg
     * @param postid
     * @param userid
     * @param msg
     * @param string_tagged
     * @param string_hashtag
     * @return The complete get profile json request parameter
     */

    public String getPostCommentRequestParmas(String userid, String postid,
                                              String msg, String string_hashtag, String string_tagged) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(
                    ApplicationConstants.GetPostCommentAPIKeys.TAGGED_USER_KEY,
                    string_tagged);
            rootJsonObj.put(
                    ApplicationConstants.GetPostCommentAPIKeys.USERID_KEY,
                    userid);
            rootJsonObj.put(
                    ApplicationConstants.GetPostCommentAPIKeys.POSTID_KEY,
                    postid);
            rootJsonObj.put(
                    ApplicationConstants.GetPostCommentAPIKeys.COMMENT_MSG_KEY,
                    msg);
            rootJsonObj.put(
                    ApplicationConstants.GetPostCommentAPIKeys.HASHTAG_KEY,
                    string_hashtag);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the get profile web
     * service with the given parameter
     *
     * @param string
     * @return The complete get explore json request parameter
     */

    public String getExploreFeedRequestParmas(String userid, String string) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(ApplicationConstants.GetExploreAPIKeys.USERID_KEY,
                    userid);
            rootJsonObj.put(ApplicationConstants.GetExploreAPIKeys.PAGE_KEY,
                    string);

            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the get hashtag post
     * web service with the given parameter
     *
     * @param string
     * @return The complete get explore json request parameter
     */

    public String getHashtagPostRequestParmas(String userid, String string) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(
                    ApplicationConstants.GetHashtagPostAPIKeys.USERID_KEY,
                    userid);
            rootJsonObj
                    .put(ApplicationConstants.GetHashtagPostAPIKeys.HASH_KEY,
                            string);

            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for set the request parameters for the follow and unfollow
     * web service with the given parameter
     *
     * @param str_status
     * @param other_userid
     * @param userid
     * @return The complete get explore json request parameter
     */

    public String setFollowUnfollowRequestParmas(String userid,
                                                 String other_userid, String str_status) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(
                    ApplicationConstants.SetFollowUnfollowAPIKeys.USERID_KEY,
                    userid);
            rootJsonObj.put(
                    ApplicationConstants.SetFollowUnfollowAPIKeys.OTHER_ID_KEY,
                    other_userid);
            rootJsonObj.put(
                    ApplicationConstants.SetFollowUnfollowAPIKeys.STATUS_KEY,
                    str_status);

            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the get free user web
     * service with the given parameter
     *
     * @param userid
     * @return The complete get explore json request parameter
     */

    public String getFreeAudioUserRequestParmas(String userid) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(ApplicationConstants.GetFreeUserAPIKeys.USERID_KEY,
                    userid);

            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the get Newsfeed web
     * service with the given parameter
     *
     * @param page_num
     * @return The complete get Newsfeed json request parameter
     */

    public String getNewsFeedRequestParmas(String userid, String page_num) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(ApplicationConstants.GetNewsFeedAPIKeys.USERID_KEY,
                    userid);
            rootJsonObj.put(ApplicationConstants.GetNewsFeedAPIKeys.PAGE_KEY,
                    page_num);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the get Newsfeed web
     * service with the given parameter
     *
     * @param like_value
     * @param post_id
     * @param userid
     * @return The complete get Newsfeed json request parameter
     */

    public String getPostLikeRequestParmas(String userid, String post_id,
                                           String like_value) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(ApplicationConstants.GetPostLikeAPIKeys.USERID_KEY,
                    userid);
            rootJsonObj.put(
                    ApplicationConstants.GetPostLikeAPIKeys.POST_ID_KEY,
                    post_id);
            rootJsonObj.put(ApplicationConstants.GetPostLikeAPIKeys.STATUS_KEY,
                    like_value);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the get Newsfeed web
     * service with the given parameter
     *
     * @param string
     * @param frndid
     * @param userid
     * @param frndid
     * @param frndid
     * @return The complete get Newsfeed json request parameter
     */

    public String getFollowerRequestParmas(String userid, String frndid,
                                           String string) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put(ApplicationConstants.GetFollowerAPIKeys.USERID_KEY,
                    userid);
            rootJsonObj.put(ApplicationConstants.GetFollowerAPIKeys.FRNDID_KEY,
                    frndid);
            rootJsonObj.put(ApplicationConstants.GetFollowerAPIKeys.PAGE_KEY,
                    string);

            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for getting the request parameters for the timeline web
     * service with the given parameter
     *
     * @param pageNumberForService
     * @return The complete get timeline followers notificaiton json request
     * parameter
     */

    public String getTimelineFollowersNotificationRequestParmas(String userid,
                                                                int pageNumberForService) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj
                    .put(ApplicationConstants.GetTimelineFollowerNotificationAPIKeys.USERID_KEY,
                            userid);
            rootJsonObj
                    .put(ApplicationConstants.GetTimelineFollowerNotificationAPIKeys.PAGE_KEY,
                            pageNumberForService);

            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();
    }

    /**
     * method used for get profile user with the given parameter
     *
     * @param requestParams
     * @param b
     * @throws UnsupportedEncodingException
     */
    public void GetProfileUser(String requestParams, boolean b, int taskType)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.API_TYPE);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.USERID_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.FRNDID_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.PAGE_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.TYPE_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.STR_SEARCH_KEY);

        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpGetRequest(activity,
                URLFactory.GetProfileUrl() + "&signature=" + str_signature
                        + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
                null, listener, b, taskType);
    }

    /**
     * method used for get profile user with the given parameter
     *
     * @param requestParams
     * @param b
     * @throws UnsupportedEncodingException
     */
    public void GetProfileUser(String requestParams, boolean b)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.API_TYPE);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.USERID_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.FRNDID_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.PAGE_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.TYPE_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.STR_SEARCH_KEY);

        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpGetRequest(activity,
                URLFactory.GetProfileUrl() + "&signature=" + str_signature
                        + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
                null, listener, b);
    }

    /**
     * method used for get profile user with the given parameter
     *
     * @param requestParams
     * @param b
     * @throws UnsupportedEncodingException
     */
    public void GetFavouritesPost(String requestParams, boolean b, int taskType)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.API_TYPE);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.USERID_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.FRNDID_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.PAGE_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.TYPE_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.STR_SEARCH_KEY);

        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpGetRequest(activity,
                URLFactory.GetFavouritesPostUrl() + "&signature=" + str_signature
                        + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
                null, listener, b, taskType);
    }

    /**
     * method used for get profile user with the given parameter
     *
     * @param requestParams
     * @param b
     * @throws UnsupportedEncodingException
     */
    public void GetFavouritesPost(String requestParams, boolean b)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.API_TYPE);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.USERID_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.FRNDID_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.PAGE_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.TYPE_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.STR_SEARCH_KEY);

        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpGetRequest(activity,
                URLFactory.GetFavouritesPostUrl() + "&signature=" + str_signature
                        + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
                null, listener, b);
    }

    /**
     * method used for get profile user with the given parameter
     *
     * @param requestParams
     * @param b
     * @throws UnsupportedEncodingException
     */
    public void GetLikesPost(String requestParams, boolean b, int taskType)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.API_TYPE);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.USERID_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.FRNDID_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.PAGE_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.TYPE_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetProfileAPIKeys.STR_SEARCH_KEY);

        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpGetRequest(activity,
                URLFactory.GetLikesPostUrl() + "&signature=" + str_signature
                        + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
                null, listener, b, taskType);
    }

    /**
     * method used for for block user with the given parameter
     *
     * @param requestParams
     * @throws UnsupportedEncodingException
     */
    public void SetBlockUser(String requestParams)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings
                .add(ApplicationConstants.setBlockUserAPIKeys.API_TYPE);
        al_signature_strings
                .add(ApplicationConstants.setBlockUserAPIKeys.USERID_KEY);
        al_signature_strings
                .add(ApplicationConstants.setBlockUserAPIKeys.FRNDID_KEY);
        al_signature_strings
                .add(ApplicationConstants.setBlockUserAPIKeys.STATUS_KEY);

        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpGetRequest(activity,
                URLFactory.SetBlockUserUrl() + "&signature=" + str_signature
                        + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
                null, listener, true);
    }

    /**
     * method used to validate influencer code
     *
     * @param requestParams
     * @throws UnsupportedEncodingException
     */
    public void validateInfluencerCode(RequestParams requestParams)
            throws UnsupportedEncodingException {

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpPostRequestWithRequestParams(
                activity,
                "http://ijbt.in/ijbt/webservices/codevalidate.php", requestParams,
                listener);
    }


    /**
     * method used for get lookagram plan user with the given parameter
     *
     * @param requestParams
     * @throws UnsupportedEncodingException
     */
    public void DeativateUser(String requestParams)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings.add("deactivateuser");
        al_signature_strings
                .add(ApplicationConstants.GetLookagramPlanAPIKeys.USERID_KEY);

        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpGetRequest(activity,
                URLFactory.DeactivateUserUrl() + "&signature=" + str_signature
                        + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
                null, listener, true);
    }

    /**
     * method used for get lookagram plan user with the given parameter
     *
     * @param requestParams
     * @throws UnsupportedEncodingException
     */
    public void LogoutUser(String requestParams)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings.add("logout");
        al_signature_strings.add("device_token");
        al_signature_strings
                .add(ApplicationConstants.GetLookagramPlanAPIKeys.USERID_KEY);

        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpGetRequest(activity,
                URLFactory.LogoutUserUrl() + "&signature=" + str_signature
                        + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
                null, listener, true);

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();

    }

    /**
     * method used for delete single post with the given parameter
     *
     * @param requestParams
     * @throws UnsupportedEncodingException
     */
    public void DeleteSinglePost(String requestParams)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings.add("deletepost");
        al_signature_strings
                .add(ApplicationConstants.GetSinglePostAPIKeys.USERID_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetSinglePostAPIKeys.POSTID_KEY);

        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpGetRequest(
                activity,
                URLFactory.DeleteSinglePostUrl() + "&signature="
                        + str_signature + "&data="
                        + URLEncoder.encode(requestParams, "UTF-8"), null,
                listener, true);

    }

    /**
     * method used for report single post with the given parameter
     *
     * @param requestParams
     * @throws UnsupportedEncodingException
     */
    public void ReportonSinglePost(String requestParams)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings.add("reportpost");
        al_signature_strings
                .add(ApplicationConstants.GetSinglePostAPIKeys.USERID_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetSinglePostAPIKeys.POSTID_KEY);
        al_signature_strings.add("text");

        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpGetRequest(
                activity,
                URLFactory.ReportOnSinglePostUrl() + "&signature="
                        + str_signature + "&data="
                        + URLEncoder.encode(requestParams, "UTF-8"), null,
                listener, true);
    }

    /**
     * method used for set follow request feeds with the given parameter
     *
     * @param requestParams
     * @throws UnsupportedEncodingException
     */
    public void SetFollowRequestFeeds(String requestParams)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings
                .add(ApplicationConstants.SetFollowUnfollowAPIKeys.API_TYPE);
        al_signature_strings
                .add(ApplicationConstants.SetFollowUnfollowAPIKeys.USERID_KEY);
        al_signature_strings
                .add(ApplicationConstants.SetFollowUnfollowAPIKeys.OTHER_ID_KEY);
        al_signature_strings
                .add(ApplicationConstants.SetFollowUnfollowAPIKeys.STATUS_KEY);

        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpGetRequest(
                activity,
                URLFactory.SetFollowRequestUrl() + "&signature="
                        + str_signature + "&data="
                        + URLEncoder.encode(requestParams, "UTF-8"), null,
                listener, true);
    }


    /**
     * method used for get newfeeds with the given parameter
     *
     * @param requestParams
     * @param str_follow
     * @throws UnsupportedEncodingException
     */
    public void GetFollowers(String requestParams, String str_follow)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        if (str_follow.equals("Followers")) {
            al_signature_strings
                    .add(ApplicationConstants.GetFollowerAPIKeys.API_TYPE_FOLLOWER);
        } else {
            al_signature_strings
                    .add(ApplicationConstants.GetFollowerAPIKeys.API_TYPE_FOLLOWING);
        }

        al_signature_strings
                .add(ApplicationConstants.GetFollowerAPIKeys.USERID_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetFollowerAPIKeys.FRNDID_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetFollowerAPIKeys.PAGE_KEY);

        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();

        if (str_follow.equals("Followers")) {
            lookagramConnectionProvider.doHttpGetRequest(
                    activity,
                    URLFactory.GetFollowerUrl() + "&signature=" + str_signature
                            + "&data="
                            + URLEncoder.encode(requestParams, "UTF-8"), null,
                    listener, true);
        } else {
            lookagramConnectionProvider.doHttpGetRequest(
                    activity,
                    URLFactory.GetFollowingUrl() + "&signature="
                            + str_signature + "&data="
                            + URLEncoder.encode(requestParams, "UTF-8"), null,
                    listener, true);
        }

    }

    /**
     * method used for get timeline followers notification with the given
     * parameter
     *
     * @param requestParams
     * @throws UnsupportedEncodingException
     */
    public void GetTimelineFeedsFollowerNotification(String requestParams)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings
                .add(ApplicationConstants.GetTimelineFollowerNotificationAPIKeys.API_TYPE);
        al_signature_strings
                .add(ApplicationConstants.GetTimelineFollowerNotificationAPIKeys.USERID_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetFollowerAPIKeys.PAGE_KEY);

        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpGetRequest(
                activity,
                URLFactory.GetTimelineFollowersNotificationUrl()
                        + "&signature=" + str_signature + "&data="
                        + URLEncoder.encode(requestParams, "UTF-8"), null,
                listener, false);
    }

    /**
     * method used for get timeline followers notification with the given
     * parameter
     *
     * @param requestParams
     * @throws UnsupportedEncodingException
     */
    public void GetTimelineFeedsMyNotification(String requestParams)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings
                .add(ApplicationConstants.GetTimelineMyNotificationAPIKeys.API_TYPE);
        al_signature_strings
                .add(ApplicationConstants.GetTimelineMyNotificationAPIKeys.USERID_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetFollowerAPIKeys.PAGE_KEY);

        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpGetRequest(
                activity,
                URLFactory.GetTimelineMyNotificationUrl() + "&signature="
                        + str_signature + "&data="
                        + URLEncoder.encode(requestParams, "UTF-8"), null,
                listener, false);
    }

    public void GetSuggestionListUser(String requestParams)
            throws UnsupportedEncodingException {
        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings.add("suggestion");
        al_signature_strings.add("userid");
        al_signature_strings.add("page");
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpGetRequest(activity,
                URLFactory.GetSuggestionUrl() + "&signature=" + str_signature
                        + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
                null, listener, true);

    }

    /**
     * method used for Post image and video user with the given parameter
     *
     * @param requestParams
     * @throws UnsupportedEncodingException
     */
    public void postIJBTImage(RequestParams requestParams)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.API_TYPE);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.USERID_KEY);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.TITLE_KEY);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.DATA_TYPE_KEY);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.HASHTAG_KEY);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.TAGGED_USER_KEY);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.PLACE_BOUGHT);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.CURRENCY);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.PRICE);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.CATEGORY);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.COMMENT);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.USER_TYPE);
        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);
        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpPostRequestWithOutHeader(activity,
                URLFactory.postImageUrl() + "&signature=" + str_signature,
                requestParams, listener);

    }

    /**
     * method used for Post image and video user with the given parameter
     *
     * @param requestParams
     * @throws UnsupportedEncodingException
     */
    public void setAllFollow(RequestParams requestParams)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings.add("followall");
        al_signature_strings.add("userid");
        al_signature_strings.add("users");
        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpPostRequestWithOutHeader(activity,
                URLFactory.SetAllFollowUrl() + "&signature=" + str_signature,
                requestParams, listener);

    }

    /**
     * method used for Post image and video user with the given parameter
     *
     * @param requestParams
     * @throws UnsupportedEncodingException
     */
    public void postVideo(RequestParams requestParams)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.API_TYPE);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.USERID_KEY);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.TITLE_KEY);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.DATA_TYPE_KEY);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.HASHTAG_KEY);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.TAGGED_USER_KEY);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.DEVICE_TYPE_KEY);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.VIDEO_PATH_KEY);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.PLACE_BOUGHT);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.CURRENCY);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.PRICE);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.CATEGORY);
        al_signature_strings
                .add(ApplicationConstants.PostImageAPIKeys.COMMENT);
        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpPostRequestWithOutHeader(activity,
                URLFactory.postImageUrl() + "&signature=" + str_signature,
                requestParams, listener);

    }

    /**
     * method used for Edit Profile user with the given parameter
     *
     * @param requestParams
     * @throws UnsupportedEncodingException
     */
    public void EditProfileUser(RequestParams requestParams)
            throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings
                .add(ApplicationConstants.EditProfileAPIKeys.API_TYPE);
        al_signature_strings
                .add(ApplicationConstants.EditProfileAPIKeys.FIRST_NAME_KEY);
        al_signature_strings
                .add(ApplicationConstants.EditProfileAPIKeys.USERNAME_KEY);
        al_signature_strings
                .add(ApplicationConstants.EditProfileAPIKeys.EMAIL_KEY);
        al_signature_strings
                .add(ApplicationConstants.EditProfileAPIKeys.USER_BIO_KEY);
        al_signature_strings
                .add(ApplicationConstants.EditProfileAPIKeys.PHONE_KEY);
        al_signature_strings
                .add(ApplicationConstants.EditProfileAPIKeys.IS_PRIVATE_KEY);
        al_signature_strings
                .add(ApplicationConstants.EditProfileAPIKeys.USERID_KEY);
        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpPostRequestWithOutHeader(activity,
                URLFactory.getEditProfileUrl() + "&signature=" + str_signature,
                requestParams, listener);

    }

    public void FBregistrationUser(RequestParams requestParams) {

        ArrayList<String> arrayList = new ArrayList<String>();

        arrayList.add("facebooklogin");
        arrayList.add("fname");
        arrayList.add("lname");
        arrayList.add("gender");
        arrayList.add("email");
        arrayList.add("facebookid");
        arrayList.add("device_token");
        arrayList.add("device_type");
        arrayList.add("dob");
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(arrayList);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpPostRequestWithOutHeader(activity,
                URLFactory.getFBSignUpUrl() + "&signature=" + str_signature,
                requestParams, listener);

    }

    /**
     * This method is used for get the connectivity status
     *
     * @return
     */
    public boolean getConnectivityStatus() {
        ConnectivityManager connManager = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info != null)
            if (info.isConnected()) {
                return true;
            } else {
                return false;
            }
        else
            return false;
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
        TextView Middle_text = (TextView) mCustomView
                .findViewById(R.id.Middle_text);
        TextView txt_reset = (TextView) mCustomView
                .findViewById(R.id.txt_reset);
        TextView txt_cancel = (TextView) mCustomView
                .findViewById(R.id.txt_cancel);

        txt_reset.setVisibility(View.INVISIBLE);
        Middle_text.setVisibility(View.VISIBLE);
        txt_cancel.setVisibility(View.INVISIBLE);
        logo.setVisibility(View.VISIBLE);
        imgbtn_back_home.setVisibility(View.VISIBLE);
        imgbtn_setting.setVisibility(View.INVISIBLE);

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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint({"InlinedApi", "InflateParams"})
    public void ActionBarProcessForChangePassword(String string) {
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
        TextView Middle_text = (TextView) mCustomView
                .findViewById(R.id.Middle_text);
        TextView txt_reset = (TextView) mCustomView
                .findViewById(R.id.txt_reset);
        TextView txt_cancel = (TextView) mCustomView
                .findViewById(R.id.txt_cancel);

        txt_reset.setVisibility(View.VISIBLE);
        Middle_text.setVisibility(View.GONE);
        txt_cancel.setVisibility(View.INVISIBLE);
        logo.setVisibility(View.VISIBLE);
        imgbtn_back_home.setVisibility(View.VISIBLE);
        imgbtn_setting.setVisibility(View.GONE);

        RelativeLayout ll_left_icon = (RelativeLayout) mCustomView
                .findViewById(R.id.ll_left_icon);
        ll_left_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                activity.onBackPressed();

            }
        });

        RelativeLayout ll_right_icon = (RelativeLayout) mCustomView
                .findViewById(R.id.ll_right_icon);

        txt_reset.setText(string);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

    }

    public void ActionBarProcessForMiddletext(String tag2) {
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

        txt_reset.setVisibility(View.GONE);
        Middle_text.setVisibility(View.VISIBLE);
        txt_cancel.setVisibility(View.GONE);
        logo.setVisibility(View.GONE);
        imgbtn_back_home.setVisibility(View.VISIBLE);
        imgbtn_setting.setVisibility(View.VISIBLE);
        imgbtn_notification.setVisibility(View.GONE);

        Middle_text.setText(tag2);

        RelativeLayout ll_left_icon = (RelativeLayout) mCustomView
                .findViewById(R.id.ll_left_icon);
        ll_left_icon.setVisibility(View.GONE);
        RelativeLayout ll_right_icon = (RelativeLayout) mCustomView
                .findViewById(R.id.ll_right_icon);
        RelativeLayout ll_notification_icon = (RelativeLayout) mCustomView
                .findViewById(R.id.ll_notification_icon);

        ll_right_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SettingFragment fragment = new SettingFragment();

                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.animator.enter_anim,
                                R.animator.exit_anim,
                                R.animator.back_anim_start,
                                R.animator.back_anim_end)
                        .replace(R.id.container, fragment).addToBackStack(null)
                        .commit();
            }
        });

        ll_notification_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TimelineFragment fragment = new TimelineFragment();

                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.animator.enter_anim,
                                R.animator.exit_anim,
                                R.animator.back_anim_start,
                                R.animator.back_anim_end)
                        .replace(R.id.container, fragment).addToBackStack(null)
                        .commit();
            }
        });

        imgbtn_back_home.setBackgroundResource(R.drawable.search_search);

        mActionBar.setCustomView(mCustomView);

        mActionBar.setDisplayShowCustomEnabled(true);

    }

    ImageView imgbtn_refresh;
    Animation rotation;

    public ImageView ActionBarProcessFeed(String tag2, final RefreshListener refreshListener) {
        ActionBar mActionBar = activity.getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(activity);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar_feed, null);

        // content of action bar

        imgbtn_refresh = (ImageView) mCustomView
                .findViewById(R.id.imgbtn_refresh);
        ImageView imgbtn_setting = (ImageView) mCustomView
                .findViewById(R.id.imgbtn_setting);
        ImageView imgbtn_notification = (ImageView) mCustomView
                .findViewById(R.id.imgbtn_notification);

        TextView Middle_text = (TextView) mCustomView
                .findViewById(R.id.Middle_text);

        imgbtn_refresh.setVisibility(View.VISIBLE);
        Middle_text.setVisibility(View.VISIBLE);
        imgbtn_setting.setVisibility(View.VISIBLE);
        imgbtn_notification.setVisibility(View.VISIBLE);

        rotation = AnimationUtils.loadAnimation(activity, R.anim.refresh_animation);
        rotation.setRepeatCount(Animation.INFINITE);

        Middle_text.setText(tag2);

        RelativeLayout ll_refresh_icon = (RelativeLayout) mCustomView
                .findViewById(R.id.ll_refresh_icon);
        RelativeLayout ll_right_icon = (RelativeLayout) mCustomView
                .findViewById(R.id.ll_right_icon);
        RelativeLayout ll_notification_icon = (RelativeLayout) mCustomView
                .findViewById(R.id.ll_notification_icon);

        ll_refresh_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgbtn_refresh.startAnimation(rotation);
                refreshListener.onStartRefresh();
            }
        });

        ll_right_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SettingFragment fragment = new SettingFragment();

                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.animator.enter_anim,
                                R.animator.exit_anim,
                                R.animator.back_anim_start,
                                R.animator.back_anim_end)
                        .replace(R.id.container, fragment).addToBackStack(null)
                        .commit();
            }
        });

        ll_notification_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TimelineFragment fragment = new TimelineFragment();

                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.animator.enter_anim,
                                R.animator.exit_anim,
                                R.animator.back_anim_start,
                                R.animator.back_anim_end)
                        .replace(R.id.container, fragment).addToBackStack(null)
                        .commit();
            }
        });
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        return imgbtn_refresh;
    }

    public void clearRefreshAnimation() {
        rotation.cancel();
    }

    public RequestParams getFBRegistrationRequestParmas(String fname,
                                                        String lname, String gender, String email, String dob,
                                                        String facebookId, String registrationId, String device_type,
                                                        File file) throws FileNotFoundException {
        RequestParams requestparams = new RequestParams();
        requestparams.put("fname", fname);
        requestparams.put("lname", lname);
        requestparams.put("gender", gender);
        requestparams.put("email", email);
        requestparams.put("dob", dob);
        requestparams.put("facebookid", facebookId);
        requestparams.put("device_token", registrationId);
        requestparams.put("device_type", device_type);
        requestparams.put("image", file);
        return requestparams;
    }

    public String getSuggestionListUserRequestParmas(String userid,
                                                     int pageNumberForService) {

        JSONArray rootJsonArr = null;
        try {
            rootJsonArr = new JSONArray();
            JSONObject rootJsonObj = new JSONObject();
            rootJsonObj.put("userid", userid);
            rootJsonObj.put("page", pageNumberForService);
            rootJsonArr.put(rootJsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootJsonArr.toString();

    }


    public void popup(String strMsg) {

        new AlertDialog.Builder(activity).setMessage(strMsg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.getSharedPreferences("prefs_login",
                                Activity.MODE_PRIVATE).edit().clear().commit();
                        Intent intent = new Intent();
                        activity.startActivity(intent.setClass(activity,
                                HomeActivity.class));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.finish();
                        activity.overridePendingTransition(0, 0);
                        callFacebookLogout(activity);
                    }

                    public void callFacebookLogout(
                            Activity paramAnonymousActivity) {
                        /*Session localSession1 = Session.getActiveSession();
                        if (localSession1 != null) {
							if (!localSession1.isClosed()) {
								localSession1.closeAndClearTokenInformation();
								Utility.clearFacebookCookies(paramAnonymousActivity);
							}
							return;
						}
						Session localSession2 = new Session(
								paramAnonymousActivity);
						Session.setActiveSession(localSession2);
						localSession2.closeAndClearTokenInformation();
						Utility.clearFacebookCookies(paramAnonymousActivity);*/
                    }

                }).show();

    }

    public void hideKeyboard() {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // download image from url

    // flicker image share method.....
    public static Bitmap downloadImage(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            java.io.InputStream inputStream = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    ////////////////////.....

    public static File savebitmap(Bitmap bmp) {
        String temp = "Share";
        String extStorageDirectory = Environment.getExternalStorageDirectory()
                .toString();
        FileOutputStream outStream = null;
        String path = Environment.getExternalStorageDirectory().toString();
        new File(path + "/TempFolder").mkdirs();
        File file = new File(path + "/TempFolder", temp + ".png");
        if (file.exists()) {
            file.delete();
            file = new File(path + "/TempFolder", temp + ".png");
        }

        try {
            outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    public void GetCardNewsFeeds(String newsFeedRequestParmas) throws UnsupportedEncodingException {

        // create the arraylist for getting the signature
        ArrayList<String> al_signature_strings = new ArrayList<String>();
        al_signature_strings
                .add(ApplicationConstants.GetNewsFeedAPIKeys.API_TYPE_IJBT);
        al_signature_strings
                .add(ApplicationConstants.GetNewsFeedAPIKeys.USERID_KEY);
        al_signature_strings
                .add(ApplicationConstants.GetNewsFeedAPIKeys.PAGE_KEY);

        // calling a method for creating the signature for country list API
        String str_signature = SignatureCommonMenthods
                .getSignatureForAPI(al_signature_strings);

        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpGetRequest(activity,
                URLFactory.GetCardNewsFeedUrl() + "&data=" + URLEncoder.encode(newsFeedRequestParmas, "UTF-8"),
                null, listener, false);
    }

    public void fetchCategoryAndCurrency(String URL) {
        IJBTConnectionProvider lookagramConnectionProvider = new IJBTConnectionProvider();
        lookagramConnectionProvider.doHttpGetRequest(
                activity,
                URL, null,
                listener, false);
    }


}
