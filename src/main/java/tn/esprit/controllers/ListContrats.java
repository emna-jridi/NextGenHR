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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Ellipse;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.stage.Stage;
import tn.esprit.models.*;
import tn.esprit.services.ServiceContrat;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.scene.paint.Color;



public class ListContrats {

    @FXML
    private TableView<Contrat> tableView;
    @FXML
    private TableColumn<Contrat, String> colDateDebut;
    @FXML
    private TableColumn<Contrat, String> colDateFin;
    @FXML
    private TableColumn<Contrat, String> colStatut;
    @FXML
    private TableColumn<Contrat, Integer> colMontant;
    @FXML
    private TableColumn<Contrat, String> colNomClient;
    @FXML
    private TableColumn<Contrat, String> colEmailClient;
    @FXML
    private TableColumn<Contrat, String> colNumTelClient;
    @FXML
    private TableColumn<Contrat, Void> colDetails;
    @FXML
    private TextField searchField;
    @FXML
    private AnchorPane anchorPaneForm;
    @FXML
    private ComboBox<String> comboTri;
    @FXML
    private ImageView refreshIcon;
    @FXML
    private TableColumn<Contrat, String> colModePaiement;



    private final ServiceContrat contratService = new ServiceContrat();
    private ObservableList<Contrat> contratList;




