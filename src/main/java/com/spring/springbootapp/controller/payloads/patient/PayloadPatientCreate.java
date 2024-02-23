package com.spring.springbootapp.controller.payloads.patient;

import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.PatientEntity;

import javax.validation.Valid;

public class PayloadPatientCreate {

    public PayloadPatientCreate(Credential credential, PatientEntity patient) {
        this.credential = credential;
        this.patient = patient;
    }
    @Valid
    private Credential credential;

    @Valid
    private PatientEntity patient;

    public Credential getCredential() {
        return credential;
    }

    public PatientEntity getPatient() {
        return patient;
    }
}
