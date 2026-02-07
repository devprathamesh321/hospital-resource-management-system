package com.hospital.hrms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hospital.hrms.entity.Bed;
import com.hospital.hrms.entity.Emergency;
import com.hospital.hrms.repository.BedRepository;
import com.hospital.hrms.repository.EmergencyRepository;

import java.util.Optional;

@Controller
@RequestMapping("/emergency")
public class EmergencyController {

    @Autowired
    private EmergencyRepository emergencyRepository;

    @Autowired
    private BedRepository bedRepository;

    // ✅ OPEN EMERGENCY PAGE (NO 404)
    @GetMapping
    public String emergencyPage(Model model) {
        model.addAttribute("emergencies", emergencyRepository.findAll());
        return "emergency";
    }

    // ✅ REGISTER PATIENT
    @PostMapping("/register")
    public String registerEmergency(
            @RequestParam String patientName,
            @RequestParam String severity) {

        int severityLevel;
        switch (severity) {
            case "Critical": severityLevel = 4; break;
            case "High": severityLevel = 3; break;
            case "Medium": severityLevel = 2; break;
            default: severityLevel = 1;
        }

        Emergency e = new Emergency();
        e.setPatientName(patientName);
        e.setSeverity(severityLevel); // ✅ FIXED
        e.setStatus("OPEN");

        emergencyRepository.save(e);

        return "redirect:/emergency";
    }

    // ✅ ASSIGN BED (NO 404)
    @GetMapping("/assignBed/{id}")
    public String assignBed(@PathVariable Long id) {

        Optional<Emergency> emergencyOpt = emergencyRepository.findById(id);
        if (emergencyOpt.isEmpty()) {
            return "redirect:/emergency";
        }

        Emergency emergency = emergencyOpt.get();

        Optional<Bed> bedOpt = bedRepository.findAll()
                .stream()
                .filter(b -> "AVAILABLE".equalsIgnoreCase(b.getStatus()))
                .findFirst();

        if (bedOpt.isPresent()) {
            Bed bed = bedOpt.get();
            bed.setStatus("OCCUPIED");

            emergency.setAssignedBed(bed);
            emergency.setStatus("ASSIGNED");

            bedRepository.save(bed);
            emergencyRepository.save(emergency);
        }

        return "redirect:/emergency";
    }
}
