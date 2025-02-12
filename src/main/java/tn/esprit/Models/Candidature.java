package tn.esprit.Models;

import java.time.LocalDateTime;

public class Candidature {
    private int id;
    private LocalDateTime dateCandidature;
    private String statut;
    private String cvUrl;
    private String lettreMotivation;






    private Offreemploi offre;






   // private Utilisateur candidat; lien maa l user
    public Candidature() {};

    public Candidature(LocalDateTime dateCandidature, String statut, String cvUrl, String lettreMotivation, Offreemploi offre) {
        this.dateCandidature = dateCandidature;
        this.statut = statut;
        this.cvUrl = cvUrl;
        this.lettreMotivation = lettreMotivation;
        this.offre = offre;
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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
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

    public Offreemploi getOffre() {
        return offre;
    }

    public void setOffre(Offreemploi offre) {
        this.offre = offre;
    }

    @Override
    public String toString() {
        return "Candidature{" +
                "dateCandidature=" + dateCandidature +
                ", statut='" + statut + '\'' +
                ", cvUrl='" + cvUrl + '\'' +
                ", lettreMotivation='" + lettreMotivation + '\'' +
                ", offre=" + offre +
                '}';
    }
}
