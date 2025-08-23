package com.learn.realworldscenarios.advancelevel.pagination.repository;

import com.learn.realworldscenarios.advancelevel.pagination.dto.UserProjection;

import java.time.Instant;
import java.util.List;

/**
 * @author prabhakar, @Date 18-08-2025
 */
public interface UserRepositoryCustom {

    List<UserProjection> findNextPage(Instant lastCreatedAt, Long lastId, int limit);
}
