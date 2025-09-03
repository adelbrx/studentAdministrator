package com.esgi.ui;

import com.esgi.model.Student;
import com.esgi.service.StudentService;
import com.esgi.ui.table.StudentTableModel;
import com.esgi.ui.table.GradesTableModel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import com.esgi.ui.render.ScoreColorRenderer;


public class StudentPanel extends JPanel {
    private final StudentService service;
    private final boolean isAdmin;

    private final StudentTableModel studentModel = new StudentTableModel();
    private final JTable studentTable = new JTable(studentModel);

    private final GradesTableModel gradesModel = new GradesTableModel();
    private final JTable gradesTable = new JTable(gradesModel);


    private final JTextField searchField = new JTextField(20);

    private final JButton addStudentBtn = new JButton("Ajouter");
    private final JButton editStudentBtn = new JButton("Modifier");
    private final JButton delStudentBtn  = new JButton("Supprimer");

    private final JButton addGradeBtn = new JButton("Ajouter/MAJ note");
    private final JButton delGradeBtn = new JButton("Supprimer note");

    public StudentPanel(StudentService service, boolean isAdmin) {
        this.service = service; this.isAdmin = isAdmin;
        setLayout(new BorderLayout(8,8));
        setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        add(topBar(), BorderLayout.NORTH);
        add(centerSplit(), BorderLayout.CENTER);
        add(bottomBar(), BorderLayout.SOUTH);
        configureRenderers();
        loadStudents();
        hookSelection();
        updateButtons();
    }

    private void configureRenderers() {
        var renderer = new ScoreColorRenderer();

        // Table des notes (droite) : col 1 = "Note"
        if (gradesTable.getColumnModel().getColumnCount() > 1) {
            gradesTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
        }

        // Table des étudiants (gauche) : col 4 = "Moyenne"
        if (studentTable.getColumnModel().getColumnCount() > 4) {
            studentTable.getColumnModel().getColumn(4).setCellRenderer(renderer);
        }
    }


