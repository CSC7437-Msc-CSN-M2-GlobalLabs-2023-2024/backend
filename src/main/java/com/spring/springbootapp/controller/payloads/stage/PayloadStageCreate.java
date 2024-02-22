package com.spring.springbootapp.controller.payloads.stage;

import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.StageEntity;

import javax.validation.Valid;

public class PayloadStageCreate {
    @Valid
    public Credential credential;
    @Valid
    public StageEntity stage;

    public Credential getCredential() {
        return credential;
    }

    public StageEntity getStage() {
        return stage;
    }
}
