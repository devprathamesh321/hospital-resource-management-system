package com.hospital.hrms.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hospital.hrms.entity.Emergency;
import com.hospital.hrms.repository.EmergencyRepository;

@Controller
@RequestMapping("/doctor")
public class DoctorDashboardController {

    @Autowired
    private EmergencyRepository emergencyRepository;

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {

        List<Emergency> active = emergencyRepository.findByStatus("ASSIGNED");
        List<Emergency> completed = emergencyRepository.findByStatus("COMPLETED");

        model.addAttribute("emergencies", active);
        model.addAttribute("activeCount", active.size());
        model.addAttribute("completedCount", completed.size());
        model.addAttribute("redAlerts",
                active.stream().filter(e -> e.getSeverity() == 4).count());

        model.addAttribute("username", session.getAttribute("username"));

        return "doctor-dashboard";
    }

    // ================= FINALIZE =================
    @PostMapping("/finalize/{id}")
    public String finalizeTreatment(@PathVariable Long id) {
        Emergency e = emergencyRepository.findById(id).orElse(null);
        if (e != null) {
            e.setStatus("COMPLETED");
            emergencyRepository.save(e);
        }
        return "redirect:/doctor/dashboard";
    }

    // ================= HISTORY =================
    @GetMapping("/history")
    public String history(Model model) {
        model.addAttribute("history",
                emergencyRepository.findByStatus("COMPLETED"));
        return "doctor-history";
    }

    // ================= SETTINGS =================
    @GetMapping("/settings")
    public String settings() {
        return "doctor-settings";
    }
}
