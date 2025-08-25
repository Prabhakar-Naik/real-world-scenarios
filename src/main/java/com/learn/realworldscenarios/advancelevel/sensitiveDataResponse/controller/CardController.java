package com.learn.realworldscenarios.advancelevel.sensitiveDataResponse.controller;

import com.learn.realworldscenarios.advancelevel.sensitiveDataResponse.dto.CardRequest;
import com.learn.realworldscenarios.advancelevel.sensitiveDataResponse.dto.CardResponse;
import com.learn.realworldscenarios.advancelevel.sensitiveDataResponse.service.CardService;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author prabhakar, @Date 11-08-2025
 */
@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<CardResponse> create(@RequestBody CardRequest cardRequest) {
        CardResponse response = cardService.create(cardRequest);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(response);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<CardResponse> getCardById(@PathVariable(required = false) Long id) {
        CardResponse response = cardService.get(id);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(response);
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<CardResponse>> getAllCards() {
        List<CardResponse> responseList = cardService.getAll();
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(responseList);
    }


}
