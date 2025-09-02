package com.esgi.ui.table;

import com.esgi.model.Student;
import javax.swing.table.AbstractTableModel;
import java.util.*;

public class StudentTableModel extends AbstractTableModel {
    private final String[] cols = {"ID", "Prénom", "Nom", "Matières (#)", "Moyenne"};
    private final List<Student> data = new ArrayList<>();

    @Override public int getRowCount() { return data.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int c) { return cols[c]; }

    @Override public Object getValueAt(int r, int c) {
        Student s = data.get(r);
        return switch (c) {
            case 0 -> s.getId();
            case 1 -> s.getFirstName();
            case 2 -> s.getLastName();
            case 3 -> s.getGrades().size();
            case 4 -> {
                if (s.getGrades().isEmpty()) yield "";
                double sum = 0; int n = 0;
                for (double v : s.getGrades().values()) { sum += v; n++; }
                yield String.format("%.2f", sum / n);
            }
            default -> "";
        };
    }

    public void setStudents(List<Student> list) { data.clear(); data.addAll(list); fireTableDataChanged(); }
    public Student getAt(int row) { return data.get(row); }
}
