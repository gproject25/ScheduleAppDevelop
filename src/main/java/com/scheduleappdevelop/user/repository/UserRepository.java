package com.scheduleappdevelop.user.repository;

import com.scheduleappdevelop.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByDeletedFalse();

    Optional<User> findByIdAndDeletedFalse(Long id);

    Optional<User> findByEmailAndDeletedFalse(String email);

    boolean existsByEmail(String email);
}
