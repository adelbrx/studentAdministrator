package com.esgi.service;

import com.esgi.model.Student;
import com.esgi.repository.StudentRepository;

import java.io.IOException;
import java.util.*;

public class StudentService {
    private final StudentRepository repo;

    public StudentService(StudentRepository repo) { this.repo = repo; }

    public Student create(String first, String last) throws IOException {
        if (first == null || first.isBlank() || last == null || last.isBlank())
            throw new IllegalArgumentException("First name/Last name are required");
        var s = new Student(UUID.randomUUID().toString(), first.trim(), last.trim());
        return repo.save(s);
    }

    public void addGrade(String studentId, String subject, double value) throws IOException {
        if (value < 0 || value > 20) throw new IllegalArgumentException("Grade should be 0..20");
        Student s = repo.findById(studentId).orElseThrow(() -> new NoSuchElementException("Student not found"));
        s.getGrades().put(subject, value);
        repo.save(s);
    }

    public List<Student> findAll() throws IOException { return repo.findAll(); }

    public boolean delete(String id) throws IOException { return repo.deleteById(id); }

    public List<Student> searchByName(String q) throws IOException {
        String qq = q == null ? "" : q.toLowerCase();
        List<Student> out = new ArrayList<>();
        for (Student s : repo.findAll()) {
            if ((s.getFirstName()+" "+s.getLastName()).toLowerCase().contains(qq)) out.add(s);
        }
        return out;
    }
}
