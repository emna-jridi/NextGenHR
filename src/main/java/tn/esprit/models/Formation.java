package tn.esprit.models;

import java.sql.Date;

public class Formation {
    private int idFormation;
    private String nomFormation;
    private String themeFormation;
    private String description;
    private String lien_formation;
    private String niveauDifficulte;
    private String niveau;
    private int duree;
    private String imageUrl;
    private Date dateFormation;


    public Formation() {
    }


    public Formation(int idFormation, String nomFormation, String themeFormation, String description,
                     String lien_formation, String niveauDifficulte, String niveau, int duree,
                     String imageUrl,  Date dateFormation) {
        this.idFormation = idFormation;
        this.nomFormation = nomFormation;
        this.themeFormation = themeFormation;
        this.description = description;
        this.lien_formation = lien_formation;
        this.niveauDifficulte = niveauDifficulte;
        this.niveau = niveau;
        this.duree = duree;
        this.imageUrl = imageUrl;

        this.dateFormation = dateFormation;
    }

    public Formation(String nomFormation, String themeFormation, String description,
                     String lien_formation, String niveauDifficulte, String niveau, int duree,
                     String imageUrl, Date dateFormation) {
        this.nomFormation = nomFormation;
        this.themeFormation = themeFormation;
        this.description = description;
        this.lien_formation = lien_formation;
        this.niveauDifficulte = niveauDifficulte;
        this.niveau = niveau;
        this.duree = duree;
        this.imageUrl = imageUrl;

        this.dateFormation = dateFormation;
    }

    // Getters et Setters
    public int getIdFormation() {
        return idFormation;
    }

    public void setIdFormation(int idFormation) {
        this.idFormation = idFormation;
    }

    public String getNomFormation() {
        return nomFormation;
    }

    public void setNomFormation(String nomFormation) {
        this.nomFormation = nomFormation;
    }

    public String getThemeFormation() {
        return themeFormation;
    }

    public void setThemeFormation(String themeFormation) {
        this.themeFormation = themeFormation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLien_formation() {
        return lien_formation;
    }

    public void setLien_formation(String lien_formation) {
        this.lien_formation = lien_formation;
    }

    public String getNiveauDifficulte() {
        return niveauDifficulte;
    }

    public void setNiveauDifficulte(String niveauDifficulte) {
        this.niveauDifficulte = niveauDifficulte;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public Date getDateFormation() {
        return dateFormation;
    }

    public void setDateFormation(Date dateFormation) {
        this.dateFormation = dateFormation;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "idFormation=" + idFormation +
                ", nomFormation='" + nomFormation + '\'' +
                ", themeFormation='" + themeFormation + '\'' +
                ", description='" + description + '\'' +
                ", lien_formation='" + lien_formation + '\'' +
                ", niveauDifficulte='" + niveauDifficulte + '\'' +
                ", niveau='" + niveau + '\'' +
                ", duree=" + duree +
                ", imageUrl='" + imageUrl + '\'' +
                ", dateFormation=" + dateFormation +
                '}';
    }
}