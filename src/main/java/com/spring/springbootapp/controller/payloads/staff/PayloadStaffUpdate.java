package com.spring.springbootapp.controller.payloads.staff;

import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.StaffEntity;

import javax.validation.Valid;

public class PayloadStaffUpdate {

    public PayloadStaffUpdate(Credential credential, StaffEntity staff, String staffEmail) {
        this.credential = credential;
        this.staff = staff;
        this.staffEmail = staffEmail;
    }
    @Valid
    Credential credential;
    @Valid
    StaffEntity staff;

    @Valid
    private String staffEmail;
    public Credential getCredential() {
        return credential;
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public String getStaffEmail() {
        return staffEmail;
    }
}
