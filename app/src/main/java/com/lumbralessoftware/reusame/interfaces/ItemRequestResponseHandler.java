package com.lumbralessoftware.reusame.interfaces;

import com.lumbralessoftware.reusame.models.ItemRequest;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public interface ItemRequestResponseHandler {

    public void sendResponseSusccesful(ItemRequest response);
    public void sendResponseFail(String error);
}