    @FXML
    public void initialize() {
        colNomClient.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomClient()));
        colEmailClient.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmailClient()));
        colNumTelClient.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelephoneClient()));
        colDateDebut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateDebutContrat().toString()));
        colDateFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateFinContrat().toString()));
        colMontant.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMontantContrat()).asObject());
        colStatut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusContrat()));
        colModePaiement.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModeDePaiement().name()));


        // Créer une TableCell personnalisée pour la colonne Statut
        colStatut.setCellFactory(new Callback<TableColumn<Contrat, String>, TableCell<Contrat, String>>() {
            @Override
            public TableCell<Contrat, String> call(TableColumn<Contrat, String> param) {
                return new TableCell<Contrat, String>() {
                    @Override
                    protected void updateItem(String status, boolean empty) {
                        super.updateItem(status, empty);

                        if (empty || status == null) {
                            setGraphic(null);  // Ne rien afficher si la cellule est vide
                        } else {
                            // Créer un StackPane pour superposer l'ellipse et le texte
                            StackPane stackPane = new StackPane();
                            Ellipse ellipse = new Ellipse(25, 15);
                            // Mettre à jour la couleur de l'ellipse en fonction du statut
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
        colDetails.setCellFactory(param -> new TableCell<Contrat, Void>() {
            private final Button btnDetails = new Button("Voir");

            {
                btnDetails.setStyle("-fx-background-color: #EEAA44; -fx-text-fill: white; -fx-cursor: hand;");
                btnDetails.setOnAction(event -> {
                    Contrat contrat = getTableView().getItems().get(getIndex());
                    afficherDetailsContrat(contrat);
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
        refreshIcon.setOnMouseClicked(event -> loadContrats());


        loadContrats();


    }



//mettre les contrats dans tableview
    private void loadContrats() {
        List<Contrat> contrats = contratService.getAll();
        for (Contrat contrat : contrats) {
            if (contrat.getStatusContrat() == null) {
                contrat.setStatusContrat("Actif");
            }
        }
        contratList = FXCollections.observableArrayList(contrats);
        tableView.setItems(contratList);
    }


//afficher details contrat
    private void afficherDetailsContrat(Contrat contrat) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/detailsContrat.fxml"));
            Parent root = loader.load();

            DetailsContrat controller = loader.getController();
            controller.setContrat(contrat);

            Stage stage = new Stage();
            stage.setTitle("Détails du Contrat");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    // Cette méthode sera appelée lorsque la modification est réussie dans ModifierContrat
    public void showAjouterContratForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterContrat.fxml"));
            AnchorPane ajouterForm = loader.load();

            // Remplacer ModifierContrat.fxml par AjouterContrat.fxml
            anchorPaneForm.getChildren().clear();
            anchorPaneForm.getChildren().add(ajouterForm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



//modifier contrat
    @FXML
    private void modifierContrat(ActionEvent event) {
        Contrat selectedContrat = tableView.getSelectionModel().getSelectedItem();

        if (selectedContrat == null) {
            showAlert("Aucun contrat sélectionné", "Veuillez sélectionner un contrat à modifier.", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierContrat.fxml"));
            AnchorPane modifierForm = loader.load();

            // Récupérer le contrôleur du formulaire de modification
            ModifierContrat controller = loader.getController();
            controller.setContrat(selectedContrat, this);
            controller.setListContratsController(this);

            // Afficher le formulaire dans l'AnchorPane prévu
            anchorPaneForm.getChildren().clear();
            anchorPaneForm.getChildren().add(modifierForm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



//supprimer contrat
    @FXML
    private void supprimerContrat(ActionEvent event) {
        Contrat contratSelectionne = tableView.getSelectionModel().getSelectedItem();
        if (contratSelectionne == null) {
            showAlert("Aucun contrat sélectionné", "Veuillez sélectionner un contrat à supprimer.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Suppression du contrat");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce contrat ?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            contratService.delete(contratSelectionne.getIdContrat());
            loadContrats();
        }
    }




//search contrat
    @FXML
    private void handleSearch(KeyEvent event) {
        String searchText = searchField.getText();
        List<Contrat> filteredContrats = contratService.searchByClientName(searchText);
        updateTableView(filteredContrats);
    }



//tri contrats
    @FXML
    private void handleTriSelection(ActionEvent event) {
        String selectedOption = comboTri.getSelectionModel().getSelectedItem();

        List<Contrat> contrats = contratService.getAll();

        switch (selectedOption) {
            case "All":
                break;
            case "Contrats Actifs":
                contrats = contrats.stream()
                        .filter(contrat -> "Actif".equals(contrat.getStatusContrat()))
                        .collect(Collectors.toList());
                break;
            case "Tri par Montant Ascendant":
                contrats.sort((c1, c2) -> Integer.compare(c1.getMontantContrat(), c2.getMontantContrat()));
                break;
            case "Tri par Montant Descendant":
                contrats.sort((c1, c2) -> Integer.compare(c2.getMontantContrat(), c1.getMontantContrat()));
                break;
            default:
                break;
        }

        // Mettre à jour la TableView après tri
        updateTableView(contrats);
    }









    private void updateTableView(List<Contrat> contrats) {
        ObservableList<Contrat> observableContrats = FXCollections.observableArrayList(contrats);
        tableView.setItems(observableContrats);
    }




    @FXML
    private void refreshTable() {
        // Récupère de nouvelles données pour la table, par exemple depuis une base de données ou une autre source
        ObservableList<Contrat> observableContrats = FXCollections.observableArrayList();

        // Mettre à jour la TableView
        tableView.setItems(observableContrats);
    }


    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



    //exporter les contrats vers un fichier excel//
    @FXML
    private void exporterContrats(MouseEvent event) {

        List<Contrat> contrats = contratService.getAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Contrats");

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID Contrat", "Nom Client(e)", "Email Client(e)", "NumTel Client(e)", "Date Début", "Date Fin", "Montant", "Statut", "Mode Paiement", "Services"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        int rowNum = 1;
        for (Contrat contrat : contrats) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(contrat.getIdContrat());
            row.createCell(1).setCellValue(contrat.getNomClient());
            row.createCell(2).setCellValue(contrat.getEmailClient());
            row.createCell(3).setCellValue(contrat.getTelephoneClient());
            row.createCell(4).setCellValue(contrat.getDateDebutContrat().toString());
            row.createCell(5).setCellValue(contrat.getDateFinContrat().toString());
            row.createCell(6).setCellValue(contrat.getMontantContrat());
            row.createCell(7).setCellValue(contrat.getStatusContrat());
            row.createCell(8).setCellValue(contrat.getModeDePaiement().toString());

            // Get services for each contract and join them into a comma-separated string
            List<Service> services = contrat.getServices();
            String serviceNames = services.stream()
                    .map(Service::getNomService)
                    .collect(Collectors.joining(", "));
            row.createCell(9).setCellValue(serviceNames);


            for (int i = 0; i < columns.length; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier Excel", "*.xlsx"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                workbook.close();
                showAlert("Succès", "Les contrats ont été exportés avec succès.", Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de l'exportation des contrats.", Alert.AlertType.ERROR);
            }
        }
    }





//générer contrat PDF via api
    @FXML
    private void genererContratPDF(MouseEvent event) {
        // Récupérer le contrat sélectionné
        Contrat contratSelectionne = tableView.getSelectionModel().getSelectedItem();
        if (contratSelectionne == null) {
            showAlert("Aucun contrat sélectionné", "Veuillez sélectionner un contrat pour le  générer en PDF.", Alert.AlertType.WARNING);
            return;
        }

        // Générer le contenu HTML du contrat
        String contractHtml = ContratToText.generateContract(contratSelectionne);

        // Créer un FileChooser pour sélectionner le dossier de sauvegarde
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        fileChooser.setInitialFileName("contrat_" + contratSelectionne.getNomClient() + ".pdf");

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            // Générer le PDF
            try {
                // Appel de la méthode pour générer le PDF
                PDFShiftService.generatePDF(contractHtml, file.getAbsolutePath());
                showAlert("PDF généré", "Le contrat a été généré avec succès.", Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                showAlert("Erreur", "Erreur lors de la génération du PDF.", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }







}
