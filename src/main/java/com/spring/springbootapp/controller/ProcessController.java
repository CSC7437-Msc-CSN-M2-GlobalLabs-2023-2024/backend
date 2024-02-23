package com.spring.springbootapp.controller;

import com.spring.springbootapp.controller.payloads.process.PayloadProcessCreate;
import com.spring.springbootapp.controller.payloads.process.PayloadProcessUpdate;
import com.spring.springbootapp.controller.payloads.stage.PayloadStageUpdate;
import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.ProcessEntity;
import com.spring.springbootapp.model.StaffEntity;
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

@RestController
@RequestMapping("/api/process")
@Controller
public class ProcessController {
    @Autowired
    ProcessRepo processRepo;

    @Autowired
    StaffRepo staffRepo;

    @Autowired
    StageRepo stageRepo;

    /**
     * Get all processes
     * @param credential
     *    The credential of the staff member
     * @param bindingResult
     *   The result of the validation
     * @return
     * A list of all processes
     */
    @PostMapping("/getAll")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getAllProcesses(@Valid @RequestBody Credential credential,
                                             BindingResult bindingResult) {
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (credential.isValid()) { // Check if credential correspond to a staff member
            List<ProcessEntity> processes = processRepo.findAll();
            return new ResponseEntity<>(processes, HttpStatus.OK);
        } else {
            String jsonBody = "{\"message\": \"Invalid credentials\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Get process by id
     * @param credential
     *    The credential of the staff member
     * @param id
     *    The id of the process
     * @param bindingResult
     *   The result of the validation
     * @return
     * The process with the given id
     */
    @PostMapping("/getById/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getProcessById(@Valid @RequestBody Credential credential,
                                            @PathVariable Long id,
                                            BindingResult bindingResult) {
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (credential.isValid()) { // Check if credential correspond to a staff member
            if (!processRepo.existsById(id)) {
                String jsonBody = "{\"message\": \"Process not found\"}";
                return new ResponseEntity<>(jsonBody, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(processRepo.findById(id), HttpStatus.OK);
        } else {
            String jsonBody = "{\"message\": \"Invalid credentials\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Delete process by id
     * @param credential
     *    The credential of the staff member
     * @param id
     *    The id of the process
     * @param bindingResult
     *   The result of the validation
     * @return
     * A message indicating the success of the operation
     */
    @DeleteMapping("/delete/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deleteProcess(@Valid @RequestBody Credential credential,
                                           @PathVariable Long id,
                                           BindingResult bindingResult) {
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (credential.isValid()) { // Check if credential correspond to a staff member
            if (!processRepo.existsById(id)) {
                String jsonBody = "{\"message\": \"Process not found\"}";
                return new ResponseEntity<>(jsonBody, HttpStatus.NOT_FOUND);
            }
            processRepo.deleteById(id);
            //let's remove the process from all staff members
            if (processRepo.findById(id).isPresent()) {
                ProcessEntity process = processRepo.findById(id).get();
                for (String email : process.getStaffEmails()) {
                    if (staffRepo.findByEmail(email).isPresent()) {
                        StaffEntity staff = staffRepo.findByEmail(email).get();
                        staff.removeProcess(process);
                        staffRepo.save(staff);
                    }
                }
            }
            String jsonBody = "{\"message\": \"Process deleted\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.OK);
        } else {
            String jsonBody = "{\"message\": \"Invalid credentials\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Create a process
     * @param payloadProcessCreate
     *    The payload containing the credential and the process
     * @param bindingResult
     *   The result of the validation
     * @return
     * The created process
     */
    @PostMapping("/create")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> createProcess(@Valid @RequestBody PayloadProcessCreate payloadProcessCreate, BindingResult bindingResult) {
        Credential credential = payloadProcessCreate.getCredential();
        ProcessEntity process = payloadProcessCreate.getProcess();
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (!credential.isValid()) {
            String jsonBody = "{\"message\": \"Invalid credentials\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.UNAUTHORIZED);
        }
        for (String email : process.getStaffEmails()) { //check if all staff members exist
            if (!staffRepo.existsByEmail(email)) {
                String jsonBody = "{\"message\": \"Staff not found\"}";
                return new ResponseEntity<>(jsonBody, HttpStatus.NOT_FOUND);
            }
        }
        for (Long stageId : process.getStageIds()) { //check if all stages exist
            if (!stageRepo.existsById(stageId)) {
                String jsonBody = "{\"message\": \"Stage not found\"}";
                return new ResponseEntity<>(jsonBody, HttpStatus.NOT_FOUND);
            }
        }
        ProcessEntity savedProcess = processRepo.save(process);
        for (String email : process.getStaffEmails()) { //add process to staff members
            if(staffRepo.findByEmail(email).isPresent()) {
                StaffEntity newStaff = staffRepo.findByEmail(email).get();
                newStaff.addProcess(process);
                staffRepo.save(newStaff);
            }
        }
        return new ResponseEntity<>(savedProcess, HttpStatus.CREATED);
    }

    /**
     * Update a process
     * @param payloadProcessUpdate
     *    The payload containing the credential, the process and the process id
     * @param bindingResult
     *   The result of the validation
     * @return
     * The updated process
     */
    @PutMapping("/update")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> updateProcess(@Valid @RequestBody PayloadProcessUpdate payloadProcessUpdate, BindingResult bindingResult) {
        Credential credential = payloadProcessUpdate.getCredential();
        ProcessEntity newProcess = payloadProcessUpdate.getProcess();
        Long processId = payloadProcessUpdate.getProcessId();
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (!credential.isValid()) {
            String jsonBody = "{\"message\": \"Invalid credentials\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.UNAUTHORIZED);
        }
        if (!processRepo.existsById(processId)) {
            String jsonBody = "{\"message\": \"Process not found\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.NOT_FOUND);
        }
        newProcess.setId(processId);
        ProcessEntity savedProcess = processRepo.save(newProcess);
        if (processRepo.findById(processId).isPresent()) {
            ProcessEntity oldProcess = processRepo.findById(processId).get();
            for (String email : oldProcess.getStaffEmails()) { //remove process from staff members
                if(staffRepo.findByEmail(email).isPresent()) {
                    StaffEntity oldStaff = staffRepo.findByEmail(email).get();
                    oldStaff.removeProcess(oldProcess);
                    staffRepo.save(oldStaff);
                }
            }
            for (String email : newProcess.getStaffEmails()) { //add process to staff members
                if(staffRepo.findByEmail(email).isPresent()) {
                    StaffEntity newStaff = staffRepo.findByEmail(email).get();
                    newStaff.addProcess(newProcess);
                    staffRepo.save(newStaff);
                }
            }
        }
        return new ResponseEntity<>(savedProcess, HttpStatus.OK);
    }
}
