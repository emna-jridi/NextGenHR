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
    private TextField idServiceField;
    @FXML
    private TextField nomServiceField;
    @FXML
    private TextField descriptionServiceField;
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
    private Service serviceToModify;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private ToggleGroup statusGroup;

    public ModifierService() {
        serviceService = new ServiceService(); // Initialisation du service
    }

    @FXML
    public void initialize() {
        // Création du ToggleGroup pour le statut
        statusGroup = new ToggleGroup();
        statusActif.setToggleGroup(statusGroup);
        statusInactif.setToggleGroup(statusGroup);
    }

    // Méthode pour passer les données du service à la fenêtre
    public void setService(Service service) {
        this.serviceToModify = service;

        // Pré-remplir les champs avec les données du service
        idServiceField.setText(String.valueOf(service.getIdService()));
        nomServiceField.setText(service.getNomService());
        descriptionServiceField.setText(service.getDescriptionService());
        typeServiceField.setText(service.getTypeService());
        dateDebutField.setValue(service.getDateDebutService());
        dateFinField.setValue(service.getDateFinService());

        // Sélectionner le statut correspondant
        if ("Actif".equals(service.getStatusService())) {
            statusActif.setSelected(true);
        } else {
            statusInactif.setSelected(true);
        }
    }

    // Méthode pour sauvegarder les modifications
    @FXML
    private void handleSave() {
        try {
            // Vérification des entrées utilisateur
            String nomService = nomServiceField.getText();
            String descriptionService = descriptionServiceField.getText();
            String typeService = typeServiceField.getText();
            LocalDate dateDebut = dateDebutField.getValue();
            LocalDate dateFin = dateFinField.getValue();
            String statusService = statusActif.isSelected() ? "Actif" : "Inactif";

            // Validation des champs
            if (nomService.isEmpty() || descriptionService.isEmpty() || typeService.isEmpty()) {
                System.out.println("Erreur : Tous les champs doivent être remplis !");
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

            // Mise à jour du service
            serviceToModify.setNomService(nomService);
            serviceToModify.setDescriptionService(descriptionService);
            serviceToModify.setTypeService(typeService);
            serviceToModify.setDateDebutService(dateDebut);
            serviceToModify.setDateFinService(dateFin);
            serviceToModify.setStatusService(statusService);

            // Mise à jour dans le service
            serviceService.update(serviceToModify);
            System.out.println("Service mis à jour avec succès !");

            // Fermer la fenêtre après sauvegarde
            ((Stage) nomServiceField.getScene().getWindow()).close();

        } catch (DateTimeParseException e) {
            System.out.println("Erreur : Format de date invalide. Utiliser yyyy-MM-dd !");
        }
    }
}
