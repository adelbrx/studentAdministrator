package com.esgi.ui.render;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ScoreColorRenderer extends DefaultTableCellRenderer {
    private static final Color RED_BG     = new Color(255, 235, 238); // <10
    private static final Color ORANGE_BG  = new Color(255, 243, 224); // 10..12
    private static final Color GREEN_BG   = new Color(232, 245, 233); // >12

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setOpaque(true);

        // Par défaut = couleurs du tableau (utile si non-sélectionné / valeur non num.)
        if (!isSelected) {
            c.setBackground(table.getBackground());
            c.setForeground(table.getForeground());
        }

        Double v = parseDouble(value);
        if (v != null && !isSelected) {
            if (v < 10.0) {
                c.setBackground(RED_BG);
            } else if (v < 12.0) {
                c.setBackground(ORANGE_BG);
            } else {
                c.setBackground(GREEN_BG);
            }
            c.setForeground(Color.DARK_GRAY);
        }
        return c;
    }

    private Double parseDouble(Object value) {
        if (value == null) return null;
        try {
            if (value instanceof Number n) return n.doubleValue();
            String s = value.toString().trim().replace(',', '.');
            if (s.isEmpty()) return null;
            return Double.parseDouble(s);
        } catch (Exception ignored) {
            return null;
        }
    }
}
