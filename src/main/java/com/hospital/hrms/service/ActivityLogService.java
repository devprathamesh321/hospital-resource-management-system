package com.hospital.hrms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.hrms.entity.ActivityLog;
import com.hospital.hrms.repository.ActivityLogRepository;

@Service
public class ActivityLogService {

    @Autowired
    private ActivityLogRepository repo;

    public void log(String title, String message, String level) {
        ActivityLog log = new ActivityLog();
        log.setTitle(title);
        log.setMessage(message);
        log.setLevel(level);
        repo.save(log);
    }

    public List<ActivityLog> recent() {
        return repo.findTop10ByOrderByCreatedAtDesc();
    }
}
