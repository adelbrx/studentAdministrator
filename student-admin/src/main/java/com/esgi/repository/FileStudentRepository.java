package com.esgi.repository;

import com.esgi.model.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;

public class FileStudentRepository implements StudentRepository {
    private final Path file;
    private final Gson gson = new Gson();
    private final Type mapType = new TypeToken<Map<String, Double>>(){}.getType();

    public FileStudentRepository(Path file) {
        this.file = file;
    }

    @Override
    public Optional<Student> findById(String id) throws IOException {
        return findAll().stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    @Override
    public List<Student> findAll() throws IOException {
        ensureFile();
        List<Student> list = new ArrayList<>();
        for (String line : Files.readAllLines(file)) {
            if (line.isBlank()) continue;
            String[] parts = line.split(";", -1);
            var student = new Student(parts[0], parts[1], parts[2]);
            Map<String, Double> grades = gson.fromJson(parts[3].isEmpty() ? "{}" : parts[3], HashMap.class);

            student.getGrades().putAll(grades);
            list.add(student);
        }
        return list;
    }

    @Override
    public Student save(Student student) throws IOException {
        List<Student> all = new ArrayList<>(findAll());
        all.removeIf(x -> x.getId().equals(student.getId()));
        all.add(student);
        writeAll(all);
        return student;
    }

    @Override
    public boolean deleteById(String id) throws IOException {
        List<Student> all = new ArrayList<>(findAll());
        boolean removed = all.removeIf(x -> x.getId().equals(id));
        if (removed) writeAll(all);
        return removed;
    }

    private void writeAll(List<Student> students) throws IOException {
        ensureFile();
        try (BufferedWriter w = Files.newBufferedWriter(file)) {
            for (Student student : students) {
                String gradesJson = gson.toJson(student.getGrades());
                w.write(String.join(";", student.getId(), student.getFirstName(), student.getLastName(), gradesJson));
                w.newLine();
            }
        }
    }

    private void ensureFile() throws IOException {
        Files.createDirectories(file.getParent());
        if (Files.notExists(file)) Files.createFile(file);
    }
}
