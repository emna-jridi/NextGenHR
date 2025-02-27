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
import org.mindrot.jbcrypt.BCrypt;

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
        emailField.textProperty().addListener((observable, oldValue, newValue) -> errorLabel.setText(""));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> errorLabel.setText(""));
    }

    @FXML
    private void handleLogin() {
        Logger logger = Logger.getLogger(Main.class.getName());
        String email = this.emailField.getText().trim();
        String password = this.passwordField.getText().trim();
        logger.info("Tentative de connexion avec l'email : " + email);

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs !");
            return;
        }

        String qry = "SELECT isActive, Role, Password FROM user WHERE EmailUser = ?";
        Connection con = DBConnection.getInstance().getCon();

        try (PreparedStatement pstm = con.prepareStatement(qry)) {
            pstm.setString(1, email);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("Password");
                int isActive = rs.getInt("isActive");
                String role = rs.getString("Role");

                // 🔹 Vérification du mot de passe haché
                if (BCrypt.checkpw(password, hashedPassword)) {
                    if (isActive != 0) {
                        System.out.println("✅ Connexion réussie ! Rôle : " + role);
                        if (role.equalsIgnoreCase("ResponsableRH")) {
                            goToDashboard();
                        } else if (role.equalsIgnoreCase("Employe")) {
                            goToHome();
                        } else {
                            errorLabel.setText("❌ Rôle non reconnu !");
                        }
                    } else {
                        errorLabel.setText("❌ Votre compte est désactivé. Contactez l'administration.");
                    }
                } else {
                    errorLabel.setText("❌ Email ou mot de passe incorrect !");
                }
            } else {
                errorLabel.setText("❌ Email ou mot de passe incorrect !");
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la connexion : " + e.getMessage());
            errorLabel.setText("❌ Une erreur est survenue !");
        }
    }

    @FXML
    private void goToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
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

    @FXML
    private void goToDashboard() {
        try {
            System.out.println("🔄 Redirection vers le Dashboard...");
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
