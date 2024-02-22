package com.spring.springbootapp.Service;

import com.spring.springbootapp.model.StaffEntity;
import com.spring.springbootapp.repository.PatientRepo;
import com.spring.springbootapp.repository.ProcessRepo;
import com.spring.springbootapp.repository.StaffRepo;
import com.spring.springbootapp.repository.StageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class StaffService {
    @Autowired
    StaffRepo staffRepo;

    @Autowired
    PatientRepo patientRepo;

    @Autowired
    ProcessRepo processRepo;

    @Autowired
    StageRepo stageRepo;

    public void clean() {
        staffRepo.deleteAll();
        patientRepo.deleteAll();
        processRepo.deleteAll();
        stageRepo.deleteAll();
    }
    public void init() {
        clean();
        if(staffRepo.count() == 0) {
            StaffEntity admin = new StaffEntity("admin@admin.com", "admin", "admin", "admin", "admin", true, new ArrayList<>());
            staffRepo.save(admin);
        }
    }
}
