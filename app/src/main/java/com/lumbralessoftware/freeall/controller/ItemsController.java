package com.lumbralessoftware.freeall.controller;

import com.lumbralessoftware.freeall.models.Item;
import com.lumbralessoftware.freeall.webservice.Client;
import com.lumbralessoftware.freeall.webservice.ResponseHandler;
import com.lumbralessoftware.freeall.webservice.ResponseListener;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class ItemsController implements ResponseHandler {
    public ResponseListener mResponseListener;

    public ItemsController(ResponseListener mResponseListener) {
        this.mResponseListener = mResponseListener;
    }

    public void request(){
        Client.getAllItems(this);
    }



    @Override
    public void sendResponseSusccesful(List<Item> response) {
        mResponseListener.onSuccess(response);
    }

    @Override
    public void sendResponseFail(String error) {

    }
}
