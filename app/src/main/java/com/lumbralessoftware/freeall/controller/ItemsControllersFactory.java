package com.lumbralessoftware.freeall.controller;

import com.lumbralessoftware.freeall.webservice.ResponseListener;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class ItemsControllersFactory {

    private  static ItemsController sItemsController;
    private  static ResponseListener sResponseListener;

    public static void setsResponseListener(ResponseListener responseListener){
        sResponseListener = responseListener;
    }

    public static ItemsController getsItemsController() {
        if (sItemsController == null) {
            sItemsController = new ItemsController(sResponseListener);
        }
        return sItemsController;
    }
}
