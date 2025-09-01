package com.esgi.app;

import com.esgi.model.Student;
import com.esgi.repository.FileStudentRepository;
import com.esgi.service.StudentService;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        var repoPath = Paths.get("src/main/resources/data/student.csv");
        var repo = new FileStudentRepository(repoPath);
        var service = new StudentService(repo);

        Student student_1 = service.create("Adel", "Bereksi");
        service.addGrade(student_1.getId(), "Math", 15.5);
        service.addGrade(student_1.getId(), "Science", 19.5);
        service.addGrade(student_1.getId(), "Physique", 18.5);
        System.out.println("Created: " + student_1);

        Student student_2 = service.create("Ismail", "Elbouch");
        service.addGrade(student_2.getId(), "Math", 16.5);
        service.addGrade(student_2.getId(), "Science", 18.5);
        service.addGrade(student_2.getId(), "Physique", 17.5);
        System.out.println("Created: " + student_2);

        System.out.println("All: " + service.findAll());
    }
}
