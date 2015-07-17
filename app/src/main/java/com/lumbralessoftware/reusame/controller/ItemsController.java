package com.lumbralessoftware.reusame.controller;

import com.google.android.gms.maps.model.LatLng;
import com.lumbralessoftware.reusame.models.Item;
import com.lumbralessoftware.reusame.webservice.Client;
import com.lumbralessoftware.reusame.interfaces.ItemResponseHandler;
import com.lumbralessoftware.reusame.interfaces.ItemResponseListener;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class ItemsController implements ItemResponseHandler {
    public ItemResponseListener mResponseListener;

    public ItemsController(ItemResponseListener mResponseListener) {
        this.mResponseListener = mResponseListener;
    }

    public void request(LatLng coordinates){
        Client.getAllItems(this, coordinates);
    }

    @Override
    public void sendResponseSusccesful(List<Item> response) {
        mResponseListener.onSuccess(response);
    }


    @Override
    public void sendResponseFail(String error) {

    }
}
