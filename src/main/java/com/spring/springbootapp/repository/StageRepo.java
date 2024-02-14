package com.spring.springbootapp.repository;

import com.spring.springbootapp.model.StageEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface StageRepo extends CrudRepository<StageEntity, Long> {
    Optional<StageEntity> findById(Long id);
    List<StageEntity> findAll();
    boolean existsById(Long id);
}