    private JComponent topBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton searchBtn = new JButton("Rechercher");
        JButton refreshBtn = new JButton("Rafraîchir");
        searchBtn.addActionListener(e -> search());
        refreshBtn.addActionListener(e -> loadStudents());
        p.add(new JLabel("Nom/Prénom:"));
        p.add(searchField);
        p.add(searchBtn);
        p.add(refreshBtn);
        return p;
    }

    private JComponent centerSplit() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(studentTable), new JScrollPane(gradesTable));
        split.setResizeWeight(0.6);
        return split;
    }

    private JComponent bottomBar() {
        JPanel root = new JPanel(new GridLayout(1,2,8,8));
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        if (isAdmin) {
            left.add(addStudentBtn);
            left.add(editStudentBtn);
            left.add(delStudentBtn);
        }
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.add(addGradeBtn);
        right.add(delGradeBtn);

        addStudentBtn.addActionListener(e -> onAddStudent());
        editStudentBtn.addActionListener(e -> onEditStudent());
        delStudentBtn.addActionListener(e -> onDeleteStudent());

        addGradeBtn.addActionListener(e -> onAddOrUpdateGrade());
        delGradeBtn.addActionListener(e -> onDeleteGrade());

        root.add(left); root.add(right);
        return root;
    }

    private void hookSelection() {
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) refreshGrades();
            updateButtons();
        });
        gradesTable.getSelectionModel().addListSelectionListener(e -> updateButtons());
    }

    private void updateButtons() {
        boolean hasStudent = studentTable.getSelectedRow() >= 0;
        if (isAdmin) {
            editStudentBtn.setEnabled(hasStudent);
            delStudentBtn.setEnabled(hasStudent);
        }
        addGradeBtn.setEnabled(hasStudent);
        delGradeBtn.setEnabled(isAdmin && hasStudent && gradesTable.getSelectedRow() >= 0);
    }

    private String selectedStudentId() {
        int r = studentTable.getSelectedRow();
        if (r < 0) return null;
        Student s = studentModel.getAt(r);
        return s.getId();
    }

    // ---- Data
    private void loadStudents() {
        try {
            studentModel.setStudents(service.findAll());
            refreshGrades();
        } catch (IOException ex) {
            error("Load students", ex);
        }
    }

    private void refreshGrades() {
        String id = selectedStudentId();
        try {
            if (id == null) gradesModel.setGrades(java.util.Map.of());
            else gradesModel.setGrades(service.getById(id).getGrades());
        } catch (Exception ex) {
            error("Load grades", ex);
        }
    }

    private void search() {
        try {
            String q = searchField.getText();
            List<Student> res = (q == null || q.isBlank()) ? service.findAll() : service.searchByName(q);
            studentModel.setStudents(res);
            gradesModel.setGrades(java.util.Map.of());
        } catch (IOException ex) {
            error("Search", ex);
        }
    }

    // ---- Étudiants
    private void onAddStudent() {
        if (!isAdmin) { info("Mode lecture. Connecte-toi en admin."); return; }
        JTextField first = new JTextField();
        JTextField last  = new JTextField();
        int ok = JOptionPane.showConfirmDialog(this, new Object[]{"Prénom:", first, "Nom:", last},
                "Ajouter un étudiant", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            try { service.create(first.getText(), last.getText()); loadStudents(); }
            catch (Exception ex) { error("Add student", ex); }
        }
    }

    private void onEditStudent() {
        if (!isAdmin) { info("Mode lecture. Connecte-toi en admin."); return; }
        String id = selectedStudentId(); if (id == null) return;
        try {
            Student s = service.getById(id);
            JTextField first = new JTextField(s.getFirstName());
            JTextField last  = new JTextField(s.getLastName());
            int ok = JOptionPane.showConfirmDialog(this, new Object[]{"Prénom:", first, "Nom:", last},
                    "Modifier étudiant", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                service.updateName(id, first.getText(), last.getText());
                loadStudents();
            }
        } catch (Exception ex) { error("Edit student", ex); }
    }

    private void onDeleteStudent() {
        if (!isAdmin) { info("Mode lecture. Connecte-toi en admin."); return; }
        String id = selectedStudentId(); if (id == null) return;
        int ok = JOptionPane.showConfirmDialog(this, "Supprimer cet étudiant ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            try { service.delete(id); loadStudents(); } catch (Exception ex) { error("Delete student", ex); }
        }
    }

    // ---- Notes
    private void onAddOrUpdateGrade() {
        String id = selectedStudentId(); if (id == null) { info("Sélectionne un étudiant."); return; }
        JTextField subject = new JTextField();
        JTextField grade   = new JTextField();
        int ok = JOptionPane.showConfirmDialog(this, new Object[]{"Matière:", subject, "Note (0..20):", grade},
                "Ajouter / Mettre à jour note", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            try {
                double v = Double.parseDouble(grade.getText().replace(',', '.'));
                if (!isAdmin) { info("Mode lecture. Connecte-toi en admin."); return; }
                service.addGrade(id, subject.getText(), v);
                refreshGrades(); loadStudents();
            } catch (NumberFormatException nfe) { info("Note invalide."); }
            catch (Exception ex) { error("Add/Update grade", ex); }
        }
    }

    private void onDeleteGrade() {
        if (!isAdmin) { info("Mode lecture. Connecte-toi en admin."); return; }
        String id = selectedStudentId(); int row = gradesTable.getSelectedRow();
        if (id == null || row < 0) { info("Sélectionne une note."); return; }
        String subject = ((String) gradesTable.getValueAt(row, 0));
        int ok = JOptionPane.showConfirmDialog(this, "Supprimer la note de " + subject + " ?", "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            try { service.deleteGrade(id, subject); refreshGrades(); loadStudents(); }
            catch (Exception ex) { error("Delete grade", ex); }
        }
    }

    private void error(String ctx, Exception ex) { JOptionPane.showMessageDialog(this, "Erreur "+ctx+" : "+ex.getMessage()); }
    private void info(String msg) { JOptionPane.showMessageDialog(this, msg); }
}
