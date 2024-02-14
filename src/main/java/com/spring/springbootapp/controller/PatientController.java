package com.spring.springbootapp.controller;

import com.spring.springbootapp.model.PatientEntity;
import com.spring.springbootapp.model.primaryKey.PatientId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import com.spring.springbootapp.repository.PatientRepo;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/patient")
@Controller
public class PatientController {
    @Autowired
    PatientRepo patientRepo;

    /**
     * Get all patients
     */
    @GetMapping("/getAll")
    @CrossOrigin(origins = "*")
    public List<PatientEntity> getAllPatients() {
        return patientRepo.findAll();
    }

    /**
     * Get a patient by id
     * check if patient exists
     * if yes, return the patient
     */
    @GetMapping("/getById")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getPatientById(@Valid @RequestBody PatientId patientId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (!patientRepo.existsById(patientId)) {
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(patientRepo.findById(patientId), HttpStatus.OK);
    }

    /**
     * Delete a patient
     * check if patient exists
     * if yes, delete the patient
     */
    @DeleteMapping("/delete")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deletePatient(@Valid @RequestBody PatientId patientId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (!patientRepo.existsById(patientId)) {
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
        patientRepo.deleteById(patientId);
        return new ResponseEntity<>("Patient deleted successfully", HttpStatus.OK);
    }

    /** Create a patient
     * check if patient already exists
     * if not, save the patient
     */
    @PostMapping("/create")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> createPatient(@Valid @RequestBody PatientEntity patient, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (patientRepo.existsById(new PatientId(patient.getEmail(), patient.getFirstName(), patient.getLastName(), patient.getAge()))) {
            return new ResponseEntity<>("Patient with provided details already exists.", HttpStatus.BAD_REQUEST);
        }
        // Additional custom validation can be performed here if needed
        PatientEntity savedPatient = patientRepo.save(patient);
        return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
    }

    /**
     * Update a patient
     * check if patient exists
     * if yes, update the patient
     */
    @PutMapping("/update")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> updatePatient(@Valid @RequestBody PatientEntity patient, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (!patientRepo.existsById(new PatientId(patient.getEmail(), patient.getFirstName(), patient.getLastName(), patient.getAge()))) {
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
        // Additional custom validation can be performed here if needed
        PatientEntity savedPatient = patientRepo.save(patient);
        return new ResponseEntity<>(savedPatient, HttpStatus.OK);
    }

}
