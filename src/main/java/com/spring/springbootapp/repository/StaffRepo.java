package com.spring.springbootapp.repository;


import com.spring.springbootapp.model.Staff;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StaffRepo extends CrudRepository<Staff, Long> {
    Staff findByEmail(String email);
    List<Staff> findAll();
}
