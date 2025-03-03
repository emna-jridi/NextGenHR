package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import tn.esprit.models.Contrat;
import tn.esprit.models.Service;
import tn.esprit.services.ServiceContrat;
import tn.esprit.services.ServiceService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AjouterContrat {

    @FXML
    private DatePicker dateDebutContrat;

    @FXML
    private DatePicker dateFinContrat;

    @FXML
    private TextField montantContrat;

    @FXML
    private TextField nomClient;

    @FXML
    private TextField emailClient;

    @FXML
    private TextField numTelClient;

    @FXML
    private Label numTelValidationLabel;


    @FXML
    private RadioButton radioActif;

    @FXML
    private RadioButton radioInactif;

    @FXML
    private ToggleGroup statusGroup;

    @FXML
    private Label emailValidationLabel;

    @FXML
    private Label montantValidationLabel;



    @FXML
    private CheckComboBox<Service> checkComboBoxServices;


    private ServiceContrat serviceContrat = new ServiceContrat();
    private ServiceService serviceService = new ServiceService();


    private Runnable onContratAdded;


    public void setOnContratAdded(Runnable onContratAdded) {
        this.onContratAdded = onContratAdded;
    }


    @FXML
    void initialize() {

        statusGroup = new ToggleGroup();
        radioActif.setToggleGroup(statusGroup);
        radioInactif.setToggleGroup(statusGroup);

        loadServices();

        // Définir un converter pour afficher uniquement le nom du service
        checkComboBoxServices.setConverter(new StringConverter<Service>() {
            @Override
            public String toString(Service service) {
                return service == null ? "" : service.getNomService();
            }

            @Override
            public Service fromString(String string) {
                return null;
            }
        });


//écouteur de validation de email
        emailClient.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                emailValidationLabel.setText("");
            } else if (isValidEmail(newValue)) {
                emailValidationLabel.setText("Email valide");
                emailValidationLabel.setStyle("-fx-text-fill: #71e071;");
            } else {
                emailValidationLabel.setText("Email invalide");
                emailValidationLabel.setStyle("-fx-text-fill: #dc5b5b;");
            }
        });


//écouteur de validation de num Tel
        numTelClient.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                numTelValidationLabel.setText("");
            } else if (!newValue.matches("\\d*")) {
                numTelValidationLabel.setText("Le numéro ne doit contenir que des chiffres.");
                numTelValidationLabel.setStyle("-fx-text-fill: #dc5b5b;");
            } else if (newValue.length() < 8) {
                numTelValidationLabel.setText("Le numéro doit contenir exactement 8 chiffres.");
                numTelValidationLabel.setStyle("-fx-text-fill: #dc5b5b;");
            } else if (newValue.length() == 8) {
                numTelValidationLabel.setText("Numéro valide");
                numTelValidationLabel.setStyle("-fx-text-fill: #71e071;");
            } else {
                numTelValidationLabel.setText("Le numéro ne doit pas dépasser 8 chiffres.");
                numTelValidationLabel.setStyle("-fx-text-fill: #dc5b5b;");
            }
        });

