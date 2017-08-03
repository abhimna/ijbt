package com.pluggdd.ijbt;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.pluggdd.ijbt.util.Comman;
import com.pluggdd.ijbt.util.CommonMethods;
import com.pluggdd.ijbt.util.GlobalConfig;
import com.pluggdd.ijbt.util.SignatureCommonMenthods;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Splash activity for ijbt
 *
 * @author
 */
public class SplashActivity extends Activity implements AnimationListener {

    private Activity activity;
    private ImageView imgLogo;
    private Animation animFadein;
    public static String registrationId = "";
    private String userID;
    private CommonMethods commonMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        activity = this;
        commonMethods = new CommonMethods(activity, this);
        userID = Comman.getPreferences(activity, "userid");
        imgLogo = (ImageView) findViewById(R.id.img_logo);
        try {
            String testString = SignatureCommonMenthods.SHA256(3);
            Log.e("SHA 256 Format ", testString);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_slide_in_left);
        animFadein.setAnimationListener(this);
        imgLogo.startAnimation(animFadein);

        System.out.println("Device id" + registrationId);
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.pluggdd.ijbt",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onAnimationEnd(Animation arg0) {
        navigateScreen();
    }

    private void navigateScreen() {
            registrationId = FirebaseInstanceId.getInstance().getToken();
            if (registrationId != null) {
                if (userID.equalsIgnoreCase("0") || userID == null) {
                    Comman.doStartActivityForFinish(activity, HomeActivity.class, "right");
                } else {
                    Comman.doStartActivityForFinish(activity, CommonFragmentActivity.class, "right");
                }
            } else {
                if (commonMethods.getConnectivityStatus()) {
                    LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            String token = intent.getStringExtra("token");
                            if (token != null) {
                                registrationId = FirebaseInstanceId.getInstance().getToken();
                                if (registrationId != null) {
                                    if (userID.equalsIgnoreCase("0") || userID == null) {
                                        Comman.doStartActivityForFinish(activity, HomeActivity.class, "right");
                                    } else {
                                        Comman.doStartActivityForFinish(activity, CommonFragmentActivity.class, "right");
                                    }
                                }
                            }

                        }
                    }, new IntentFilter("tokenReceiver"));
                } else {
                    GlobalConfig.showToast(activity,
                            getResources().getString(R.string.no_data_available));
                }
            }

    }

    @Override
    public void onAnimationRepeat(Animation arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation arg0) {
        // TODO Auto-generated method stub

    }

}