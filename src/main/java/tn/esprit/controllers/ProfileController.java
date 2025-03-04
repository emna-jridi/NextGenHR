package tn.esprit.controllers;

import tn.esprit.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.services.ServiceUser;
import tn.esprit.utils.SessionManager;

import java.io.IOException;
import java.time.LocalDate;

public class ProfileController {

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
    private Button Back;


    private final ServiceUser userService = new ServiceUser();
    private User loggedInUser;

    @FXML
    public void initialize() {
        loggedInUser = SessionManager.getInstance().getLoggedInUser();

        if (loggedInUser != null) {
            loadUserData();
        }
    }

    private void loadUserData() {
        nom.setText(loggedInUser.getNomUser());
        prenom.setText(loggedInUser.getPrenomUser());
        email.setText(loggedInUser.getEmailUser());
        telephone.setText(loggedInUser.getTelephoneUser());
        adresse.setText(loggedInUser.getAdresseUser());
        mdp.setText(loggedInUser.getPassword());

        if (loggedInUser.getDateNaissanceUser() != null) {
            dateNaissance.setValue(loggedInUser.getDateNaissanceUser());
        }
    }

    @FXML
    private void OnUpdateClicked(ActionEvent event) {
        if (loggedInUser == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non connecté.");
            return;
        }

        String nomInput = nom.getText().trim();
        String prenomInput = prenom.getText().trim();
        String emailInput = email.getText().trim();
        String mdpInput = mdp.getText().trim();
        String adresseInput = adresse.getText().trim();
        String telephoneInput = telephone.getText().trim();
        LocalDate dateNaissanceInput = dateNaissance.getValue();

        if (nomInput.isEmpty() || prenomInput.isEmpty() || emailInput.isEmpty() || mdpInput.isEmpty() || adresseInput.isEmpty() || telephoneInput.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Tous les champs doivent être remplis.");
            return;
        }

        if (!telephoneInput.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le téléphone doit être un nombre valide.");
            return;
        }

        loggedInUser.setNomUser(nomInput);
        loggedInUser.setPrenomUser(prenomInput);
        loggedInUser.setEmailUser(emailInput);
        loggedInUser.setPassword(mdpInput);
        loggedInUser.setAdresseUser(adresseInput);
        loggedInUser.setTelephoneUser(telephoneInput);

        if (dateNaissanceInput != null) {
            loggedInUser.setDateNaissanceUser(dateNaissanceInput);
        }

        try {
            userService.update(loggedInUser);
            showAlert(Alert.AlertType.INFORMATION, "Mise à jour", "Les informations ont été mises à jour avec succès.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la mise à jour.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void GoBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/UsersDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de retourner à la page d'accueil.");
        }
    }
}

