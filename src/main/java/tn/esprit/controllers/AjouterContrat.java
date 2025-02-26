package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import tn.esprit.models.Contrat;
import tn.esprit.models.Service;
import tn.esprit.models.TypeContrat;
import tn.esprit.services.ServiceContrat;
import tn.esprit.services.ServiceService;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AjouterContrat {

    @FXML
    private ComboBox<TypeContrat> comboTypeContrat;

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
    private RadioButton radioActif;

    @FXML
    private RadioButton radioInactif;

    @FXML
    private ToggleGroup statusGroup;

    @FXML
    private Label emailValidationLabel;


    /*@FXML
    private CheckComboBox<Service> checkComboBoxServices;*/


    private ServiceContrat serviceContrat = new ServiceContrat();
    private ServiceService serviceService = new ServiceService();


    private Runnable onContratAdded;


    public void setOnContratAdded(Runnable onContratAdded) {
        this.onContratAdded = onContratAdded;
    }


    @FXML
    void initialize() {
// Remplir le ComboBox avec les valeurs de l'enum TypeContrat
        comboTypeContrat.getItems().setAll(TypeContrat.values());

        statusGroup = new ToggleGroup();
        radioActif.setToggleGroup(statusGroup);
        radioInactif.setToggleGroup(statusGroup);

        //loadServices();

        // Définir un converter pour afficher uniquement le nom du service
        /*checkComboBoxServices.setConverter(new StringConverter<Service>() {
            @Override
            public String toString(Service service) {
                return service == null ? "" : service.getNomService();
            }

            @Override
            public Service fromString(String string) {
                return null;
            }
        });*/



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
    }



    /*private void loadServices() {
        List<Service> services = serviceService.getAll();
        ObservableList<Service> serviceList = FXCollections.observableArrayList(services);
        checkComboBoxServices.getItems().setAll(serviceList);
    }*/


    @FXML
    void ajouter(ActionEvent event) {

        TypeContrat type = comboTypeContrat.getValue();
        String montantStr = montantContrat.getText();
        String nom = nomClient.getText();
        String email = emailClient.getText();
        String status = radioActif.isSelected() ? "Actif" : "Inactif";

        if (type == null && montantStr.isEmpty() && nom.isEmpty() && email.isEmpty() &&
                dateDebutContrat.getValue() == null && dateFinContrat.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir les champs svp.");
            return;
        }

        if (type == null) {
            showAlert("Erreur", "Le type du contrat est obligatoire.");
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

        if (!isValidEmail(email)) {
            showAlert("Erreur", "L'email est au format incorrect.");
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

        int montant = 0;
        try {
            montant = Integer.parseInt(montantStr);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le montant doit être un nombre valide.");
            return;
        }

        Contrat contrat = new Contrat(type, dateDebutContrat.getValue(), dateFinContrat.getValue(), status, montant, nom, email);

        serviceContrat.add(contrat);

        if (onContratAdded != null) {
            onContratAdded.run();
        }

        showAlert("Succès", "Le contrat a été ajouté avec succès.");

        closeWindow();
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

    private void closeWindow() {
        ((Stage) comboTypeContrat.getScene().getWindow()).close();
    }
}
