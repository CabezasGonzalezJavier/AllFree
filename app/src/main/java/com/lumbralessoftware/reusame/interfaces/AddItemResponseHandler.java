package com.lumbralessoftware.reusame.interfaces;

import com.lumbralessoftware.reusame.models.Item;
import com.lumbralessoftware.reusame.models.ItemRequest;

/**
 * Created by javiergonzalezcabezas on 18/7/15.
 */
public interface AddItemResponseHandler {
    public void sendResponseSusccesful(Item response);
    public void sendResponseFail(String error);
}
