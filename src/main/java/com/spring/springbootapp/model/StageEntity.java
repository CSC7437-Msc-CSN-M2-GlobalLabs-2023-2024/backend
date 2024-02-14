package com.spring.springbootapp.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "stage")
public class StageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private boolean completed;
    @NotBlank
    @Email
    private Long staffEmail;

    public Long getId() {
        return id;
    }

    public void setId(Long stageID) {
        this.id = stageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Long getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(Long staffEmail) {
        this.staffEmail = staffEmail;
    }

}
