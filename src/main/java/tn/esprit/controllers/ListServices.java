package tn.esprit.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.esprit.models.Contrat;
import tn.esprit.models.Service;
import tn.esprit.services.ServiceService;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    /*@FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;*/
    @FXML
    private TextField searchField;
    @FXML
    private CheckBox chkFiltrerType;
    @FXML
    private CheckBox chkTriServicesActifs;
    @FXML
    private ComboBox<String> comboTri;
    @FXML
    private AnchorPane anchorPaneForm;


    private final ServiceService serviceService = new ServiceService();
    private ObservableList<Service> serviceList;

    @FXML
    public void initialize() {

        //colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdService()).asObject());
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomService()));
        colDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescriptionService()));
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeService()));
        colDateDebut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateDebutService().toString()));
        colDateFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateFinService().toString()));
        colStatut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusService()));
        // Créer une TableCell personnalisée pour la colonne Statut
        colStatut.setCellFactory(new Callback<TableColumn<Service, String>, TableCell<Service, String>>() {
            @Override
            public TableCell<Service, String> call(TableColumn<Service, String> param) {
                return new TableCell<Service, String>() {
                    @Override
                    protected void updateItem(String status, boolean empty) {
                        super.updateItem(status, empty);

                        if (empty || status == null) {
                            setGraphic(null);  // Ne rien afficher si la cellule est vide
                        } else {
                            // Créer un StackPane pour superposer l'ellipse et le texte
                            StackPane stackPane = new StackPane();

                            // Créer une ellipse au lieu d'un cercle pour élargir la forme
                            Ellipse ellipse = new Ellipse(25, 15); // (largeur, hauteur)

                            // Mettre à jour la couleur de l'ellipse en fonction du statut
                            if (status.equals("Actif")) {
                                ellipse.setFill(Color.rgb(144, 238, 144));  // Vert clair pour "Actif"
                            } else if (status.equals("Inactif")) {
                                ellipse.setFill(Color.rgb(255, 160, 122));  // Rouge clair pour "Inactif"
                            } else {
                                ellipse.setFill(Color.GRAY);  // Gris pour les autres statuts
                            }

                            // Créer un Label pour afficher le texte à l'intérieur de l'ellipse
                            Label label = new Label(status);
                            if (status.equals("Actif")) {
                                label.setTextFill(Color.rgb(0, 128, 0));  // Texte vert foncé pour "Actif"
                            } else if (status.equals("Inactif")) {
                                label.setTextFill(Color.rgb(139, 0, 0));  // Texte rouge foncé pour "Inactif"
                            } else {
                                label.setTextFill(Color.BLACK);  // Texte noir pour les autres statuts
                            }

                            label.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;"); // Style du texte

                            // Ajouter l'ellipse et le texte au StackPane
                            stackPane.getChildren().addAll(ellipse, label);
                            StackPane.setAlignment(label, Pos.CENTER);

                            // Mettre le StackPane dans la cellule
                            setGraphic(stackPane);
                        }
                    }
                };
            }
        });

        loadServices();

        /*chkFiltrerType.setOnAction(this::filtrerTypeService);
        chkTriServicesActifs.setOnAction(this::filterActiveServices);*/
    }

    private void loadServices() {
        List<Service> services = serviceService.getAll();
        serviceList = FXCollections.observableArrayList(services);
        tableView.setItems(serviceList);
    }



    // Cette méthode sera appelée lorsque la modification est réussie dans ModifierService
    public void showAjouterServiceForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterService.fxml"));
            AnchorPane ajouterForm = loader.load();

            // Remplacer ModifierContrat.fxml par AjouterService.fxml
            anchorPaneForm.getChildren().clear();
            anchorPaneForm.getChildren().add(ajouterForm);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            controller.setService(serviceSelectionne, this);

            controller.setListServicesController(this); // Passer le contrôleur parent

            // Afficher le formulaire dans l'AnchorPane prévu
            anchorPaneForm.getChildren().clear();
            anchorPaneForm.getChildren().add(root);
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

    /*@FXML
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
    }*/

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
    private void handleTriSelection(ActionEvent event) {
        String selectedOption = comboTri.getSelectionModel().getSelectedItem();

        List<Service> services = serviceService.getAll();

        switch (selectedOption) {
            case "All":
                // Ne rien filtrer, on garde la liste complète
                break;
            case "Services Actifs":
                // Ici vous pouvez ajouter une logique pour filtrer les contrats actifs
                services = services.stream()
                        .filter(contrat -> "Actif".equals(contrat.getStatusService()))
                        .collect(Collectors.toList());
                break;
            case "Tri par Type Service":
                services = services.stream()
                        .sorted(Comparator.comparing(Service::getTypeService)) // Trie par type
                        .collect(Collectors.toList());
                break;
            default:
                break;
        }

        // Mettre à jour la TableView après tri
        updateTableView(services);
    }


    /*@FXML
    private void filtrerTypeService(ActionEvent event) {
        if (chkFiltrerType.isSelected()) {

            chkTriServicesActifs.setSelected(false);

            List<Service> servicesTriesParType = serviceService.sortServicesByType();
            updateTableView(servicesTriesParType);
        } else {

            loadServices();
        }
    }

    @FXML
    private void filterActiveServices(ActionEvent event) {
        if (chkTriServicesActifs.isSelected()) {

            chkFiltrerType.setSelected(false);

            List<Service> servicesActifs = serviceService.filterActiveServices();
            updateTableView(servicesActifs);
        } else {

            loadServices();
        }
    }*/

}
