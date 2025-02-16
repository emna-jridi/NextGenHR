package entities;

import java.time.LocalDate;
import java.util.Date;



public class conge { // Class name starts with uppercase 'C'

    private int id;
    private String Type_conge;
    private LocalDate Date_debut;
    private LocalDate Date_fin;
    private String Status;

    // Default constructor
    public conge() {
    }

    // Constructor with all attributes
    public conge(int id, String Type_conge, LocalDate Date_debut, LocalDate Date_fin, String Status) {
        this.id = id;
        this.Type_conge = Type_conge;
        this.Date_debut = Date_debut;
        this.Date_fin = Date_fin;
        this.Status = Status;
    }

    // Constructor without id
    public conge(String Type_conge, LocalDate Date_debut, LocalDate Date_fin, String Status) {
        this.Type_conge = Type_conge;
        this.Date_debut = Date_debut;
        this.Date_fin = Date_fin;
        this.Status = Status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType_conge() {
        return Type_conge;
    }

    public void setType_conge(String Type_conge) {
        this.Type_conge = Type_conge;
    }

    public LocalDate getDate_debut() {
        return Date_debut;
    }

    public void setDate_debut(LocalDate Date_debut) {
        this.Date_debut = Date_debut;
    }

    public LocalDate getDate_fin() {
        return Date_fin;
    }

    public void setDate_fin(LocalDate Date_fin) {
        this.Date_fin = Date_fin;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    // toString method
    @Override
    public String toString() {
        return "conge{" +
                "id=" + id +
                ", Type_conge='" + Type_conge + '\'' +
                ", Date_debut=" + Date_debut +
                ", Date_fin=" + Date_fin +
                ", Status='" + Status + '\'' +
                "}\n";
    }
}