package tn.esprit.models;

import java.time.LocalDateTime;

public class Candidature {
    private int id;
    private LocalDateTime dateCandidature;
    private Statut statut;
    private String cvUrl;
    private String lettreMotivation;
    private String Nom;
    private String prenom;
    private String email;
    private String telephone;

    private Offreemploi offreemploi;

    private User candidat;

    public User getCandidat() {
        return candidat;
    }

    public void setCandidat(User candidat) {
        this.candidat = candidat;
    }

    // private Utilisateur candidat; lien maa l user
    public Candidature() {};

    public Candidature(LocalDateTime dateCandidature, Statut statut, String cvUrl, String lettreMotivation, Offreemploi offreemploi, String nom, String prenom, String email, String telephone, User candidat) {
        this.dateCandidature = dateCandidature;
        this.statut = statut;
        this.cvUrl = cvUrl;
        this.lettreMotivation = lettreMotivation;
       this.offreemploi =offreemploi;
        this.Nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.candidat = candidat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateCandidature() {
        return dateCandidature;
    }

    public void setDateCandidature(LocalDateTime dateCandidature) {
        this.dateCandidature = dateCandidature;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public String getLettreMotivation() {
        return lettreMotivation;
    }

    public void setLettreMotivation(String lettreMotivation) {

        this.lettreMotivation = lettreMotivation;
    }



    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    public Offreemploi getOffreemploi() {
        return offreemploi;
    }

    public void setOffreemploi(Offreemploi offreemploi) {
        this.offreemploi = offreemploi;
    }

    @Override
    public String toString() {
        return "Candidatures {" +
                " Nom="+ Nom +
                ",prenom=" + prenom +
                ", email=" + email +
                ", telephone=" + telephone +
                ", cvUrl='" + cvUrl + '\'' +
                ", lettreMotivation='" + lettreMotivation + '\'' +
                " dateCandidature= " + dateCandidature +
                ", offre=" + offreemploi +
                ", statut='" + statut + '\'' +




                '}';
    }
}
