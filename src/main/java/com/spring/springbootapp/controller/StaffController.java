package com.spring.springbootapp.controller;

//import com.spring.springbootapp.Service.StaffService;
import com.spring.springbootapp.model.StaffEntity;
import com.spring.springbootapp.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@Controller
public class StaffController {
    @Autowired
    StaffRepo staffRepo;
    //@Autowired
    //StaffService staffService;

    @GetMapping("/staff/getAll")
    @CrossOrigin(origins = "*")
    public List<StaffEntity> getAllStaff() {
        return staffRepo.findAll();
    }

    @GetMapping("/staff/get/{email}")
    @CrossOrigin(origins = "*")
    public StaffEntity getStaff(@PathVariable String email) {
        return staffRepo.findByEmail(email);
    }

    @DeleteMapping("/staff/delete/{email}")
    @CrossOrigin(origins = "*")
    public void deleteStaff(@PathVariable String email) {
        staffRepo.delete(staffRepo.findByEmail(email));
    }

    @PostMapping("/staff/createOrUpdate")
    @CrossOrigin(origins = "*")
    public StaffEntity createMember(@RequestBody StaffEntity staff) {
        return staffRepo.save(staff);
    }
}