package com.lumbralessoftware.reusame.interfaces;

import com.lumbralessoftware.reusame.models.VotingResult;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public interface VoteResponseListener {
    public void onSuccess(final VotingResult successResponse);
    public void onError(final String errorResponse);
}
