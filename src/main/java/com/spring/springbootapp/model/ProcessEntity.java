package com.spring.springbootapp.model;

import com.spring.springbootapp.model.primaryKey.PatientId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "process")
public class ProcessEntity {

    public ProcessEntity(String name, PatientId patientId, List<String> staffEmails, List<Long> stageIds) {
        this.name = name;
        this.patientId = patientId;
        this.staffEmails = staffEmails;
        this.stageIds = stageIds;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private PatientId patientId;

    @ElementCollection
    @CollectionTable(name = "process_staff_emails", joinColumns = @JoinColumn(name = "process_id"))
    @Column(name = "staff_email")
    private List<String> staffEmails;

    @ElementCollection
    @CollectionTable(name = "process_stage_ids", joinColumns = @JoinColumn(name = "process_id"))
    @Column(name = "stage_id")
    private List<Long> stageIds;

    public ProcessEntity() {

    }

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

    public PatientId getPatientId() {
        return patientId;
    }

    public void setPatientId(PatientId patientId) {
        this.patientId = patientId;
    }

    public List<String> getStaffEmails() {
        return staffEmails;
    }

    public void setStaffEmails(List<String> staffEmails) {
        this.staffEmails = staffEmails;
    }

    public List<Long> getStageIds() {
        return stageIds;
    }

    public void setStageIds(List<Long> stageIds) {
        this.stageIds = stageIds;
    }

    public void addStaffEmail(String staffEmail) {
        staffEmails.add(staffEmail);
    }

    public void removeStaffEmail(String staffEmail) {
        staffEmails.remove(staffEmail);
    }

    public void addStageId(Long stageId) {
        stageIds.add(stageId);
    }

    public void removeStageId(Long stageId) {
        stageIds.remove(stageId);
    }

}
