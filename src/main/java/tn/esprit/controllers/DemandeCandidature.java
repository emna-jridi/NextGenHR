package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.models.Candidature;
import tn.esprit.models.Offreemploi;
import tn.esprit.models.Statut;
import tn.esprit.services.ServiceCandidature;
import tn.esprit.services.ServiceOffre;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class DemandeCandidature {


    @FXML
    private DatePicker dateCandidature;

    @FXML
    private Button importCV;

    @FXML
    private Button importLettre;

    @FXML
    private ComboBox<Offreemploi> listeoffres;

    @FXML
    private TextField txtCv;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtLettre;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private TextField txtTelephone;
    @FXML
    private Label alertemail;
    @FXML
    private Label labeltelephone;






    @FXML
    void selectionner(ActionEvent event) {

    }

    private final ServiceCandidature serviceCandidature = new ServiceCandidature();
    private final ServiceOffre serviceOffre = new ServiceOffre();


    @FXML
    public void initialize() {
        txtTelephone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtTelephone.setText(oldValue);
            }

            if (newValue.length() != 8) {
                txtTelephone.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-background-color: #ffe6e6;");
                labeltelephone.setText("Le numéro doit contenir exactement 8 chiffres !");
                labeltelephone.setTextFill(javafx.scene.paint.Color.RED);
                labeltelephone.setVisible(true);
            } else {
                txtTelephone.setStyle("-fx-border-color: green; -fx-border-width: 2px; -fx-background-color: #e6ffe6;");
                labeltelephone.setText("Numéro valide !");
                labeltelephone.setTextFill(javafx.scene.paint.Color.GREEN);
                labeltelephone.setVisible(true);
            }
        });

        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                txtEmail.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-background-color: #ffe6e6;");
                alertemail.setText("Email invalide ! Format attendu : xxxx@xxx.xx");
                alertemail.setTextFill(javafx.scene.paint.Color.RED);
                alertemail.setVisible(true);
            } else {
                txtEmail.setStyle("-fx-border-color: green; -fx-border-width: 2px; -fx-background-color: #e6ffe6;");
                alertemail.setText("Email valide !");
                alertemail.setTextFill(javafx.scene.paint.Color.GREEN);
                alertemail.setVisible(true);
            }
        });

        loadOffres();
    }



    void loadOffres() {
        List<Offreemploi> offres = serviceOffre.getAll();
        ObservableList<Offreemploi> offresAffichees = FXCollections.observableArrayList(offres);
        listeoffres.setItems(offresAffichees);
    }


    @FXML
    void enregistrer(ActionEvent event) {

            if (!txtEmail.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText(null);
                alert.setContentText("L'email saisi est invalide. Veuillez entrer un email au format xxxx@xxx.xx");
                alert.showAndWait();
                return;
            }
        Offreemploi selectedOffre = listeoffres.getValue();

        if (selectedOffre != null) {

            Candidature candidature = new Candidature();
            candidature.setDateCandidature(LocalDateTime.now());
            candidature.setCvUrl(txtCv.getText());
            candidature.setLettreMotivation(txtLettre.getText());
            candidature.setNom(txtNom.getText());
            candidature.setPrenom(txtPrenom.getText());
            candidature.setEmail(txtEmail.getText());
            candidature.setTelephone(txtTelephone.getText());
            candidature.setOffreemploi(selectedOffre);
            candidature.setStatut(Statut.En_cours);
            serviceCandidature.ajouter(candidature);
            loadOffres();
            candidature.setOffreemploi(serviceOffre.getbyid((selectedOffre.getId())));


            showAlert(Alert.AlertType.CONFIRMATION,"Succès","Votre candidature a été ajoutée avec succès");

        } else {

            System.out.println("Veuillez sélectionner une offre.");
        }
        clearFields();
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void clearFields() {
        txtCv.clear();
        dateCandidature.setValue(null);
        txtEmail.clear();
        txtLettre.clear();
        txtNom.clear();
        txtPrenom.clear();
        txtTelephone.clear();
        labeltelephone.setVisible(false);
        alertemail.setVisible(false);
        txtEmail.setStyle(null);
  txtTelephone.setStyle(null);
    }



    @FXML
    void importerCV(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tous les fichiers", "*.*"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            txtCv.setText(selectedFile.getAbsolutePath());
            System.out.println("Fichier sélectionné : " + selectedFile.getAbsolutePath());
        }
    }


    @FXML
    void importerLettre(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documents Word", "*.docx", "*.doc"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tous les fichiers", "*.*"));

        Stage stage = (Stage) importLettre.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            txtLettre.setText(selectedFile.getAbsolutePath());
            System.out.println("Fichier Lettre de Motivation sélectionné : " + selectedFile.getAbsolutePath());
        }
    }}



