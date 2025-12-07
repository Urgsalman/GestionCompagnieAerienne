package com.compagnie.models;

import java.util.Date;

public class Billet {

    private String numBillet;
    private Integer identificateurPassager;
    private ClasseBillet classe;
    private Double tarif;
    private StatutBillet statut;
    private Date dateEmission;
    private String numVol;
    private String numSiege;

    public Billet() {}

    public Billet(String numBillet, Integer identificateurPassager, ClasseBillet classe,
                  Double tarif, StatutBillet statut, Date dateEmission,
                  String numVol, String numSiege) {
        this.numBillet = numBillet;
        this.identificateurPassager = identificateurPassager;
        this.classe = classe;
        this.tarif = tarif;
        this.statut = statut;
        this.dateEmission = dateEmission;
        this.numVol = numVol;
        this.numSiege = numSiege;
    }

    public String getNumBillet() { return numBillet; }
    public void setNumBillet(String numBillet) { this.numBillet = numBillet; }
    public Integer getIdentificateurPassager() { return identificateurPassager; }
    public void setIdentificateurPassager(Integer identificateurPassager) { this.identificateurPassager = identificateurPassager; }
    public ClasseBillet getClasse() { return classe; }
    public void setClasse(ClasseBillet classe) { this.classe = classe; }
    public Double getTarif() { return tarif; }
    public void setTarif(Double tarif) { this.tarif = tarif; }
    public StatutBillet getStatut() { return statut; }
    public void setStatut(StatutBillet statut) { this.statut = statut; }
    public Date getDateEmission() { return dateEmission; }
    public void setDateEmission(Date dateEmission) { this.dateEmission = dateEmission; }
    public String getNumVol() { return numVol; }
    public void setNumVol(String numVol) { this.numVol = numVol; }
    public String getNumSiege() { return numSiege; }
    public void setNumSiege(String numSiege) { this.numSiege = numSiege; }
}