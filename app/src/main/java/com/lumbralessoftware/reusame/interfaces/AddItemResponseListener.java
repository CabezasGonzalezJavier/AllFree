package com.lumbralessoftware.reusame.interfaces;

import com.lumbralessoftware.reusame.models.Item;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 18/7/15.
 */
public interface AddItemResponseListener {
    public void onSuccess(final Item successResponse);
    public void onError(final String errorResponse);
}
