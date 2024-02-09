package com.spring.springbootapp.controller;

//import com.spring.springbootapp.Service.StaffService;
import com.spring.springbootapp.model.Staff;
import com.spring.springbootapp.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/")
@Controller
public class StaffController {
    @Autowired
    StaffRepo staffRepo;
    //@Autowired
    //StaffService staffService;

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/staff/getAll")
    public List<Staff> getAllStaff() {
        return staffRepo.findAll();
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/staff/get/{email}")
    public Staff getStaff(@PathVariable String email) {
        return staffRepo.findByEmail(email);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/staff/delete/{email}")
    public void deleteStaff(@PathVariable String email) {
        staffRepo.delete(staffRepo.findByEmail(email));
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/staff/createOrUpdate")
    public Staff createMember(@RequestBody Staff staff) {
        return staffRepo.save(staff);
    }
}