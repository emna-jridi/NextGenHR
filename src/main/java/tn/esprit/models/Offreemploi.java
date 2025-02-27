package tn.esprit.models;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class Offreemploi {

    private int id;
    private String titre, description, competences, localisation;
    private LocalDateTime dateCreation, dateExpiration;

    private experience experiencerequise;
    private Niveauetudes niveauEtudes;
    private Niveaulangues niveaulangues;
    private TypeContrat typecontrat;



    private List<Candidature> candidatures;







    public int getId() {
        return id;
    }



    public void setId(int id) {
        this.id = id;
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

    public experience getExperiencerequise() {
        return experiencerequise;
    }

    public void setExperiencerequise(experience experiencerequise) {
        this.experiencerequise = experiencerequise;
    }

    public Niveauetudes getNiveauEtudes() {

        return niveauEtudes;
    }

    public void setNiveauEtudes(Niveauetudes niveauEtudes) {
        this.niveauEtudes = niveauEtudes;
    }

    public String getCompetences() {
        return competences;
    }

    public void setCompetences(String competences) {
        this.competences = competences;
    }

    public TypeContrat getTypecontrat() {
        return typecontrat;
    }

    public void setTypecontrat(TypeContrat typecontrat) {
        this.typecontrat = typecontrat;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public Niveaulangues getNiveaulangues() {
        return niveaulangues;
    }

    public void setNiveaulangues(Niveaulangues niveaulangues) {
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






    public List<Candidature> getCandidatures() {

        return candidatures;
    }

    public void setCandidatures(List<Candidature> candidatures) {

        this.candidatures = candidatures;
    }


    public Offreemploi(){};



    public Offreemploi(String titre, String description, experience experiencerequise, Niveauetudes niveauEtudes, String competences, TypeContrat typecontrat, String localisation, Niveaulangues niveaulangues, LocalDateTime dateCreation, LocalDateTime dateExpiration,List<Candidature> candidatures) {

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



    }
    @Override
    public String toString() {
        return "Offreemploi {" +

                " titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", competences='" + competences + '\'' +
                ", experiencerequise='" + experiencerequise + '\'' +
                ", niveauEtudes='" + niveauEtudes + '\'' +
                ", niveaulangues='" + niveaulangues + '\'' +
                ", typecontrat='" + typecontrat + '\'' +
                ", localisation='" + localisation + '\'' +
                ", dateCreation=" + dateCreation +
                ", dateExpiration=" + dateExpiration +
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
