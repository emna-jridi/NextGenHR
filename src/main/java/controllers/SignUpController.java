package controllers;

import entities.User;
import entities.User.Role;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.ServiceUser;

import java.io.IOException;
import java.time.LocalDate;

public class SignUpController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private DatePicker dateNaissanceField;
    @FXML
    private TextField adresseField;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private CheckBox checkboxEmploye;
    @FXML
    private CheckBox checkboxRH;
    @FXML
    private Label errorLabel;

    // 🔥 Instance du Service pour le CRUD
    private final ServiceUser serviceUser = new ServiceUser();

    @FXML
    private void handleSignUp() {
        // 1. Récupérer les données
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        LocalDate dateNaissance = dateNaissanceField.getValue();
        String adresse = adresseField.getText();
        String telephone = telephoneField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // 2. Vérification des champs obligatoires
        if (nom.isEmpty() || prenom.isEmpty() || dateNaissance == null || adresse.isEmpty() ||
                telephone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Tous les champs sont obligatoires !");
            return;
        }

        // 3. Vérification des mots de passe
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Les mots de passe ne correspondent pas !");
            return;
        }

        // 4. Vérification des rôles
        if (!checkboxEmploye.isSelected() && !checkboxRH.isSelected()) {
            errorLabel.setText("Veuillez sélectionner au moins un rôle !");
            return;
        }

        // 5. Détermination du rôle
        Role role = Role.EMPLOYE; // Par défaut
        if (checkboxRH.isSelected()) {
            role = Role.RESPONSABLE_RH;
        }

        // 6. Création de l'utilisateur
        User user = new User();
        user.setNomUser(nom);
        user.setPrenomUser(prenom);
        user.setDateNaissanceUser(dateNaissance);
        user.setAdresseUser(adresse);
        user.setTelephoneUser(telephone);
        user.setEmailUser(email);
        user.setPassword(password);
        user.setRole(role);

        // 7. Ajout dans la base de données via le Service
        serviceUser.add(user);

        // 8. Confirmation et redirection
        errorLabel.setText("Inscription réussie !");
        goToLogin();
    }

    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
