package com.spring.springbootapp.controller.payloads.stage;

import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.StageEntity;

import javax.validation.Valid;

public class PayloadStageUpdate {
    @Valid
    private Credential credential;
    @Valid
    private StageEntity stage;
    public Credential getCredential() {
        return credential;
    }

    public StageEntity getStage() {
        return stage;
    }
}
