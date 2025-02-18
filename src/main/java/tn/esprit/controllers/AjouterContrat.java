package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Contrat;
import tn.esprit.services.ServiceContrat;

public class AjouterContrat {

    @FXML
    private TextField typeContrat;

    @FXML
    private DatePicker dateDebutContrat;

    @FXML
    private DatePicker dateFinContrat;

    @FXML
    private TextField montantContrat;

    @FXML
    private TextField nomClient;

    @FXML
    private TextField emailClient;

    @FXML
    private RadioButton radioActif;

    @FXML
    private RadioButton radioInactif;

    @FXML
    private ToggleGroup statusGroup;

    private ServiceContrat serviceContrat = new ServiceContrat();

    // Callback à appeler après l'ajout d'un contrat
    private Runnable onContratAdded;

    // Méthode pour définir le callback
    public void setOnContratAdded(Runnable onContratAdded) {
        this.onContratAdded = onContratAdded;
    }

    @FXML
    void initialize() {
        // Initialisation du ToggleGroup
        statusGroup = new ToggleGroup(); // Créez le groupe de boutons radio
        radioActif.setToggleGroup(statusGroup);  // Associez les boutons radio au groupe
        radioInactif.setToggleGroup(statusGroup);
    }

    @FXML
    void ajouter(ActionEvent event) {
        // Récupération des données du formulaire
        String type = typeContrat.getText();
        String montantStr = montantContrat.getText();
        String nom = nomClient.getText();
        String email = emailClient.getText();

        // Vérification du statut du contrat
        String status = radioActif.isSelected() ? "Actif" : "Inactif";

        // Validation des champs
        if (type.isEmpty() || montantStr.isEmpty() || nom.isEmpty() || email.isEmpty() || dateDebutContrat.getValue() == null || dateFinContrat.getValue() == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        // Conversion du montant en entier
        int montant = 0;
        try {
            montant = Integer.parseInt(montantStr);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le montant doit être un nombre valide.");
            return;
        }

        // Création d'un nouveau contrat
        Contrat contrat = new Contrat(type, dateDebutContrat.getValue(), dateFinContrat.getValue(), status, montant, nom, email);

        // Appel de la méthode pour ajouter le contrat
        serviceContrat.add(contrat);

        // Si le callback est défini, on l'appelle pour rafraîchir la liste
        if (onContratAdded != null) {
            onContratAdded.run();
        }

        // Affichage d'un message de succès
        showAlert("Succès", "Le contrat a été ajouté avec succès.");

        // Fermer la fenêtre après ajout
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
        // Fermer la fenêtre courante
        ((Stage) typeContrat.getScene().getWindow()).close();
    }
}
