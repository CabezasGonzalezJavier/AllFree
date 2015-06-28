package com.lumbralessoftware.freeall.interfaces;

import com.lumbralessoftware.freeall.models.ItemRequest;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public interface ItemRequestResponseHandler {

    public void sendResponseSusccesful(ItemRequest response);
    public void sendResponseFail(String error);
}
