package com.lumbralessoftware.freeall.interfaces;

import com.lumbralessoftware.freeall.models.Item;
import com.lumbralessoftware.freeall.models.Registration;
import com.lumbralessoftware.freeall.models.Token;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public interface RegistrationResponseHandler {

    public void sendResponseSusccesful(Registration response);
    public void sendResponseFail(String error);
}
