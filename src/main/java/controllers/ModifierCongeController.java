package controllers;

import entities.conge;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.ServiceConge;

import java.time.LocalDate;

public class ModifierCongeController {

    @FXML
    private TextField idField;
    @FXML
    private ComboBox<String> typeCon;
    @FXML
    private DatePicker datedep;
    @FXML
    private DatePicker datefin;
    @FXML
    private TextField status;

    private final ServiceConge serviceConge;
    private conge selectedConge; // L'objet Congé à modifier

    public ModifierCongeController() {
        this.serviceConge = new ServiceConge();
    }

    @FXML
    public void initialize() {
        System.out.println("ModifierCongeController chargé !");

        // Ajouter les options dans ComboBox (si ce n'est pas déjà fait)
        typeCon.getItems().addAll("Congé annuel", "Congé obligatoire", "Congé spécial", "Congé de maternité ou parental");
    }

    // ✅ Méthode pour recevoir les données du congé sélectionné
    public void setCongeData(conge c) {
        if (c == null) {
            showAlert("Erreur", "Aucune donnée de congé sélectionnée !");
            return;
        }
        this.selectedConge = c;

        System.out.println("Données reçues : ID = " + c.getId());

        // Remplir les champs
        idField.setText(String.valueOf(c.getId()));
        typeCon.setValue(c.getType_conge());
        datedep.setValue(c.getDate_debut());
        datefin.setValue(c.getDate_fin());
        status.setText(c.getStatus());
    }

    // ✅ Méthode pour enregistrer les modifications
    @FXML
    private void handleSave() {
        if (selectedConge == null) {
            showAlert("Erreur", "Aucun congé sélectionné !");
            return;
        }

        // Vérification des champs
        if (!validateFields()) return;

        // Mettre à jour les données du congé
        selectedConge.setType_conge(typeCon.getValue());
        selectedConge.setDate_debut(datedep.getValue());
        selectedConge.setDate_fin(datefin.getValue());
        selectedConge.setStatus(status.getText());

        // Mise à jour dans la base de données
        serviceConge.update(selectedConge, selectedConge.getId());

        // Afficher une alerte de succès
        showAlert("Succès", "Congé mis à jour avec succès !");

        // Fermer la fenêtre après modification
        closeWindow();
    }

    // ✅ Méthode pour annuler les modifications et fermer la fenêtre
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    // ✅ Vérification des champs avant modification
    private boolean validateFields() {
        if (typeCon.getValue() == null || typeCon.getValue().trim().isEmpty() ||
                datedep.getValue() == null || datefin.getValue() == null ||
                status.getText().trim().isEmpty()) {

            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return false;
        }

        // Vérifier que date_debut est avant date_fin
        if (datedep.getValue().isAfter(datefin.getValue())) {
            showAlert("Erreur", "La date de début ne peut pas être après la date de fin !");
            return false;
        }

        return true;
    }

    // ✅ Fermer la fenêtre actuelle
    private void closeWindow() {
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.close();
    }

    // ✅ Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
