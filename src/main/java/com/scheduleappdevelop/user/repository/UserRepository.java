package com.scheduleappdevelop.user.repository;

import com.scheduleappdevelop.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
