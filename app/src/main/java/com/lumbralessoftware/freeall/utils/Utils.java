package com.lumbralessoftware.freeall.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.lumbralessoftware.freeall.controller.SharedPreferenceController;

import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class Utils {
    /**
     * this method check internet
     * @param activity
     * @return
     */

    public static boolean isOnline(Activity activity) {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     *
     * @param tag
     * @param error
     */
    public static String logResponse(String tag, RetrofitError error)
    {
        String serverError = null;

        try {
            if (error != null && error.getResponse() != null && error.getResponse().getBody() != null) {
                serverError = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
            }
            if (serverError != null) {
                Log.d(tag, serverError);
            }
        } catch (Exception e) {
            Log.e(tag, "Error fetching server response", e);
        }
        
        return serverError;
    }

    public static String getAuthorizationHeader()
    {
        return "Bearer " + SharedPreferenceController.getInstance().getAccessToken();
    }
}
