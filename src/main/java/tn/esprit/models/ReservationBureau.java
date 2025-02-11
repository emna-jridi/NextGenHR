package tn.esprit.models;

import java.sql.Date;
import java.sql.Time;

public class ReservationBureau {
    private int idReservation;
    private int idEmploye;
    private int idBureau;
    private Date dateReservation;
    private Time dureeReservation;
    private String statutReservation;

    // Constructeurs
    public ReservationBureau() {}

    public ReservationBureau(int idEmploye, int idBureau, Date dateReservation, Time dureeReservation, String statutReservation) {
        this.idEmploye = idEmploye;
        this.idBureau = idBureau;
        this.dateReservation = dateReservation;
        this.dureeReservation = dureeReservation;
        this.statutReservation = statutReservation;
    }

    // Getters & Setters
    public int getIdReservation() { return idReservation; }
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }

    public int getIdEmploye() { return idEmploye; }
    public void setIdEmploye(int idEmploye) { this.idEmploye = idEmploye; }

    public int getIdBureau() { return idBureau; }
    public void setIdBureau(int idBureau) { this.idBureau = idBureau; }

    public Date getDateReservation() { return dateReservation; }
    public void setDateReservation(Date dateReservation) { this.dateReservation = dateReservation; }

    public Time getDureeReservation() { return dureeReservation; }
    public void setDureeReservation(Time dureeReservation) { this.dureeReservation = dureeReservation; }

    public String getStatutReservation() { return statutReservation; }
    public void setStatutReservation(String statutReservation) { this.statutReservation = statutReservation; }

    // Méthode toString pour afficher les informations de l'objet
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
