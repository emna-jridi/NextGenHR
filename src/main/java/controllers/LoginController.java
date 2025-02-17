package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {
        // Efface le message d'erreur lorsque l'utilisateur commence à taper
        emailField.textProperty().addListener((observable, oldValue, newValue) -> errorLabel.setText(""));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> errorLabel.setText(""));
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Vérification des champs vides
        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs !");
            return;
        }

        // Validation des identifiants
        if (email.equals("admin@example.com") && password.equals("admin123")) {
            System.out.println("Connexion réussie !");
            goToHome(); // Navigation vers une autre page après connexion
        } else {
            errorLabel.setText("Email ou mot de passe incorrect !");
        }
    }

    // Méthode pour naviguer vers Home.fxml après connexion réussie
    private void goToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow(); // Récupère la fenêtre actuelle
            stage.setScene(new Scene(root)); // Change la scène
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignUp.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
