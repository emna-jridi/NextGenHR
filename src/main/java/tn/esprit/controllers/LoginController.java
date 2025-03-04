package tn.esprit.controllers;

import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.esprit.models.ROLE;
import tn.esprit.models.SessionManager;
import tn.esprit.models.User;
import tn.esprit.test.main;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import tn.esprit.utils.MyDatabase;

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
        Logger logger = Logger.getLogger(main.class.getName());
        String email = this.emailField.getText().trim();
        String password = this.passwordField.getText().trim();
        logger.info("Tentative de connexion avec l'email : " + email);

        // Vérification si les champs email et mot de passe sont remplis
        if (!email.isEmpty() && !password.isEmpty()) {
            String qry = "SELECT ID_User, NomUser, PrenomUser, EmailUser, TelephoneUser, role, isActive FROM user WHERE EmailUser = ? AND Password = ?";

            try (Connection con = MyDatabase.getInstance().getCnx();
                 PreparedStatement pstm = con.prepareStatement(qry)) {

                pstm.setString(1, email);
                pstm.setString(2, password);
                try (ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        int isActive = rs.getInt("isActive");
                        if (isActive != 0) {
                            ROLE role = ROLE.valueOf(rs.getString("role")); // Convertir String en Enum

                            int idUser = rs.getInt("ID_User");
                            String nomUser = rs.getString("NomUser");
                            String prenomUser = rs.getString("PrenomUser");
                            String emailUser = rs.getString("EmailUser");
                            String telephoneUser = rs.getString("TelephoneUser");// Récupération de l'ID de l'utilisateur

                            // Création de l'objet User et stockage dans SessionManager
                            User user = new User(idUser, nomUser, prenomUser, emailUser, telephoneUser, role, isActive != 0);

                            SessionManager.setConnectedUser(user);
                            System.out.println("Utilisateur connecté : " + user); // Enregistrement de l'utilisateur dans la session

                            // Log de l'utilisateur connecté
                            logger.info("Utilisateur connecté : " + user.getEmailUser() + " avec rôle : " + role);

                            // Vérifier que l'utilisateur a bien été enregistré dans la session
                            User connectedUser = SessionManager.getConnectedUser();
                            if (connectedUser != null) {
                                logger.info("Utilisateur dans la session : " + connectedUser);
                            } else {
                                logger.warning("Aucun utilisateur trouvé dans la session !");
                            }

                            System.out.println("✅ Connexion réussie ! Rôle : " + role);
                            navigateBasedOnRole(role.name()); // Naviguer en fonction du rôle
                        } else {
                            this.errorLabel.setText("❌ Votre compte est désactivé. Contactez l'administration.");
                        }
                    } else {
                        this.errorLabel.setText("❌ Email ou mot de passe incorrect !");
                    }
                }
            } catch (SQLException e) {
                System.out.println("❌ Erreur lors de la connexion : " + e.getMessage());
                this.errorLabel.setText("❌ Une erreur est survenue !");
            }
        } else {
            this.errorLabel.setText("Veuillez remplir tous les champs !");
        }
    }


    private void navigateBasedOnRole(String role) {
        switch (role.toLowerCase()) {
            case "responsablerh":
                this.goToDashboard();
                break;
            case "employe":
                this.goToHome();
                break;
            case "candidat":
                this.goToDashCndidat();
                break;
            default:
                this.errorLabel.setText("❌ Rôle non reconnu !");
        }
    }

    // Méthodes pour naviguer vers les différentes vues
    @FXML
    private void goToHome() {
        navigateTo("/MainViewEm.fxml");
    }

    @FXML
    private void goToSignUp() {
        navigateTo("/SignUp.fxml");
    }

    @FXML
    private void goToDashCndidat() {
        navigateTo("/OFFRECandidat.fxml");
    }

    @FXML
    private void goToDashboard() {
        navigateTo("/MainViewRH.fxml");
    }

    private void navigateTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
