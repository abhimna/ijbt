package com.pluggdd.ijbt;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.pluggdd.ijbt.network.APIResponse;
import com.pluggdd.ijbt.network.IJBTResponseController;
import com.pluggdd.ijbt.session.UserSessionManager;
import com.pluggdd.ijbt.util.ApplicationConstants;
import com.pluggdd.ijbt.util.Comman;
import com.pluggdd.ijbt.util.CommonMethods;
import com.pluggdd.ijbt.util.GlobalConfig;
import com.pluggdd.ijbt.util.InfluencerCodePromptDialog;
import com.pluggdd.ijbt.util.OnCodeEntered;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class HomeActivity extends Activity implements IJBTResponseController, OnCodeEntered {

    private Activity activity;
    private Intent intent;
    public static String FacebookId;
    private String Device_type = "android";
    private String Dob;
    private String Email;
    private String Fname;
    private String Gender;
    private String Lname;
    private Bitmap bitmap;
    private CommonMethods commonMethods;
    File outputFile;
    protected int tasktype;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private CallbackManager mFBCallBackManager;
    /*private TextView txtForgot;
    private EditText etUsername;
    private EditText etPassword;
    private String strUserid;
    private String strUsername;
    private static Twitter twitter;
    private static RequestToken requestTokentwi;*/
    public static final int WEBVIEW_REQUEST_CODE = 100;
    InfluencerCodePromptDialog influencerCodePromptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home);
        mFBCallBackManager = CallbackManager.Factory.create();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setBodyUI();

    }

    private void setBodyUI() {
        activity = this;
        intent = new Intent();
        commonMethods = new CommonMethods(activity, this);
        initializeFaceBookLogin();
    }

    private void initializeFaceBookLogin() {

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        mFBCallBackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mFBCallBackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        if (commonMethods.getConnectivityStatus()) {
                            tasktype = ApplicationConstants.TaskType.FBLOGIN;
                            //Snackbar.make(view, "Successs", Snackbar.LENGTH_SHORT).show();
                            GraphRequest request = GraphRequest.newMeRequest(
                                    loginResult.getAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            // Application code
                                            try {
                                                if (response.getError() != null) {
                                                    GlobalConfig.showToast(activity,
                                                            getResources().getString(R.string.fb_error_connection));

                                                } else {
                                                    FacebookId = object.optString("id");
                                                    Fname = object.optString("first_name");
                                                    Lname = object.optString("last_name");
                                                    Email = object.optString("email");
                                                    Dob = object.optString("birthday");
                                                    Gender = object.optString("gender");
                                                    bitmap = getBitmapFromURL(
                                                            ("https://graph.facebook.com/" + FacebookId + "/picture?type=large"));
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            System.out.println((new StringBuilder("facebook id..."))
                                                                    .append(HomeActivity.FacebookId).toString());
                                                            if (commonMethods.getConnectivityStatus()) {
                                                                try {
                                                                    savePathBitmap();
                                                                    if (bitmap != null) {
                                                                        File file = new File(outputFile.getAbsolutePath());
                                                                        if(SplashActivity.registrationId!=null) {
                                                                            commonMethods.FBregistrationUser(
                                                                                    commonMethods.getFBRegistrationRequestParmas(Fname,
                                                                                            Lname, Gender, Email, Dob,
                                                                                            HomeActivity.FacebookId.toString(),
                                                                                            SplashActivity.registrationId, Device_type,
                                                                                            file));
                                                                        }
                                                                        else {
                                                                            commonMethods.FBregistrationUser(
                                                                                    commonMethods.getFBRegistrationRequestParmas(Fname,
                                                                                            Lname, Gender, Email, Dob,
                                                                                            HomeActivity.FacebookId.toString(),
                                                                                            "Test Token", Device_type,
                                                                                            file));
                                                                        }
                                                                    }
                                                                } catch (FileNotFoundException filenotfoundexception) {
                                                                    filenotfoundexception.printStackTrace();
                                                                }
                                                            } else {
                                                                GlobalConfig.showToast(activity,
                                                                        getResources().getString(R.string.internet_error_message));
                                                            }
                                                        }

                                                    });
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,first_name,last_name,name,email,gender,birthday,location");
                            request.setParameters(parameters);
                            request.executeAsync();
                        } else {
                            GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
                        }
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(HomeActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(HomeActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        RelativeLayout btn_fb_login = (RelativeLayout) findViewById(R.id.connect_fb);
        btn_fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(HomeActivity.this, Arrays.asList("public_profile,email,user_birthday,user_friends"));
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(com.facebook.AccessToken oldToken, com.facebook.AccessToken newToken) {
                com.facebook.AccessToken.setCurrentAccessToken(newToken);
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFBCallBackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }

    private void displayMessage(Profile profile) {
        if (profile != null) {
            String profileName = profile.getName();
            Log.e("Profile Name is : ", profileName);
        }
    }

    @Override
    public void OnComplete(APIResponse apiResponse) {
        switch (tasktype) {/*
            case ApplicationConstants.TaskType.AUTHENTICATE_CODE:
                System.out.println(
                        (new StringBuilder("Resonse of facebook sig up")).append(apiResponse.getResponse()).toString());
                if (apiResponse.getCode() == 200) {
                    try {
                        JSONObject res = new JSONObject(apiResponse.getResponse());
                        String strMsg = res.getString("msg");
                        String strStatus = res.getString("status");
                        if (strStatus.equalsIgnoreCase("1")) {
                            LoginManager.getInstance().logInWithReadPermissions(HomeActivity.this, Arrays.asList("public_profile,email,user_birthday,user_friends"));
                        } else {
                            GlobalConfig.showToast(activity, strMsg);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {
                    GlobalConfig.showToast(activity, "Please try again");
                }
                break;*/
            case ApplicationConstants.TaskType.FBLOGIN:
                System.out.println(
                        (new StringBuilder("Resonse of facebook sig up")).append(apiResponse.getResponse()).toString());
                if (apiResponse.getCode() == 200) {

                    try {
                        JSONObject res = new JSONObject(apiResponse.getResponse());
                        String strMsg = res.getString("message");
                        String strStatus = res.getString("status");
                        String LoginStatus = res.getString("login_status");
                        if (strStatus.equals("true")) {

                            if (LoginStatus.equals("null")) {
                                final String strUserid = res.getString("userid");
                                final String strUsername = res.getString("user_name");
                                new AlertDialog.Builder(activity,R.style.DialogStyle).setMessage(strMsg)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Comman.SetPreferences(activity, "userid", strUserid);
                                                Comman.SetPreferences(activity, "user_name", strUsername);
                                                Comman.SetPreferencesBoolean(activity, "is_not_first_time", false);
                                                showInfluencerPopup();
                                                dialog.dismiss();
                                            }

                                        }).show();
                            } else {
                                String strUserid = res.getString("userid");
                                String strUsername = res.getString("user_name");
                                Comman.SetPreferences(activity, "userid", strUserid);
                                Comman.SetPreferences(activity, "user_name", strUsername);
                                showInfluencerPopup();
                            }

                        } else {
                            GlobalConfig.showToast(activity, strMsg);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
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

    private void showInfluencerPopup() {
        influencerCodePromptDialog = new InfluencerCodePromptDialog(this, this);
        influencerCodePromptDialog.show();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    private void savePathBitmap() {
        File file = activity.getCacheDir();
        outputFile = null;
        try {
            outputFile = File.createTempFile("thumb", ".jpeg", file);
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }
        try {
            FileOutputStream fileoutputstream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileoutputstream);
            fileoutputstream.flush();
            fileoutputstream.close();
            return;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onInfluencerCodeValidated(String influencerCode) {
        if (influencerCode == null) {
            showInfluencerError();
            //UserSessionManager.getInstance(HomeActivity.this).setIsInfluencer(false);
        } else {
            influencerCodePromptDialog.dismiss();
            UserSessionManager.getInstance(HomeActivity.this).setIsInfluencer(true);
            intent.putExtra("user_name", Comman.getPreferences(activity, "user_name"));
            intent.putExtra("userid", Comman.getPreferences(activity, "userid"));
            intent.setClass(activity, CommonFragmentActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            activity.finish();
            overridePendingTransition(0, 0);
        }

    }

    private void showInfluencerError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);
        //AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.error_influencer_dialog, null);
        builder.setView(view, 0, 0, 0, 0);

        final Dialog dialog = builder.create();
        dialog.show();

        Button btnOk = (Button) view.findViewById(R.id.btn_ok);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onNormalUserLoggedIn() {
        influencerCodePromptDialog.dismiss();
        UserSessionManager.getInstance(HomeActivity.this).setIsInfluencer(false);
        intent.putExtra("user_name", Comman.getPreferences(activity, "user_name"));
        intent.putExtra("userid", Comman.getPreferences(activity, "userid"));
        intent.setClass(activity, CommonFragmentActivity.class);
        intent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        activity.finish();
        overridePendingTransition(0, 0);
    }
}
