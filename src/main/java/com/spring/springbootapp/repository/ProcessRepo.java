package com.spring.springbootapp.repository;

import com.spring.springbootapp.model.ProcessEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProcessRepo extends CrudRepository<ProcessEntity, Long> {
    Optional<ProcessEntity> findById(Long id);
    List<ProcessEntity> findAll();
    boolean existsById(Long id);
}
