package tn.esprit.controllers;

import tn.esprit.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.services.ServiceUser;
import tn.esprit.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelephone;
    @FXML
    private PasswordField txtMotDePasse;
    @FXML
    private DatePicker dateNaissance;
    @FXML
    private Button btnMettreAJour;

    private final ServiceUser userService = new ServiceUser();
    private User loggedInUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loggedInUser = SessionManager.getInstance().getLoggedInUser();
        if (loggedInUser != null) {
            loadUserData();
        }
    }

    private void loadUserData() {
        txtNom.setText(loggedInUser.getNomUser());
        txtPrenom.setText(loggedInUser.getPrenomUser());
        txtEmail.setText(loggedInUser.getEmailUser());
        txtTelephone.setText(loggedInUser.getTelephoneUser());
        txtMotDePasse.setText(loggedInUser.getPassword());

        if (loggedInUser.getDateNaissanceUser() != null) {
            dateNaissance.setValue(loggedInUser.getDateNaissanceUser());
        }
    }

    @FXML
    private void mettreAJourUtilisateur(ActionEvent event) {
        if (loggedInUser != null) {
            loggedInUser.setNomUser(txtNom.getText());
            loggedInUser.setPrenomUser(txtPrenom.getText());
            loggedInUser.setEmailUser(txtEmail.getText());
            loggedInUser.setPassword(txtMotDePasse.getText());

            try {
                loggedInUser.setTelephoneUser(txtTelephone.getText());
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le téléphone doit être un nombre valide.");
                return;
            }

            LocalDate selectedDate = dateNaissance.getValue();
            if (selectedDate != null) {
                loggedInUser.setDateNaissanceUser(selectedDate);
            }

            userService.update(loggedInUser);
            showAlert(Alert.AlertType.INFORMATION, "Mise à jour réussie", "Les informations ont été mises à jour avec succès.");
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
