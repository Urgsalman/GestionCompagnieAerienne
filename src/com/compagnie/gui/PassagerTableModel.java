package com.compagnie.gui;

import com.compagnie.models.Passager;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.text.SimpleDateFormat;

public class PassagerTableModel extends AbstractTableModel {

    private final List<Passager> passagers;
    private final String[] columnNames = {"ID", "Nom", "Prénom", "Passeport", "Date Naissance", "Type"};
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public PassagerTableModel(List<Passager> passagers) {
        this.passagers = passagers;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return passagers.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Passager getPassagerAt(int row) {
        return passagers.get(row);
    }

    @Override
    public Object getValueAt(int row, int col) {
        Passager p = passagers.get(row);

        switch (col) {
            case 0: return p.getIdentificateurPassager();
            case 1: return p.getNom();
            case 2: return p.getPrenom();
            case 3: return p.getNumPasseport();
            case 4: return dateFormat.format(p.getDateNaissance());
            case 5: return p.getTypePassager();
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) return Integer.class;
        return super.getColumnClass(columnIndex);
    }
}