package com.spring.springbootapp.controller;

import com.spring.springbootapp.controller.payloads.staff.PayloadStaffCreate;
import com.spring.springbootapp.controller.payloads.staff.PayloadStaffUpdate;
import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.ProcessEntity;
import com.spring.springbootapp.model.StaffEntity;
import com.spring.springbootapp.model.StageEntity;
import com.spring.springbootapp.repository.ProcessRepo;
import com.spring.springbootapp.repository.StaffRepo;
import com.spring.springbootapp.repository.StageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staff")
@Controller
public class StaffController {
    @Autowired
    StaffRepo staffRepo;

    @Autowired
    ProcessRepo processRepo;

    @Autowired
    StageRepo stageRepo;

    /**
     * Get all staff members
     * @param credential
     *    The credential of the staff member
     * @param bindingResult
     *   The result of the validation
     * @return
     * A list of all staff members
     */
    @PostMapping("/getAll")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getAll(@Valid @RequestBody Credential credential, BindingResult bindingResult) {
        credential.setStaffRepo(staffRepo);
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if(credential.isValid()) { // Check if credential correspond to a staff member
            List<StaffEntity> staff = staffRepo.findAll();
            // Remove passwordHash from the response
            for (StaffEntity s : staff) {
                s.setPasswordHash(null);
            }
            return new ResponseEntity<>(staff, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Get staff by email
     * @param credential
     *    The credential of the staff member
     * @param email
     *    The email of the staff member
     * @param bindingResult
     *   The result of the validation
     * @return
     * The staff member with the given email
     */
    @PostMapping("/getByEmail/{email}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getByEmail(@Valid @RequestBody Credential credential,
                                             @PathVariable String email,
                                             BindingResult bindingResult) {
        credential.setStaffRepo(staffRepo);
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if(credential.isValid()) { // Check if credential correspond to a staff member
            if (!staffRepo.existsById(email)) {
                return new ResponseEntity<>("Staff not found", HttpStatus.NOT_FOUND);
            }
            Optional<StaffEntity> staff = staffRepo.findByEmail(email);
            // Remove passwordHash from the response
            staff.ifPresent(staffEntity -> staffEntity.setPasswordHash(null));
            return new ResponseEntity<>(staff, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Delete staff by email
     * @param credential
     *    The credential of the staff member
     * @param email
     *    The email of the staff member
     * @param bindingResult
     *   The result of the validation
     * @return
     * The staff member with the given email
     */
    @DeleteMapping("/delete/{email}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> delete(@Valid @RequestBody Credential credential,
                                         @PathVariable String email,
                                         BindingResult bindingResult) {
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        // Check if credential correspond to a staff member and if the staff member is an admin
        Optional<StaffEntity> staff = staffRepo.findByEmail(credential.getEmail());
        if (credential.isValid() && staff.isPresent() && staff.get().isAdmin()) {
            if (!staffRepo.existsById(email)) {
                return new ResponseEntity<>("Staff not found", HttpStatus.NOT_FOUND);
            }
            staffRepo.deleteById(email);
            // Remove staff email in processes list
            List<ProcessEntity> processes = processRepo.findAll();
            for (ProcessEntity process : processes) {
                process.getStaffEmails().remove(email);
                processRepo.save(process);
            }
            // Remove all stages of the staff
            List<StageEntity> stages = stageRepo.findAll();
            for (StageEntity stage : stages) {
                if (stage.getStaffEmail().equals(email)) {
                    stageRepo.deleteById(stage.getId());
                }
            }
            return new ResponseEntity<>("Staff deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Create a staff member
     * @param payloadStaffCreate
     *    The payload containing the credential and the staff member
     * @param bindingResult
     *   The result of the validation
     * @return
     * The staff member created
     */
    @PostMapping("/create")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> create(@Valid @RequestBody PayloadStaffCreate payloadStaffCreate, BindingResult bindingResult) {
        Credential credential = payloadStaffCreate.getCredential();
        StaffEntity staff = payloadStaffCreate.getStaff();
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        // Check if credential correspond to a staff member and if the staff member is an admin
        Optional<StaffEntity> staffOptional = staffRepo.findByEmail(credential.getEmail());
        if (credential.isValid() && staffOptional.isPresent() && staffOptional.get().isAdmin()) {
            StaffEntity savedStaff = staffRepo.save(staff);
            // Update process list of the staff
            List<ProcessEntity> processes = processRepo.findAll();
            for (ProcessEntity process : processes) {
                process.getStaffEmails().add(savedStaff.getEmail());
                processRepo.save(process);
            }
            return new ResponseEntity<>(savedStaff, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Update a staff member
     * @param payloadStaffUpdate
     *    The payload containing the credential and the staff member
     * @param bindingResult
     *   The result of the validation
     * @return
     * The staff member updated
     */
    @PutMapping("/update")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> update(@Valid @RequestBody PayloadStaffUpdate payloadStaffUpdate, BindingResult bindingResult) {
        Credential credential = payloadStaffUpdate.getCredential();
        String staffEmail = payloadStaffUpdate.getStaffEmail();
        StaffEntity staff = payloadStaffUpdate.getStaff();
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        // Check if credential correspond to a staff member and if the staff member is an admin or the staff member itself
        Optional<StaffEntity> staffOptional = staffRepo.findByEmail(staffEmail);
        if (credential.isValid() && staffOptional.isPresent() && ((staffRepo.findByEmail(credential.getEmail()).isPresent() && staffRepo.findByEmail(credential.getEmail()).get().isAdmin()) || staffOptional.get().getEmail().equals(staff.getEmail()))) {
            if (!staffRepo.existsById(staffEmail)) {
                return new ResponseEntity<>("Staff not found", HttpStatus.NOT_FOUND);
            }
            staffRepo.deleteById(staffEmail);
            StaffEntity savedStaff = staffRepo.save(staff);
            // Update process list of the staff
            List<ProcessEntity> processes = processRepo.findAll();
            for (ProcessEntity process : processes) {
                process.getStaffEmails().remove(staffEmail);
                process.getStaffEmails().add(savedStaff.getEmail());
                processRepo.save(process);
            }
            // Update all stages of the staff
            List<StageEntity> stages = stageRepo.findAll();
            for (StageEntity stage : stages) {
                if (stage.getStaffEmail().equals(staffEmail)) {
                    stage.setStaffEmail(savedStaff.getEmail());
                    stageRepo.save(stage);
                }
            }
            return new ResponseEntity<>(savedStaff, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Login a staff member
     * @param credential
     *    The credential of the staff member
     * @param bindingResult
     *   The result of the validation
     * @return
     * A message indicating if the login was successful or not
     */
    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> login(@Valid @RequestBody Credential credential,
                                  BindingResult bindingResult) {
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (credential.isValid()) {
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

}
