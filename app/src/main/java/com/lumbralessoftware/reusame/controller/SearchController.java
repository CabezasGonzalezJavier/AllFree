package com.lumbralessoftware.reusame.controller;

import com.lumbralessoftware.reusame.interfaces.ItemResponseHandler;
import com.lumbralessoftware.reusame.interfaces.ItemResponseListener;
import com.lumbralessoftware.reusame.models.Item;
import com.lumbralessoftware.reusame.webservice.Client;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 4/7/15.
 */
public class SearchController implements ItemResponseHandler {

    public ItemResponseListener mResponseListener;

    public SearchController(ItemResponseListener mResponseListener) {
        this.mResponseListener = mResponseListener;
    }

    public void request(String name){
        Client.getSearch(this, name);
    }

    @Override
    public void sendResponseSusccesful(List<Item> response) {
        mResponseListener.onSuccess(response);
    }

    @Override
    public void sendResponseFail(String error) {

    }
}
