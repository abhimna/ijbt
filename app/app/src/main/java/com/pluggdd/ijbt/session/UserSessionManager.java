package com.pluggdd.ijbt.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserSessionManager {
    private SharedPreferences pref;
    private Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;
    // Shared pref file name
    private final String PREFER_NAME = "IJBT_PREF";
    private final String IS_INFLUENCER = "IS_INFLUENCER";


    private static UserSessionManager mInstance = null;

    public static UserSessionManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UserSessionManager(context);
        }
        return mInstance;
    }

    // Constructor
    public UserSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void clearSession() {
        pref.edit().clear().commit();
    }

    public void setIsInfluencer(boolean isInfluencer) {
        editor.putBoolean(IS_INFLUENCER, isInfluencer);
        editor.commit();
    }

    public boolean isInfluencer() {
        return pref.getBoolean(IS_INFLUENCER, false);
    }
}