package com.lumbralessoftware.reusame.interfaces;

import com.lumbralessoftware.reusame.models.VotingResult;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public interface VoteResponseHandler {

    public void sendResponseSusccesful(VotingResult response);
    public void sendResponseFail(String error);
}
