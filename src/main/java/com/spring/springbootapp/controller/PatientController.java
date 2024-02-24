package com.spring.springbootapp.controller;

import com.spring.springbootapp.controller.payloads.patient.PayloadPatientCreate;
import com.spring.springbootapp.controller.payloads.patient.PayloadPatientDelete;
import com.spring.springbootapp.controller.payloads.patient.PayloadPatientGetById;
import com.spring.springbootapp.controller.payloads.patient.PayloadPatientUpdate;
import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.PatientEntity;
import com.spring.springbootapp.model.ProcessEntity;
import com.spring.springbootapp.model.StaffEntity;
import com.spring.springbootapp.model.primaryKey.PatientId;
import com.spring.springbootapp.repository.ProcessRepo;
import com.spring.springbootapp.repository.StaffRepo;
import com.spring.springbootapp.repository.StageRepo;
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

    @Autowired
    ProcessRepo processRepo;

    @Autowired
    StageRepo stageRepo;

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
        if(credential.isValid()) { // Check if credential correspond to a staff member
            List<PatientEntity> patients = patientRepo.findAll();
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } else {
            String jsonBody = "{\"message\": \"Invalid credentials\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.UNAUTHORIZED);
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
            String jsonBody = "{\"message\": \"Invalid credentials\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.UNAUTHORIZED);
        }
        if (!patientRepo.existsById(patientId)) { // Check if the patient exists
            String jsonBody = "{\"message\": \"Patient not found\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.NOT_FOUND);
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
        //Check if credential correspond to a staff member
        if (!credential.isValid()) {
            String jsonBody = "{\"message\": \"Invalid credentials\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.UNAUTHORIZED);
        }
        // Check if the patient exists
        if (!patientRepo.existsById(patientId)) {
            String jsonBody = "{\"message\": \"Patient not found\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.NOT_FOUND);
        }
        //When delete a patient, all processes associated with the patient are deleted
        List<ProcessEntity> processes = processRepo.findAll();
        for (ProcessEntity process : processes) {
            // Remove all stages of the process
            for (Long stageId : process.getStageIds()) {
                stageRepo.deleteById(stageId);
            }
            //Remove the process from all staff members
            for (String email : process.getStaffEmails()) {
                if (staffRepo.findByEmail(email).isPresent()) {
                    StaffEntity staff = staffRepo.findByEmail(email).get();
                    staff.removeProcess(process);
                    staffRepo.save(staff);
                }
            }
            // Remove the process
            if (process.getPatientId().equals(patientId)) {
                processRepo.deleteById(process.getId());
            }
        }
        patientRepo.deleteById(patientId);
        String jsonBody = "{\"message\": \"Patient deleted successfully\"}";
        return new ResponseEntity<>(jsonBody, HttpStatus.OK);
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
            String jsonBody = "{\"message\": \"Invalid credentials\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.UNAUTHORIZED);
        }
        if (patientRepo.existsById(new PatientId(patient.getEmail(), patient.getFirstName(), patient.getLastName(), patient.getAge()))) {
            String jsonBody = "{\"message\": \"Patient with provided details already exists.\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.BAD_REQUEST);
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
            String jsonBody = "{\"message\": \"Invalid credentials\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.UNAUTHORIZED);
        }
        if (!patientRepo.existsById(oldPatientId)) {
            String jsonBody = "{\"message\": \"Patient not found\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.NOT_FOUND);
        }
        patientRepo.deleteById(oldPatientId);
        PatientEntity savedPatient = patientRepo.save(newPatient);
        return new ResponseEntity<>(savedPatient, HttpStatus.OK);
    }
}
