package com.spring.springbootapp;

import com.spring.springbootapp.controller.PatientController;
import com.spring.springbootapp.controller.ProcessController;
import com.spring.springbootapp.controller.payloads.process.PayloadProcessCreate;
import com.spring.springbootapp.controller.payloads.process.PayloadProcessUpdate;
import com.spring.springbootapp.controller.payloads.staff.PayloadStaffUpdate;
import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.ProcessEntity;
import com.spring.springbootapp.repository.PatientRepo;
import com.spring.springbootapp.repository.ProcessRepo;
import com.spring.springbootapp.repository.StaffRepo;
import com.spring.springbootapp.repository.StageRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessControllerTests {
    @Mock
    private PatientRepo patientRepo;

    @Mock
    private ProcessRepo processRepo;
    @Mock
    StageRepo stagesRepo;

    @Mock
    StaffRepo staffRepo;

    @InjectMocks
    private ProcessController processController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        reset(patientRepo);

        reset(processRepo);
        reset(stagesRepo);
    }

    @AfterEach
    public void tearDown() {
        reset(patientRepo);

        reset(processRepo);
        reset(stagesRepo);
    }
    @Test
    void testGetAllProcesses() {
        // Mock a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);

        List<ProcessEntity> processes = new ArrayList<>();
        processes.add(new ProcessEntity("Process 1", null, new ArrayList<>(), new ArrayList<>()));
        processes.add(new ProcessEntity("Process 2", null, new ArrayList<>(), new ArrayList<>()));
        when(processRepo.findAll()).thenReturn(processes);
        ResponseEntity<?> result = processController.getAllProcesses(cred, mock(BindingResult.class));
        assertEquals(processes, result.getBody());
    }

    @Test
    void testGetProcessById() {
        // Mock a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);
        ProcessEntity process = new ProcessEntity("Process 1", null, new ArrayList<>(), new ArrayList<>());
        Long ID = process.getId();
        when(processRepo.existsById(ID)).thenReturn(true);
        Optional<ProcessEntity> optionalProcessEntity = Optional.of(process);
        when(processRepo.findById(ID)).thenReturn(optionalProcessEntity);
        ResponseEntity<?> responseEntity = processController.getProcessById(cred, ID, mock(BindingResult.class));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(process, optionalProcessEntity.get());
    }

    @Test
    void testDeleteProcess() {
        // Mock a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);
        ProcessEntity process = new ProcessEntity("Process 1", null, new ArrayList<>(), new ArrayList<>());
        Long ID = process.getId();
        when(processRepo.existsById(ID)).thenReturn(true);
        // return empty optional
        when(processRepo.findById(ID)).thenReturn(Optional.empty());
        ResponseEntity<?> responseEntity = processController.deleteProcess(cred, ID, mock(BindingResult.class));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(processRepo, times(1)).deleteById(ID);
    }

    @Test
    void testCreateProcess() {
        // Mock a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);
        ProcessEntity process = new ProcessEntity("Process 1", null, new ArrayList<>(), new ArrayList<>());
        when(processRepo.save(process)).thenReturn(process);
        when(patientRepo.existsById(process.getPatientId())).thenReturn(true);
        PayloadProcessCreate payloadProcessCreate = new PayloadProcessCreate(cred, process);
        ResponseEntity<?> responseEntity = processController.createProcess(payloadProcessCreate, mock(BindingResult.class));
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(process, responseEntity.getBody());
        verify(processRepo).save(process);
    }

    @Test
    void testUpdateProcess() {
        //Mock a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);
        // First, create a process
        ProcessEntity oldProcess = new ProcessEntity("Process 1", null, new ArrayList<>(), new ArrayList<>());
        when(processRepo.save(oldProcess)).thenReturn(oldProcess);
        PayloadProcessCreate payloadProcessCreate = new PayloadProcessCreate(cred, oldProcess);
        processController.createProcess(payloadProcessCreate, mock(BindingResult.class));
        Long ID = oldProcess.getId();

        // Now we can test the update
        ProcessEntity updatedProcess = new ProcessEntity("Updated Process", null, new ArrayList<>(), new ArrayList<>());
        when(processRepo.existsById(ID)).thenReturn(true);
        when(processRepo.save(updatedProcess)).thenReturn(updatedProcess);
        PayloadProcessUpdate payloadProcessUpdate = new PayloadProcessUpdate(updatedProcess, cred, ID);
        ResponseEntity<?> responseEntity = processController.updateProcess(payloadProcessUpdate, mock(BindingResult.class));

        // Assert that the response status is OK (200)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Assert that the response body contains the updated process
        assertEquals(updatedProcess, responseEntity.getBody());
    }
}
