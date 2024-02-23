package com.spring.springbootapp;

import com.spring.springbootapp.controller.PatientController;
import com.spring.springbootapp.controller.StageController;
import com.spring.springbootapp.controller.payloads.stage.PayloadStageCreate;
import com.spring.springbootapp.controller.payloads.stage.PayloadStageUpdate;
import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.StageEntity;
import com.spring.springbootapp.repository.PatientRepo;
import com.spring.springbootapp.repository.ProcessRepo;
import com.spring.springbootapp.repository.StageRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StageControllerTests {
    @Mock
    private PatientRepo patientRepo;

    @Mock
    private ProcessRepo processRepo;
    @Mock
    StageRepo stageRepo;

    @InjectMocks
    private StageController stageController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        reset(patientRepo);
        reset(processRepo);
        reset(stageRepo);
    }

    @AfterEach
    public void tearDown() {
        reset(patientRepo);
        reset(processRepo);
        reset(stageRepo);
    }


    @Test
    void testGetAllStages() {
        // Simulate a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);

        List<StageEntity> stages = new ArrayList<>();
        stages.add(new StageEntity("Stage 1", true, "test@example.com"));
        stages.add(new StageEntity("Stage 2", false, "test2@example.com"));
        when(stageRepo.findAll()).thenReturn(stages);
        ResponseEntity<?> result = stageController.getAllStages(cred, mock(BindingResult.class));
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(stages, result.getBody());
    }

    @Test
    void testGetStageById() {
        // Simulate a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);

        StageEntity stage = new StageEntity("Stage 1", true, "test@example.com");
        Long ID = stage.getId();
        when(stageRepo.existsById(ID)).thenReturn(true);
        Optional<StageEntity> optionalStageEntity = Optional.of(stage);
        when(stageRepo.findById(ID)).thenReturn(optionalStageEntity);
        ResponseEntity<?> responseEntity = stageController.getStageById(cred, ID, mock(BindingResult.class));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(stage, optionalStageEntity.get());
    }

    @Test
    void testDeleteStage() {
        // Simulate a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);
        // Create a stage
        StageEntity stage = new StageEntity("Stage 1", true, "admin@admin.com");
        stageRepo.save(stage);
        Long ID = stage.getId();
        when(stageRepo.existsById(ID)).thenReturn(true);
        ResponseEntity<?> responseEntity = stageController.deleteStage(cred, ID, mock(BindingResult.class));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // the following line serves to verify that the method was called 1 time
        verify(stageRepo, times(1)).deleteById(ID);
    }

    @Test
    void testCreateStage() {
        // Simulate a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);
        StageEntity stage = new StageEntity("Stage 1", true, "test@example.com");
        when(stageRepo.save(stage)).thenReturn(stage);
        when(processRepo.existsById(stage.getProcessId())).thenReturn(true);
        PayloadStageCreate payloadStageCreate = new PayloadStageCreate(cred, stage);
        ResponseEntity<?> responseEntity = stageController.createStage(payloadStageCreate, mock(BindingResult.class));
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(stage, responseEntity.getBody());
        verify(stageRepo).save(stage);
    }

    @Test
    void testUpdateStage() {
        // Simulate a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);
        // In first place, we need to create the stage
        StageEntity stage = new StageEntity("Stage 1", true, "test@example.com");
        when(stageRepo.save(stage)).thenReturn(stage);
        PayloadStageCreate payloadStageCreate = new PayloadStageCreate(cred, stage);
        stageController.createStage(payloadStageCreate, mock(BindingResult.class));
        Long ID = stage.getId();
        // Now we can test the update
        when(stageRepo.existsById(ID)).thenReturn(true);
        when(stageRepo.save(stage)).thenReturn(stage);
        PayloadStageUpdate payloadStageUpdate = new PayloadStageUpdate(cred, stage);
        ResponseEntity<?> responseEntity = stageController.updateStage(payloadStageUpdate, mock(BindingResult.class));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(stage, responseEntity.getBody());
    }
}
