package com.spring.springbootapp.controller.payloads.process;

import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.ProcessEntity;

import javax.validation.Valid;

public class PayloadProcessUpdate {

    public PayloadProcessUpdate(ProcessEntity process, Credential credential, Long processId) {
        this.process = process;
        this.credential = credential;
        this.processId = processId;
    }
    @Valid
    public ProcessEntity process;
    @Valid
    public Credential credential;
    @Valid
    public Long processId;

    public Credential getCredential() {
        return  credential;
    }

    public ProcessEntity getProcess() {
        return process;
    }

    public Long getProcessId() {
        return processId;
    }
}
