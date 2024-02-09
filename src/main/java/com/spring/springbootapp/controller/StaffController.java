package com.spring.springbootapp.controller;

//import com.spring.springbootapp.Service.StaffService;
import com.spring.springbootapp.model.Staff;
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
    public List<Staff> getAllStaff() {
        return staffRepo.findAll();
    }

    @GetMapping("/staff/get/{email}")
    @CrossOrigin(origins = "*")
    public Staff getStaff(@PathVariable String email) {
        return staffRepo.findByEmail(email);
    }

    @GetMapping("/staff/delete/{email}")
    @CrossOrigin(origins = "*")
    public void deleteStaff(@PathVariable String email) {
        staffRepo.delete(staffRepo.findByEmail(email));
    }

    @PostMapping("/staff/createOrUpdate")
    @CrossOrigin(origins = "*")
    public Staff createMember(@RequestBody Staff staff) {
        return staffRepo.save(staff);
    }
}