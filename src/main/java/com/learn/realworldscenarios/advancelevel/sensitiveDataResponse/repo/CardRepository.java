package com.learn.realworldscenarios.advancelevel.sensitiveDataResponse.repo;

import com.learn.realworldscenarios.advancelevel.sensitiveDataResponse.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author prabhakar, @Date 11-08-2025
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

}
