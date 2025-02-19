package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Contrat;
import tn.esprit.services.ServiceContrat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if (type.isEmpty()) {
            showAlert("Erreur", "Le type du contrat est obligatoire.");
            return;
        }

        if (montantStr.isEmpty()) {
            showAlert("Erreur", "Le montant du contrat est obligatoire.");
            return;
        }

        if (nom.isEmpty()) {
            showAlert("Erreur", "Le nom du client est obligatoire.");
            return;
        }

        if (email.isEmpty()) {
            showAlert("Erreur", "L'email du client est obligatoire.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Erreur", "L'email est au format incorrect.");
            return;
        }

        if (dateDebutContrat.getValue() == null) {
            showAlert("Erreur", "La date de début du contrat est obligatoire.");
            return;
        }

        if (dateFinContrat.getValue() == null) {
            showAlert("Erreur", "La date de fin du contrat est obligatoire.");
            return;
        }

        if (dateDebutContrat.getValue().isAfter(dateFinContrat.getValue())) {
            showAlert("Erreur", "La date de début ne peut pas être après la date de fin.");
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



    private boolean isValidEmail(String email) {
        // Expression régulière pour valider un email au format standard
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches(); // Retourne true si l'email correspond au pattern
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
