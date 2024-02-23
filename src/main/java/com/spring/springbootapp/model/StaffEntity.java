package com.spring.springbootapp.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "staff")
public class StaffEntity {

    public StaffEntity(String email, String passwordHash, String firstName, String lastName, String position, boolean admin, List<Long> processIds) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.admin = admin;
        this.processIds = processIds;
    }

    public StaffEntity() {
    }

    @NotBlank
    @Email
    @Id
    private String email;
    @NotBlank
    private String passwordHash;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String position;

    @NotBlank
    @NotNull
    private boolean admin;

    @ElementCollection
    @CollectionTable(name = "staff_process_ids", joinColumns = @JoinColumn(name = "staff_email"))
    @Column(name = "process_id")
    private List<Long> processIds;

    public List<Long> getProcessIds() {
        return processIds;
    }

    public void setProcessIds(List<Long> processIds) {
        this.processIds = processIds;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void addProcess(ProcessEntity process) {
        processIds.add(process.getId());
    }

    public void removeProcess(ProcessEntity process) {
        processIds.remove(process.getId());
    }
}
