package com.lumbralessoftware.freeall.interfaces;

import com.lumbralessoftware.freeall.models.Item;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public interface UpdateableFragment {
    public void update(List<Item> items);
}
