package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import services.ServiceUser;

import java.io.IOException;

public class ChangePasswordController {

    @FXML
    private TextField verificationCodeField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorLabel;

    private final ServiceUser serviceUser = new ServiceUser();

    @FXML
    private void handleChangePassword(ActionEvent event) {
        String code = verificationCodeField.getText().trim();
        String password = newPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        System.out.println("Code entré par l'utilisateur : " + code);

        if (code.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Tous les champs sont requis.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Les mots de passe ne correspondent pas.");
            return;
        }

        if (password.length() < 6) {
            errorLabel.setText("Le mot de passe doit contenir au moins 6 caractères.");
            return;
        }

        if (serviceUser.verifyResetCode(code)) {
            boolean success = serviceUser.updatePassword(code, password);
            if (success) {
                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setText("Mot de passe changé avec succès. Vous pouvez vous connecter.");
            } else {
                errorLabel.setText("Erreur lors du changement de mot de passe. Veuillez réessayer.");
            }
        } else {
            errorLabel.setText("Code de vérification invalide.");
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Erreur lors du chargement de la page de connexion.");
        }
    }
}
