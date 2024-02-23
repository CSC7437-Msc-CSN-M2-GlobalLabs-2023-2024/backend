package com.spring.springbootapp.controller.payloads.staff;

import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.StaffEntity;

import javax.validation.Valid;

public class PayloadStaffCreate {

    public PayloadStaffCreate(Credential credential, StaffEntity staff) {
        this.credential = credential;
        this.staff = staff;
    }
    @Valid
    private Credential credential;
    @Valid
    private StaffEntity staff;
    public Credential getCredential() {
        return credential;
    }

    public StaffEntity getStaff() {
        return staff;
    }
}
