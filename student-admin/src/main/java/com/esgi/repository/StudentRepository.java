package com.esgi.repository;

import com.esgi.model.Student;
import java.io.IOException;
import java.util.*;

public interface StudentRepository {
    Optional<Student> findById(String id) throws IOException;
    List<Student> findAll() throws IOException;
    Student save(Student s) throws IOException;
    boolean deleteById(String id) throws IOException;
}
