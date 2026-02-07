package com.hospital.hrms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hospital.hrms.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
