package com.lumbralessoftware.reusame.controller;

import com.lumbralessoftware.reusame.interfaces.RegistrationResponseHandler;
import com.lumbralessoftware.reusame.interfaces.RegistrationResponseListener;
import com.lumbralessoftware.reusame.models.Registration;
import com.lumbralessoftware.reusame.models.Token;
import com.lumbralessoftware.reusame.webservice.Client;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class RegistrationController implements RegistrationResponseHandler {
    public RegistrationResponseListener mResponseListener;

    public RegistrationController(RegistrationResponseListener mResponseListener) {
        this.mResponseListener = mResponseListener;
    }

    public void request(String backend, String access_token, String access_secret, String email) {
        Token token = new Token();
        token.setAccessToken(access_token);
        token.setAccessSecret(access_secret);
        token.setEmail(email);
        Client.postRegistrationToken(this, backend, token);
    }



    @Override
    public void sendResponseSusccesful(Registration response) {
        mResponseListener.onSuccess(response);
    }

    @Override
    public void sendResponseFail(String error) {

    }
}
