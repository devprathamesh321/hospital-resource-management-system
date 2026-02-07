package com.hospital.hrms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hospital.hrms.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
