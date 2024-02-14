package com.spring.springbootapp.repository;

import com.spring.springbootapp.model.PatientEntity;
import com.spring.springbootapp.model.primaryKey.PatientId;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface PatientRepo extends CrudRepository<PatientEntity, PatientId> {
    Optional<PatientEntity> findById(PatientId patientId);
    List<PatientEntity> findAll();
    boolean existsById(PatientId patientId);
}
