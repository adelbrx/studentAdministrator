package com.esgi.ui.table;

import javax.swing.table.AbstractTableModel;
import java.util.*;

public class GradesTableModel extends AbstractTableModel {
    private final String[] cols = {"Matière", "Note"};
    private final List<Map.Entry<String, Double>> data = new ArrayList<>();

    @Override public int getRowCount() { return data.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int c) { return cols[c]; }
    @Override public Object getValueAt(int r, int c) { var e = data.get(r); return c == 0 ? e.getKey() : e.getValue(); }

    public void setGrades(Map<String, Double> grades) {
        data.clear();
        var list = new ArrayList<>(grades.entrySet());
        list.sort(Map.Entry.comparingByKey());
        data.addAll(list);
        fireTableDataChanged();
    }
    public String getSubjectAt(int row) { return data.get(row).getKey(); }
}
