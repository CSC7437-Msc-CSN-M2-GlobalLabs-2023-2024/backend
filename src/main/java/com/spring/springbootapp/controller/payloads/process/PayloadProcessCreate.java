package com.spring.springbootapp.controller.payloads.process;

import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.ProcessEntity;

import javax.validation.Valid;

public class PayloadProcessCreate {
    @Valid
    private Credential credential;
    @Valid
    private ProcessEntity process;
    public Credential getCredential() {
        return credential;
    }

    public ProcessEntity getProcess() {
        return process;
    }
}
