package tn.esprit.models;

import java.util.Objects;

public class Bureau {
    private int idBureau;
    private String refBureau;
    private int capacite;
    private String disponibilite;

    // Constructeurs
    public Bureau(int idBureau, String refBureau, int capacite, String disponibilité) {}

    public Bureau(String refBureau, int capacite, String disponibilite) {
        this.refBureau = Objects.requireNonNull(refBureau, "RefBureau cannot be null");
        this.capacite = capacite;
        this.disponibilite = Objects.requireNonNull(disponibilite, "Disponibilite cannot be null");
    }

    // Getters & Setters
    public int getIdBureau() { return idBureau; }
    public void setIdBureau(int idBureau) { this.idBureau = idBureau; }

    public String getRefBureau() { return refBureau; }
    public void setRefBureau(String refBureau) {
        this.refBureau = Objects.requireNonNull(refBureau, "RefBureau cannot be null");
    }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    public String getDisponibilite() { return disponibilite; }
    public void setDisponibilite(String disponibilite) {
        this.disponibilite = Objects.requireNonNull(disponibilite, "Disponibilite cannot be null");
    }

    // toString
    @Override
    public String toString() {
        return "Bureau{" +
                "idBureau=" + idBureau +
                ", refBureau='" + refBureau + '\'' +
                ", capacite=" + capacite +
                ", disponibilite='" + disponibilite + '\'' +
                '}';
    }
}
