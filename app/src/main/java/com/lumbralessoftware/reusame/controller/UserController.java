package com.lumbralessoftware.reusame.controller;

import com.lumbralessoftware.reusame.interfaces.ItemRequestResponseHandler;
import com.lumbralessoftware.reusame.interfaces.ItemRequestResponseListener;
import com.lumbralessoftware.reusame.interfaces.VoteResponseHandler;
import com.lumbralessoftware.reusame.interfaces.VoteResponseListener;
import com.lumbralessoftware.reusame.models.ItemRequest;
import com.lumbralessoftware.reusame.models.VotingResult;
import com.lumbralessoftware.reusame.webservice.Client;

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
        mItemRequestResponseListener.onError(error);
    }
}
