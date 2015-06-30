package com.lumbralessoftware.freeall.utils;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.lumbralessoftware.freeall.controller.SharedPreferenceController;
import java.util.Calendar;
import java.util.Date;

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

    public static String getAuthorizationHeader() {
        return "Bearer " + SharedPreferenceController.getInstance().getAccessToken();
    }

    public static Calendar dateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static LatLng getLastLocation(Context context) {
        LocationManager service = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        Location location = service.getLastKnownLocation(provider);
        if (location != null) {
            return new LatLng(location.getLatitude(),location.getLongitude());
        }
        else  {
            return new LatLng(Constants.DEFAULT_LATITUDE, Constants.DEFAULT_LONGITUDE);
        }
    }
}
