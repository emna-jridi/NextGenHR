package tn.esprit.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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
}
