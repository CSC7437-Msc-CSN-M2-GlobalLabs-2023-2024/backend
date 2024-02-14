package com.spring.springbootapp.controller;

import com.spring.springbootapp.model.ProcessEntity;
import com.spring.springbootapp.repository.ProcessRepo;
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

    /**
     * Get all processes
     */
    @GetMapping("/getAll")
    @CrossOrigin(origins = "*")
    public List<ProcessEntity> getAllProcesses() {
        return processRepo.findAll();
    }

    /**
     * Get a process by ID
     */
    @GetMapping("/getById/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getProcessById(@PathVariable Long id) {
        if (!processRepo.existsById(id)) {
            return new ResponseEntity<>("Process not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(processRepo.findById(id), HttpStatus.OK);
    }

    /**
     * Delete a process
     */
    @DeleteMapping("/delete/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deleteProcess(@PathVariable Long id) {
        if (!processRepo.existsById(id)) {
            return new ResponseEntity<>("Process not found", HttpStatus.NOT_FOUND);
        }
        processRepo.deleteById(id);
        return new ResponseEntity<>("Process deleted successfully", HttpStatus.OK);
    }

    /** Create a process */
    @PostMapping("/create")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> createProcess(@Valid @RequestBody ProcessEntity process, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        // Additional validation logic if needed
        ProcessEntity savedProcess = processRepo.save(process);
        return new ResponseEntity<>(savedProcess, HttpStatus.CREATED);
    }

    /**
     * Update a process
     */
    @PutMapping("/update")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> updateProcess(@Valid @RequestBody ProcessEntity process, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (!processRepo.existsById(process.getId())) {
            return new ResponseEntity<>("Process not found", HttpStatus.NOT_FOUND);
        }
        // Additional validation logic if needed
        ProcessEntity savedProcess = processRepo.save(process);
        return new ResponseEntity<>(savedProcess, HttpStatus.OK);
    }
}
