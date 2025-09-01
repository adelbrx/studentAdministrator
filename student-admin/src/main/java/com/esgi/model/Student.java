package com.esgi.model;

import java.util.*;

public class Student {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final Map<String, Double> grades = new HashMap<>();

    public Student(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Map<String, Double> getGrades() {
        return grades;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + id + ") " + grades;
    }
}
