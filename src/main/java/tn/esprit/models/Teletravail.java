package tn.esprit.models;

import java.time.LocalDate;

public class Teletravail {
    private int idTeletravail;
    private int idEmploye;
    private LocalDate dateDemandeTT;
    private LocalDate dateDebutTT;
    private LocalDate dateFinTT;
    private String statutTT;
    private String raisonTT;

    // Constructeur
    public Teletravail(int idEmploye, LocalDate dateDemandeTT, LocalDate dateDebutTT, LocalDate dateFinTT, String statutTT, String raisonTT) {
        this.idEmploye = idEmploye;
        this.dateDemandeTT = dateDemandeTT;
        this.dateDebutTT = dateDebutTT;
        this.dateFinTT = dateFinTT;
        this.statutTT = statutTT;
        this.raisonTT = raisonTT;
    }

    public Teletravail(int idTeletravail, int idEmploye, LocalDate dateDemandeTT, LocalDate dateDebutTT, LocalDate dateFinTT, String statutTT, String raisonTT) {
        this(idEmploye, dateDemandeTT, dateDebutTT, dateFinTT, statutTT, raisonTT);
        this.idTeletravail = idTeletravail;
    }

    public Teletravail() {
    }

    // Getters et Setters
    public int getIdTeletravail() {
        return idTeletravail;
    }

    public void setIdTeletravail(int idTeletravail) {
        this.idTeletravail = idTeletravail;
    }

    public int getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(int idEmploye) {
        this.idEmploye = idEmploye;
    }

    public LocalDate getDateDemandeTT() {
        return dateDemandeTT;
    }

    public void setDateDemandeTT(LocalDate dateDemandeTT) {
        this.dateDemandeTT = dateDemandeTT;
    }

    public LocalDate getDateDebutTT() {
        return dateDebutTT;
    }

    public void setDateDebutTT(LocalDate dateDebutTT) {
        this.dateDebutTT = dateDebutTT;
    }

    public LocalDate getDateFinTT() {
        return dateFinTT;
    }

    public void setDateFinTT(LocalDate dateFinTT) {
        this.dateFinTT = dateFinTT;
    }

    public String getStatutTT() {
        return statutTT;
    }

    public void setStatutTT(String statutTT) {
        this.statutTT = statutTT;
    }

    public String getRaisonTT() {
        return raisonTT;
    }

    public void setRaisonTT(String raisonTT) {
        this.raisonTT = raisonTT;
    }

    // Ajoutez cette variable d'instance (en dehors des attributs existants)
    private String nomEmploye;

    // Ajoutez le getter et setter correspondant
    public String getNomEmploye() {
        return nomEmploye;
    }

    public void setNomEmploye(String nomEmploye) {
        this.nomEmploye = nomEmploye;
    }

    private String statsTT; // Pour stocker "Validé: X, Refusé: Y"

    public String getStatsTT() {
        return statsTT;
    }

    public void setStatsTT(String statsTT) {
        this.statsTT = statsTT;
    }

        @Override
    public String toString() {
        return "Teletravail{" +
                "idTeletravail=" + idTeletravail +
                "| idEmploye=" + idEmploye +
                (nomEmploye != null ? "| nomEmploye='" + nomEmploye + '\'' : "") +
                "| dateDemandeTT=" + dateDemandeTT +
                "| dateDebutTT=" + dateDebutTT +
                "| dateFinTT=" + dateFinTT +
                "| statutTT='" + statutTT + '\'' +
                "| raisonTT='" + raisonTT + '\'' +
                '}';
    }

}