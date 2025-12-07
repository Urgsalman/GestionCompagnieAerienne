package com.compagnie.gui;

import com.compagnie.models.Passager;

public class PassagerComboItem {
    private final Integer id;
    private final String displayValue;

    public PassagerComboItem(Passager p) {
        this.id = p.getIdentificateurPassager();
        this.displayValue = p.getNom() + " " + p.getPrenom() + " (ID: " + p.getIdentificateurPassager() + ")";
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return displayValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PassagerComboItem that = (PassagerComboItem) obj;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}