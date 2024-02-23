package com.spring.springbootapp.controller.payloads.patient;

import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.primaryKey.PatientId;

import javax.validation.Valid;

public class PayloadPatientGetById {

    public PayloadPatientGetById(Credential credential, PatientId patientId) {
        this.credential = credential;
        this.patientId = patientId;
    }
    @Valid
    private Credential credential;

    @Valid
    private PatientId patientId;

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    public void setPatientId(PatientId patientId) {
        this.patientId = patientId;
    }
}
