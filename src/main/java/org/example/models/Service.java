package org.example.models;

import java.util.Date;



public class Service {

    private int idService;
    private String nomService;
    private String descriptionService;
    private String typeService;
    private Date dateDebutService;
    private Date dateFinService;
    private String statusService;
    private int idContrat;


    //Constructeurs
    public Service() {
    }

    public Service(int idService, String nomService, String descriptionService, String typeService, Date dateDebutService, Date dateFinService, String statusService, int idContrat) {
        this.idService = idService;
        this.nomService = nomService;
        this.descriptionService = descriptionService;
        this.typeService = typeService;
        this.dateDebutService = dateDebutService;
        this.dateFinService = dateFinService;
        this.statusService = statusService;
        this.idContrat = idContrat;
    }

    public Service(String nomService, String descriptionService, String typeService, Date dateDebutService, Date dateFinService, String statusService, int idContrat) {
        this.nomService = nomService;
        this.descriptionService = descriptionService;
        this.typeService = typeService;
        this.dateDebutService = dateDebutService;
        this.dateFinService = dateFinService;
        this.statusService = statusService;
        this.idContrat = idContrat;
    }



    // Getters and Setters
    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public String getDescriptionService() {
        return descriptionService;
    }

    public void setDescriptionService(String descriptionService) {
        this.descriptionService = descriptionService;
    }

    public String getTypeService() {
        return typeService;
    }

    public void setTypeService(String typeService) {
        this.typeService = typeService;
    }

    public Date getDateDebutService() {
        return dateDebutService;
    }

    public void setDateDebutService(Date dateDebutService) {
        this.dateDebutService = dateDebutService;
    }

    public Date getDateFinService() {
        return dateFinService;
    }

    public void setDateFinService(Date dateFinService) {
        this.dateFinService = dateFinService;
    }

    public String getStatusService() {
        return statusService;
    }

    public void setStatusService(String statusService) {
        this.statusService = statusService;
    }

    public int getIdContrat() {
        return idContrat;
    }

    public void setIdContrat(int idContrat) {
        this.idContrat = idContrat;
    }



    //méthode toString
    @Override
    public String toString() {
        return "Service{" +
                "idService=" + idService +
                ", nomService='" + nomService + '\'' +
                ", descriptionService='" + descriptionService + '\'' +
                ", typeService='" + typeService + '\'' +
                ", dateDebutService=" + dateDebutService +
                ", dateFinService=" + dateFinService +
                ", statusService='" + statusService + '\'' +
                ", idContrat=" + idContrat +
                '}';
    }
}
