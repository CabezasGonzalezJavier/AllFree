package com.lumbralessoftware.freeall.interfaces;

import com.lumbralessoftware.freeall.models.Item;
import com.lumbralessoftware.freeall.models.VotingResult;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public interface VoteResponseHandler {

    public void sendResponseSusccesful(VotingResult response);
    public void sendResponseFail(String error);
}
