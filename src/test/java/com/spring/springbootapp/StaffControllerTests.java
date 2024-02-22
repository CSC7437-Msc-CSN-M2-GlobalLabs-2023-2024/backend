//package com.spring.springbootapp;
//
//import com.spring.springbootapp.controller.StaffController;
//import com.spring.springbootapp.model.StaffEntity;
//import com.spring.springbootapp.repository.StaffRepo;
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
//class StaffControllerTests {
//
//    @Mock
//    private StaffRepo staffRepo;
//
//    @InjectMocks
//    private StaffController staffController;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        reset(staffRepo);
//    }
//
//    @AfterEach
//    public void tearDown() {
//        reset(staffRepo);
//    }
//
//    @Test
//    void testGetAllStaff() {
//        List<StaffEntity> staffList = new ArrayList<>();
//        staffList.add(new StaffEntity("test@example.com", "password", "John", "Doe", "Manager", true, new ArrayList<>()));
//        staffList.add(new StaffEntity("test2@example.com", "password", "Jane", "Smith", "Assistant", false, new ArrayList<>()));
//        when(staffRepo.findAll()).thenReturn(staffList);
//        List<StaffEntity> result = staffController.getAllStaff();
//        assertEquals(staffList, result);
//    }
//
//    @Test
//    void testGetStaffByEmail() {
//        StaffEntity staff = new StaffEntity("test@example.com", "password", "John", "Doe", "Manager", true, new ArrayList<>());
//        when(staffRepo.existsById("test@example.com")).thenReturn(true);
//        Optional<StaffEntity> optionalStaffEntity = Optional.of(staff);
//        when(staffRepo.findById("test@example.com")).thenReturn(optionalStaffEntity);
//        ResponseEntity<?> responseEntity = staffController.getStaffByEmail("test@example.com");
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(staff, optionalStaffEntity.get());
//    }
//
//    @Test
//    void testDeleteStaff() {
//        StaffEntity staff = new StaffEntity("test@example.com", "password", "John", "Doe", "Manager", true, new ArrayList<>());
//        when(staffRepo.existsById("test@example.com")).thenReturn(true);
//        ResponseEntity<?> responseEntity = staffController.deleteStaff("test@example.com");
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        verify(staffRepo, times(1)).deleteById("test@example.com");
//    }
//
//    @Test
//    void testCreateStaff() {
//        StaffEntity staff = new StaffEntity("test@example.com", "password", "John", "Doe", "Manager", true, new ArrayList<>());
//        when(staffRepo.existsById("test@example.com")).thenReturn(false);
//        when(staffRepo.save(staff)).thenReturn(staff);
//        ResponseEntity<?> responseEntity = staffController.createStaff(staff, mock(BindingResult.class));
//        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//        assertEquals(staff, responseEntity.getBody());
//        verify(staffRepo).save(staff);
//    }
//
//    @Test
//    void testUpdateStaff() {
//        StaffEntity oldStaff = new StaffEntity("test@example.com", "password", "John", "Doe", "Manager", true, new ArrayList<>());
//        when(staffRepo.existsById("test@example.com")).thenReturn(false);
//        when(staffRepo.save(oldStaff)).thenReturn(oldStaff);
//        staffController.createStaff(oldStaff, mock(BindingResult.class));
//
//        StaffEntity updatedStaff = new StaffEntity("test@example.com", "newPassword", "newJohn", "newDoe", "newManager", false, new ArrayList<>());
//        when(staffRepo.existsById("test@example.com")).thenReturn(true);
//        when(staffRepo.save(updatedStaff)).thenReturn(updatedStaff);
//        ResponseEntity<?> responseEntity = staffController.updateStaff(updatedStaff, mock(BindingResult.class));
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(updatedStaff, responseEntity.getBody());
//    }
//}
