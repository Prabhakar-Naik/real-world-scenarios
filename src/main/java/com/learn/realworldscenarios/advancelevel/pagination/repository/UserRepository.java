package com.learn.realworldscenarios.advancelevel.pagination.repository;

import com.learn.realworldscenarios.advancelevel.pagination.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author prabhakar, @Date 18-08-2025
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
