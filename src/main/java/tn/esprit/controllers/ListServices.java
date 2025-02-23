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
import javafx.scene.input.KeyEvent;
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

    @FXML
    private TextField searchField;

    @FXML
    private CheckBox chkFiltrerType;
    @FXML
    private CheckBox chkTriServicesActifs;

    private final ServiceService serviceService = new ServiceService();
    private ObservableList<Service> serviceList;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdService()).asObject());
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomService()));
        colDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescriptionService()));
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeService()));
        colDateDebut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateDebutService().toString()));
        colDateFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateFinService().toString()));
        colStatut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusService()));

        loadServices();


        chkFiltrerType.setOnAction(this::filtrerTypeService);
        chkTriServicesActifs.setOnAction(this::filterActiveServices);
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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterService.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Ajouter un Service");
            stage.setScene(new Scene(root));
            stage.show();


            AjouterService ajouterServiceController = loader.getController();
            ajouterServiceController.setOnServiceAdded(this::loadServices);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ouverture de la page d'ajout.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        String searchText = searchField.getText();
        List<Service> filteredServices = serviceService.getServicesByName(searchText);
        updateTableView(filteredServices);
    }

    private void updateTableView(List<Service> services) {
        ObservableList<Service> observableServices = FXCollections.observableArrayList(services);
        tableView.setItems(observableServices);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void filtrerTypeService(ActionEvent event) {
        if (chkFiltrerType.isSelected()) {
            // Si la case "Filtrer par Type" est cochée, décocher "Services Actifs"
            chkTriServicesActifs.setSelected(false);

            // Appliquer le filtrage par type
            List<Service> servicesTriesParType = serviceService.sortServicesByType();
            updateTableView(servicesTriesParType);
        } else {
            // Recharger les services sans filtre lorsque la case "Filtrer par Type" est décochée
            loadServices();
        }
    }

    @FXML
    private void filterActiveServices(ActionEvent event) {
        if (chkTriServicesActifs.isSelected()) {
            // Si la case "Services Actifs" est cochée, décocher "Filtrer par Type"
            chkFiltrerType.setSelected(false);

            // Appliquer le filtrage des services actifs
            List<Service> servicesActifs = serviceService.filterActiveServices();
            updateTableView(servicesActifs);
        } else {
            // Recharger les services sans filtre lorsque la case "Services Actifs" est décochée
            loadServices();
        }
    }

}
