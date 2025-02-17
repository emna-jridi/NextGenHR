package tn.esprit.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationSalle {

    private int idReservation;
    private int idEmploye;
    private int idSalle;
    private LocalDate dateReservation;
    private LocalTime dureeReservation;
    private String statutReservation;

    public ReservationSalle() {}

    public ReservationSalle(int idEmploye, int idSalle, LocalDate dateReservation, LocalTime dureeReservation, String statutReservation) {
        this.idEmploye = idEmploye;
        this.idSalle = idSalle;
        this.dateReservation = dateReservation;
        this.dureeReservation = dureeReservation;
        this.statutReservation = statutReservation;
    }

    public ReservationSalle(int idReservation, int idEmploye, int idSalle, LocalDate dateReservation, LocalTime dureeReservation, String statutReservation) {
        this.idReservation = idReservation;
        this.idEmploye = idEmploye;
        this.idSalle = idSalle;
        this.dateReservation = dateReservation;
        this.dureeReservation = dureeReservation;
        this.statutReservation = statutReservation;
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

    public int getIdSalle() {
        return idSalle;
    }

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
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
        return "ReservationSalle{" +
                "idReservation=" + idReservation +
                ", idEmploye=" + idEmploye +
                ", idSalle=" + idSalle +
                ", dateReservation=" + dateReservation +
                ", dureeReservation=" + dureeReservation +
                ", statutReservation='" + statutReservation + '\'' +
                '}';
    }
}