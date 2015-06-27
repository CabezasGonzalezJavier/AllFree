package com.lumbralessoftware.freeall.controller;

import com.lumbralessoftware.freeall.interfaces.ItemResponseListener;
import com.lumbralessoftware.freeall.interfaces.RegistrationResponseListener;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class ControllersFactory {

    private  static ItemsController sItemsController;
    private  static ItemResponseListener sItemsResponseListener;
    private  static RegistrationController sRegistrationController;
    private  static RegistrationResponseListener sRegistrationResponseListener;

    public static void setsItemResponseListener(ItemResponseListener responseListener){
        sItemsResponseListener = responseListener;
    }

    public static ItemsController getsItemsController() {
        if (sItemsController == null) {
            sItemsController = new ItemsController(sItemsResponseListener);
        }
        return sItemsController;
    }

    public static void setsRegistrationResponseListener(RegistrationResponseListener responseListener){
        sRegistrationResponseListener = responseListener;
    }

    public static RegistrationController getsRegistrationController() {
        if (sRegistrationController == null) {
            sRegistrationController = new RegistrationController(sRegistrationResponseListener);
        }
        return sRegistrationController;
    }
}
