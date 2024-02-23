package com.spring.springbootapp;

import com.spring.springbootapp.controller.StaffController;
import com.spring.springbootapp.controller.StageController;
import com.spring.springbootapp.controller.payloads.staff.PayloadStaffCreate;
import com.spring.springbootapp.controller.payloads.staff.PayloadStaffUpdate;
import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.StaffEntity;
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
class StaffControllerTests {
    @Mock
    private PatientRepo patientRepo;

    @Mock
    private ProcessRepo processRepo;
    @Mock
    StageRepo stageRepo;

    @Mock
    StaffRepo staffRepo;

    @InjectMocks
    private StaffController staffController;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        reset(patientRepo);
        reset(processRepo);
        reset(stageRepo);
        reset(staffRepo);
    }

    @AfterEach
    public void tearDown() {
        reset(patientRepo);
        reset(processRepo);
        reset(stageRepo);
        reset(staffRepo);
    }

    @Test
    void testGetAllStaff() {
        // Create a list of staff
        List<StaffEntity> staffList = new ArrayList<>();
        staffList.add(new StaffEntity("test@example.com", "password", "John", "Doe", "Manager", true, new ArrayList<>()));
        staffList.add(new StaffEntity("test2@example.com", "password", "Jane", "Smith", "Assistant", false, new ArrayList<>()));
        // Mock credentials
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);

        when(staffRepo.findAll()).thenReturn(staffList);
        ResponseEntity<?> result = staffController.getAll(cred, mock(BindingResult.class));
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(staffList, result.getBody());
    }

    @Test
    void testGetStaffByEmail() {
        // Mock a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);
        // Create a staff member
        StaffEntity staff = new StaffEntity("test@example.com", "password", "John", "Doe", "Manager", true, new ArrayList<>());
        when(staffRepo.existsById(staff.getEmail())).thenReturn(true);
        Optional<StaffEntity> optionalStaffEntity = Optional.of(staff);
        when(staffRepo.findById(staff.getEmail())).thenReturn(optionalStaffEntity);

        ResponseEntity<?> responseEntity = staffController.getByEmail(cred, staff.getEmail(), mock(BindingResult.class));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(staff, optionalStaffEntity.get());
    }

    @Test
    void testDeleteStaff() {
        // Mock a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);
        StaffEntity staff = new StaffEntity("notadmin@example.com", "password", "John", "Doe", "Manager", false, new ArrayList<>());
        when(staffRepo.existsById(staff.getEmail())).thenReturn(true);
        ResponseEntity<?> responseEntity = staffController.delete(cred, staff.getEmail(), mock(BindingResult.class));
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        verify(staffRepo, times(0)).deleteById(staff.getEmail());
    }

    @Test
    void testCreateStaff() {
        // Mock a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);
        StaffEntity staff = new StaffEntity("test@example.com", "password", "John", "Doe", "Manager", true, new ArrayList<>());
        when(staffRepo.existsById(staff.getEmail())).thenReturn(false);
        when(staffRepo.save(staff)).thenReturn(staff);
        when(staffRepo.findByEmail(cred.getEmail())).thenReturn(Optional.of(staff));
        PayloadStaffCreate payloadStaffCreate = new PayloadStaffCreate(cred,staff);
        ResponseEntity<?> responseEntity = staffController.create(payloadStaffCreate, mock(BindingResult.class));
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(staff, responseEntity.getBody());
        verify(staffRepo).save(staff);
    }

    @Test
    void testUpdateStaff() {
        // Mock a valid credential
        Credential cred = mock(Credential.class);
        when(cred.isValid()).thenReturn(true);
        // Create a staff member
        StaffEntity oldStaff = new StaffEntity("test@example.com", "password", "John", "Doe", "Manager", true, new ArrayList<>());
        when(staffRepo.existsById(oldStaff.getEmail())).thenReturn(false);
        when(staffRepo.save(oldStaff)).thenReturn(oldStaff);
        PayloadStaffCreate payloadStaffCreate = new PayloadStaffCreate(cred,oldStaff);
        staffController.create(payloadStaffCreate, mock(BindingResult.class));
        // Now we can test the update
        StaffEntity updatedStaff = new StaffEntity("test@example.com", "newPassword", "newJohn", "newDoe", "newManager", false, new ArrayList<>());
        when(staffRepo.existsById(updatedStaff.getEmail())).thenReturn(true);
        when(staffRepo.save(updatedStaff)).thenReturn(updatedStaff);
        when(staffRepo.findByEmail(oldStaff.getEmail())).thenReturn(Optional.of(oldStaff));
        PayloadStaffUpdate  payloadStaffUpdate = new PayloadStaffUpdate(cred, updatedStaff, oldStaff.getEmail());
        ResponseEntity<?> responseEntity = staffController.update(payloadStaffUpdate, mock(BindingResult.class));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedStaff, responseEntity.getBody());
    }
}
