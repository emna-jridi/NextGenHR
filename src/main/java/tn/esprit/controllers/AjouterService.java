package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
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

    @FXML
    private ToggleGroup statusGroup;

    private final ServiceService serviceService = new ServiceService();

    private Runnable onServiceAdded;

    public void setOnServiceAdded(Runnable onServiceAdded) {
        this.onServiceAdded = onServiceAdded;
    }

    @FXML
    void ajouterService(ActionEvent event) {
        String nom = nomService.getText().trim();
        String description = descriptionService.getText().trim();
        String type = typeService.getText().trim();
        LocalDate dateDebut = dateDebutService.getValue();
        LocalDate dateFin = dateFinService.getValue();
        String status = radioActif.isSelected() ? "Actif" : "Inactif";


        if (nom.isEmpty() && description.isEmpty() && type.isEmpty() && dateDebut == null && dateFin == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs svp.");
            return;
        }

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

        Service service = new Service(nom, description, type, dateDebut, dateFin, status);
        serviceService.add(service);

        if (onServiceAdded != null) {
            onServiceAdded.run();
        }

        showAlert("Succès", "Le service a été ajouté avec succès.");
        closeWindow();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        ((Stage) nomService.getScene().getWindow()).close();
    }
}
