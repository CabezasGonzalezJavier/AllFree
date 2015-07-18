package com.lumbralessoftware.reusame.controller;

import com.lumbralessoftware.reusame.interfaces.AddItemResponseHandler;
import com.lumbralessoftware.reusame.interfaces.AddItemResponseListener;
import com.lumbralessoftware.reusame.models.Item;
import com.lumbralessoftware.reusame.webservice.Client;

/**
 * Created by javiergonzalezcabezas on 18/7/15.
 */
public class AddItemController implements AddItemResponseHandler {

    private AddItemResponseListener mAddItemResponseListener;

    public AddItemController(AddItemResponseListener addItemResponseListener) {
        mAddItemResponseListener = addItemResponseListener;
    }

    public void request(Item item) {
        Client.createItem(this,item);
    }


    @Override
    public void sendResponseSusccesful(Item response) {
        mAddItemResponseListener.onSuccess(response);
    }

    @Override
    public void sendResponseFail(String error) {
        mAddItemResponseListener.onError(error);
    }
}
