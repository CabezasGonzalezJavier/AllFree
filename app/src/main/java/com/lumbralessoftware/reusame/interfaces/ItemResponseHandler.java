package com.lumbralessoftware.reusame.interfaces;

import com.lumbralessoftware.reusame.models.Item;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public interface ItemResponseHandler {

    public void sendResponseSusccesful(List<Item> response);
    public void sendResponseFail(String error);
}
