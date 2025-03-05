package tn.esprit.controllers;

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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;
import javafx.util.Callback;
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
    @FXML
    private TableColumn<Service, Void> colDetails;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> comboTri;
    @FXML
    private AnchorPane anchorPaneForm;
    @FXML
    private ImageView refreshIcon;


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
                            setGraphic(null);
                        } else {
                            // Créer un StackPane pour superposer l'ellipse et le texte
                            StackPane stackPane = new StackPane();

                            Ellipse ellipse = new Ellipse(25, 15);


                            if (status.equals("Actif")) {
                                ellipse.setFill(Color.rgb(144, 238, 144));
                            } else if (status.equals("Inactif")) {
                                ellipse.setFill(Color.rgb(255, 160, 122));
                            } else {
                                ellipse.setFill(Color.GRAY);
                            }

                            // Créer un Label pour afficher le texte à l'intérieur de l'ellipse
                            Label label = new Label(status);
                            if (status.equals("Actif")) {
                                label.setTextFill(Color.rgb(0, 128, 0));
                            } else if (status.equals("Inactif")) {
                                label.setTextFill(Color.rgb(139, 0, 0));
                            } else {
                                label.setTextFill(Color.BLACK);
                            }

                            label.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");

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



        //bouton details contrat
        colDetails.setCellFactory(param -> new TableCell<Service, Void>() {
            private final Button btnDetails = new Button("Voir");

            {
                btnDetails.setStyle("-fx-background-color: #EEAA44; -fx-text-fill: white; -fx-cursor: hand;");
                btnDetails.setOnAction(event -> {
                    Service service = getTableView().getItems().get(getIndex());
                    afficherDetailsService(service);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnDetails);
                }
            }
        });


        //refresh the tableview
        refreshIcon.setOnMouseClicked(event -> loadServices());
        loadServices();

    }




    private void loadServices() {
        List<Service> services = serviceService.getAll();
        serviceList = FXCollections.observableArrayList(services);
        tableView.setItems(serviceList);
    }



    //afficher details contrat
    private void afficherDetailsService(Service service) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/detailsService.fxml"));
            Parent root = loader.load();

            DetailsService controller = loader.getController();
            controller.setService(service);

            Stage stage = new Stage();
            stage.setTitle("Détails du Service");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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


//modifier service
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

            controller.setListServicesController(this);

            // Afficher le formulaire dans l'AnchorPane prévu
            anchorPaneForm.getChildren().clear();
            anchorPaneForm.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //supprimer service
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
    private void handleSearch(KeyEvent event) {
        String searchText = searchField.getText();
        List<Service> filteredServices = serviceService.getServicesByName(searchText);
        updateTableView(filteredServices);
    }

    private void updateTableView(List<Service> services) {
        ObservableList<Service> observableServices = FXCollections.observableArrayList(services);
        tableView.setItems(observableServices);
    }



    @FXML
    private void refreshTable() {

        ObservableList<Service> observableServices = FXCollections.observableArrayList();

        tableView.setItems(observableServices);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



    //tri services
    @FXML
    private void handleTriSelection(ActionEvent event) {
        String selectedOption = comboTri.getSelectionModel().getSelectedItem();

        List<Service> services = serviceService.getAll();

        switch (selectedOption) {
            case "All":
                break;
            case "Services Actifs":
                services = services.stream()
                        .filter(contrat -> "Actif".equals(contrat.getStatusService()))
                        .collect(Collectors.toList());
                break;
            case "Tri par Type Service":
                services = services.stream()
                        .sorted(Comparator.comparing(Service::getTypeService))
                        .collect(Collectors.toList());
                break;
            default:
                break;
        }

        updateTableView(services);
    }



}
