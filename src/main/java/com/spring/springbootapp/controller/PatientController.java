package com.spring.springbootapp.controller;

import com.spring.springbootapp.controller.payloads.patient.PayloadPatientCreate;
import com.spring.springbootapp.controller.payloads.patient.PayloadPatientDelete;
import com.spring.springbootapp.controller.payloads.patient.PayloadPatientGetById;
import com.spring.springbootapp.controller.payloads.patient.PayloadPatientUpdate;
import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.PatientEntity;
import com.spring.springbootapp.model.primaryKey.PatientId;
import com.spring.springbootapp.repository.StaffRepo;
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

    @Autowired
    StaffRepo staffRepo;

    public PatientController(PatientRepo patientRepo) {
    }

    /**
     * Get all patients
     * @param credential
     *    The credential of the staff member
     * @param bindingResult
     *   The result of the validation
     * @return
     * A list of all patients
     */
    @PostMapping("/getAll")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getAllPatients(@Valid @RequestBody Credential credential, BindingResult bindingResult) {
        credential.setStaffRepo(staffRepo);
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        System.out.println("email:"+credential.getEmail());
        System.out.println("passwordHash:"+credential.getPasswordHash());
        if(credential.isValid()) { // Check if credential correspond to a staff member
            List<PatientEntity> patients = patientRepo.findAll();
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * Get a patient by its id
     * @param payloadPatientGetById
     *    The payload containing the credential and the patient id
     * @param bindingResult
     *   The result of the validation
     * @return
     * The patient with the provided id
     */
    @PostMapping("/getById")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getPatientById(@Valid @RequestBody PayloadPatientGetById payloadPatientGetById, BindingResult bindingResult) {
        Credential credential = payloadPatientGetById.getCredential();
        PatientId patientId = payloadPatientGetById.getPatientId();
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) { // Check if the request is valid
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (!credential.isValid()) { // Check if credential correspond to a staff member
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        if (!patientRepo.existsById(patientId)) { // Check if the patient exists
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(patientRepo.findById(patientId), HttpStatus.OK);
    }


    /**
     * Delete a patient
     * @param payloadPatientDelete
     *    The payload containing the credential and the patient id
     * @param bindingResult
     *   The result of the validation
     * @return
     * A message indicating the success of the operation
     */
    @DeleteMapping("/delete")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> delete(@Valid @RequestBody PayloadPatientDelete payloadPatientDelete, BindingResult bindingResult) {
        Credential credential = payloadPatientDelete.getCredential();
        PatientId patientId = payloadPatientDelete.getPatientId();
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (!credential.isValid()) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        if (!patientRepo.existsById(patientId)) {
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
        patientRepo.deleteById(patientId);
        return new ResponseEntity<>("Patient deleted successfully", HttpStatus.OK);
    }

    /**
     * Create a patient
     * @param payloadPatientCreate
     *    The payload containing the credential and the patient
     * @param bindingResult
     *   The result of the validation
     * @return
     * The created patient
     */
    @PostMapping("/create")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> create(@Valid @RequestBody PayloadPatientCreate payloadPatientCreate, BindingResult bindingResult) {
        Credential credential = payloadPatientCreate.getCredential();
        PatientEntity patient = payloadPatientCreate.getPatient();
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (!credential.isValid()) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        if (patientRepo.existsById(new PatientId(patient.getEmail(), patient.getFirstName(), patient.getLastName(), patient.getAge()))) {
            return new ResponseEntity<>("Patient with provided details already exists.", HttpStatus.BAD_REQUEST);
        }
        PatientEntity savedPatient = patientRepo.save(patient);
        return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
    }


    /**
     * Update a patient
     * @param payloadPatientUpdate
     *    The payload containing the credential, the new patient and the old patient id
     * @param bindingResult
     *   The result of the validation
     * @return
     * The updated patient
     */
    @PutMapping("/update")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> update(@Valid @RequestBody PayloadPatientUpdate payloadPatientUpdate,
                                           BindingResult bindingResult) {
        Credential credential = payloadPatientUpdate.getCredential();
        PatientEntity newPatient = payloadPatientUpdate.getPatient();
        PatientId oldPatientId = payloadPatientUpdate.getPatientId();
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (!credential.isValid()) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        if (!patientRepo.existsById(oldPatientId)) {
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
        patientRepo.deleteById(oldPatientId);
        PatientEntity savedPatient = patientRepo.save(newPatient);
        return new ResponseEntity<>(savedPatient, HttpStatus.OK);
    }
}
