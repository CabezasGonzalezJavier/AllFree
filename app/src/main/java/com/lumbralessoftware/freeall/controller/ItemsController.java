package com.lumbralessoftware.freeall.controller;

import com.lumbralessoftware.freeall.interfaces.VoteResponseHandler;
import com.lumbralessoftware.freeall.interfaces.VoteResponseListener;
import com.lumbralessoftware.freeall.models.Item;
import com.lumbralessoftware.freeall.models.VotingResult;
import com.lumbralessoftware.freeall.webservice.Client;
import com.lumbralessoftware.freeall.interfaces.ItemResponseHandler;
import com.lumbralessoftware.freeall.interfaces.ItemResponseListener;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class ItemsController implements ItemResponseHandler, VoteResponseHandler {
    public ItemResponseListener mResponseListener;
    public VoteResponseListener mVoteResponseListener;

    public ItemsController(ItemResponseListener mResponseListener, VoteResponseListener voteResponseListener) {
        this.mResponseListener = mResponseListener;
        mVoteResponseListener = voteResponseListener;
    }

    public void request(){
        Client.getAllItems(this);
    }

    public void vote(Integer itemId, Double score) {
        Client.voteItem(this, itemId, score);
    }

    @Override
    public void sendResponseSusccesful(List<Item> response) {
        mResponseListener.onSuccess(response);
    }

    @Override
    public void sendResponseSusccesful(VotingResult response) {
        mVoteResponseListener.onSuccess(response);
    }

    @Override
    public void sendResponseFail(String error) {

    }
}
