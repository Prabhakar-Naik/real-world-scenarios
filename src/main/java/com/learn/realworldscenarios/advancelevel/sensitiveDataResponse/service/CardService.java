package com.learn.realworldscenarios.advancelevel.sensitiveDataResponse.service;

import com.learn.realworldscenarios.advancelevel.sensitiveDataResponse.dto.CardRequest;
import com.learn.realworldscenarios.advancelevel.sensitiveDataResponse.dto.CardResponse;
import com.learn.realworldscenarios.advancelevel.sensitiveDataResponse.entity.Card;
import com.learn.realworldscenarios.advancelevel.sensitiveDataResponse.repo.CardRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author prabhakar, @Date 11-08-2025
 */
@Service
public class CardService {

    private final CardRepository repo;

    public CardService(CardRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public CardResponse create(@NotNull CardRequest request) {
        // Validate PAN format/length here (Luhn if needed) — minimal example:
        String pan = request.getPan().replace("\\s+","");
        if (pan.length() < 12 || pan.length() > 19) {
            throw new IllegalArgumentException("Invalid pan length");
        }

        Card card = new Card();
        card.setOwnerName(request.getOwnerName());
        card.setPanEncrypted(pan); // AttributeConverter will encrypt before DB write

        Card savedCard = repo.save(card);
        String masked = maskPan(pan);
        return new CardResponse(savedCard.getId(), savedCard.getOwnerName(), masked);
    }

    @Transactional(readOnly = true)
    public CardResponse get(Long id) {
        Card card = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Card not found"));
        String decryptedPan  = card.getPanEncrypted(); // converter decrypted it for us
        return new CardResponse(card.getId(), card.getOwnerName(), maskPan(decryptedPan));
    }

    @Transactional(readOnly = true)
    public List<CardResponse> getAll(){
        List<Card> cards = repo.findAll();
        List<CardResponse> cardResponses = new ArrayList<>();
        for (Card card : cards) {
            cardResponses.add(new CardResponse(card.getId(), card.getOwnerName(), maskPan(card.getPanEncrypted())));
        }
        return cardResponses;
    }

    private @NotNull String maskPan(@NotNull String pan) {
        // show only last 2 digits, group with 41** **** **** **** style
        // String first2 = pan.length() > 4 ? pan.substring(0,2) : pan;
        // show only last 4 digits, group with **** **** **** 1234 style
        String last4 = pan.length() > 4 ? pan.substring(pan.length() - 4) : pan;
        //return first2+"** **** **** " + last4;
        return "**** **** **** " + last4;
    }

}
