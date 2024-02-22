package com.spring.springbootapp.controller;

import com.spring.springbootapp.controller.payloads.stage.PayloadStageCreate;
import com.spring.springbootapp.controller.payloads.stage.PayloadStageUpdate;
import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.StageEntity;
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

    /**
     * Get all stages
     * This api is available to all staff members
     * @param credential The credentials of the staff member
     *                   (email and passwordHash)
     *                   to be used to authenticate
     *                   the staff member
     *                   (required)
     * @return A list of all stages
     *        if the credentials are valid
     *        (HTTP 200 OK)
     *        or an error message
     *        if the credentials are invalid
     *        (HTTP 401 UNAUTHORIZED)
     *
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
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Get a stage by ID
     * This api is available to all staff members
     * @param credential The credentials of the staff member
     *                   (email and passwordHash)
     *                   to be used to authenticate
     *                   the staff member
     *                   (required)
     * @param id The ID of the stage to be retrieved
     *           (required)
     * @return The stage with the given ID
     *        if the credentials are valid
     *        and the stage exists
     *        (HTTP 200 OK)
     *        or an error message
     *        if the credentials are invalid
     *        (HTTP 401 UNAUTHORIZED)
     *        or an error message
     *        if the stage does not exist
     *        (HTTP 404 NOT FOUND)
     *
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
                return new ResponseEntity<>("Stage not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(stageRepo.findById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Delete a stage
     * This api is available to all staff members
     * @param credential The credentials of the staff member
     *                   (email and passwordHash)
     *                   to be used to authenticate
     *                   the staff member
     *                   (required)
     * @param id The ID of the stage to be deleted
     *           (required)
     * @return A message indicating that the stage was deleted successfully
     *        if the credentials are valid
     *        and the stage exists
     *        (HTTP 200 OK)
     *        or an error message
     *        if the credentials are invalid
     *        (HTTP 401 UNAUTHORIZED)
     *        or an error message
     *        if the stage does not exist
     *        (HTTP 404 NOT FOUND)
     *
     */
    @DeleteMapping("/delete/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deleteStage(@Valid @RequestBody Credential credential,
                                         @PathVariable Long id,
                                         BindingResult bindingResult) {
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (credential.isValid()) { // Check if credential correspond to a staff member
            if (!stageRepo.existsById(id)) {
                return new ResponseEntity<>("Stage not found", HttpStatus.NOT_FOUND);
            }
            stageRepo.deleteById(id);
            return new ResponseEntity<>("Stage deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

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
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        StageEntity savedStage = stageRepo.save(stage);
        return new ResponseEntity<>(savedStage, HttpStatus.CREATED);
    }


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
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        if (!stageRepo.existsById(stage.getId())) {
            return new ResponseEntity<>("Stage not found", HttpStatus.NOT_FOUND);
        }
        // Additional validation logic if needed
        StageEntity savedStage = stageRepo.save(stage);
        return new ResponseEntity<>(savedStage, HttpStatus.OK);
    }
}
