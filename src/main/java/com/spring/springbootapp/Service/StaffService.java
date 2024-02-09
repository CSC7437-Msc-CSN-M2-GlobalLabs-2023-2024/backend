package com.spring.springbootapp.Service;


import com.spring.springbootapp.model.Staff;
import org.springframework.stereotype.Service;

import java.util.List;

@Service()
public interface StaffService {
    Staff create(Staff staff);
    List<Staff> findAll();
    Staff findByEmail(String email);
}
