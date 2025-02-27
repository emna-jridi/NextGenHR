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
        Logger logger = Logger.getLogger(Main.class.getName());
        String email = this.emailField.getText().trim();
        String password = this.passwordField.getText().trim();
        logger.info("Tentative de connexion avec l'email : " + email);
        if (!email.isEmpty() && !password.isEmpty()) {
            String qry = "SELECT isActive, Role FROM user WHERE EmailUser = ? AND Password = ?";
            Connection con = DBConnection.getInstance().getCon();

            try {
                PreparedStatement pstm = con.prepareStatement(qry);

                label106: {
                    try {
                        pstm.setString(1, email);
                        pstm.setString(2, password);
                        ResultSet rs = pstm.executeQuery();

                        label99: {
                            try {
                                if (!rs.next()) {
                                    this.errorLabel.setText("❌ Email ou mot de passe incorrect !");
                                    break label99;
                                }

                                int isActive = rs.getInt("isActive");
                                System.out.println("Valeur de isActive pour l'email '" + email + "': " + isActive);
                                if (isActive != 0) {
                                    String role = rs.getString("Role");
                                    System.out.println("✅ Connexion réussie ! Rôle : " + role);
                                    if (role.equalsIgnoreCase("ResponsableRH")) {
                                        this.goToDashboard();
                                    } else if (role.equalsIgnoreCase("Employe")) {
                                        this.goToHome();
                                    } else {
                                        this.errorLabel.setText("❌ Rôle non reconnu !");
                                    }
                                    break label99;
                                }

                                this.errorLabel.setText("❌ Votre compte est désactivé. Contactez l'administration.");
                            } catch (Throwable var12) {
                                if (rs != null) {
                                    try {
                                        rs.close();
                                    } catch (Throwable var11) {
                                        var12.addSuppressed(var11);
                                    }
                                }

                                throw var12;
                            }

                            if (rs != null) {
                                rs.close();
                            }
                            break label106;
                        }

                        if (rs != null) {
                            rs.close();
                        }
                    } catch (Throwable var13) {
                        if (pstm != null) {
                            try {
                                pstm.close();
                            } catch (Throwable var10) {
                                var13.addSuppressed(var10);
                            }
                        }

                        throw var13;
                    }

                    if (pstm != null) {
                        pstm.close();
                    }

                    return;
                }

                if (pstm != null) {
                    pstm.close();
                }

            } catch (SQLException var14) {
                SQLException e = var14;
                System.out.println("❌ Erreur lors de la connexion : " + e.getMessage());
                this.errorLabel.setText("❌ Une erreur est survenue !");
            }
        } else {
            this.errorLabel.setText("Veuillez remplir tous les champs !");
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



