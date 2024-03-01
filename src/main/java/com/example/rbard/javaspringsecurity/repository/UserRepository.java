package com.example.rbard.javaspringsecurity.repository;

import com.example.rbard.javaspringsecurity.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByUsername(String username);
  Optional<User> findByUsername(String username);

}
