package com.spring.springbootapp;

import com.spring.springbootapp.model.primaryKey.PatientId;
import org.mockito.ArgumentMatcher;

public class PatientIdMatcher implements ArgumentMatcher<PatientId> {

    private final PatientId expectedId;

    public PatientIdMatcher(PatientId expectedId) {
        this.expectedId = expectedId;
    }

    @Override
    public boolean matches(PatientId actualId) {
        return actualId.getEmail().equals(expectedId.getEmail())
                && actualId.getFirstName().equals(expectedId.getFirstName())
                && actualId.getLastName().equals(expectedId.getLastName())
                && actualId.getAge() == expectedId.getAge();
    }
}
