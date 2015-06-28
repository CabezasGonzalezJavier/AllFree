package com.lumbralessoftware.freeall.interfaces;

import com.lumbralessoftware.freeall.models.Item;
import com.lumbralessoftware.freeall.models.VotingResult;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public interface VoteResponseListener {
    public void onSuccess(final VotingResult successResponse);
    public void onError(final String errorResponse);
}
