package tn.esprit.models;

import java.sql.Date;

public class Formation {
private int idFormation ;
private String nomFormation , themeFormation ,description ,lien_formation;
private Date dateFormation ;
    private String niveauDifficulte;
    private String statut;
    // private List<Employee> employees;
    public Formation() {}
    public Formation( int idFormation,String nomFormation, String themeFormation,String description,String lien_formation, Date dateFormation , String statut , String niveauDifficulte) {
        this.nomFormation = nomFormation;
        this.themeFormation = themeFormation;
        this.dateFormation = dateFormation;
        this.idFormation = idFormation;
        this.description = description;
        this.lien_formation = lien_formation;
        this.statut=statut;
        this.niveauDifficulte=niveauDifficulte;
    }

    public Formation(String nomFormation, String themeFormation, String description, String lien_formation, Date dateFormation , String statut , String niveauDifficulte) {
        this.nomFormation = nomFormation;
        this.themeFormation = themeFormation;
        this.dateFormation = dateFormation;
        this.description = description;
        this.lien_formation = lien_formation;
        this.statut=statut;
        this.niveauDifficulte=niveauDifficulte;


    }

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

    public Date getDateFormation() {
        return dateFormation;
    }

    public void setDateFormation(Date dateFormation) {
        this.dateFormation = dateFormation;
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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "idFormation=" + idFormation +
                ", nomFormation='" + nomFormation + '\'' +
                ", themeFormation='" + themeFormation + '\'' +
                ", description='" + description + '\'' +
                ", lien_formation='" + lien_formation + '\'' +
                ", dateFormation=" + dateFormation +
                ", niveauDifficulte=" + niveauDifficulte +
                ", statut=" + statut +
                '}' + "\n";
    }
}
