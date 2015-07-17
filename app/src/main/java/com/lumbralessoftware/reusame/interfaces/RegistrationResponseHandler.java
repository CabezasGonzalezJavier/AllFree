package com.lumbralessoftware.reusame.interfaces;

import com.lumbralessoftware.reusame.models.Registration;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public interface RegistrationResponseHandler {

    public void sendResponseSusccesful(Registration response);
    public void sendResponseFail(String error);
}
