package com.hospital.hrms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.hrms.entity.Doctor;
import com.hospital.hrms.repository.DoctorRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ActivityLogService activityLogService;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public void saveDoctor(Doctor doctor) {
        doctorRepository.save(doctor);

        activityLogService.log(
            "Doctor Added",
            "Dr. " + doctor.getName() + " registered (" + doctor.getSpecialization() + ")",
            "INFO"
        );
    }

    public void toggleDuty(Long id) {
        Doctor d = doctorRepository.findById(id).orElseThrow();
        d.setOnDuty(!d.isOnDuty());
        doctorRepository.save(d);

        activityLogService.log(
            "Doctor Status",
            "Dr. " + d.getName() + (d.isOnDuty() ? " is now ON DUTY" : " is now OFF DUTY"),
            "WARNING"
        );
    }
}
