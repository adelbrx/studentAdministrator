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

    public List<Student> findAll() throws IOException { return repo.findAll(); }
    public boolean delete(String id) throws IOException { return repo.deleteById(id); }

    public Optional<Student> findOptional(String id) throws IOException { return repo.findById(id); }

    public Student getById(String id) throws IOException {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Student not found"));
    }

    // modifif des noms
    public Student updateName(String id, String first, String last) throws IOException {
        if (first == null || first.isBlank() || last == null || last.isBlank())
            throw new IllegalArgumentException("First name/Last name are required");
        var s = getById(id);
        s.setFirstName(first);
        s.setLastName(last);
        return repo.save(s);
    }

    // notes: ajout / upsert
    public void addGrade(String studentId, String subject, double value) throws IOException {
        if (subject == null || subject.isBlank()) throw new IllegalArgumentException("Subject is required");
        if (value < 0 || value > 20) throw new IllegalArgumentException("Grade should be 0..20");
        Student s = getById(studentId);
        s.getGrades().put(subject.trim(), value);
        repo.save(s);
    }

    // suppression d'une note
    public void deleteGrade(String studentId, String subject) throws IOException {
        Student s = getById(studentId);
        s.getGrades().remove(subject);
        repo.save(s);
    }

    public List<Student> searchByName(String q) throws IOException {
        String qq = q == null ? "" : q.toLowerCase();
        List<Student> out = new ArrayList<>();
        for (Student s : repo.findAll()) {
            if ((s.getFirstName()+" "+s.getLastName()).toLowerCase().contains(qq)) out.add(s);
        }
        return out;
    }
}
