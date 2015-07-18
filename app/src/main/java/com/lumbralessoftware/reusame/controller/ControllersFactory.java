package com.lumbralessoftware.reusame.controller;

import com.lumbralessoftware.reusame.interfaces.AddItemResponseListener;
import com.lumbralessoftware.reusame.interfaces.ItemRequestResponseListener;
import com.lumbralessoftware.reusame.interfaces.ItemResponseListener;
import com.lumbralessoftware.reusame.interfaces.RegistrationResponseListener;
import com.lumbralessoftware.reusame.interfaces.VoteResponseListener;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class ControllersFactory {

    private  static ItemsController sItemsController;
    private  static UserController sUserController;
    public  static SearchController sSearchController;
    private  static AddItemController sAddItemController;

    private  static ItemResponseListener sItemsResponseListener;
    private  static ItemRequestResponseListener sItemRequestResponseListener;
    private  static VoteResponseListener sVoteResponseListener;
    private  static RegistrationController sRegistrationController;
    private  static RegistrationResponseListener sRegistrationResponseListener;
    private static AddItemResponseListener sAddItemResponseListener;

    public static void setItemResponseListener(ItemResponseListener responseListener){
        sItemsResponseListener = responseListener;
    }
    public static void setsVoteResponseListener(VoteResponseListener responseListener){
        sVoteResponseListener = responseListener;
    }
    public static void setItemRequestResponseListener(ItemRequestResponseListener responseListener){
        sItemRequestResponseListener = responseListener;
    }
    public static void setRegistrationResponseListener(RegistrationResponseListener responseListener){
        sRegistrationResponseListener = responseListener;
    }
    public static void setAddItemResponseListener(AddItemResponseListener responseListener){
        sAddItemResponseListener = responseListener;
    }

    public static AddItemController getAddItemController() {
        if (sAddItemController == null) {
            sAddItemController = new AddItemController(sAddItemResponseListener);
        }
        return sAddItemController;
    }

    public static ItemsController getItemsController() {
        if (sItemsController == null) {
            sItemsController = new ItemsController(sItemsResponseListener);
        }
        return sItemsController;
    }

    public static SearchController getsSearchController () {
        if (sSearchController == null) {
            sSearchController = new SearchController(sItemsResponseListener);
        }
        return sSearchController;
    }

    public static UserController getUserController() {

        if (sUserController == null) {
            sUserController = new UserController(sVoteResponseListener, sItemRequestResponseListener);
        }
        return sUserController;
    }


    public static RegistrationController getRegistrationController() {
        if (sRegistrationController == null) {
            sRegistrationController = new RegistrationController(sRegistrationResponseListener);
        }
        return sRegistrationController;
    }


}
