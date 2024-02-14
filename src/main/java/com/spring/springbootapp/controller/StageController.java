package com.spring.springbootapp.controller;

import com.spring.springbootapp.model.StageEntity;
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
@RequestMapping("/api/stage")
@Controller
public class StageController {
    @Autowired
    StageRepo stageRepo;

    /**
     * Get all stages
     */
    @GetMapping("/getAll")
    @CrossOrigin(origins = "*")
    public List<StageEntity> getAllStages() {
        return stageRepo.findAll();
    }

    /**
     * Get stage by ID
     */
    @GetMapping("/getById/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getStageById(@PathVariable Long id) {
        if (!stageRepo.existsById(id)) {
            return new ResponseEntity<>("Stage not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(stageRepo.findById(id), HttpStatus.OK);
    }

    /**
     * Delete stage by ID
     */
    @DeleteMapping("/delete/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deleteStage(@PathVariable Long id) {
        if (!stageRepo.existsById(id)) {
            return new ResponseEntity<>("Stage not found", HttpStatus.NOT_FOUND);
        }
        stageRepo.deleteById(id);
        return new ResponseEntity<>("Stage deleted successfully", HttpStatus.OK);
    }

    /** Create a stage */
    @PostMapping("/create")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> createStage(@Valid @RequestBody StageEntity stage, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        // Additional validation logic if needed
        StageEntity savedStage = stageRepo.save(stage);
        return new ResponseEntity<>(savedStage, HttpStatus.CREATED);
    }

    /**
     * Update a stage
     */
    @PutMapping("/update")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> updateStage(@Valid @RequestBody StageEntity stage, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (!stageRepo.existsById(stage.getId())) {
            return new ResponseEntity<>("Stage not found", HttpStatus.NOT_FOUND);
        }
        // Additional validation logic if needed
        StageEntity savedStage = stageRepo.save(stage);
        return new ResponseEntity<>(savedStage, HttpStatus.OK);
    }
}
