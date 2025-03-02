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
import services.EmailService;
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
    @FXML
    private void handleForgotPassword() {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            errorLabel.setText("Veuillez entrer votre email !");
            return;
        }

        // Vérifier si l'email existe dans la base de données
        String checkQuery = "SELECT * FROM user WHERE EmailUser = ?";
        Connection con = DBConnection.getInstance().getCon();

        try (PreparedStatement pstm = con.prepareStatement(checkQuery)) {
            pstm.setString(1, email);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                // Générer un code de réinitialisation
                String resetCode = generateResetCode();

                // Stocker le code en base (optionnel selon ta logique)
                storeResetCode(email, resetCode);

                // Envoyer l'email avec le code
                sendResetEmail(email, resetCode);

                // Rediriger vers la page de reset
                goTochangepassword();
            } else {
                errorLabel.setText("❌ Email introuvable !");
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println("❌ Erreur : " + e.getMessage());
            errorLabel.setText("❌ Une erreur est survenue !");
        }
    }
    private String generateResetCode() {
        int code = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(code);
    }
    private void storeResetCode(String email, String resetCode) {
        String updateQuery = "UPDATE user SET reset_code = ? WHERE EmailUser = ?";
        Connection con = DBConnection.getInstance().getCon();

        try (PreparedStatement pstm = con.prepareStatement(updateQuery)) {
            pstm.setString(1, resetCode);
            pstm.setString(2, email);
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'enregistrement du code : " + e.getMessage());
        }
    }
    private void sendResetEmail(String email, String resetCode) {
        String subject = "Réinitialisation de votre mot de passe";
        String message = "Bonjour,\n\nVoici votre code de réinitialisation : " + resetCode +
                "\n\nVeuillez l'utiliser pour réinitialiser votre mot de passe.\n\nCordialement,\nL'équipe RH";

        try {
            EmailService.sendEmail(email, subject, message);
            System.out.println("📧 Email envoyé à " + email);
        } catch (Exception e) {
            System.out.println("❌ Erreur d'envoi d'email : " + e.getMessage());
            errorLabel.setText("❌ Impossible d'envoyer l'email !");
        }
    }
    @FXML
    private void goTochangepassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/changepassword.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Réinitialisation du mot de passe");
            stage.show();
        } catch (IOException e) {
            System.out.println("❌ Erreur de redirection : " + e.getMessage());
        }
    }

}
