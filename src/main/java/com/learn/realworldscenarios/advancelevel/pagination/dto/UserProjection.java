package com.learn.realworldscenarios.advancelevel.pagination.dto;

import java.time.Instant;

/**
 * @author prabhakar, @Date 18-08-2025
 */

public class UserProjection {
    private final Long id;
    private final String name;
    private final Instant createdAt;

    public UserProjection(Long id, String name, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Instant getCreatedAt() { return createdAt; }
}
