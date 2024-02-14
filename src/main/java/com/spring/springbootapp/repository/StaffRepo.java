package com.spring.springbootapp.repository;


import com.spring.springbootapp.model.StaffEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StaffRepo extends CrudRepository<StaffEntity, String> {
    StaffEntity findByEmail(String email);
    List<StaffEntity> findAll();
    boolean existsByEmail(String email);
}
