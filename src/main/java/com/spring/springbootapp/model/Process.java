package com.spring.springbootapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "process")
public class Process {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String patientEmail;

    private String staffEmailsList;

    private String stagesList;

    public Long getProcessID() {
        return id;
    }

    public void setProcessID(Long processID) {
        this.id = processID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatient() {
        return patientEmail;
    }

    public void setPatient(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getStaffList() {
        return staffEmailsList;
    }

    public void setStaffList(String staffEmailsList) {
        this.staffEmailsList = staffEmailsList;
    }

    public String getStagesList() {
        return stagesList;
    }

    public void setStagesList(String stagesList) {
        this.stagesList = stagesList;
    }

}
