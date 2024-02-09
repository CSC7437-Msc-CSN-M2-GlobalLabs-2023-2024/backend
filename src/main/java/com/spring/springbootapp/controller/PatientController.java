package com.spring.springbootapp.controller;

import com.spring.springbootapp.model.PatientEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import com.spring.springbootapp.repository.PatientRepo;

import java.util.List;

@RestController
@RequestMapping("/")
@Controller
public class PatientController {
    @Autowired
    PatientRepo patientRepo;

    @GetMapping("/patient/getAll")
    @CrossOrigin(origins = "*")
    public List<PatientEntity> getAllPatient() {
        return patientRepo.findAll();
    }

    @GetMapping("/patient/get/{email}")
    @CrossOrigin(origins = "*")
    public PatientEntity getPatient(@PathVariable String email) {
        return patientRepo.findByEmail(email);
    }

    @DeleteMapping("/patient/delete/{email}")
    @CrossOrigin(origins = "*")
    public void deletePatient(@PathVariable String email) {
        patientRepo.delete(patientRepo.findByEmail(email));
    }

    @PostMapping("/patient/createOrUpdate")
    @CrossOrigin(origins = "*")
    public PatientEntity createMember(@RequestBody PatientEntity patient) {
        return patientRepo.save(patient);
    }

}
