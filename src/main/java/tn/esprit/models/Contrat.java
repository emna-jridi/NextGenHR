package tn.esprit.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Contrat {

    private int idContrat;
    private String typeContrat;
    private LocalDate dateDebutContrat;
    private LocalDate dateFinContrat;
    private String statusContrat;
    private int montantContrat;
    private String nomClient;
    private String emailClient;
    //private int idService;
    private List<Service> services;


    // Constructeurs

    public Contrat() {
    }

    public Contrat(int idContrat, String typeContrat, LocalDate dateDebutContrat, LocalDate dateFinContrat,
                   String statusContrat, int montantContrat, String nomClient, String emailClient) {
        this.idContrat = idContrat;
        this.typeContrat = typeContrat;
        this.dateDebutContrat = dateDebutContrat;
        this.dateFinContrat = dateFinContrat;
        this.statusContrat = statusContrat;
        this.montantContrat = montantContrat;
        this.nomClient = nomClient;
        this.emailClient = emailClient;
        //this.idService = idService;
        //this.services = new ArrayList<>();
    }

    public Contrat(String typeContrat, LocalDate dateDebutContrat, LocalDate dateFinContrat,
                   String statusContrat, int montantContrat, String nomClient, String emailClient) {
        this.typeContrat = typeContrat;
        this.dateDebutContrat = dateDebutContrat;
        this.dateFinContrat = dateFinContrat;
        this.statusContrat = statusContrat;
        this.montantContrat = montantContrat;
        this.nomClient = nomClient;
        this.emailClient = emailClient;
        //this.idService = idService;
        //this.services = new ArrayList<>();
    }



    // Getters et Setters
    public int getIdContrat() {
        return idContrat;
    }

    public void setIdContrat(int idContrat) {
        this.idContrat = idContrat;
    }

    public String getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(String typeContrat) {
        this.typeContrat = typeContrat;
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


    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }


    /*public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }*/




    //méthode toString
    @Override
    public String toString() {
        return "Contrat{" +
                "idContrat=" + idContrat +
                ", typeContrat='" + typeContrat + '\'' +
                ", dateDebutContrat=" + dateDebutContrat +
                ", dateFinContrat=" + dateFinContrat +
                ", statusContrat='" + statusContrat + '\'' +
                ", montantContrat=" + montantContrat +
                ", nomClient='" + nomClient + '\'' +
                ", emailClient='" + emailClient + '\'' +
                ", services=" + services +
                //", idService=" + idService +
                '}';
    }
}
