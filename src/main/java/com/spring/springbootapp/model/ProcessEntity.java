package com.spring.springbootapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "process")
public class ProcessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String patientEmail;

    private String staffEmailsList;

    private String stagesList;

    public Long getId() {
        return id;
    }

    public void setId(Long processID) {
        this.id = processID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getStaffEmailsList() {
        return staffEmailsList;
    }

    public void setStaffEmailsList(String staffEmailsList) {
        this.staffEmailsList = staffEmailsList;
    }

    public String getStagesList() {
        return stagesList;
    }

    public void setStagesList(String stagesList) {
        this.stagesList = stagesList;
    }

}