//écouteur de validation du montant
        montantContrat.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                montantValidationLabel.setText("");
            } else if (!newValue.matches("\\d+")) { // Vérifie que ce sont uniquement des chiffres
                montantValidationLabel.setText("Le montant doit contenir uniquement des chiffres.");
                montantValidationLabel.setStyle("-fx-text-fill: #dc5b5b;");
            } else if (Integer.parseInt(newValue) <= 0) { // Vérifie que le montant est strictement positif
                montantValidationLabel.setText("Le montant doit être supérieur à 0.");
                montantValidationLabel.setStyle("-fx-text-fill: #dc5b5b;");
            } else {
                montantValidationLabel.setText("Montant valide");
                montantValidationLabel.setStyle("-fx-text-fill: #71e071;");
            }
        });


    }




    @FXML
    void clearFields(ActionEvent event) {
        dateDebutContrat.setValue(null);
        dateFinContrat.setValue(null);
        montantContrat.clear();
        nomClient.clear();
        emailClient.clear();
        numTelClient.clear();
        radioActif.setSelected(true);
        radioInactif.setSelected(false);



        // Réinitialiser les labels de validation
        emailValidationLabel.setText("");
        numTelValidationLabel.setText("");
        montantValidationLabel.setText("");
    }




    private void loadServices() {
        List<Service> services = serviceService.getAll();
        ObservableList<Service> serviceList = FXCollections.observableArrayList(services);
        checkComboBoxServices.getItems().setAll(serviceList);
    }


    @FXML
    void ajouter(ActionEvent event) {

        String montantStr = montantContrat.getText();
        String nom = nomClient.getText();
        String email = emailClient.getText();
        String telClient = numTelClient.getText();
        String status = radioActif.isSelected() ? "Actif" : "Inactif";

        if (montantStr.isEmpty() && nom.isEmpty() && email.isEmpty() && telClient.isEmpty() &&
                dateDebutContrat.getValue() == null && dateFinContrat.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir les champs svp.");
            return;
        }

        if (montantStr.isEmpty()) {
            showAlert("Erreur", "Le montant du contrat est obligatoire.");
            return;
        }

        if (nom.isEmpty()) {
            showAlert("Erreur", "Le nom du client est obligatoire.");
            return;
        }

        if (email.isEmpty()) {
            showAlert("Erreur", "L'email du client est obligatoire.");
            return;
        }

        if (telClient.isEmpty()) {
            showAlert("Erreur", "Le numéro de téléphone de client est obligatoire.");
            return;
        }

        if (!telClient.matches("\\d{8}")) {
            showAlert("Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres.");
            return;
        }


        if (!isValidEmail(email)) {
            showAlert("Erreur", "L'email est au format incorrect.");
            return;
        }

        if (!montantStr.matches("\\d+")) {
            showAlert("Erreur", "Le montant doit contenir uniquement des chiffres.");
            return;
        }

        if (dateDebutContrat.getValue() == null) {
            showAlert("Erreur", "La date de début du contrat est obligatoire.");
            return;
        }

        if (dateFinContrat.getValue() == null) {
            showAlert("Erreur", "La date de fin du contrat est obligatoire.");
            return;
        }

        if (dateDebutContrat.getValue().isAfter(dateFinContrat.getValue())) {
            showAlert("Erreur", "La date de début ne peut pas être après la date de fin.");
            return;
        }

        int montant = Integer.parseInt(montantStr);

        if (montant <= 0) {
            showAlert("Erreur", "Le montant doit être strictement supérieur à 0.");
            return;
        }

        Contrat contrat = new Contrat(dateDebutContrat.getValue(), dateFinContrat.getValue(), status, montant, nom, email, telClient);

        // Récupérer les services sélectionnés
        ObservableList<Service> selectedServices = checkComboBoxServices.getCheckModel().getCheckedItems();
        List<Service> servicesList = selectedServices.stream().collect(Collectors.toList());
        contrat.setServices(servicesList);

        serviceContrat.add(contrat);

        if (onContratAdded != null) {
            onContratAdded.run();
        }

        showAlert("Succès", "Le contrat a été ajouté avec succès.");

        // Réinitialisation des champs du formulaire
        resetForm();

        //closeWindow();
    }


    private void resetForm() {
        // Réinitialisation des TextField
        montantContrat.clear();
        nomClient.clear();
        emailClient.clear();
        numTelClient.clear();

        // Réinitialisation des DatePicker
        dateDebutContrat.setValue(null);
        dateFinContrat.setValue(null);

        // Réinitialisation des RadioButton
        radioActif.setSelected(false);
        radioInactif.setSelected(false);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    /*private void closeWindow() {
        ((Stage) comboTypeContrat.getScene().getWindow()).close();
    }*/
}
