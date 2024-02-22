package com.spring.springbootapp.controller;

import com.spring.springbootapp.controller.payloads.staff.PayloadStaffCreate;
import com.spring.springbootapp.controller.payloads.staff.PayloadStaffUpdate;
import com.spring.springbootapp.model.Credential;
import com.spring.springbootapp.model.StaffEntity;
import com.spring.springbootapp.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staff")
@Controller
public class StaffController {
    @Autowired
    StaffRepo staffRepo;

    /**
     * Get all staff members
     * This api is available to all staff members
     * @param credential The credentials of the staff member
     *                   (email and passwordHash)
     *                   to be used to authenticate
     *                   the staff member
     *                   (required)
     * @return A list of all staff members
     *        if the credentials are valid
     *        (HTTP 200 OK)
     *        or an error message
     *        if the credentials are invalid
     *        (HTTP 401 UNAUTHORIZED)
     *
     */
    @PostMapping("/getAll")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getAll(@Valid @RequestBody Credential credential, BindingResult bindingResult) {
        credential.setStaffRepo(staffRepo);
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if(credential.isValid()) { // Check if credential correspond to a staff member
            List<StaffEntity> staff = staffRepo.findAll();
            // Remove passwordHash from the response
            for (StaffEntity s : staff) {
                s.setPasswordHash(null);
            }
            return new ResponseEntity<>(staff, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Get staff by email
     * This api is available to all staff members
     * @param email The email of the staff member
     *              to be used to retrieve the staff member
     *              (required)
     *              (in path)
     * @param credential The credentials of the staff member
     *                   (email and passwordHash)
     *                   to be used to authenticate
     *                   the staff member
     *                   (required)
     * @return The staff member with the given email
     *       if the credentials are valid
     *       (HTTP 200 OK)
     *       or an error message
     *       if the credentials are invalid
     *       (HTTP 401 UNAUTHORIZED)
     *       or if the staff member is not found
     *       (HTTP 404 NOT FOUND)
     *       or an error message
     */
    @PostMapping("/getByEmail/{email}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getByEmail(@Valid @RequestBody Credential credential,
                                             @PathVariable String email,
                                             BindingResult bindingResult) {
        credential.setStaffRepo(staffRepo);
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if(credential.isValid()) { // Check if credential correspond to a staff member
            if (!staffRepo.existsById(email)) {
                return new ResponseEntity<>("Staff not found", HttpStatus.NOT_FOUND);
            }
            Optional<StaffEntity> staff = staffRepo.findByEmail(email);
            // Remove passwordHash from the response
            staff.ifPresent(staffEntity -> staffEntity.setPasswordHash(null));
            return new ResponseEntity<>(staff, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Delete staff by email
     * This api is available to only the admin staff member
     * @param email The email of the staff member
     *              to be used to delete the staff member
     *              (required)
     *              (in path)
     * @param credential The credentials of the staff member
     *                   (email and passwordHash)
     *                   to be used to authenticate
     *                   the staff member
     *                   (required)
     * @return A success message
     *      if the credentials are valid
     *      and the staff member is deleted
     *      (HTTP 200 OK)
     *      or an error message
     *      if the credentials are invalid
     *      (HTTP 401 UNAUTHORIZED)
     *      or if the staff member is not found
     *      (HTTP 404 NOT FOUND)
     *      or an error message
     *      if the staff member is not an admin
     *      (HTTP 401 UNAUTHORIZED)
     */
    @DeleteMapping("/delete/{email}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> delete(@Valid @RequestBody Credential credential,
                                         @PathVariable String email,
                                         BindingResult bindingResult) {
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        // Check if credential correspond to a staff member and if the staff member is an admin
        Optional<StaffEntity> staff = staffRepo.findByEmail(credential.getEmail());
        if (credential.isValid() && staff.isPresent() && staff.get().isAdmin()) {
            if (!staffRepo.existsById(email)) {
                return new ResponseEntity<>("Staff not found", HttpStatus.NOT_FOUND);
            }
            staffRepo.deleteById(email);
            return new ResponseEntity<>("Staff deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/create")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> create(@Valid @RequestBody PayloadStaffCreate payloadStaffCreate, BindingResult bindingResult) {
        Credential credential = payloadStaffCreate.getCredential();
        StaffEntity staff = payloadStaffCreate.getStaff();
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        // Check if credential correspond to a staff member and if the staff member is an admin
        Optional<StaffEntity> staffOptional = staffRepo.findByEmail(credential.getEmail());
        if (credential.isValid() && staffOptional.isPresent() && staffOptional.get().isAdmin()) {
            StaffEntity savedStaff = staffRepo.save(staff);
            return new ResponseEntity<>(savedStaff, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }


    @PutMapping("/update")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> update(@Valid @RequestBody PayloadStaffUpdate payloadStaffUpdate, BindingResult bindingResult) {
        Credential credential = payloadStaffUpdate.getCredential();
        StaffEntity staff = payloadStaffUpdate.getStaff();
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        // Check if credential correspond to a staff member and if the staff member is an admin or the staff member itself
        Optional<StaffEntity> staffOptional = staffRepo.findByEmail(credential.getEmail());
        if (credential.isValid() && staffOptional.isPresent() && (staffOptional.get().isAdmin() || staffOptional.get().getEmail().equals(staff.getEmail()))) {
            if (!staffRepo.existsById(staff.getEmail())) {
                return new ResponseEntity<>("Staff not found", HttpStatus.NOT_FOUND);
            }
            staffRepo.deleteById(staff.getEmail());
            StaffEntity savedStaff = staffRepo.save(staff);
            return new ResponseEntity<>(savedStaff, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> login(@Valid @RequestBody Credential credential,
                                  BindingResult bindingResult) {
        credential.setStaffRepo(staffRepo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (credential.isValid()) {
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

}
