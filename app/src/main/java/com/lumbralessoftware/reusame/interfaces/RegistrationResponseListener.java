package com.lumbralessoftware.reusame.interfaces;

import com.lumbralessoftware.reusame.models.Registration;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public interface RegistrationResponseListener {
    public void onSuccess(final Registration successResponse);
    public void onError(final String errorResponse);
}
