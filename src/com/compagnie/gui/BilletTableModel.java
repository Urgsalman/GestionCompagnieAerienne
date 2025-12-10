package com.compagnie.gui;

import com.compagnie.models.Billet;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.text.SimpleDateFormat;

public class BilletTableModel extends AbstractTableModel {

    private final List<Billet> billets;
    private final String[] columnNames = {"N° Billet", "Vol", "Siège", "Passager ID", "Classe", "Tarif", "Statut", "Date Emission"};
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public BilletTableModel(List<Billet> billets) {
        this.billets = billets;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return billets.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    // Permet de récupérer l'objet Billet à partir de l'index de ligne
    public Billet getBilletAt(int row) {
        return billets.get(row);
    }

    @Override
    public Object getValueAt(int row, int col) {
        Billet b = billets.get(row);

        switch (col) {
            case 0: return b.getNumBillet();
            case 1: return b.getNumVol();
            case 2: return b.getNumSiege();
            case 3: return b.getIdentificateurPassager();
            case 4: return b.getClasse();
            case 5: return String.format("%.2f €", b.getTarif());
            case 6: return b.getStatut();
            case 7: return dateFormat.format(b.getDateEmission());
            default: return null;
        }
    }

    // Pour que le JTable puisse centrer les IDs
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 3) return Integer.class;
        return super.getColumnClass(columnIndex);
    }
}