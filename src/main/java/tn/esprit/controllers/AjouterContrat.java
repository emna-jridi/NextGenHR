package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Contrat;
import tn.esprit.models.TypeContrat;
import tn.esprit.services.ServiceContrat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AjouterContrat {

    @FXML
    private ComboBox<TypeContrat> comboTypeContrat;  // Remplacer TextField par ComboBox


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


    private Runnable onContratAdded;

//refresh the Contrats list
    public void setOnContratAdded(Runnable onContratAdded) {
        this.onContratAdded = onContratAdded;
    }

    //initialiser ToggleGroup
    @FXML
    void initialize() {
// Remplir le ComboBox avec les valeurs de l'enum TypeContrat
        comboTypeContrat.getItems().setAll(TypeContrat.values());
        statusGroup = new ToggleGroup();
        radioActif.setToggleGroup(statusGroup);
        radioInactif.setToggleGroup(statusGroup);
    }

    @FXML
    void ajouter(ActionEvent event) {

        // Récupérer le type de contrat sélectionné dans le ComboBox
        TypeContrat type = comboTypeContrat.getValue();
        String montantStr = montantContrat.getText();
        String nom = nomClient.getText();
        String email = emailClient.getText();
        String status = radioActif.isSelected() ? "Actif" : "Inactif";


        // Vérifier si tous les champs sont vides
        if (type == null && montantStr.isEmpty() && nom.isEmpty() && email.isEmpty() &&
                dateDebutContrat.getValue() == null && dateFinContrat.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir les champs svp.");
            return;
        }


        if (type == null) {
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



        int montant = 0;
        try {
            montant = Integer.parseInt(montantStr);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le montant doit être un nombre valide.");
            return;
        }


        Contrat contrat = new Contrat(type, dateDebutContrat.getValue(), dateFinContrat.getValue(), status, montant, nom, email);


        serviceContrat.add(contrat);


        if (onContratAdded != null) {
            onContratAdded.run();
        }


        showAlert("Succès", "Le contrat a été ajouté avec succès.");


        closeWindow();
    }



    private boolean isValidEmail(String email) {

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {

        ((Stage) comboTypeContrat.getScene().getWindow()).close();
    }
}
