package com.lumbralessoftware.reusame.interfaces;

import com.lumbralessoftware.reusame.models.Item;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public interface ItemResponseListener {
    public void onSuccess(final List<Item> successResponse);
    public void onError(final String errorResponse);
}
