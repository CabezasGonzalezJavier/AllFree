package com.lumbralessoftware.reusame.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.lumbralessoftware.reusame.R;

/**
 * Created by javiergonzalezcabezas on 23/07/15.
 */
public class AppRate {


    public static void showRateDialog(final Context mContext)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle(String.format(mContext.getString(R.string.app_rate_rate), mContext.getString(R.string.app_name)));
        alert.setMessage(String.format(mContext.getString(R.string.app_rate_enjoying), mContext.getString(R.string.app_name)));
        alert.setCancelable(true);
        alert.setPositiveButton(String.format(mContext.getString(R.string.app_rate_rate), mContext.getString(R.string.app_name)),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Constants.APP_PNAME)));
                        dialog.cancel();
                    }
                }
        );
        alert.setNegativeButton(mContext.getString(R.string.app_rate_no_rate), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }
        );
        AlertDialog alert11 = alert.create();
        alert11.show();
    }
}
