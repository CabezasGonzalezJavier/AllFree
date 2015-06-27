package com.lumbralessoftware.freeall.interfaces;

import com.lumbralessoftware.freeall.models.Item;
import com.lumbralessoftware.freeall.models.Registration;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public interface RegistrationResponseListener {
    public void onSuccess(final Registration successResponse);
    public void onError(final String errorResponse);
}
