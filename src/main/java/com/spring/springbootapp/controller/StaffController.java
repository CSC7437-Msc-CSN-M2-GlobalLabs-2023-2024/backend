package com.spring.springbootapp.controller;

import com.spring.springbootapp.model.StaffEntity;
import com.spring.springbootapp.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/staff")
@Controller
public class StaffController {
    @Autowired
    StaffRepo staffRepo;

    /**
     * Get all staff
     */
    @GetMapping("/getAll")
    @CrossOrigin(origins = "*")
    public List<StaffEntity> getAllStaff() {
        return staffRepo.findAll();
    }

    /**
     * Get staff by email
     */
    @GetMapping("/getByEmail/{email}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getStaffByEmail(@PathVariable String email) {
        if (!staffRepo.existsById(email)) {
            return new ResponseEntity<>("Staff not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(staffRepo.findById(email), HttpStatus.OK);
    }

    /**
     * Delete staff by email
     */
    @DeleteMapping("/delete/{email}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deleteStaff(@PathVariable String email) {
        if (!staffRepo.existsById(email)) {
            return new ResponseEntity<>("Staff not found", HttpStatus.NOT_FOUND);
        }
        staffRepo.deleteById(email);
        return new ResponseEntity<>("Staff deleted successfully", HttpStatus.OK);
    }

    /** Create a staff */
    @PostMapping("/create")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> createStaff(@Valid @RequestBody StaffEntity staff, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        // Additional validation logic if needed
        StaffEntity savedStaff = staffRepo.save(staff);
        return new ResponseEntity<>(savedStaff, HttpStatus.CREATED);
    }

    /**
     * Update a staff
     */
    @PutMapping("/update")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> updateStaff(@Valid @RequestBody StaffEntity staff, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (!staffRepo.existsById(staff.getEmail())) {
            return new ResponseEntity<>("Staff not found", HttpStatus.NOT_FOUND);
        }
        // Additional validation logic if needed
        StaffEntity savedStaff = staffRepo.save(staff);
        return new ResponseEntity<>(savedStaff, HttpStatus.OK);
    }
}
