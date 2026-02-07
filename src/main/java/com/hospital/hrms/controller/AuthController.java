package com.hospital.hrms.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hospital.hrms.entity.User;
import com.hospital.hrms.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // ---------------- HOME ----------------
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // ---------------- LOGIN PAGE ----------------
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // ---------------- LOGIN ACTION ----------------
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {

        User user = userService.authenticate(username, password);

        if (user == null) {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }

        session.setAttribute("username", user.getUsername());

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/admin/dashboard";
        } else if ("DOCTOR".equalsIgnoreCase(user.getRole())) {
            return "redirect:/doctor/dashboard";
        } else {
            return "redirect:/emergency/dashboard";
        }
    }

    // ---------------- REGISTER PAGE ----------------
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // ---------------- REGISTER ACTION ----------------
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String role,
                           Model model) {

        if (userService.findByUsername(username) != null) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // academic project OK
        user.setRole(role);

        userService.saveUser(user);

        model.addAttribute("success", "Registration successful. Please login.");
        return "login";
    }

    // ---------------- FORGOT PASSWORD ----------------
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String resetPassword(@RequestParam String username,
                                @RequestParam String newPassword,
                                Model model) {

        User user = userService.findByUsername(username);

        if (user == null) {
            model.addAttribute("error", "Username not found");
            return "forgot-password";
        }

        user.setPassword(newPassword);
        userService.saveUser(user);

        model.addAttribute("success", "Password reset successful. Please login.");
        return "login";
    }

    // ---------------- LOGOUT ----------------
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}