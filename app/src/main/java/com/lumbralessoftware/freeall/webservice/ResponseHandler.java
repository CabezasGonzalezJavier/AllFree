package com.lumbralessoftware.freeall.webservice;

import com.lumbralessoftware.freeall.models.Item;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public interface ResponseHandler {

    public void sendResponseSusccesful(List<Item> response);
    public void sendResponseFail(String error);
}
