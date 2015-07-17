package com.lumbralessoftware.reusame.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class Location implements Serializable {

    @Expose
    private String location;
    @SerializedName("long_position")
    @Expose
    private String longPosition;
    @SerializedName("lat_position")
    @Expose
    private String latPosition;

    /**
     *
     * @return
     * The location
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return
     * The longPosition
     */
    public String getLongPosition() {
        return longPosition;
    }

    /**
     *
     * @param longPosition
     * The long_position
     */
    public void setLongPosition(String longPosition) {
        this.longPosition = longPosition;
    }

    /**
     *
     * @return
     * The latPosition
     */
    public String getLatPosition() {
        return latPosition;
    }

    /**
     *
     * @param latPosition
     * The lat_position
     */
    public void setLatPosition(String latPosition) {
        this.latPosition = latPosition;
    }

}
