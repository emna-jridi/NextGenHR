package controllers;

import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import test.Main;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import utils.DBConnection;

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
        final Logger logger = Logger.getLogger(Main.class.getName());
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        logger.info("Tentative de connexion avec l'email : " + email);

        // Vérification des champs vides
        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs !");
            return;
        }

        String qry = "SELECT * FROM user WHERE EmailUser = ? AND Password = ?";
        Connection con = DBConnection.getInstance().getCon();
        try {
            PreparedStatement pstm = con.prepareStatement(qry);
            pstm.setString(1, email);
            pstm.setString(2, password);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) { // Si un utilisateur est trouvé
                String role = rs.getString("Role"); // Récupère le rôle de l'utilisateur
                System.out.println("✅ Connexion réussie ! Rôle : " + role);

                // Redirection selon le rôle
                if (role.equalsIgnoreCase("ResponsableRH")) {
                    goToDashboard(); // Redirection vers le dashboard
                } else if (role.equalsIgnoreCase("Employe")) {
                   goToHome(); // Redirection vers la Home Page
                } else {
                    errorLabel.setText("❌ Rôle non reconnu !");
                }
            } else {
                errorLabel.setText("❌ Email ou mot de passe incorrect !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la connexion : " + e.getMessage());
            errorLabel.setText("❌ Une erreur est survenue !");
        }
    }

    // Méthode pour naviguer vers Home.fxml pour l'employé
   @FXML
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

    // Méthode pour naviguer vers SignUp.fxml
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

    // Méthode pour naviguer vers dashboard.fxml pour responsable-rh
    @FXML
    private void goToDashboard() {
        try {
            System.out.println("🔄 Redirection vers le Dashboard..."); // Débug
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



