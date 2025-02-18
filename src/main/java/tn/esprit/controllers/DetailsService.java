package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tn.esprit.models.Service;
import tn.esprit.services.ServiceService;

public class DetailsService {

    @FXML
    private Label labelNomService;

    @FXML
    private Label labelDescriptionService;

    @FXML
    private Label labelTypeService;

    @FXML
    private Label labelDateDebut;

    @FXML
    private Label labelDateFin;

    @FXML
    private Label labelStatutService;

    private ServiceService serviceService = new ServiceService ();
    private Service service; // Le service dont on affiche les détails

    // Méthode pour initialiser et afficher les détails du service
    @FXML
    public void initialize() {
        // Supposons que l'ID du service soit passé à cette scène via une autre méthode.
        // Ici, on simule l'appel à la méthode getServiceById pour obtenir les détails d'un service.
        service = serviceService.getById(1); // Exemple: chercher le service avec ID 1

        if (service != null) {
            afficherDetailsService(service);
        }
    }

    // Méthode pour afficher les détails du service dans l'interface
    private void afficherDetailsService(Service service) {
        labelNomService.setText(service.getNomService());
        labelDescriptionService.setText(service.getDescriptionService());
        labelTypeService.setText(service.getTypeService());
        labelDateDebut.setText(service.getDateDebutService().toString());
        labelDateFin.setText(service.getDateFinService().toString());
        labelStatutService.setText(service.getStatusService());
    }

    // Méthode pour retourner à la liste des services
    @FXML
    void retour(ActionEvent event) {
        // Logique pour fermer cette fenêtre et retourner à la liste des services.
        // Vous pouvez fermer la scène actuelle ou charger une autre scène contenant la liste des services.
        System.out.println("Retour à la liste des services");
    }
}
