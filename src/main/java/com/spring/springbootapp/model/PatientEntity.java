package com.spring.springbootapp.model;

import com.spring.springbootapp.model.primaryKey.PatientId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "patient")
@IdClass(PatientId.class)
public class PatientEntity {
    @Id
    @NotBlank
    @Email
    private String email;

    @Id
    @NotBlank
    private String firstName;

    @Id
    @NotBlank
    private String lastName;

    @Id
    @NotBlank
    @Min(value = 0, message = "Age must be greater than or equal to 0")
    private int age;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Sex sex;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
