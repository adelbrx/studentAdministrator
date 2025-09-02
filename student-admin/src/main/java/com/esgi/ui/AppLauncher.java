package com.esgi.ui;

import com.esgi.repository.FileStudentRepository;
import com.esgi.service.StudentService;

import javax.swing.*;
import java.nio.file.Paths;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var repo = new FileStudentRepository(Paths.get("src/main/resources/data/students.csv"));
            var service = new StudentService(repo);
            new LoginFrame(service).setVisible(true);
        });
    }
}
