package com.esgi.ui;

import com.esgi.model.Student;
import com.esgi.service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

public class GradesDialog extends JDialog {
    private final StudentService service;
    private final String studentId;
    private final boolean isAdmin;
    private final Runnable onClose;

    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Matière", "Note"}, 0);
    private final JTable table = new JTable(model);

    public GradesDialog(StudentService service, String studentId, boolean isAdmin, Runnable onClose) {
        this.service = service;
        this.studentId = studentId;
        this.isAdmin = isAdmin;
        this.onClose = onClose;

        setTitle("Notes de l’étudiant");
        setSize(480, 360);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout(8,8));

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);

        refresh();
    }

    private JPanel buildButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        if (isAdmin) {
            JButton add = new JButton("Ajouter/Mettre à jour");
            JButton del = new JButton("Supprimer matière");
            add.addActionListener(e -> onAddOrUpdate());
            del.addActionListener(e -> onDelete());
            p.add(add);
            p.add(del);
        }
        JButton close = new JButton("Fermer");
        close.addActionListener(e -> { dispose(); if (onClose != null) onClose.run(); });
        p.add(close);
        return p;
    }

    private void refresh() {
        try {
            Student s = findStudent(studentId);
            model.setRowCount(0);
            for (Map.Entry<String, Double> e : s.getGrades().entrySet()) {
                model.addRow(new Object[]{e.getKey(), e.getValue()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
        }
    }

    private Student findStudent(String id) throws IOException {
        // pas de service.get(id) ? on filtre la liste
        return service.findAll().stream()
                .filter(st -> st.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Étudiant introuvable"));
    }

    private void onAddOrUpdate() {
        JTextField subject = new JTextField();
        JTextField grade = new JTextField();
        Object[] msg = {"Matière:", subject, "Note (0..20):", grade};
        int ok = JOptionPane.showConfirmDialog(this, msg, "Ajouter/Mettre à jour note", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            try {
                double v = Double.parseDouble(grade.getText().replace(',', '.'));
                service.addGrade(studentId, subject.getText(), v);
                refresh();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Note invalide.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
            }
        }
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionne une matière.");
            return;
        }
        String subject = (String) model.getValueAt(row, 0);
        try {
            // pas de méthode dédiée ? on retire du map puis save via service (delete+create si besoin)
            Student s = findStudent(studentId);
            s.getGrades().remove(subject);
            String first = s.getFirstName();
            String last  = s.getLastName();
            var rest = s.getGrades();

            service.delete(s.getId());
            Student ns = service.create(first, last);
            for (var e : rest.entrySet()) {
                service.addGrade(ns.getId(), e.getKey(), e.getValue());
            }
            // NB: l'id change; ,a ajoute "update" dans le service si besoin==>hadi on la laisse apres

            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
        }
    }
}
