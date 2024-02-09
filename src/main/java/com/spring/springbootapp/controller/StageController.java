package com.spring.springbootapp.controller;


import com.spring.springbootapp.model.StageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.spring.springbootapp.repository.StageRepo;


@RestController
@RequestMapping("/")
@Controller
public class StageController {
    @Autowired
    StageRepo stageRepo;

    @GetMapping("/stage/getAll")
    @CrossOrigin(origins = "*")
    public Iterable<StageEntity> getAllStage() {
        return stageRepo.findAll();
    }

    @GetMapping("/stage/get/{id}")
    @CrossOrigin(origins = "*")
    public StageEntity getStage(@PathVariable String id) {
        return stageRepo.findById(id);
    }

    @DeleteMapping("/stage/delete/{id}")
    @CrossOrigin(origins = "*")
    public void deleteStage(@PathVariable String id) {
        stageRepo.delete(stageRepo.findById(id));
    }

    @PostMapping("/stage/createOrUpdate")
    @CrossOrigin(origins = "*")
    public StageEntity createMember(@RequestBody StageEntity stage) {
        return stageRepo.save(stage);
    }

}
