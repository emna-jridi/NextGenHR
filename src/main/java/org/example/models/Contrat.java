package org.example.models;

import java.util.Date;

public class Contrat {
    private int idContrat;
    private String typeContrat;
    private Date dateDebutContrat;
    private Date dateFinContrat;
    private String statusContrat;
    private int montantContrat;
    private String nomClient;
    private String emailClient;
    private int idService;

    // Constructeurs
    public Contrat() {
    }

    public Contrat(int idContrat, String typeContrat, Date dateDebutContrat, Date dateFinContrat,
                   String statusContrat, int montantContrat, String nomClient, String emailClient, int idService) {
        this.idContrat = idContrat;
        this.typeContrat = typeContrat;
        this.dateDebutContrat = dateDebutContrat;
        this.dateFinContrat = dateFinContrat;
        this.statusContrat = statusContrat;
        this.montantContrat = montantContrat;
        this.nomClient = nomClient;
        this.emailClient = emailClient;
        this.idService = idService;
    }

    public Contrat(String typeContrat, Date dateDebutContrat, Date dateFinContrat,
                   String statusContrat, int montantContrat, String nomClient, String emailClient, int idService) {
        this.typeContrat = typeContrat;
        this.dateDebutContrat = dateDebutContrat;
        this.dateFinContrat = dateFinContrat;
        this.statusContrat = statusContrat;
        this.montantContrat = montantContrat;
        this.nomClient = nomClient;
        this.emailClient = emailClient;
        this.idService = idService;
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

    public Date getDateDebutContrat() {
        return dateDebutContrat;
    }

    public void setDateDebutContrat(Date dateDebutContrat) {
        this.dateDebutContrat = dateDebutContrat;
    }

    public Date getDateFinContrat() {
        return dateFinContrat;
    }

    public void setDateFinContrat(Date dateFinContrat) {
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

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

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
                ", idService=" + idService +
                '}';
    }
}
