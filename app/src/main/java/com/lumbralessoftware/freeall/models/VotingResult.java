package com.lumbralessoftware.freeall.models;

import com.google.gson.annotations.Expose;

public class VotingResult {

    @Expose
    private Double rating;
    @Expose
    private String error;

    /**
     * @return The rating
     */
    public Double getRating() {
        return rating;
    }

    /**
     * @param rating The rating
     */
    public void setRating(Double rating) {
        this.rating = rating;
    }

    /**
     * @return The error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error The error
     */
    public void setError(String error) {
        this.error = error;
    }

}