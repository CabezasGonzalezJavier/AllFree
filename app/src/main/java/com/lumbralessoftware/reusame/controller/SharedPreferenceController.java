package com.lumbralessoftware.reusame.controller;

        import android.content.Context;
        import android.content.SharedPreferences;

public class SharedPreferenceController
{

    private static final String PREF_NAME = "com.lumbralessoftware.freeall";
    private static final String TWITTER_ACCESS = "twitter_access";
    private static final String TWITTER_SECRET = "twitter_secret";
    private static final String TWITTER_EMAIL = "twitter_email";
    private static final String FACEBOOK_ACCESS = "facebook_access";

    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";



    private static SharedPreferenceController sInstance;
    private final SharedPreferences mPref;

    private SharedPreferenceController(Context context)
    {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context)
    {
        if (sInstance == null) {
            sInstance = new SharedPreferenceController(context);
        }
    }

    public static synchronized SharedPreferenceController getInstance()
    {
        if (sInstance == null) {
            throw new IllegalStateException(SharedPreferenceController.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }


    public void setTwitterAccess(String value)
    {
        mPref.edit().putString(TWITTER_ACCESS, value).commit();
    }

    public String getTwitterAccess()
    {
        return mPref.getString(TWITTER_ACCESS, "");
    }

    public void setTwitterSecret(String value)
    {
        mPref.edit().putString(TWITTER_SECRET, value).commit();
    }

    public String getTwitterEmail()
    {
        return mPref.getString(TWITTER_EMAIL, "");
    }

    public void setTwitterEmail(String value)
    {
        mPref.edit().putString(TWITTER_EMAIL, value).commit();
    }

    public String getTwitterSecret()
    {
        return mPref.getString(TWITTER_SECRET, "");
    }
    public void setFacebookAccess(String value)
    {
        mPref.edit().putString(FACEBOOK_ACCESS, value).commit();
    }

    public String getFacebookAccess()
    {
        return mPref.getString(FACEBOOK_ACCESS, "");
    }

    public void setAccessToken(String value)
    {
        mPref.edit().putString(ACCESS_TOKEN, value).commit();
    }

    public String getAccessToken()
    {
        return mPref.getString(ACCESS_TOKEN, "");
    }

    public void setRefreshToken(String value)
    {
        mPref.edit().putString(REFRESH_TOKEN, value).commit();
    }

    public String getRefreshToken()
    {
        return mPref.getString(REFRESH_TOKEN, "");
    }
}