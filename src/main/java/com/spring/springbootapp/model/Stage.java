package com.spring.springbootapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "stage")
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private boolean completed;
    @NotBlank
    private Long staffEmail;

    public Long getStageID() {
        return id;
    }

    public void setStageID(Long stageID) {
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

    public Long getStaffID() {
        return staffEmail;
    }

    public void setStaffID(Long staffEmail) {
        this.staffEmail = staffEmail;
    }

}
