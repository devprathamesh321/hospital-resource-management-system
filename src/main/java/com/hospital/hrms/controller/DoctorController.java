package com.hospital.hrms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hospital.hrms.entity.Doctor;
import com.hospital.hrms.service.DoctorService;

@Controller
@RequestMapping("/admin")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // ✅ LIST DOCTORS
    @GetMapping("/doctors")
    public String doctors(Model model) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "doctors";
    }

    // ✅ ADD DOCTOR
    @PostMapping("/addDoctor")
    public String addDoctor(Doctor doctor) {
        doctorService.saveDoctor(doctor);
        return "redirect:/admin/doctors";
    }

    // ✅ TOGGLE ON / OFF DUTY
    @GetMapping("/toggleDuty/{id}")
    public String toggleDuty(@PathVariable Long id) {
        doctorService.toggleDuty(id);
        return "redirect:/admin/doctors";
    }
}
