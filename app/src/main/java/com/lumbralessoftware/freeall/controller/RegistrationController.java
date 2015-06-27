package com.lumbralessoftware.freeall.controller;

import com.lumbralessoftware.freeall.interfaces.ItemResponseHandler;
import com.lumbralessoftware.freeall.interfaces.ItemResponseListener;
import com.lumbralessoftware.freeall.interfaces.RegistrationResponseHandler;
import com.lumbralessoftware.freeall.interfaces.RegistrationResponseListener;
import com.lumbralessoftware.freeall.models.Item;
import com.lumbralessoftware.freeall.models.Registration;
import com.lumbralessoftware.freeall.models.Token;
import com.lumbralessoftware.freeall.utils.Constants;
import com.lumbralessoftware.freeall.webservice.Client;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class RegistrationController implements RegistrationResponseHandler {
    public RegistrationResponseListener mResponseListener;

    public RegistrationController(RegistrationResponseListener mResponseListener) {
        this.mResponseListener = mResponseListener;
    }

    public void request(String access_token, String access_secret) {
        Token token = new Token();
        token.setAccessToken(access_token);
        token.setAccessSecret(access_secret);
        Client.postRegistrationToken(this, Constants.BACKEND_TWITTER, token);
    }



    @Override
    public void sendResponseSusccesful(Registration response) {
        mResponseListener.onSuccess(response);
    }

    @Override
    public void sendResponseFail(String error) {

    }
}
