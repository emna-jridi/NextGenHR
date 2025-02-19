package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.Service;
import tn.esprit.services.ServiceService;

import java.time.LocalDate;

public class AjouterService {

    @FXML
    private TextField nomService;

    @FXML
    private TextArea descriptionService;

    @FXML
    private TextField typeService;

    @FXML
    private DatePicker dateDebutService;

    @FXML
    private DatePicker dateFinService;

    @FXML
    private RadioButton radioActif;

    @FXML
    private RadioButton radioInactif;

    private ServiceService serviceService = new ServiceService(); // Assurez-vous que le service est bien implémenté

    // Callback à appeler après l'ajout d'un contrat
    private Runnable onServiceAdded;

    // Méthode pour définir le callback
    public void setOnServiceAdded(Runnable onServiceAdded) {
        this.onServiceAdded = onServiceAdded;
    }

    @FXML
    private ToggleGroup statusGroup;  // Déclaration du groupe de boutons radio

    @FXML
    void initialize() {
        // Initialisation du ToggleGroup
        statusGroup = new ToggleGroup();  // Créez le groupe de boutons radio
        radioActif.setToggleGroup(statusGroup);  // Associez les boutons radio au groupe
        radioInactif.setToggleGroup(statusGroup);
    }

    @FXML
    void ajouterService(ActionEvent event) {
        // Récupération des données du formulaire
        String nom = nomService.getText();
        String description = descriptionService.getText();
        String type = typeService.getText();
        LocalDate dateDebut = dateDebutService.getValue();
        LocalDate dateFin = dateFinService.getValue();
        String status = radioActif.isSelected() ? "Actif" : "Inactif";

        // Validation des champs
        if (nom.isEmpty()) {
            showAlert("Erreur", "Le nom du service est obligatoire.");
            return;
        }

        if (description.isEmpty()) {
            showAlert("Erreur", "La description du service est obligatoire.");
            return;
        }

        if (type.isEmpty()) {
            showAlert("Erreur", "Le type du service est obligatoire.");
            return;
        }

        if (dateDebut == null) {
            showAlert("Erreur", "La date de début du service est obligatoire.");
            return;
        }

        if (dateFin == null) {
            showAlert("Erreur", "La date de fin du service est obligatoire.");
            return;
        }

        if (dateDebut.isAfter(dateFin)) {
            showAlert("Erreur", "La date de début ne peut pas être après la date de fin.");
            return;
        }



        // Création d'un nouvel objet Service
        Service service = new Service(nom, description, type, dateDebut, dateFin, status);

        // Appel de la méthode d'ajout du service dans la base de données (ou toute autre logique nécessaire)
        serviceService.add(service);



        // Si le callback est défini, on l'appelle pour rafraîchir la liste
        if (onServiceAdded != null) {
            onServiceAdded.run();
        }

        // Affichage d'un message de succès
        showAlert("Succès", "Le service a été ajouté avec succès.");
    }

    // Méthode pour afficher des alertes
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
