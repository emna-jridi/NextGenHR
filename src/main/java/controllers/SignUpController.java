package controllers;

import entities.User;
import entities.User.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.ServiceUser;

import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Pattern;

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

    /**
     * Méthode pour gérer la sélection des CheckBox
     */
    @FXML
    private void handleCheckboxSelection(ActionEvent event) {
        CheckBox source = (CheckBox) event.getSource();

        // Si Employé est sélectionné, désélectionner RH et vice versa
        if (source.equals(checkboxEmploye)) {
            checkboxRH.setSelected(false);
        } else if (source.equals(checkboxRH)) {
            checkboxEmploye.setSelected(false);
        }
    }

    /**
     * Méthode pour gérer l'inscription
     */
    @FXML
    private void handleSignUp() {
        // 1. Récupérer les données
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        LocalDate dateNaissance = dateNaissanceField.getValue();
        String adresse = adresseField.getText().trim();
        String telephone = telephoneField.getText().trim();
        String email = emailField.getText().trim();
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

        // 4. Validation de l'email
        if (!isValidEmail(email)) {
            errorLabel.setText("Email invalide !");
            return;
        }

        // 5. Validation du téléphone
        if (!isValidPhone(telephone)) {
            errorLabel.setText("Téléphone invalide !");
            return;
        }

        // 6. Vérification des rôles
        if (!checkboxEmploye.isSelected() && !checkboxRH.isSelected()) {
            errorLabel.setText("Veuillez sélectionner au moins un rôle !");
            return;
        }

        // 7. Détermination du rôle
        Role role = checkboxEmploye.isSelected() ? Role.EMPLOYE : Role.RESPONSABLE_RH;

        // 8. Création de l'utilisateur
        User user = new User();
        user.setNomUser(nom);
        user.setPrenomUser(prenom);
        user.setDateNaissanceUser(dateNaissance);
        user.setAdresseUser(adresse);
        user.setTelephoneUser(telephone);
        user.setEmailUser(email);
        user.setPassword(password);
        user.setRole(role);

        // 9. Ajout dans la base de données via le Service
        serviceUser.add(user);

        // 10. Confirmation et redirection
        errorLabel.setText("Inscription réussie !");
        goToLogin();
    }

    /**
     * Validation de l'email avec une expression régulière
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    /**
     * Validation du téléphone (Exemple : Numéro à 10 chiffres)
     */
    private boolean isValidPhone(String phone) {
        String phoneRegex = "^\\d{8}$";
        return Pattern.compile(phoneRegex).matcher(phone).matches();
    }

    /**
     * Redirection vers la page de connexion
     */
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
