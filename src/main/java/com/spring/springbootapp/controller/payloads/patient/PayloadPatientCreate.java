package com.spring.springbootapp.controller.payloads.patient;

import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.PatientEntity;

import javax.validation.Valid;

public class PayloadPatientCreate {
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
