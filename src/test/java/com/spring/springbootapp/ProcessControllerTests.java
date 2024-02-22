//package com.spring.springbootapp;
//
//import com.spring.springbootapp.controller.ProcessController;
//import com.spring.springbootapp.model.ProcessEntity;
//import com.spring.springbootapp.repository.ProcessRepo;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ProcessControllerTests {
//
//    @Mock
//    private ProcessRepo processRepo;
//
//    @InjectMocks
//    private ProcessController processController;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        reset(processRepo);
//    }
//
//    @AfterEach
//    public void tearDown() {
//        reset(processRepo);
//    }
//
//    @Test
//    void testGetAllProcesses() {
//        List<ProcessEntity> processes = new ArrayList<>();
//        processes.add(new ProcessEntity("Process 1", null, new ArrayList<>(), new ArrayList<>()));
//        processes.add(new ProcessEntity("Process 2", null, new ArrayList<>(), new ArrayList<>()));
//        when(processRepo.findAll()).thenReturn(processes);
//        List<ProcessEntity> result = processController.getAllProcesses();
//        assertEquals(processes, result);
//    }
//
//    @Test
//    void testGetProcessById() {
//        ProcessEntity process = new ProcessEntity("Process 1", null, new ArrayList<>(), new ArrayList<>());
//        Long ID = process.getId();
//        when(processRepo.existsById(ID)).thenReturn(true);
//        Optional<ProcessEntity> optionalProcessEntity = Optional.of(process);
//        when(processRepo.findById(ID)).thenReturn(optionalProcessEntity);
//        ResponseEntity<?> responseEntity = processController.getProcessById(ID);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(process, optionalProcessEntity.get());
//    }
//
//    @Test
//    void testDeleteProcess() {
//        ProcessEntity process = new ProcessEntity("Process 1", null, new ArrayList<>(), new ArrayList<>());
//        Long ID = process.getId();
//        when(processRepo.existsById(ID)).thenReturn(true);
//        ResponseEntity<?> responseEntity = processController.deleteProcess(ID);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        verify(processRepo, times(1)).deleteById(ID);
//    }
//
//    @Test
//    void testCreateProcess() {
//        ProcessEntity process = new ProcessEntity("Process 1", null, new ArrayList<>(), new ArrayList<>());
//        when(processRepo.save(process)).thenReturn(process);
//        ResponseEntity<?> responseEntity = processController.createProcess(process, mock(BindingResult.class));
//        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//        assertEquals(process, responseEntity.getBody());
//        verify(processRepo).save(process);
//    }
//
//    @Test
//    void testUpdateProcess() {
//        // First, create a process
//        ProcessEntity oldProcess = new ProcessEntity("Process 1", null, new ArrayList<>(), new ArrayList<>());
//        when(processRepo.save(oldProcess)).thenReturn(oldProcess);
//        processController.createProcess(oldProcess, mock(BindingResult.class));
//        Long ID = oldProcess.getId();
//
//        // Now we can test the update
//        ProcessEntity updatedProcess = new ProcessEntity("Updated Process", null, new ArrayList<>(), new ArrayList<>());
//        when(processRepo.existsById(ID)).thenReturn(true);
//        when(processRepo.save(updatedProcess)).thenReturn(updatedProcess);
//        ResponseEntity<?> responseEntity = processController.updateProcess(updatedProcess, mock(BindingResult.class));
//
//        // Assert that the response status is OK (200)
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//
//        // Assert that the response body contains the updated process
//        assertEquals(updatedProcess, responseEntity.getBody());
//    }
//}
