package com.lumbralessoftware.freeall.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by javiergonzalezcabezas on 28/6/15.
 */
public class DataWrapperItems implements Serializable {

    private ArrayList<Item> item;

    public DataWrapperItems(ArrayList<Item> data) {
        this.item = data;
    }

    public ArrayList<Item> getItems() {
        return this.item;
    }

}
