package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.event.ActionEvent;

import javafx.scene.Node;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


import java.io.IOException;

public class HomeController {

    @FXML
    private Button btnGestionSalaires;

    @FXML
    private Button btnGestionRecrutement;

    @FXML
    private Button btnGestionCompetences;

    @FXML
    private Button btnGestionConges;

    @FXML
    private Button btnGestionBienEtre;

    @FXML
    private Text profilText;

    @FXML
    private void initialize() {

    }
    @FXML
    private StackPane contentArea;

    @FXML
    private void handleProfile() {
        try {
            // Charger le fichier FXML du profil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent profileView = loader.load();

            // Ajouter la vue du profil dans la zone centrale
            contentArea.getChildren().clear();
            contentArea.getChildren().add(profileView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
