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

    // Constructeurs
    public Teletravail() {}

    public Teletravail(int idEmploye, LocalDate dateDemandeTT, LocalDate dateDebutTT, LocalDate dateFinTT, String statutTT, String raisonTT) {
        this.idEmploye = idEmploye;
        this.dateDemandeTT = dateDemandeTT;
        this.dateDebutTT = dateDebutTT;
        this.dateFinTT = dateFinTT;
        this.statutTT = statutTT;
        this.raisonTT = raisonTT;
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

    @Override
    public String toString() {
        return "Teletravail{" +
                "idTeletravail=" + idTeletravail +
                ", idEmploye=" + idEmploye +
                ", dateDemandeTT=" + dateDemandeTT +
                ", dateDebutTT=" + dateDebutTT +
                ", dateFinTT=" + dateFinTT +
                ", statutTT='" + statutTT + '\'' +
                ", raisonTT='" + raisonTT + '\'' +
                '}';
    }

}