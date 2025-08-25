package com.learn.realworldscenarios.advancelevel.sensitiveDataResponse.dto;

/**
 * @author prabhakar, @Date 11-08-2025
 */
// CardRequest: accepts raw PAN in request (POST)
public class CardRequest {

    private String ownerName;
    private String pan; // raw PAN from the client

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }
}
