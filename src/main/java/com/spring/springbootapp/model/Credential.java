package com.spring.springbootapp.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.spring.springbootapp.repository.StaffRepo;

public class Credential {
    private StaffRepo staffRepo;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String passwordHash;

    public Credential() {
        // Default constructor
    }

    public Credential(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public StaffRepo getStaffRepo() {
        return staffRepo;
    }

    public void setStaffRepo(StaffRepo staffRepo) {
        this.staffRepo = staffRepo;
    }


public boolean isValid() {
        if(email != null && passwordHash != null && staffRepo.existsByEmail(email)) {
            return staffRepo.findByEmail(email).isPresent() && staffRepo.findByEmail(email).get().getPasswordHash().equals(passwordHash);
        }
        return false;
    }
}
