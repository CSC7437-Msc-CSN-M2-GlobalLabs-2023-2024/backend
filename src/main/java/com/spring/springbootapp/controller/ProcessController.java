package com.spring.springbootapp.controller;

import com.spring.springbootapp.controller.payloads.process.PayloadProcessCreate;
import com.spring.springbootapp.controller.payloads.process.PayloadProcessUpdate;
import com.spring.springbootapp.controller.payloads.stage.PayloadStageUpdate;
import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.ProcessEntity;
import com.spring.springbootapp.model.StaffEntity;
import com.spring.springbootapp.repository.ProcessRepo;
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
@RequestMapping("/api/process")
@Controller
public class ProcessController {
    @Autowired
    ProcessRepo processRepo;

    @Autowired
    StaffRepo staffRepo;

    /**
     * Get all processes
     * This api is available to all staff members
     * @param credential The credentials of the staff member
     *                   (email and passwordHash)
     *                   to be used to authenticate
     *                   the staff member
     *                   (required)
     * @return A list of all processes
     *        if the credentials are valid
     *        (HTTP 200 OK)
     *        or an error message
     *        if the credentials are invalid
     *        (HTTP 401 UNAUTHORIZED)
     *
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
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Get a process by ID
     * This api is available to all staff members
     * @param credential The credentials of the staff member
     *                   (email and passwordHash)
     *                   to be used to authenticate
     *                   the staff member
     *                   (required)
     * @param id The ID of the process to be retrieved
     *           (required)
     * @return The process with the specified ID
     *        if the credentials are valid
     *        and the process exists
     *        (HTTP 200 OK)
     *        or an error message
     *        if the credentials are invalid
     *        (HTTP 401 UNAUTHORIZED)
     *        or an error message
     *        if the process does not exist
     *        (HTTP 404 NOT FOUND)
     *
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
                return new ResponseEntity<>("Process not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(processRepo.findById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Delete a process
     * This api is available to all staff members
     * @param credential The credentials of the staff member
     *                   (email and passwordHash)
     *                   to be used to authenticate
     *                   the staff member
     *                   (required)
     * @param id The ID of the process to be deleted
     *           (required)
     * @return A message indicating that the process was deleted
     *        if the credentials are valid
     *        and the process exists
     *        (HTTP 200 OK)
     *        or an error message
     *        if the credentials are invalid
     *        (HTTP 401 UNAUTHORIZED)
     *        or an error message
     *        if the process does not exist
     *        (HTTP 404 NOT FOUND)
     *
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
                return new ResponseEntity<>("Process not found", HttpStatus.NOT_FOUND);
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
            return new ResponseEntity<>("Process deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

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
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        for (String email : process.getStaffEmails()) { //check if all staff members exist
            if (!staffRepo.existsByEmail(email)) {
                return new ResponseEntity<>("Staff not found", HttpStatus.NOT_FOUND);
            }
        }
        for (Long stageId : process.getStageIds()) { //check if all stages exist
            if (!processRepo.existsById(stageId)) {
                return new ResponseEntity<>("Stage not found", HttpStatus.NOT_FOUND);
            }
        }
        for (String email : process.getStaffEmails()) { //add process to staff members
            if(staffRepo.findByEmail(email).isPresent()) {
                StaffEntity newStaff = staffRepo.findByEmail(email).get();
                newStaff.addProcess(process);
                staffRepo.save(newStaff);
            }
        }
        ProcessEntity savedProcess = processRepo.save(process);
        return new ResponseEntity<>(savedProcess, HttpStatus.CREATED);
    }

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
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        if (!processRepo.existsById(processId)) {
            return new ResponseEntity<>("Process not found", HttpStatus.NOT_FOUND);
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
