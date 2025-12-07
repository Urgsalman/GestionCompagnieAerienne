package com.compagnie.models;

import java.util.Date;

public class Passager {

    private Integer identificateurPassager;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private String numPasseport;
    private String nationalite;
    private TypePassager typePassager;
    private Genre genre;
    private String email;
    private String telephone;

    public Passager() {}

    public Passager(String nom, String prenom, Date dateNaissance, String numPasseport,
                    String nationalite, TypePassager typePassager, Genre genre,
                    String email, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.numPasseport = numPasseport;
        this.nationalite = nationalite;
        this.typePassager = typePassager;
        this.genre = genre;
        this.email = email;
        this.telephone = telephone;
    }

    public Passager(Integer identificateurPassager, String nom, String prenom, Date dateNaissance,
                    String numPasseport, String nationalite, TypePassager typePassager,
                    Genre genre, String email, String telephone) {
        this(nom, prenom, dateNaissance, numPasseport, nationalite, typePassager, genre, email, telephone);
        this.identificateurPassager = identificateurPassager;
    }

    public Integer getIdentificateurPassager() { return identificateurPassager; }
    public void setIdentificateurPassager(Integer identificateurPassager) { this.identificateurPassager = identificateurPassager; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public Date getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(Date dateNaissance) { this.dateNaissance = dateNaissance; }
    public String getNumPasseport() { return numPasseport; }
    public void setNumPasseport(String numPasseport) { this.numPasseport = numPasseport; }
    public String getNationalite() { return nationalite; }
    public void setNationalite(String nationalite) { this.nationalite = nationalite; }
    public TypePassager getTypePassager() { return typePassager; }
    public void setTypePassager(TypePassager typePassager) { this.typePassager = typePassager; }
    public Genre getGenre() { return genre; }
    public void setGenre(Genre genre) { this.genre = genre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}