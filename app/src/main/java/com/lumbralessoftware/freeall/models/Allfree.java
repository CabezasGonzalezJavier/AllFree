package com.lumbralessoftware.freeall.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class Allfree {

    @Expose
    private Integer id;
    @Expose
    private String name;
    @Expose
    private String description;
    @Expose
    private String image;
    @Expose
    private String category;
    @Expose
    private Location location;
    @Expose
    private String owner;
    @Expose
    private String created;
    @SerializedName("expires_on")
    @Expose
    private Object expiresOn;
    @SerializedName("user_rating")
    @Expose
    private Object userRating;

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The image
     */
    public String getImage() {
        return image;
    }

    /**
     *
     * @param image
     * The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     *
     * @return
     * The category
     */
    public String getCategory() {
        return category;
    }

    /**
     *
     * @param category
     * The category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     *
     * @return
     * The location
     */
    public Location getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     *
     * @return
     * The owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     *
     * @param owner
     * The owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     *
     * @return
     * The created
     */
    public String getCreated() {
        return created;
    }

    /**
     *
     * @param created
     * The created
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     *
     * @return
     * The expiresOn
     */
    public Object getExpiresOn() {
        return expiresOn;
    }

    /**
     *
     * @param expiresOn
     * The expires_on
     */
    public void setExpiresOn(Object expiresOn) {
        this.expiresOn = expiresOn;
    }

    /**
     *
     * @return
     * The userRating
     */
    public Object getUserRating() {
        return userRating;
    }

    /**
     *
     * @param userRating
     * The user_rating
     */
    public void setUserRating(Object userRating) {
        this.userRating = userRating;
    }

}
