package tn.esprit.Controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;

public class MainViewController {


        @FXML
        private AnchorPane contentPane;

        @FXML
        private void showOffre() {
            switchView("/AjouterOffre.fxml");
        }

        @FXML
        private void showSecondView() {
            switchView("/Candidature.fxml");
        }

       private void switchView(String fxmlPath) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                AnchorPane pane = loader.load();

                // Animation de transition
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


