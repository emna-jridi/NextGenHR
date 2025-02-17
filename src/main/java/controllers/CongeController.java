package controllers;

import entities.conge;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.ServiceConge;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


import java.time.LocalDate;

public class CongeController {

    @FXML
    private ListView<String> listViewConge;

    @FXML
    private ComboBox<String> typeCon; // ComboBox for Type_conge

    @FXML
    private DatePicker datedep; // DatePicker for Date_debut

    @FXML
    private DatePicker datefin; // DatePicker for Date_fin

    @FXML
    private TextField status; // TextField for Status

    private ServiceConge serviceConge; // Service to handle database operations

    public CongeController() {
        this.serviceConge = new ServiceConge(); // Initialize the service
    }
    private int selectedCongeId = -1; // Stocke l'ID du congé sélectionné

    @FXML
    public void initialize() {
        // Initialize the ComboBox with types of leave
        typeCon.getItems().addAll("Congé annuel", "Congé obligatoire", "Congé spécial", "Congé de maternité ou parental");
    }

    @FXML
    private void handleDeleteAction() {
        String selectedItem = listViewConge.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            System.out.println("Veuillez sélectionner un congé à supprimer !");
            return;
        }

        // Extraire l'ID du congé depuis la chaîne de la ListView
        int id = Integer.parseInt(selectedItem.split("\\|")[0].replace("ID: ", "").trim());

        // Confirmer la suppression
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer ce congé ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            serviceConge.delete(id);
            System.out.println("Congé supprimé avec succès !");
            handleAfficherAction(); // Rafraîchir la liste après suppression
        }
    }

    @FXML
    private void handleAfficherAction() {
        listViewConge.getItems().clear(); // Vider la liste avant d'ajouter de nouveaux éléments
        for (conge c : serviceConge.getAll()) {
            listViewConge.getItems().add(
                    "ID: " + c.getId() + " | Type: " + c.getType_conge() +
                            " | Début: " + c.getDate_debut() + " | Fin: " + c.getDate_fin() +
                            " | Statut: " + c.getStatus()
            );
        }
    }
    @FXML
    private void handleUpdateAction() {
        if (selectedCongeId == -1) {
            System.out.println("Veuillez sélectionner un congé à modifier !");
            return;
        }

        // Récupérer les nouvelles valeurs
        String typeConge = typeCon.getValue();
        LocalDate dateDebut = datedep.getValue();
        LocalDate dateFin = datefin.getValue();
        String statusText = status.getText();

        // Validation
        if (typeConge == null || dateDebut == null || dateFin == null || statusText.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs !");
            return;
        }

        // Confirmer la modification
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment modifier ce congé ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            conge updatedConge = new conge(selectedCongeId, typeConge, dateDebut, dateFin, statusText);
            serviceConge.update(updatedConge, selectedCongeId);
            System.out.println("Congé modifié avec succès !");
            handleAfficherAction(); // Rafraîchir la liste
            clearForm(); // Réinitialiser les champs
            selectedCongeId = -1; // Réinitialiser l’ID sélectionné
        }
    }

    @FXML
    private void handleSelection() {
        String selectedItem = listViewConge.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        // Extraire l'ID depuis la chaîne de la ListView
        selectedCongeId = Integer.parseInt(selectedItem.split("\\|")[0].replace("ID: ", "").trim());

        // Récupérer le congé depuis la base
        for (conge c : serviceConge.getAll()) {
            if (c.getId() == selectedCongeId) {
                typeCon.setValue(c.getType_conge());
                datedep.setValue(c.getDate_debut());
                datefin.setValue(c.getDate_fin());
                status.setText(c.getStatus());
                break;
            }
        }
    }

    @FXML
    private void handelSubmitAction() {
        // Get values from the UI components
        String typeConge = typeCon.getValue(); // Get selected type of leave
        LocalDate dateDebut = datedep.getValue(); // Get selected start date
        LocalDate dateFin = datefin.getValue(); // Get selected end date
        String statusText = status.getText(); // Get status text

        // Validate input fields
        if (typeConge == null || dateDebut == null || dateFin == null || statusText.isEmpty()) {
            System.out.println("Please fill all fields!");
            return;
        }

        // Create a new conge object
        conge newConge = new conge();
        newConge.setType_conge(typeConge);
        newConge.setDate_debut(dateDebut);
        newConge.setDate_fin(dateFin);
        newConge.setStatus(statusText);

        // Add the new conge to the database
        serviceConge.add(newConge);

        // Clear the form after submission
        clearForm();

        System.out.println("Congé added successfully!");
    }

    private void clearForm() {
        // Clear all input fields
        typeCon.setValue(null);
        datedep.setValue(null);
        datefin.setValue(null);
        status.clear();
    }
}