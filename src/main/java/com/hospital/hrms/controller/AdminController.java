package com.hospital.hrms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hospital.hrms.entity.Bed;
import com.hospital.hrms.repository.BedRepository;
import com.hospital.hrms.service.ActivityLogService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private ActivityLogService activityLogService;

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        long totalBeds = bedRepository.countByActiveTrue();
        long occupiedBeds = bedRepository.findByActiveTrue()
                .stream()
                .filter(b -> "OCCUPIED".equalsIgnoreCase(b.getStatus()))
                .count();

        model.addAttribute("bedOccupancy", occupiedBeds + " / " + totalBeds);
        model.addAttribute("doctorsOnDuty", 25);
        model.addAttribute("activeEmergencies", 7);
        model.addAttribute("waitingPatients", 14);
        model.addAttribute("activities", activityLogService.recent());

        return "admin-dashboard";
    }

    // ================= BEDS =================
    @GetMapping("/beds")
    public String beds(Model model,
                       @RequestParam(required = false) String error) {

        model.addAttribute("beds", bedRepository.findByActiveTrue());
        model.addAttribute("error", error);

        return "beds";
    }

    @PostMapping("/addBed")
    public String addBed(@RequestParam String bedType,
                         @RequestParam String status,
                         @RequestParam String ward) {

        Bed bed = new Bed();
        bed.setBedType(bedType);
        bed.setStatus(status);
        bed.setWard(ward);
        bed.setActive(true);

        bedRepository.save(bed);

        activityLogService.log(
                "Bed Added",
                bedType + " bed added in " + ward,
                "INFO"
        );

        return "redirect:/admin/beds";
    }

    // ================= SOFT DELETE (FIXED) =================
    @PostMapping("/deleteBed/{id}")
    public String deleteBed(@PathVariable Long id) {

        Bed bed = bedRepository.findById(id).orElse(null);
        if (bed == null) {
            return "redirect:/admin/beds";
        }

        // ❌ Cannot remove occupied beds
        if ("OCCUPIED".equalsIgnoreCase(bed.getStatus())) {
            return "redirect:/admin/beds?error=occupied";
        }

        // ✅ SOFT DELETE
        bed.setActive(false);
        bedRepository.save(bed);

        activityLogService.log(
                "Bed Archived",
                "Bed ID " + id + " archived (FK safe)",
                "WARNING"
        );

        return "redirect:/admin/beds";
    }

    // ================= EDIT =================
    @GetMapping("/editBed/{id}")
    public String editBed(@PathVariable Long id, Model model) {

        Bed bed = bedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bed not found"));

        model.addAttribute("bed", bed);
        return "edit-bed";
    }

    @PostMapping("/updateBed")
    public String updateBed(@ModelAttribute Bed bed) {

        bedRepository.save(bed);

        activityLogService.log(
                "Bed Updated",
                "Bed ID " + bed.getBedId() + " updated",
                "INFO"
        );

        return "redirect:/admin/beds";
    }

    // ================= REPORTS =================
    @GetMapping("/reports")
    public String reports() {
        return "reports";
    }
}
