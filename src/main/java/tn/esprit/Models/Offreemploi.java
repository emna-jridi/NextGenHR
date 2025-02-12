package tn.esprit.Models;

import java.time.LocalDateTime;

public class Offreemploi {

    private int id, candidaturesrecues;
    private String titre, description, experiencerequise, niveauEtudes, competences, typecontrat, localisation, niveaulangues;
    private LocalDateTime dateCreation, dateExpiration;
    private String statut;



   // private List<Candidature> candidatures;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCandidaturesrecues() {
        return candidaturesrecues;
    }

    public void setCandidaturesrecues(int candidaturesrecues) {
        this.candidaturesrecues = candidaturesrecues;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExperiencerequise() {
        return experiencerequise;
    }

    public void setExperiencerequise(String experiencerequise) {
        this.experiencerequise = experiencerequise;
    }

    public String getNiveauEtudes() {
        return niveauEtudes;
    }

    public void setNiveauEtudes(String niveauEtudes) {
        this.niveauEtudes = niveauEtudes;
    }

    public String getCompetences() {
        return competences;
    }

    public void setCompetences(String competences) {
        this.competences = competences;
    }

    public String getTypecontrat() {
        return typecontrat;
    }

    public void setTypecontrat(String typecontrat) {
        this.typecontrat = typecontrat;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getNiveaulangues() {
        return niveaulangues;
    }

    public void setNiveaulangues(String niveaulangues) {
        this.niveaulangues = niveaulangues;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
    public Offreemploi(){};



    public Offreemploi(int candidaturesrecues, String titre, String description, String experiencerequise, String niveauEtudes, String competences, String typecontrat, String localisation, String niveaulangues, LocalDateTime dateCreation, LocalDateTime dateExpiration, String statut) {
        this.candidaturesrecues = candidaturesrecues;
        this.titre = titre;
        this.description = description;
        this.experiencerequise = experiencerequise;
        this.niveauEtudes = niveauEtudes;
        this.competences = competences;
        this.typecontrat = typecontrat;
        this.localisation = localisation;
        this.niveaulangues = niveaulangues;
        this.dateCreation = dateCreation;
        this.dateExpiration = dateExpiration;
        this.statut= statut;
    }
    @Override
    public String toString() {
        return "Offreemploi{" +
                "candidaturesrecues=" + candidaturesrecues +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", experiencerequise='" + experiencerequise + '\'' +
                ", niveauEtudes='" + niveauEtudes + '\'' +
                ", competences='" + competences + '\'' +
                ", typecontrat='" + typecontrat + '\'' +
                ", localisation='" + localisation + '\'' +
                ", niveaulangues='" + niveaulangues + '\'' +
                ", dateCreation=" + dateCreation +
                ", dateExpiration=" + dateExpiration +
                ", statut=" + statut +
                '}';
    }



}
