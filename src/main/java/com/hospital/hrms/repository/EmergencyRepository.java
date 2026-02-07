package com.hospital.hrms.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.hrms.entity.Emergency;

public interface EmergencyRepository extends JpaRepository<Emergency, Long> {

    List<Emergency> findByStatus(String status);
}
