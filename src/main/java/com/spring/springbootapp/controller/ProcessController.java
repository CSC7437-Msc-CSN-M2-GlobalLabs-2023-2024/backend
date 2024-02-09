package com.spring.springbootapp.controller;

import com.spring.springbootapp.model.ProcessEntity;
import com.spring.springbootapp.repository.ProcessRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@Controller
public class ProcessController {
    @Autowired
    ProcessRepo processRepo;

    @GetMapping("/process/getAll")
    @CrossOrigin(origins = "*")
    public Iterable<ProcessEntity> getAllProcess() {
        return processRepo.findAll();
    }

    @GetMapping("/process/get/{id}")
    @CrossOrigin(origins = "*")
    public ProcessEntity getProcess(@PathVariable String id) {
        return processRepo.findById(id);
    }

    @DeleteMapping("/process/delete/{id}")
    @CrossOrigin(origins = "*")
    public void deleteProcess(@PathVariable String id) {
        processRepo.delete(processRepo.findById(id));
    }

    @PostMapping("/process/createOrUpdate")
    @CrossOrigin(origins = "*")
    public ProcessEntity createMember(@RequestBody ProcessEntity process) {
        return processRepo.save(process);
    }
}
