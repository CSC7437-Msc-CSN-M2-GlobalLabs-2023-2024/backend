package com.spring.springbootapp.repository;

import com.spring.springbootapp.model.ProcessEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProcessRepo extends CrudRepository<ProcessEntity, Long> {
    ProcessEntity findById(String id);
    Iterable<ProcessEntity> findAll();
}
