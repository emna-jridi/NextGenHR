package tn.esprit.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Contrat {

    private int idContrat;
    private LocalDate dateDebutContrat;
    private LocalDate dateFinContrat;
    private String statusContrat;
    private int montantContrat;
    private String nomClient;
    private String emailClient;
    private List<Service> services;
    private String telephoneClient;

    // Constructeurs

    public Contrat() {
        this.services = new ArrayList<>();
    }

    public Contrat(int idContrat, LocalDate dateDebutContrat, LocalDate dateFinContrat,
                   String statusContrat, int montantContrat, String nomClient, String emailClient, String telephoneClient) {
        this.idContrat = idContrat;
        this.dateDebutContrat = dateDebutContrat;
        this.dateFinContrat = dateFinContrat;
        this.statusContrat = statusContrat;
        this.montantContrat = montantContrat;
        this.nomClient = nomClient;
        this.emailClient = emailClient;
        this.telephoneClient = telephoneClient;
        this.services = new ArrayList<>();
    }

    public Contrat( LocalDate dateDebutContrat, LocalDate dateFinContrat,
                   String statusContrat, int montantContrat, String nomClient, String emailClient, String telephoneClient) {
        this.dateDebutContrat = dateDebutContrat;
        this.dateFinContrat = dateFinContrat;
        this.statusContrat = statusContrat;
        this.montantContrat = montantContrat;
        this.nomClient = nomClient;
        this.emailClient = emailClient;
        this.telephoneClient = telephoneClient;
        this.services = new ArrayList<>();
    }

    // Getters et Setters
    public int getIdContrat() {
        return idContrat;
    }

    public void setIdContrat(int idContrat) {
        this.idContrat = idContrat;
    }

    public LocalDate getDateDebutContrat() {
        return dateDebutContrat;
    }

    public void setDateDebutContrat(LocalDate dateDebutContrat) {
        this.dateDebutContrat = dateDebutContrat;
    }

    public LocalDate getDateFinContrat() {
        return dateFinContrat;
    }

    public void setDateFinContrat(LocalDate dateFinContrat) {
        this.dateFinContrat = dateFinContrat;
    }

    public String getStatusContrat() {
        return statusContrat;
    }

    public void setStatusContrat(String statusContrat) {
        this.statusContrat = statusContrat;
    }

    public int getMontantContrat() {
        return montantContrat;
    }

    public void setMontantContrat(int montantContrat) {
        this.montantContrat = montantContrat;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public String getTelephoneClient() {return telephoneClient;}

    public void setTelephoneClient(String telephoneClient) {this.telephoneClient = telephoneClient;}

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    //méthode toString
    @Override
    public String toString() {
        return "Contrat{" +
                "idContrat=" + idContrat +
                ", dateDebutContrat=" + dateDebutContrat +
                ", dateFinContrat=" + dateFinContrat +
                ", statusContrat='" + statusContrat + '\'' +
                ", montantContrat=" + montantContrat +
                ", nomClient='" + nomClient + '\'' +
                ", emailClient='" + emailClient + '\'' +
                ", telephoneClient='" + telephoneClient + '\'' +
                ", services=" + (services != null ? services.toString() : "Aucun service associé") +
                '}';
    }
}
