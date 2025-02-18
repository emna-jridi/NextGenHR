package tn.esprit.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Service;
import tn.esprit.services.ServiceService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ListServices {

    @FXML
    private TableView<Service> tableView;
    @FXML
    private TableColumn<Service, Integer> colId;
    @FXML
    private TableColumn<Service, String> colNom;
    @FXML
    private TableColumn<Service, String> colDescription;
    @FXML
    private TableColumn<Service, String> colType;
    @FXML
    private TableColumn<Service, String> colDateDebut;
    @FXML
    private TableColumn<Service, String> colDateFin;
    @FXML
    private TableColumn<Service, String> colStatut;

    @FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;

    private final ServiceService serviceService = new ServiceService();
    private ObservableList<Service> serviceList;

    @FXML
    public void initialize() {
        // Liaison des colonnes avec les propriétés de Service
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdService()).asObject());
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomService()));
        colDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescriptionService()));
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeService()));
        colDateDebut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateDebutService().toString()));
        colDateFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateFinService().toString()));
        colStatut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusService()));

        loadServices();
    }

    private void loadServices() {
        List<Service> services = serviceService.getAll();
        serviceList = FXCollections.observableArrayList(services);
        tableView.setItems(serviceList);
    }

    @FXML
    private void modifierService(ActionEvent event) {
        Service serviceSelectionne = tableView.getSelectionModel().getSelectedItem();
        if (serviceSelectionne == null) {
            showAlert("Aucun service sélectionné", "Veuillez sélectionner un service à modifier.", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierService.fxml"));
            Parent root = loader.load();

            ModifierService controller = loader.getController();
            controller.setService(serviceSelectionne);

            Stage stage = new Stage();
            stage.setTitle("Modifier Service");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Rafraîchir la liste après modification
            loadServices();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void supprimerService(ActionEvent event) {
        Service serviceSelectionne = tableView.getSelectionModel().getSelectedItem();
        if (serviceSelectionne == null) {
            showAlert("Aucun service sélectionné", "Veuillez sélectionner un service à supprimer.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Suppression du service");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce service ?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            serviceService.delete(serviceSelectionne.getIdService());
            loadServices();
        }
    }

    @FXML
    private void ajouterService(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page AjouterService
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterService.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la page AjouterService
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Service");
            stage.setScene(new Scene(root));
            stage.show();

            // Passer le contrôleur ListServices à AjouterService
            AjouterService ajouterServiceController = loader.getController();
            ajouterServiceController.setOnServiceAdded(this::loadServices); // Callback pour actualiser la liste

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ouverture de la page d'ajout.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
