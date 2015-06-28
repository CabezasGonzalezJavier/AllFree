package com.lumbralessoftware.freeall.interfaces;

import com.lumbralessoftware.freeall.models.ItemRequest;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public interface ItemRequestResponseListener {
    public void onSuccess(final ItemRequest successResponse);
    public void onError(final String errorResponse);
}
