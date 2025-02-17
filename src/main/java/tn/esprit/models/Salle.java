package tn.esprit.models;

import java.util.Objects;

public class Salle {
    private int idSalle;
    private String refSalle;
    private int capacite;
    private String typeSalle; // "Réunion" ou "Conférence"
    private String disponibilite;

    public Salle(int idSalle, String refSalle, int capacite, String typeSalle, String disponibilite) {
        this.idSalle = idSalle;
        this.refSalle = Objects.requireNonNull(refSalle, "RefSalle cannot be null");
        this.capacite = capacite;
        this.typeSalle = Objects.requireNonNull(typeSalle, "TypeSalle cannot be null");
        this.disponibilite = Objects.requireNonNull(disponibilite, "Disponibilite cannot be null");
    }

    public Salle(String refSalle, int capacite, String typeSalle, String disponibilite) {
        this.refSalle = Objects.requireNonNull(refSalle, "RefSalle cannot be null");
        this.capacite = capacite;
        this.typeSalle = Objects.requireNonNull(typeSalle, "TypeSalle cannot be null");
        this.disponibilite = Objects.requireNonNull(disponibilite, "Disponibilite cannot be null");
    }

    public int getIdSalle() {
        return idSalle;
    }

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
    }

    public String getRefSalle() {
        return refSalle;
    }

    public void setRefSalle(String refSalle) {
        this.refSalle = Objects.requireNonNull(refSalle, "RefSalle cannot be null");
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getTypeSalle() {
        return typeSalle;
    }

    public void setTypeSalle(String typeSalle) {
        this.typeSalle = Objects.requireNonNull(typeSalle, "TypeSalle cannot be null");
    }

    public String getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(String disponibilite) {
        this.disponibilite = Objects.requireNonNull(disponibilite, "Disponibilite cannot be null");
    }

    @Override
    public String toString() {
        return "Salle{" +
                "idSalle=" + idSalle +
                ", refSalle='" + refSalle + '\'' +
                ", capacite=" + capacite +
                ", typeSalle='" + typeSalle + '\'' +
                ", disponibilite='" + disponibilite + '\'' +
                '}';
    }
}