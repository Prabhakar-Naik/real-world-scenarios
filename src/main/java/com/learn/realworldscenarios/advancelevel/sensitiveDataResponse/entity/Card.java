package com.learn.realworldscenarios.advancelevel.sensitiveDataResponse.entity;

import com.learn.realworldscenarios.advancelevel.sensitiveDataResponse.crypto.AesGcmConverter;
import jakarta.persistence.*;

/**
 * @author prabhakar, @Date 11-08-2025
 */
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ownerName;

    @Convert(converter = AesGcmConverter.class)
    @Column(name = "pan_encrypted", length = 1024)
    private String panEncrypted; // encrypted PAN stored here

    // constructors, getters setters (avoid auto-generated toString that prints fields)
    public Card() {
    }

    public Card(String ownerName, String panEncrypted) {
        this.ownerName = ownerName;
        this.panEncrypted = panEncrypted;
    }

    public Long getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPanEncrypted() {
        return panEncrypted;
    }

    public void setPanEncrypted(String panEncrypted) {
        this.panEncrypted = panEncrypted;
    }
}
