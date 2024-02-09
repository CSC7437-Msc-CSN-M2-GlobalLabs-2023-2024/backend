package com.spring.springbootapp.Service.ServiceImpl;

import com.spring.springbootapp.repository.StaffRepo;
import com.spring.springbootapp.Service.StaffService;
import com.spring.springbootapp.model.Staff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {
    private static final Logger LOG = LoggerFactory.getLogger(StaffService.class);
    @Autowired
    private StaffRepo staffRepo;

    @Override
    public Staff create(Staff user) {
        return staffRepo.save(user);
    }

    @Override
    public List<Staff> findAll() {
        return staffRepo.findAll();
    }

    @Override
    public Staff findByEmail(String email) {
        return staffRepo.findByEmail(email);
    }
}
