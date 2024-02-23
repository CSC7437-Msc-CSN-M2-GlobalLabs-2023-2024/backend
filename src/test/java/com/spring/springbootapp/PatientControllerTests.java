package com.spring.springbootapp;

import com.spring.springbootapp.controller.PatientController;
import com.spring.springbootapp.controller.payloads.patient.PayloadPatientCreate;
import com.spring.springbootapp.controller.payloads.patient.PayloadPatientDelete;
import com.spring.springbootapp.controller.payloads.patient.PayloadPatientGetById;
import com.spring.springbootapp.controller.payloads.patient.PayloadPatientUpdate;
import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.PatientEntity;
import com.spring.springbootapp.model.Sex;
import com.spring.springbootapp.model.StaffEntity;
import com.spring.springbootapp.model.primaryKey.PatientId;
import com.spring.springbootapp.repository.PatientRepo;
import com.spring.springbootapp.repository.ProcessRepo;
import com.spring.springbootapp.repository.StaffRepo;
import com.spring.springbootapp.repository.StageRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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
class PatientControllerTests {
    @Mock
    private PatientRepo patientRepo;

    @Mock
    private ProcessRepo processRepo;
    @Mock
    StageRepo stagesRepo;

    @InjectMocks
    private PatientController patientController;

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


    /**
     * Test to get all patients being a staff member with valid credentials (in database)
     */
    @Test
    void test1GetAllPatients() {
        // Simulate a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);

        // Create a list of patients
        List<PatientEntity> patients = new ArrayList<>();
        patients.add(new PatientEntity("test@example.com", "John", "Doe", 30, Sex.M));
        patients.add(new PatientEntity("test2@example.com", "Jane", "Smith", 25, Sex.F));
        when(patientRepo.findAll()).thenReturn(patients);

        // Test the method
        ResponseEntity<?> responseEntity = patientController.getAllPatients(cred, mock(BindingResult.class));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(patients, responseEntity.getBody());
    }

    /**
     * Test to get all patients being a staff member with invalid credentials (not in database)
     */
    @Test
    void test2GetAllPatients() {
        // Simulate an invalid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(false);

        // Create a list of patients
        List<PatientEntity> patients = new ArrayList<>();
        patients.add(new PatientEntity("test@example.com", "John", "Doe", 30, Sex.M));
        patients.add(new PatientEntity("test2@example.com", "Jane", "Smith", 25, Sex.F));
        when(patientRepo.findAll()).thenReturn(patients);

        // Test the method
        ResponseEntity<?> responseEntity = patientController.getAllPatients(cred, mock(BindingResult.class));
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    /**
     * Test to get a patient by its id being a staff member with valid credentials (in database)
     */
    @Test
    void testGetPatientById() {
        PatientEntity patient = new PatientEntity("test@example.com", "John", "Doe", 30, Sex.M);
        PatientId patientId = new PatientId("test@example.com", "John", "Doe", 30);
        when(patientRepo.existsById(patientId)).thenReturn(true);
        Optional<PatientEntity> optionalPatientEntity = Optional.of(patient);
        when(patientRepo.findById(patientId)).thenReturn(optionalPatientEntity);
        // Mock a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);
        // Perform the request
        PayloadPatientGetById payloadPatientGetById = new PayloadPatientGetById(cred, patientId);
        ResponseEntity<?> responseEntity = patientController.getPatientById(payloadPatientGetById, mock(BindingResult.class));
        // Assert that the response status is OK (200)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(patient, optionalPatientEntity.get());
    }

    @Test
    void testDeletePatient() {
        PatientEntity patient = new PatientEntity("test@example.com", "John", "Doe", 30, Sex.M);
        PatientId patientId = new PatientId(patient.getEmail(), patient.getFirstName(), patient.getLastName(), patient.getAge());
        patientRepo.save(patient);
        when(patientRepo.existsById(patientId)).thenReturn(true);
        // Mock a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);
        // Perform the request
        PayloadPatientDelete payloadPatientDelete = new PayloadPatientDelete(cred, patientId);
        ResponseEntity<?> responseEntity = patientController.delete(payloadPatientDelete, mock(BindingResult.class));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // the following line serve to verify that the method was called 1 time
        verify(patientRepo, times(1)).deleteById(patientId);
    }

    @Test
    void testCreatePatient() {
        PatientEntity patient = new PatientEntity("test@example.com", "John", "Doe", 30, Sex.M);
        PatientId patientId = new PatientId(patient.getEmail(), patient.getFirstName(), patient.getLastName(), patient.getAge());
        when(patientRepo.existsById(argThat(new PatientIdMatcher(patientId)))).thenReturn(false);
        when(patientRepo.save(patient)).thenReturn(patient);
        // Mock a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);
        // Perform the request
        PayloadPatientCreate payloadPatientCreate = new PayloadPatientCreate(cred, patient);
        ResponseEntity<?> responseEntity = patientController.create(payloadPatientCreate, mock(BindingResult.class));
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(patient, responseEntity.getBody());
        verify(patientRepo).save(patient);
    }


    @Test
    void testUpdatePatient() {
        // First, create a patient
        PatientEntity oldPatient = new PatientEntity("test@example.com", "John", "Doe", 30, Sex.M);
        PatientId oldPatientId = new PatientId("test@example.com", "John", "Doe", 30);
        when(patientRepo.existsById(argThat(new PatientIdMatcher(oldPatientId)))).thenReturn(false);
        when(patientRepo.save(oldPatient)).thenReturn(oldPatient);
        // Mock a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);
        PayloadPatientCreate payloadPatientCreate = new PayloadPatientCreate(cred, oldPatient);
        patientController.create(payloadPatientCreate, mock(BindingResult.class));
        // Now we can test the update
        PatientEntity updatedPatient = new PatientEntity("newMail@example.com", "John", "Doe", 30, Sex.M);
        when(patientRepo.existsById(oldPatientId)).thenReturn(true);
        when(patientRepo.save(updatedPatient)).thenReturn(updatedPatient);
        PayloadPatientUpdate payloadPatientUpdate = new PayloadPatientUpdate(cred, updatedPatient, oldPatientId);
        ResponseEntity<?> responseEntity = patientController.update(payloadPatientUpdate, mock(BindingResult.class));
        // Assert that the response status is OK (200)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Assert that the response body contains the updated patient
        assertEquals(updatedPatient, responseEntity.getBody());
    }
}

