package tn.esprit.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class ReservationBureau {
    private int idReservation;
    private int idEmploye;
    private int idBureau;
    private LocalDate dateReservation;
    private LocalTime dureeReservation;
    private String statutReservation;

    // Constructeurs
    public ReservationBureau() {}

    public ReservationBureau(int idEmploye, int idBureau, LocalDate dateReservation, LocalTime dureeReservation, String statutReservation) {
        this.idEmploye = idEmploye;
        this.idBureau = idBureau;
        this.dateReservation = Objects.requireNonNull(dateReservation, "DateReservation cannot be null");
        this.dureeReservation = Objects.requireNonNull(dureeReservation, "DureeReservation cannot be null");
        this.statutReservation = Objects.requireNonNull(statutReservation, "StatutReservation cannot be null");
    }

    // Getters & Setters
    public int getIdReservation() { return idReservation; }
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }

    public int getIdEmploye() { return idEmploye; }
    public void setIdEmploye(int idEmploye) { this.idEmploye = idEmploye; }

    public int getIdBureau() { return idBureau; }
    public void setIdBureau(int idBureau) { this.idBureau = idBureau; }

    public LocalDate getDateReservation() { return dateReservation; }
    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = Objects.requireNonNull(dateReservation, "DateReservation cannot be null");
    }

    public LocalTime getDureeReservation() { return dureeReservation; }
    public void setDureeReservation(LocalTime dureeReservation) {
        this.dureeReservation = Objects.requireNonNull(dureeReservation, "DureeReservation cannot be null");
    }

    public String getStatutReservation() { return statutReservation; }
    public void setStatutReservation(String statutReservation) {
        this.statutReservation = Objects.requireNonNull(statutReservation, "StatutReservation cannot be null");
    }

    // Override equals and hashCode for better object comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationBureau that = (ReservationBureau) o;
        return idReservation == that.idReservation &&
                idEmploye == that.idEmploye &&
                idBureau == that.idBureau &&
                Objects.equals(dateReservation, that.dateReservation) &&
                Objects.equals(dureeReservation, that.dureeReservation) &&
                Objects.equals(statutReservation, that.statutReservation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReservation, idEmploye, idBureau, dateReservation, dureeReservation, statutReservation);
    }

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

    // Builder Pattern for easier object creation
    public static class Builder {
        private int idReservation;
        private int idEmploye;
        private int idBureau;
        private LocalDate dateReservation;
        private LocalTime dureeReservation;
        private String statutReservation;

        public Builder setIdReservation(int idReservation) {
            this.idReservation = idReservation;
            return this;
        }

        public Builder setIdEmploye(int idEmploye) {
            this.idEmploye = idEmploye;
            return this;
        }

        public Builder setIdBureau(int idBureau) {
            this.idBureau = idBureau;
            return this;
        }

        public Builder setDateReservation(LocalDate dateReservation) {
            this.dateReservation = Objects.requireNonNull(dateReservation, "DateReservation cannot be null");
            return this;
        }

        public Builder setDureeReservation(LocalTime dureeReservation) {
            this.dureeReservation = Objects.requireNonNull(dureeReservation, "DureeReservation cannot be null");
            return this;
        }

        public Builder setStatutReservation(String statutReservation) {
            this.statutReservation = Objects.requireNonNull(statutReservation, "StatutReservation cannot be null");
            return this;
        }

        public ReservationBureau build() {
            return new ReservationBureau(idEmploye, idBureau, dateReservation, dureeReservation, statutReservation);
        }
    }
}
