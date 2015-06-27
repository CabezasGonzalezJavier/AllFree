package com.lumbralessoftware.freeall.controller;

import com.lumbralessoftware.freeall.interfaces.ItemResponseListener;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class ItemsControllersFactory {

    private  static ItemsController sItemsController;
    private  static ItemResponseListener sResponseListener;

    public static void setsResponseListener(ItemResponseListener responseListener){
        sResponseListener = responseListener;
    }

    public static ItemsController getsItemsController() {
        if (sItemsController == null) {
            sItemsController = new ItemsController(sResponseListener);
        }
        return sItemsController;
    }
}
