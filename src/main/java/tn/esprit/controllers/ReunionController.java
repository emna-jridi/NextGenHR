package tn.esprit.controllers;


import tn.esprit.models.Reunion;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import tn.esprit.services.ServiceReunion;
import java.time.LocalDate;


import java.time.LocalDate;
import java.util.List;

public class ReunionController {

    @FXML
    private TextField titre;

    @FXML
    private DatePicker dat;

    @FXML
    private ComboBox<String> typ;

    @FXML
    private TextField description;

    ServiceReunion serviceReunion = new ServiceReunion();
    private int reunionIdToUpdate = -1;
    private boolean isEditing = false;

    @FXML
    public void initialize() {
        typ.getItems().addAll("En ligne", "Présentielle");
    }
    @FXML
    private FlowPane containerReunions;


    @FXML
    private void handelSubmitAction(ActionEvent event) {
        if (!validerChamps()) {
            return; // Ne pas continuer si la validation échoue
        }

        String titreReunion = titre.getText().trim();
        LocalDate dateReunion = dat.getValue();
        String typeReunion = typ.getValue();
        String desc = description.getText().trim();

        Reunion reunion = new Reunion();
        reunion.setTitre(titreReunion);
        reunion.setDate(dateReunion);
        reunion.setType(typeReunion);
        reunion.setDescription(desc);

        if (reunionIdToUpdate == -1) {
            // Ajout
            serviceReunion.add(reunion);
            afficherAlerte("Succès", "Réunion ajoutée !");
        } else {
            // Modification
            serviceReunion.update(reunion, reunionIdToUpdate);
            afficherAlerte("Succès", "Réunion mise à jour !");
            reunionIdToUpdate = -1;
        }

        clearForm();
        handleAfficherAction(new ActionEvent()); // Rafraîchir l'affichage
    }




    // Méthode pour afficher une boîte de dialogue d'alerte
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void handleAfficherAction(ActionEvent event) {
        containerReunions.getChildren().clear(); // Nettoyer avant d'afficher

        List<Reunion> reunions = serviceReunion.getAll();

        for (Reunion reunion : reunions) {
            VBox card = createReunionCard(reunion);
            containerReunions.getChildren().add(card);
        }
    }

    private Button editButton; // Déclaré globalement pour suivre l'état du bouton

    private VBox createReunionCard(Reunion reunion) {
        VBox card = new VBox(5);
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ff9800; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-padding: 10px;");
        card.setPrefWidth(280);

        Label titleLabel = new Label("📌 " + reunion.getTitre());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label dateLabel = new Label("📅 Date: " + reunion.getDate());
        Label typeLabel = new Label("📍 Type: " + reunion.getType());
        Label descLabel = new Label("📝 " + reunion.getDescription());

        Button editButton = new Button("Modifier");
        editButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold;");
        editButton.setOnAction(e -> populateForm(reunion)); // Remplit le formulaire pour modification

        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        deleteButton.setOnAction(e -> {
            serviceReunion.delete(reunion.getId());
            handleAfficherAction(new ActionEvent());
        });

        card.getChildren().addAll(titleLabel, dateLabel, typeLabel, descLabel, editButton, deleteButton);
        return card;
    }



    @FXML
    private void handleModifierAction(ActionEvent event) {
        if (reunionIdToUpdate == -1) {
            afficherAlerte("Erreur", "Veuillez sélectionner une réunion !");
            return;
        }

        if (!validerChamps()) return; // Vérification avant de continuer

        Reunion updatedReunion = new Reunion();
        updatedReunion.setId(reunionIdToUpdate);
        updatedReunion.setTitre(titre.getText().trim());
        updatedReunion.setDate(dat.getValue());
        updatedReunion.setType(typ.getValue());
        updatedReunion.setDescription(description.getText().trim());

        serviceReunion.update(updatedReunion, reunionIdToUpdate);
        afficherAlerte("Succès", "Réunion mise à jour !");

        // Réinitialisation
        reunionIdToUpdate = -1;
        clearForm();
        handleAfficherAction(new ActionEvent()); // Rafraîchir l'affichage
    }

    // Vérifier si on est en modification

    private void handleEditAction(Reunion reunion) {
        if (!isEditing) {
            // Premier clic : Remplir les champs
            titre.setText(reunion.getTitre());
            dat.setValue(reunion.getDate());
            typ.setValue(reunion.getType());
            description.setText(reunion.getDescription());

            reunionIdToUpdate = reunion.getId(); // Stocker l'ID de la réunion
            isEditing = true; // Passer en mode modification
            editButton.setText("Confirmer Modification"); // Changer le texte du bouton
        } else {
            // Deuxième clic : Confirmer la modification
            if (reunionIdToUpdate == -1) {
                afficherAlerte("Erreur", "Aucune réunion sélectionnée !");
                return;
            }

            Reunion updatedReunion = new Reunion();
            updatedReunion.setId(reunionIdToUpdate);
            updatedReunion.setTitre(titre.getText());
            updatedReunion.setDate(dat.getValue());
            updatedReunion.setType(typ.getValue());
            updatedReunion.setDescription(description.getText());

            serviceReunion.update(updatedReunion, reunionIdToUpdate);
            afficherAlerte("Succès", "Réunion mise à jour !");

            // Réinitialiser après modification
            reunionIdToUpdate = -1;
            isEditing = false;
            editButton.setText("Modifier");
            clearForm();
            handleAfficherAction(new ActionEvent()); // Rafraîchir l'affichage
        }
    }

    private void clearForm() {
        // Clear all input fields
        titre.clear();
        dat.setValue(null);
        typ.setValue(null);
        description.clear();
    }
    private boolean validerChamps() {
        String titreReunion = titre.getText().trim();
        LocalDate dateReunion = dat.getValue();
        String typeReunion = typ.getValue();
        String desc = description.getText().trim();

        // Vérifier si les champs sont vides
        if (titreReunion.isEmpty() || dateReunion == null || typeReunion == null || desc.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez remplir tous les champs !");
            return false;
        }

        // Vérifier que le titre commence par une majuscule et ne contient pas de chiffres
        if (!titreReunion.matches("^[A-Z][a-zA-Z ]*$")) {
            afficherAlerte("Erreur", "Le titre doit commencer par une majuscule et ne contenir aucun chiffre !");
            return false;
        }

        // Vérifier que la description commence par une majuscule
        if (!desc.matches("^[A-Z].*")) {
            afficherAlerte("Erreur", "La description doit commencer par une majuscule !");
            return false;
        }

        return true;
    }
    private void populateForm(Reunion reunion) {
        titre.setText(reunion.getTitre());
        dat.setValue(reunion.getDate());
        typ.setValue(reunion.getType());
        description.setText(reunion.getDescription());
        reunionIdToUpdate = reunion.getId(); // Stocker l'ID pour la mise à jour
    }
}
