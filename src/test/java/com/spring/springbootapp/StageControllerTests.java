//package com.spring.springbootapp;
//
//import com.spring.springbootapp.controller.StageController;
//import com.spring.springbootapp.model.StageEntity;
//import com.spring.springbootapp.repository.StageRepo;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
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
//class StageControllerTests {
//
//    @Mock
//    private StageRepo stageRepo;
//
//    @InjectMocks
//    private StageController stageController;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        reset(stageRepo);
//    }
//
//    @AfterEach
//    public void tearDown() {
//        reset(stageRepo);
//    }
//
//    @Test
//    void testGetAllStages() {
//        List<StageEntity> stages = new ArrayList<>();
//        stages.add(new StageEntity("Stage 1", true, "test@example.com"));
//        stages.add(new StageEntity("Stage 2", false, "test2@example.com"));
//        when(stageRepo.findAll()).thenReturn(stages);
//        List<StageEntity> result = stageController.getAllStages();
//        assertEquals(stages, result);
//    }
//
//    @Test
//    void testGetStageById() {
//        StageEntity stage = new StageEntity("Stage 1", true, "test@example.com");
//        when(stageRepo.existsById(1L)).thenReturn(true);
//        Optional<StageEntity> optionalStageEntity = Optional.of(stage);
//        when(stageRepo.findById(1L)).thenReturn(optionalStageEntity);
//        ResponseEntity<?> responseEntity = stageController.getStageById(1L);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(stage, optionalStageEntity.get());
//    }
//
//    @Test
//    void testDeleteStage() {
//        when(stageRepo.existsById(1L)).thenReturn(true);
//        ResponseEntity<?> responseEntity = stageController.deleteStage(1L);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        // the following line serves to verify that the method was called 1 time
//        verify(stageRepo, times(1)).deleteById(1L);
//    }
//
//    @Test
//    void testCreateStage() {
//        StageEntity stage = new StageEntity("Stage 1", true, "test@example.com");
//        when(stageRepo.save(stage)).thenReturn(stage);
//        ResponseEntity<?> responseEntity = stageController.createStage(stage, mock(BindingResult.class));
//        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//        assertEquals(stage, responseEntity.getBody());
//        verify(stageRepo).save(stage);
//    }
//
//    @Test
//    void testUpdateStage() {
//        // In first place, we need to create the stage
//        StageEntity stage = new StageEntity("Stage 1", true, "test@example.com");
//        when(stageRepo.save(stage)).thenReturn(stage);
//        stageController.createStage(stage, mock(BindingResult.class));
//        Long ID = stage.getId();
//        // Now we can test the update
//        when(stageRepo.existsById(ID)).thenReturn(true);
//        when(stageRepo.save(stage)).thenReturn(stage);
//        ResponseEntity<?> responseEntity = stageController.updateStage(stage, mock(BindingResult.class));
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(stage, responseEntity.getBody());
//    }
//}
