package com.hospital.hrms.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.hospital.hrms.entity.Bed;

public interface BedRepository extends JpaRepository<Bed, Long> {

    Optional<Bed> findFirstByStatusAndActiveTrue(String status);

    List<Bed> findByActiveTrue();

    long countByActiveTrue();
}
