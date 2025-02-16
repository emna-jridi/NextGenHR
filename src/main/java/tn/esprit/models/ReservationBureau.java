package tn.esprit.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationBureau {

    private int idReservation;
    private int idEmploye;
    private int idBureau;
    private LocalDate dateReservation;
    private LocalTime dureeReservation;
    private String statutReservation;

    public ReservationBureau() {}

    public ReservationBureau(int idEmploye, int idBureau, LocalDate dateReservation, LocalTime dureeReservation, String statutReservation) {
        this.idEmploye = idEmploye;
        this.idBureau = idBureau;
        this.dateReservation = dateReservation;
        this.dureeReservation = dureeReservation;
        this.statutReservation = statutReservation;
    }

    public ReservationBureau(int idReservation, int idEmploye, int idBureau, LocalDate dateReservation, LocalTime dureeReservation, String statutReservation) {

    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(int idEmploye) {
        this.idEmploye = idEmploye;
    }

    public int getIdBureau() {
        return idBureau;
    }

    public void setIdBureau(int idBureau) {
        this.idBureau = idBureau;
    }

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = dateReservation;
    }

    public LocalTime getDureeReservation() {
        return dureeReservation;
    }

    public void setDureeReservation(LocalTime dureeReservation) {
        this.dureeReservation = dureeReservation;
    }

    public String getStatutReservation() {
        return statutReservation;
    }

    public void setStatutReservation(String statutReservation) {
        this.statutReservation = statutReservation;
    }

    @Override
    public String toString() {
        return "ReservationBureau{" +
                "idReservation=" + idReservation +
                ", idEmploye=" + idEmploye +
                ", idBureau=" + idBureau +
                ", dateReservation=" + dateReservation +
                ", dureeReservation=" + dureeReservation +
                ", statutReservation='" + statutReservation + '\'' +
                '}';
    }
}
