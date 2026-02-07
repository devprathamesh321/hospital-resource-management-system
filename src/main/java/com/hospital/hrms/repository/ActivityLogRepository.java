package com.hospital.hrms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hospital.hrms.entity.ActivityLog;
import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findTop10ByOrderByCreatedAtDesc();
}
