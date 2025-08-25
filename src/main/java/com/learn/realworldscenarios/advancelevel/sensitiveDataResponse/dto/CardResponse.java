package com.learn.realworldscenarios.advancelevel.sensitiveDataResponse.dto;

/**
 * @author prabhakar, @Date 11-08-2025
 */

public class CardResponse {

    private Long id;
    private String ownerName;
    private String maskedPan;

    public CardResponse(Long id, String ownerName, String maskedPan) {
        this.id = id;
        this.ownerName = ownerName;
        this.maskedPan = maskedPan;
    }

    public Long getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getMaskedPan() {
        return maskedPan;
    }
}
