package tn.esprit.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MainViewController {

    @FXML
    private AnchorPane contentPane;

    @FXML
    private VBox homePage;

    @FXML
    private void showHome() {
        contentPane.getChildren().setAll(homePage);
    }

    @FXML
    private void showSalle() {
        switchView("/Salle.fxml");
    }

    @FXML
    private void showTeletravailRH() {
        switchView("/TeletravailRH.fxml");
    }
    @FXML
    private void showTeletravailEm() {
        switchView("/TeletravailEm.fxml");
    }

    @FXML
    private void showReservationRH() {
        switchView("/ReservationRH.fxml");
    }

    @FXML
    private void showReserationSalle() {
        switchView("/ReservationSalle.fxml");
    }

    @FXML
    private void showConge() {
        switchView("/con_form.fxml");
    }

    @FXML
    private void showReunion() {
        switchView("/reunion.fxml");
    }

    @FXML
    private void showContrat() {
        switchView("/ListeContrats.fxml");
    }

    @FXML
    private void showServices() {
        switchView("/ListeServices.fxml");
    }

    @FXML
    private void showCandidature() {
        switchView("/Candidature.fxml");
    }

    @FXML
    private void showOffreEmploi() {
        switchView("/AjouterOffre.fxml");
    }

    @FXML
    private void handleProfile() {
        switchView("/Profile.fxml");
    }


        @FXML
    private Text profilText;




    private void switchView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane pane = loader.load();

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
    private void initialize() {

    }
    @FXML
    private StackPane contentArea;
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Charger la page de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml")); // Assure-toi que le chemin est correct
            Parent root = loader.load();

            // Obtenir la scène actuelle et la remplacer par la page de connexion
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'erreur correctement dans un vrai projet
        }
    }
    
}