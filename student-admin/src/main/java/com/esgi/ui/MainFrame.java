package com.esgi.ui;

import com.esgi.service.StudentService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame(StudentService service, boolean isAdmin, String username) {
        setTitle("Gestion des Étudiants - connecté: " + username + (isAdmin ? " (admin)" : " (lecture)"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 640);
        setLocationRelativeTo(null);

        setJMenuBar(buildMenuBar());
        getContentPane().add(new StudentPanel(service, isAdmin), BorderLayout.CENTER);
        getContentPane().add(status("Prêt."), BorderLayout.SOUTH);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("Fichier");
        JMenuItem quit = new JMenuItem("Quitter");
        quit.addActionListener(e -> System.exit(0));
        file.add(quit);
        JMenu help = new JMenu("Aide");
        JMenuItem about = new JMenuItem("À propos");
        about.addActionListener(e -> JOptionPane.showMessageDialog(this,"Démo Swing CRUD + notes"));
        help.add(about);
        bar.add(file); bar.add(help);
        return bar;
    }

    private JComponent status(String txt) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
        p.add(new JLabel(txt), BorderLayout.WEST);
        return p;
    }
}
