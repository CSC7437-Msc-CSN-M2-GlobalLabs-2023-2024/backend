package com.spring.springbootapp.repository;

import com.spring.springbootapp.model.PatientEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
public interface PatientRepo extends CrudRepository<PatientEntity, Long> {
    PatientEntity findByEmail(String email);
    List<PatientEntity> findAll();
}
