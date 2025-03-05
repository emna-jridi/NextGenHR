package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Service;
import tn.esprit.services.ServiceService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ModifierService {


    @FXML
    private TextField nomServiceField;
    @FXML
    private TextArea descriptionServiceField;
    @FXML
    private TextField typeServiceField;
    @FXML
    private DatePicker dateDebutField;
    @FXML
    private DatePicker dateFinField;
    @FXML
    private RadioButton statusActif;
    @FXML
    private RadioButton statusInactif;

    private final ServiceService serviceService;
    private ListServices listServicesController;
    private Service serviceToModify;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private ToggleGroup statusGroup;

    public ModifierService() {
        serviceService = new ServiceService();
    }

    @FXML
    public void initialize() {

        statusGroup = new ToggleGroup();
        statusActif.setToggleGroup(statusGroup);
        statusInactif.setToggleGroup(statusGroup);
    }




    @FXML
    private void handleCancel() {
        listServicesController.showAjouterServiceForm();
        ((Stage) nomServiceField.getScene().getWindow()).close();
    }




    public void setListServicesController(ListServices controller) {
        this.listServicesController = controller;
    }



//initialiser les champs avec données du service
    public void setService(Service service, ListServices listServicesController) {
        this.serviceToModify = service;


        nomServiceField.setText(service.getNomService());
        descriptionServiceField.setText(service.getDescriptionService());
        typeServiceField.setText(service.getTypeService());
        dateDebutField.setValue(service.getDateDebutService());
        dateFinField.setValue(service.getDateFinService());

        if ("Actif".equals(service.getStatusService())) {
            statusActif.setSelected(true);
        } else {
            statusInactif.setSelected(true);
        }
    }



    //enregistrer les modifications
    @FXML
    private void handleSave() {
        try {

            String nomService = nomServiceField.getText();
            String descriptionService = descriptionServiceField.getText();
            String typeService = typeServiceField.getText();
            LocalDate dateDebut = dateDebutField.getValue();
            LocalDate dateFin = dateFinField.getValue();
            String statusService = statusActif.isSelected() ? "Actif" : "Inactif";

            if (nomService.isEmpty() && descriptionService.isEmpty() && typeService.isEmpty()) {
                System.out.println("Erreur : Tous les champs doivent être remplis !");
                return;
            }

            if (nomService.isEmpty()) {
                showAlert("Erreur", "Le nom de service est obligatoire.");
                return;
            }

            if (descriptionService.isEmpty()) {
                showAlert("Erreur", "La description du service est obligatoire.");
                return;
            }

            if (typeService.isEmpty()) {
                showAlert("Erreur", "Le type du service est obligatoire.");
                return;
            }

            if (dateDebut == null || dateFin == null) {
                System.out.println("Erreur : Les dates doivent être sélectionnées !");
                return;
            }

            if (dateFin.isBefore(dateDebut)) {
                System.out.println("Erreur : La date de fin ne peut pas être avant la date de début !");
                return;
            }

            serviceToModify.setNomService(nomService);
            serviceToModify.setDescriptionService(descriptionService);
            serviceToModify.setTypeService(typeService);
            serviceToModify.setDateDebutService(dateDebut);
            serviceToModify.setDateFinService(dateFin);
            serviceToModify.setStatusService(statusService);

            serviceService.update(serviceToModify);
            showAlert("Succès", "Le service a été mise à jour avec succès.");
            // Appeler la méthode dans ListContrats pour afficher le formulaire AjouterContrat
            listServicesController.showAjouterServiceForm();

            ((Stage) statusInactif.getScene().getWindow()).close();

        } catch (DateTimeParseException e) {
            System.out.println("Erreur : Format de date invalide. Utiliser yyyy-MM-dd !");
        }
    }




    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
