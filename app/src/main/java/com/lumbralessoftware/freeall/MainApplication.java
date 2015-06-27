package com.lumbralessoftware.freeall;

import android.app.Application;
import android.graphics.Typeface;

import com.lumbralessoftware.freeall.utils.Constants;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class MainApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.


    @Override
    public void onCreate() {
        super.onCreate();

        initializeTypefaces();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.TWITTER_KEY, Constants.TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
//        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    public static class Fonts
    {
        public static Typeface PENCIL;
    }

    /**
     * This method create font from asset
     */
    private void initializeTypefaces()
    {

        Fonts.PENCIL = Typeface.createFromAsset(getAssets(), Constants.FONT_PATH);
    }

}