package com.lumbralessoftware.freeall.controller;

import com.lumbralessoftware.freeall.interfaces.ItemRequestResponseHandler;
import com.lumbralessoftware.freeall.interfaces.ItemRequestResponseListener;
import com.lumbralessoftware.freeall.interfaces.ItemResponseHandler;
import com.lumbralessoftware.freeall.interfaces.ItemResponseListener;
import com.lumbralessoftware.freeall.interfaces.VoteResponseHandler;
import com.lumbralessoftware.freeall.interfaces.VoteResponseListener;
import com.lumbralessoftware.freeall.models.Item;
import com.lumbralessoftware.freeall.models.ItemRequest;
import com.lumbralessoftware.freeall.models.VotingResult;
import com.lumbralessoftware.freeall.webservice.Client;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class UserController implements VoteResponseHandler, ItemRequestResponseHandler {
    public VoteResponseListener mVoteResponseListener;
    public ItemRequestResponseListener mItemRequestResponseListener;

    public UserController(VoteResponseListener voteResponseListener, ItemRequestResponseListener itemRequestResponseListener) {
        mItemRequestResponseListener = itemRequestResponseListener;
        mVoteResponseListener = voteResponseListener;
    }

    public void vote(Integer itemId, Double score) {
        Client.voteItem(this, itemId, score);
    }

    public void want(Integer itemId, ItemRequest item) {
        Client.wantItem(this, itemId, item);
    }

    @Override
    public void sendResponseSusccesful(VotingResult response) {
        mVoteResponseListener.onSuccess(response);
    }

    @Override
    public void sendResponseSusccesful(ItemRequest response) {
        mItemRequestResponseListener.onSuccess(response);
    }

    @Override
    public void sendResponseFail(String error) {

    }
}
