package com.esgi.ui;

import com.esgi.service.StudentService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final StudentService service;
    private final JTextField user = new JTextField(15);
    private final JPasswordField pass = new JPasswordField(15);

    public LoginFrame(StudentService service) {
        this.service = service;
        setTitle("Connexion");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(340, 160);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridLayout(3,2,6,6));
        p.add(new JLabel("Utilisateur:")); p.add(user);
        p.add(new JLabel("Mot de passe:")); p.add(pass);
        JButton login = new JButton("Se connecter");
        p.add(new JLabel()); p.add(login);
        add(p);

        login.addActionListener(e -> {
            String u = user.getText().trim();
            String pwd = new String(pass.getPassword());
            boolean isAdmin = "admin".equals(u) && "admin".equals(pwd);
            dispose();
            new MainFrame(service, isAdmin, u.isEmpty() ? "invite" : u).setVisible(true);
        });
    }
}
