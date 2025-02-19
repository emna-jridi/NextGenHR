package tn.esprit.models;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class Offreemploi {

    private int id, candidaturesrecues;
    private String titre, description, experiencerequise, niveauEtudes, competences, typecontrat, localisation, niveaulangues;
    private LocalDateTime dateCreation, dateExpiration;
    private String statut;



    private List<Candidature> candidatures;







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




    public List<Candidature> getCandidatures() {

        return candidatures;
    }

    public void setCandidatures(List<Candidature> candidatures) {

        this.candidatures = candidatures;
    }


    public Offreemploi(){};



    public Offreemploi(int candidaturesrecues, String titre, String description, String experiencerequise, String niveauEtudes, String competences, String typecontrat, String localisation, String niveaulangues, LocalDateTime dateCreation, LocalDateTime dateExpiration, String statut,List<Candidature> candidatures) {
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
        this.candidatures = candidatures;

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
                ", candidatures=" + candidatures +

                '}';
    }
  /* public boolean isValid() {
        return dateExpiration.isAfter(LocalDateTime.now());
    }
    public long getDaysRemaining() {
        // Vérifie si l'offre est valide
        if (isValid()) {
            // Calcule la différence entre la date actuelle et la date d'expiration en jours
            return ChronoUnit.DAYS.between(LocalDateTime.now(), dateExpiration);
        }
        // Retourne 0 si l'offre est expirée
        return 0;
    }*/




}
