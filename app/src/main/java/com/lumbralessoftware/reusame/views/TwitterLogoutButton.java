package com.lumbralessoftware.reusame.views;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.lang.ref.WeakReference;

public class TwitterLogoutButton extends Button {
    final WeakReference<Activity> activityRef;
    volatile TwitterAuthClient authClient;

    public TwitterLogoutButton(Context context) {
        this(context, (AttributeSet)null);
    }

    public TwitterLogoutButton(Context context, AttributeSet attrs) {
        this(context, attrs, 16842824);
    }

    public TwitterLogoutButton(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, (TwitterAuthClient)null);
    }

    TwitterLogoutButton(Context context, AttributeSet attrs, int defStyle, TwitterAuthClient authClient) {
        super(context, attrs, defStyle);
        this.activityRef = new WeakReference(this.getActivity());
        this.authClient = authClient;
        this.setupButton();
    }

    @TargetApi(21)
    private void setupButton() {
        Resources res = this.getResources();
        super.setCompoundDrawablesWithIntrinsicBounds(res.getDrawable(com.twitter.sdk.android.core.R.drawable.tw__ic_logo_default), (Drawable)null, (Drawable)null, (Drawable)null);
        super.setCompoundDrawablePadding(res.getDimensionPixelSize(com.twitter.sdk.android.core.R.dimen.tw__login_btn_drawable_padding));
        super.setText(com.twitter.sdk.android.core.R.string.tw__login_btn_txt);
        super.setTextColor(res.getColor(com.twitter.sdk.android.core.R.color.tw__solid_white));
        super.setTextSize(0, (float)res.getDimensionPixelSize(com.twitter.sdk.android.core.R.dimen.tw__login_btn_text_size));
        super.setTypeface(Typeface.DEFAULT_BOLD);
        super.setPadding(res.getDimensionPixelSize(com.twitter.sdk.android.core.R.dimen.tw__login_btn_left_padding), 0, res.getDimensionPixelSize(com.twitter.sdk.android.core.R.dimen.tw__login_btn_right_padding), 0);
        super.setBackgroundResource(com.twitter.sdk.android.core.R.drawable.tw__login_btn);
        if(Build.VERSION.SDK_INT >= 21) {
            super.setAllCaps(false);
        }

    }

    protected Activity getActivity() {
        if(this.getContext() instanceof Activity) {
            return (Activity)this.getContext();
        } else if(this.isInEditMode()) {
            return null;
        } else {
            throw new IllegalStateException("TwitterLoginButton requires an activity. Override getActivity to provide the activity for this button.");
        }
    }
}
