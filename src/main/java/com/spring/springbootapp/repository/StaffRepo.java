package com.spring.springbootapp.repository;


import com.spring.springbootapp.model.StaffEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StaffRepo extends CrudRepository<StaffEntity, String> {
    Optional<StaffEntity>findByEmail(String email);
    List<StaffEntity> findAll();
    boolean existsByEmail(String email);
}
