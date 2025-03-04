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
import org.mindrot.jbcrypt.BCrypt;
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

    private final ServiceUser serviceUser = new ServiceUser();

    @FXML
    private void handleCheckboxSelection(ActionEvent event) {
        CheckBox source = (CheckBox) event.getSource();

        if (source.equals(checkboxEmploye)) {
            checkboxRH.setSelected(false);
        } else if (source.equals(checkboxRH)) {
            checkboxEmploye.setSelected(false);
        }
    }

    @FXML
    private void handleSignUp() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        LocalDate dateNaissance = dateNaissanceField.getValue();
        String adresse = adresseField.getText().trim();
        String telephone = telephoneField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // 1. Vérification des champs obligatoires
        if (nom.isEmpty() || prenom.isEmpty() || dateNaissance == null || adresse.isEmpty() ||
                telephone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Tous les champs sont obligatoires !");
            return;
        }

        // 2. Vérification des mots de passe
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Les mots de passe ne correspondent pas !");
            return;
        }

        // 3. Validation de l'email
        if (!isValidEmail(email)) {
            errorLabel.setText("Email invalide !");
            return;
        }

        // 4. Validation du téléphone
        if (!isValidPhone(telephone)) {
            errorLabel.setText("Téléphone invalide !");
            return;
        }

        // 5. Validation de la longueur du mot de passe
        if (password.length() <= 8) {
            errorLabel.setText("Le mot de passe doit contenir plus de 8 caractères !");
            return;
        }

        // 6. Vérification de la date de naissance
        if (dateNaissance.isAfter(LocalDate.now())) {
            errorLabel.setText("La date de naissance ne peut pas être dans le futur !");
            return;
        }

        // 7. Vérification des rôles
        if (!checkboxEmploye.isSelected() && !checkboxRH.isSelected()) {
            errorLabel.setText("Veuillez sélectionner au moins un rôle !");
            return;
        }

        // Détermination du rôle
        Role role = checkboxEmploye.isSelected() ? Role.EMPLOYE : Role.RESPONSABLE_RH;

        // Hachage du mot de passe avant l'enregistrement
        String hashedPassword = hashPassword(password);

        // Création de l'utilisateur
        User user = new User();
        user.setNomUser(nom);
        user.setPrenomUser(prenom);
        user.setDateNaissanceUser(dateNaissance);
        user.setAdresseUser(adresse);
        user.setTelephoneUser(telephone);
        user.setEmailUser(email);
        user.setPassword(hashedPassword); // On enregistre le mot de passe haché
        user.setRole(role);

        // Ajout dans la base de données
        serviceUser.add(user);

        errorLabel.setText("Inscription réussie !");
        goToLogin();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        String phoneRegex = "^\\d{8}$";
        return Pattern.compile(phoneRegex).matcher(phone).matches();
    }

    // 🔹 Méthode pour hasher un mot de passe avec BCrypt
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12)); // Coût de 12 pour un bon équilibre entre sécurité et performance
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
