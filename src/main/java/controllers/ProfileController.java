package controllers;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.ServiceUser;
import utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField email;
    @FXML
    private TextField telephone;
    @FXML
    private TextField adresse;
    @FXML
    private DatePicker dateNaissance;
    @FXML
    private PasswordField mdp;
    @FXML
    Button btnMettreAJour;

    private final ServiceUser userService = new ServiceUser();
    private User loggedInUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loggedInUser = SessionManager.getInstance().getLoggedInUser();

        if (loggedInUser != null) {
            System.out.println("Utilisateur chargé: " + loggedInUser.getNomUser());
            loadUserData();
        } else {
            System.out.println("Aucun utilisateur trouvé dans la session !");
        }
    }

    private void loadUserData() {
        // Vérifie si les valeurs sont bien récupérées
        System.out.println("Chargement des données de l'utilisateur...");

        nom.setText(loggedInUser.getNomUser());
        prenom.setText(loggedInUser.getPrenomUser());
        email.setText(loggedInUser.getEmailUser());
        telephone.setText(loggedInUser.getTelephoneUser());
        adresse.setText(loggedInUser.getAdresseUser());

        // Ne pas afficher le mot de passe pour des raisons de sécurité
        mdp.setText("");

        if (loggedInUser.getDateNaissanceUser() != null) {
            dateNaissance.setValue(loggedInUser.getDateNaissanceUser());
            System.out.println("Date de naissance chargée : " + loggedInUser.getDateNaissanceUser());
        }
    }

    @FXML
    private void OnUpdateClicked(ActionEvent event) {
        if (loggedInUser != null) {
            // Vérification des champs obligatoires
            if (nom.getText().isEmpty() || prenom.getText().isEmpty() || email.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            // Vérification de l'email
            if (!email.getText().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer une adresse email valide.");
                return;
            }

            // Vérification du téléphone (uniquement des chiffres, longueur 8-15 caractères)
            if (!telephone.getText().matches("\\d{8,15}")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit contenir entre 8 et 15 chiffres.");
                return;
            }

            // Mise à jour des informations de l'utilisateur
            loggedInUser.setNomUser(nom.getText());
            loggedInUser.setPrenomUser(prenom.getText());
            loggedInUser.setEmailUser(email.getText());
            loggedInUser.setAdresseUser(adresse.getText());
            loggedInUser.setTelephoneUser(telephone.getText());

            // Mise à jour de la date de naissance
            if (dateNaissance.getValue() != null) {
                loggedInUser.setDateNaissanceUser(dateNaissance.getValue());
            }

            // Mise à jour du mot de passe uniquement si un nouveau est entré
            if (!mdp.getText().isEmpty()) {
                loggedInUser.setPassword(mdp.getText());
            }

            // Vérification du rôle (par défaut : Employé si non défini)
            if (loggedInUser.getRole() == null) {
                loggedInUser.setRole(User.Role.valueOf("Employe"));
            }

            // Debugging : Afficher l'ID utilisateur avant mise à jour
            System.out.println("Mise à jour de l'utilisateur ID: " + loggedInUser.getIdUser() + " | Role: " + loggedInUser.getRole());

            // Mise à jour en base de données
            boolean success = userService.update(loggedInUser);

            if (success) {
                // 🔹 Mettre à jour l'utilisateur dans la session
                SessionManager.getInstance().setLoggedInUser(loggedInUser);
                System.out.println("Mise à jour réussie !");
                showAlert(Alert.AlertType.INFORMATION, "Mise à jour", "Les informations ont été mises à jour avec succès.");
            } else {
                System.out.println("Échec de la mise à jour !");
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour des informations.");
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
