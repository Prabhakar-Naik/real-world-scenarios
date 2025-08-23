package com.learn.realworldscenarios.advancelevel.pagination.repository;

import com.learn.realworldscenarios.advancelevel.pagination.dto.UserProjection;
import com.learn.realworldscenarios.advancelevel.pagination.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * @author prabhakar, @Date 18-08-2025
 */
@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<UserProjection> findNextPage(Instant lastCreatedAt, Long lastId, int limit) {
        // JPQL selecting User entity and mapping to projection manually
        String jpql = "SELECT u FROM User u " +
                "WHERE (u.createdAt < :lastCreatedAt) " +
                "OR (u.createdAt = :lastCreatedAt AND u.id < :lastId) " +
                "ORDER BY u.createdAt DESC, u.id DESC";

        TypedQuery<User> query = em.createQuery(jpql, User.class);
        query.setParameter("lastCreatedAt", lastCreatedAt);
        query.setParameter("lastId", lastId);
        query.setMaxResults(limit);

        List<User> users = query.getResultList();

        // map to projection (lightweight DTO)
        return users.stream()
                .map(u -> new UserProjection(u.getId(), u.getName(), u.getCreatedAt()))
                .toList();
    }
}
