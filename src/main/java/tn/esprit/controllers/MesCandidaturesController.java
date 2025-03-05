package tn.esprit.controllers;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import tn.esprit.models.Candidature;
import tn.esprit.models.SessionManager;
import tn.esprit.models.Statut;
import tn.esprit.models.User;
import tn.esprit.services.ServiceCandidature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MesCandidaturesController {
    @FXML
    private TableColumn<Candidature, String> CV;

    @FXML
    private TableColumn<Candidature, String> Date;

    @FXML
    private TableColumn<Candidature, String> Email;

    @FXML
    private TableColumn<Candidature, String> Lettre;

    @FXML
    private TableColumn<Candidature, String> Nom;

    @FXML
    private TableColumn<Candidature, String> Offre;

    @FXML
    private TableColumn<Candidature, String> Prènom;

    @FXML
    private TableColumn<Candidature, String> Statut;

    @FXML
    private TableColumn<Candidature, String> Téléphone;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private TableView<Candidature> tableacceptées;
    private final ServiceCandidature serviceCandidature = new ServiceCandidature();
    @FXML
    private void initialize() {
        Nom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        Prènom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
        Email.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        Téléphone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelephone()));
        Offre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOffreemploi().getTitre()));
        Date.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateCandidature().toString()));
        Lettre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLettreMotivation()));
        CV.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCvUrl()));
        Statut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatut().toString()));
        Statut.setCellFactory(column -> new TableCell<Candidature, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    // Appliquer la couleur selon le statut
                    switch (item) {
                        case "En_cours":
                            setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
                            break;
                        case "acceptée":
                            setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                            break;
                        case "disqualifiée":
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                            break;
                    }
                }
            }
        });

        loadCandidatures();
    }
    @FXML
    private void loadCandidatures() {

        User connectedUser = SessionManager.getConnectedUser();

        if (connectedUser != null) {
            List<Candidature> candidatures = serviceCandidature.getCandidaturesByUserId(connectedUser.getIdUser());
            ObservableList<Candidature> candidatureList = FXCollections.observableArrayList(candidatures);
            tableacceptées.setItems(candidatureList);
        }
    }

    @FXML
    void supprimermacandidature(ActionEvent event) {
        Candidature selectedCandidature = tableacceptées.getSelectionModel().getSelectedItem();

        if (selectedCandidature != null) {
            if (selectedCandidature.getStatut() == tn.esprit.models.Statut.En_cours) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation de suppression");
                confirmationAlert.setHeaderText("Suppression de la candidature");
                confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette candidature ?");
                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    int candidatureId = selectedCandidature.getId();
                    serviceCandidature.delete(candidatureId);
                    tableacceptées.getItems().remove(selectedCandidature);
                    loadCandidatures();
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Suppression réussie");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("La candidature a été supprimée avec succès !");
                    successAlert.showAndWait();
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Suppression impossible",
                        "Vous ne pouvez supprimer qu'une candidature avec le statut 'En_cours'.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection",
                    "Veuillez sélectionner une candidature à supprimer.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void switchView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent pane = loader.load();

            // Ajout d'une animation de transition
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), pane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            contentPane.getChildren().setAll(pane);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de " + fxmlPath);
            e.printStackTrace();
        }
    }
    @FXML
    void pageprécédente(ActionEvent event) {
        switchView("/DemandeCandidature.fxml");

    }
}

