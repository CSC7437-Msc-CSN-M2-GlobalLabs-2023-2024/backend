package com.spring.springbootapp.controller;

import com.spring.springbootapp.controller.payloads.stage.PayloadStageCreate;
import com.spring.springbootapp.controller.payloads.stage.PayloadStageUpdate;
import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.ProcessEntity;
import com.spring.springbootapp.model.StageEntity;
import com.spring.springbootapp.repository.ProcessRepo;
import com.spring.springbootapp.repository.StageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.spring.springbootapp.repository.StaffRepo;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/stage")
@Controller
public class StageController {
    @Autowired
    StageRepo stageRepo;

    @Autowired
    StaffRepo staffRepo;

    @Autowired
    ProcessRepo processRepo;

    /**
     * Get all stages
     * @param credential
     *    The credential of the staff member
     * @param bindingResult
     *   The result of the validation
     * @return
     * A list of all stages
     */
    @PostMapping("/getAll")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getAllStages(@Valid @RequestBody Credential credential,
                                         BindingResult bindingResult) {
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (credential.isValid()) { // Check if credential correspond to a staff member
            List<StageEntity> stages = stageRepo.findAll();
            return new ResponseEntity<>(stages, HttpStatus.OK);
        } else {
            String jsonBody = "{\"message\": \"Invalid credentials\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Get stage by id
     * @param credential
     *    The credential of the staff member
     * @param id
     *    The id of the stage
     * @param bindingResult
     *   The result of the validation
     * @return
     * The stage with the given id
     */
    @PostMapping("/getById/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getStageById(@Valid @RequestBody Credential credential,
                                          @PathVariable Long id,
                                          BindingResult bindingResult) {
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (credential.isValid()) { // Check if credential correspond to a staff member
            if (!stageRepo.existsById(id)) {
                String jsonBody = "{\"message\": \"Stage not found\"}";
                return new ResponseEntity<>(jsonBody, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(stageRepo.findById(id), HttpStatus.OK);
        } else {
            String jsonBody = "{\"message\": \"Invalid credentials\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Delete a stage by id
     * @param credential
     *    The credential of the staff member
     * @param stageId
     *    The id of the stage
     * @param bindingResult
     *   The result of the validation
     * @return
     * A message indicating the result of the operation
     */
    @DeleteMapping("/delete/{stageId}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deleteStage(@Valid @RequestBody Credential credential,
                                         @PathVariable Long stageId,
                                         BindingResult bindingResult) {
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        // Check if credential correspond to a staff member
        if (!credential.isValid()) {
            String jsonBody = "{\"message\": \"Invalid credentials\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.UNAUTHORIZED);
        }
        // Check if the stage exists
        if (!stageRepo.existsById(stageId)) {
            String jsonBody = "{\"message\": \"Stage not found\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.NOT_FOUND);
        }
        // Delete the stage in process list
        if (stageRepo.findById(stageId).isPresent() && processRepo.findById(stageRepo.findById(stageId).get().getProcessId()).isPresent()) {
            ProcessEntity process = processRepo.findById(stageRepo.findById(stageId).get().getProcessId()).get();
            List<Long> newProcessStages = process.getStageIds();
            newProcessStages.remove(stageId);
            process.setStageIds(newProcessStages);
            processRepo.save(process);
        }
        // Delete the stage
        stageRepo.deleteById(stageId);
        String jsonBody = "{\"message\": \"Stage deleted successfully\"}";
        return new ResponseEntity<>(jsonBody, HttpStatus.OK);
    }

    /**
     * Create a stage
     * @param payloadStageCreate
     *    The payload containing the credential and the stage to create
     * @param bindingResult
     *   The result of the validation
     * @return
     * The stage created
     */
    @PostMapping("/create")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> createStage(@Valid @RequestBody PayloadStageCreate payloadStageCreate, BindingResult bindingResult) {
        Credential credential = payloadStageCreate.getCredential();
        StageEntity stage = payloadStageCreate.getStage();
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (!credential.isValid()) {
            String jsonBody = "{\"message\": \"Invalid credentials\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.UNAUTHORIZED);
        }
        // Check if the processID of the stage exists
        if (!processRepo.existsById(stage.getProcessId())) {
            String jsonBody = "{\"message\": \"Process not found\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.NOT_FOUND);
        }
        StageEntity savedStage = stageRepo.save(stage);
        return new ResponseEntity<>(savedStage, HttpStatus.CREATED);
    }

    /**
     * Update a stage
     * @param payloadStageUpdate
     *    The payload containing the credential and the stage to update
     * @param bindingResult
     *   The result of the validation
     * @return
     * The updated stage
     */
    @PutMapping("/update")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> updateStage(@Valid @RequestBody PayloadStageUpdate payloadStageUpdate, BindingResult bindingResult) {
        Credential credential = payloadStageUpdate.getCredential();
        StageEntity stage = payloadStageUpdate.getStage();
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (!credential.isValid()) {
            String jsonBody = "{\"message\": \"Invalid credentials\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.UNAUTHORIZED);
        }
        if (!stageRepo.existsById(stage.getId())) {
            String jsonBody = "{\"message\": \"Stage not found\"}";
            return new ResponseEntity<>(jsonBody, HttpStatus.NOT_FOUND);
        }
        // Additional validation logic if needed
        StageEntity savedStage = stageRepo.save(stage);
        return new ResponseEntity<>(savedStage, HttpStatus.OK);
    }
}
