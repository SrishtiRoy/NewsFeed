package com.and.newsfeed.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by srishtic on 11/29/16.
 */

public class SharedPrefernceManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";


    public static final String KEY_EMAIL = "email";
    public static final String KEY_URL = "url";
    public static final String KEY_LOGGEDIN = "loggedIn";

    // Constructor
    public SharedPrefernceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void saveLoginData( String email) {
        // Storing login value as TRUE

        // Storing name in pref
        editor.putString(KEY_EMAIL, email);


        // commit changes
        editor.commit();
    }
    public void saveLoginProfileIcon( String url) {
        // Storing login value as TRUE

        // Storing name in pref
        editor.putString(KEY_URL, url);


        // commit changes
        editor.commit();
    }

    public String getLoginEmail()
    {
        String str=pref.getString(KEY_EMAIL,"");
        return  str;
    }
    public String getProfielPicUrl()
    {
        String str=pref.getString(KEY_URL,"");
        return  str;
    }

    public void setLoggedIn(boolean isLoggedIn)
    {
        editor.putBoolean(KEY_LOGGEDIN, isLoggedIn);


        // commit changes
        editor.commit();
    }
    public boolean isGoogleLoggedIn()
    {
        boolean isLoggedIn=pref.getBoolean(KEY_LOGGEDIN,false);
        return  isLoggedIn;
    }

}