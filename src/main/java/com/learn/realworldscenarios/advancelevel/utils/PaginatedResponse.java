package com.learn.realworldscenarios.advancelevel.utils;

import lombok.*;

import java.util.List;
import java.util.Optional;

/**
 * @author prabhakar, @Date 18-08-2025
 */
@Setter
@Getter
@Data
public class PaginatedResponse<T> {
    private List<T> items;
    private String nextCursor; // base64 encoded optional

    public PaginatedResponse() {}
    public PaginatedResponse(List<T> items, String nextCursor) {
        this.items = items;
        this.nextCursor = nextCursor;
    }

    public List<T> getItems() { return items; }
    public void setItems(List<T> items) { this.items = items; }

    public Optional<String> getNextCursor() { return Optional.ofNullable(nextCursor); }
    public void setNextCursor(String nextCursor) { this.nextCursor = nextCursor; }
}
