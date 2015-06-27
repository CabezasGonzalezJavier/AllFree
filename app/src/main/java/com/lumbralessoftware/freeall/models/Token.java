package com.lumbralessoftware.freeall.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("access_secret")
    @Expose
    private String accessSecret;

    /**
     *
     * @return
     * The accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     *
     * @param accessToken
     * The access_token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     *
     * @return
     * The accessSecret
     */
    public String getAccessSecret() {
        return accessSecret;
    }

    /**
     *
     * @param accessSecret
     * The access_secret
     */
    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

}