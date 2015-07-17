package com.lumbralessoftware.reusame.interfaces;

import com.lumbralessoftware.reusame.models.ItemRequest;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public interface ItemRequestResponseListener {
    public void onSuccess(final ItemRequest successResponse);
    public void onError(final String errorResponse);
}
