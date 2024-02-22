package com.spring.springbootapp.controller.payloads.patient;

import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.primaryKey.PatientId;

import javax.validation.Valid;

public class PayloadPatientDelete {
    @Valid
    private Credential credential;

    @Valid
    private PatientId patientId;


    public Credential getCredential() {
        return credential;
    }

    public PatientId getPatientId() {
        return patientId;
    }
}
