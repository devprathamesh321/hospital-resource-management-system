package com.hospital.hrms.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "emergencies")
public class Emergency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emergencyId;

    private String patientName;

    // ✅ FIELD NAME = severity
    private int severity;

    private String status;

    @ManyToOne
    @JoinColumn(name = "bed_id")
    private Bed assignedBed;

    // ---------- GETTERS & SETTERS ----------

    public Long getEmergencyId() {
        return emergencyId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getSeverity() {
        return severity;
    }

    // ✅ CORRECT SETTER
    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Bed getAssignedBed() {
        return assignedBed;
    }

    public void setAssignedBed(Bed assignedBed) {
        this.assignedBed = assignedBed;
    }
}
