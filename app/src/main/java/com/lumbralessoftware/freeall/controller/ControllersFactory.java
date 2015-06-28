package com.lumbralessoftware.freeall.controller;

import com.lumbralessoftware.freeall.interfaces.ItemRequestResponseListener;
import com.lumbralessoftware.freeall.interfaces.ItemResponseListener;
import com.lumbralessoftware.freeall.interfaces.RegistrationResponseListener;
import com.lumbralessoftware.freeall.interfaces.VoteResponseListener;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class ControllersFactory {

    private  static ItemsController sItemsController;
    private  static ItemResponseListener sItemsResponseListener;

    private  static VoteResponseListener sVoteResponseListener;
    private  static RegistrationController sRegistrationController;
    private  static RegistrationResponseListener sRegistrationResponseListener;

    public static void setItemResponseListener(ItemResponseListener responseListener){
        sItemsResponseListener = responseListener;
    }
    public static void setsVoteResponseListener(VoteResponseListener responseListener){
        sVoteResponseListener = responseListener;
    }

    public static ItemsController getItemsController() {
        if (sItemsController == null) {
            sItemsController = new ItemsController(sItemsResponseListener, sVoteResponseListener);
        }
        return sItemsController;
    }

    public static void setRegistrationResponseListener(RegistrationResponseListener responseListener){
        sRegistrationResponseListener = responseListener;
    }

    public static RegistrationController getRegistrationController() {
        if (sRegistrationController == null) {
            sRegistrationController = new RegistrationController(sRegistrationResponseListener);
        }
        return sRegistrationController;
    }
}
