package com.spring.springbootapp.repository;

import com.spring.springbootapp.model.StageEntity;
import org.springframework.data.repository.CrudRepository;


public interface StageRepo extends CrudRepository<StageEntity, Long> {
    StageEntity findById(String id);
    Iterable<StageEntity> findAll();
}
