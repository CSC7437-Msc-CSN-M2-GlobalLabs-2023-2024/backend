package com.spring.springbootapp.Service;


import com.spring.springbootapp.model.StaffEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service()
public interface StaffService {
    StaffEntity create(StaffEntity staff);
    List<StaffEntity> findAll();
    StaffEntity findByEmail(String email);
}
