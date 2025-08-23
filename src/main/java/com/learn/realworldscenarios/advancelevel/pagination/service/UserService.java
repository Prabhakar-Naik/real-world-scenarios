package com.learn.realworldscenarios.advancelevel.pagination.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.realworldscenarios.advancelevel.pagination.dto.Cursor;
import com.learn.realworldscenarios.advancelevel.pagination.dto.UserDto;
import com.learn.realworldscenarios.advancelevel.pagination.dto.UserProjection;
import com.learn.realworldscenarios.advancelevel.pagination.model.User;
import com.learn.realworldscenarios.advancelevel.pagination.repository.UserRepository;
import com.learn.realworldscenarios.advancelevel.pagination.repository.UserRepositoryCustom;
import com.learn.realworldscenarios.advancelevel.utils.PaginatedResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author prabhakar, @Date 18-08-2025
 */

@Service
public class UserService {

    private final UserRepositoryCustom repositoryCustom;
    private final UserRepository userRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    private static final int MAX_LIMIT = 200;

    public UserService(UserRepositoryCustom repo, UserRepository userRepository) {
        this.repositoryCustom = repo;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<UserDto> getUsers(String cursorBase64, int limit) {
        if (limit <= 0) limit = 50;
        limit = Math.min(limit, MAX_LIMIT);

        Instant lastCreatedAt = Instant.now();
        long lastId = Long.MAX_VALUE;

        if (cursorBase64 != null && !cursorBase64.isBlank()) {
            try {
                String json = new String(Base64.getUrlDecoder().decode(cursorBase64), StandardCharsets.UTF_8);
                Cursor c = mapper.readValue(json, Cursor.class);
                lastCreatedAt = Instant.ofEpochMilli(c.lastCreatedAtEpochMilli());
                lastId = c.lastId();
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid cursor");
            }
        }

        // fetch one extra to detect hasMore
        List<UserProjection> rows = repositoryCustom.findNextPage(lastCreatedAt, lastId, limit + 1);
        boolean hasMore = rows.size() > limit;
        if (hasMore) rows = rows.subList(0, limit);

        List<UserDto> dtos = rows.stream()
                .map(r -> new UserDto(r.getId(), r.getName()))
                .collect(Collectors.toList());

        String nextCursor = null;
        if (hasMore) {
            UserProjection last = rows.get(rows.size() - 1);
            Cursor next = new Cursor(last.getCreatedAt().toEpochMilli(), last.getId());
            try {
                String json = mapper.writeValueAsString(next);
                nextCursor = Base64.getUrlEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
            } catch (JsonProcessingException ignored) {
            }
        }

        return new PaginatedResponse<UserDto>(dtos, nextCursor);
    }


    // helper: generate test users in batches (for testing)
    @Transactional
    public int generateTestUsers(int count) {
        final int batch = 1000;
        int created = 0;
        while (created < count) {
            int toCreate = Math.min(batch, count - created);
            int finalCreated = created;
            List<User> batchList = java.util.stream.IntStream.range(0, toCreate)
                    .mapToObj(i -> new User("User-" + (finalCreated + i + 1), Instant.now()))
                    .collect(Collectors.toList());
            userRepository.saveAll(batchList);
            created += toCreate;
        }
        return created;
    }

}
