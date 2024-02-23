package com.spring.springbootapp.controller.payloads.patient;

import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.PatientEntity;
import com.spring.springbootapp.model.primaryKey.PatientId;

import javax.validation.Valid;

public class PayloadPatientUpdate {
    public PayloadPatientUpdate(Credential credential, PatientEntity patient, PatientId patientId) {
        this.credential = credential;
        this.patient = patient;
        this.patientId = patientId;
    }
    @Valid
    private Credential credential;
    @Valid
    private PatientEntity patient;
    @Valid
    private PatientId patientId;
    public Credential getCredential() {
        return credential;
    }

    public PatientEntity getPatient() {
        return patient;
    }

    public PatientId getPatientId() {
        return patientId;
    }
}
