package tn.esprit.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Service {

    private int idService;
    private String nomService;
    private String descriptionService;
    private String typeService;
    private LocalDate dateDebutService;
    private LocalDate dateFinService;
    private String statusService;
    private List<Contrat> contrats;


    //Constructeurs
    public Service() {
        this.contrats = new ArrayList<>();
    }

    public Service(int idService, String nomService, String descriptionService, String typeService, LocalDate dateDebutService, LocalDate dateFinService, String statusService) {
        this.idService = idService;
        this.nomService = nomService;
        this.descriptionService = descriptionService;
        this.typeService = typeService;
        this.dateDebutService = dateDebutService;
        this.dateFinService = dateFinService;
        this.statusService = statusService;
        this.contrats = new ArrayList<>();
    }

    public Service(String nomService, String descriptionService, String typeService, LocalDate dateDebutService, LocalDate dateFinService, String statusService) {
        this.nomService = nomService;
        this.descriptionService = descriptionService;
        this.typeService = typeService;
        this.dateDebutService = dateDebutService;
        this.dateFinService = dateFinService;
        this.statusService = statusService;
        this.contrats = new ArrayList<>();
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

    public LocalDate getDateDebutService() {
        return dateDebutService;
    }

    public void setDateDebutService(LocalDate dateDebutService) {
        this.dateDebutService = dateDebutService;
    }

    public LocalDate getDateFinService() {
        return dateFinService;
    }

    public void setDateFinService(LocalDate dateFinService) {
        this.dateFinService = dateFinService;
    }

    public String getStatusService() {
        return statusService;
    }

    public void setStatusService(String statusService) {
        this.statusService = statusService;
    }


    public List<Contrat> getContrats() { return contrats; }
    public void setContrats(List<Contrat> contrats) { this.contrats = contrats; }







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
                ", contrats=" + contrats +
                '}';
    }
}
