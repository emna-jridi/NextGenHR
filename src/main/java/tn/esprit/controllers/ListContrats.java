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
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.stage.Stage;
import tn.esprit.models.Contrat;
import tn.esprit.models.ContratToText;
import tn.esprit.models.PDFShiftService;
import tn.esprit.services.ServiceContrat;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ListContrats {

    @FXML
    private TableView<Contrat> tableView;
    @FXML
    private TableColumn<Contrat, Integer> colId;
    @FXML
    private TableColumn<Contrat, String> colType;
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
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;
    @FXML
    private Button btnGenererPDF;
    @FXML
    private TextField searchField;
    @FXML
    private CheckBox chkFiltrerActifs;
    @FXML
    private CheckBox chkTriMontantAsc;
    @FXML
    private CheckBox chkTriMontantDesc;

    private final ServiceContrat contratService = new ServiceContrat();
    private ObservableList<Contrat> contratList;

    @FXML
    public void initialize() {

        //colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdContrat()).asObject());
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeContrat().name()));
        colDateDebut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateDebutContrat().toString()));
        colDateFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateFinContrat().toString()));
        colMontant.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMontantContrat()).asObject());
        colNomClient.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomClient()));
        colEmailClient.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmailClient()));
        colStatut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusContrat()));

        loadContrats();
    }

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

    @FXML
    private void modifierContrat(ActionEvent event) {
        Contrat contratSelectionne = tableView.getSelectionModel().getSelectedItem();

        if (contratSelectionne == null) {
            showAlert("Aucun contrat sélectionné", "Veuillez sélectionner un contrat à modifier.", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierContrat.fxml"));
            Parent root = loader.load();

            ModifierContrat controller = loader.getController();
            controller.setContrat(contratSelectionne);

            Stage stage = new Stage();
            stage.setTitle("Modifier Contrat");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadContrats();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    @FXML
    private void ajouterContrat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterContrat.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter un Contrat");
            stage.setScene(new Scene(root));
            stage.show();

            AjouterContrat ajouterContratController = loader.getController();
            ajouterContratController.setOnContratAdded(() -> loadContrats());

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ouverture de la page d'ajout.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        String searchText = searchField.getText();
        List<Contrat> filteredContrats = contratService.searchByClientName(searchText);
        updateTableView(filteredContrats);
    }

    @FXML
    private void filtrerContrats(ActionEvent event) {
        List<Contrat> contrats = contratService.getAll();

        if (chkFiltrerActifs.isSelected()) {
            contrats = contrats.stream()
                    .filter(contrat -> "Actif".equals(contrat.getStatusContrat()))
                    .collect(Collectors.toList());
        }

        if (chkTriMontantAsc.isSelected()) {
            contrats.sort((c1, c2) -> Integer.compare(c1.getMontantContrat(), c2.getMontantContrat()));
        } else if (chkTriMontantDesc.isSelected()) {
            contrats.sort((c1, c2) -> Integer.compare(c2.getMontantContrat(), c1.getMontantContrat()));
        }

        updateTableView(contrats);
    }


    @FXML
    private void handleTriMontantAsc(ActionEvent event) {
        if (chkTriMontantAsc.isSelected()) {
            chkTriMontantDesc.setSelected(false);
        }
        filtrerContrats(null);
    }


    @FXML
    private void handleTriMontantDesc(ActionEvent event) {
        if (chkTriMontantDesc.isSelected()) {
            chkTriMontantAsc.setSelected(false);
        }
        filtrerContrats(null);
    }


    private void updateTableView(List<Contrat> contrats) {
        ObservableList<Contrat> observableContrats = FXCollections.observableArrayList(contrats);
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
        String[] columns = {"ID Contrat", "Type Contrat", "Date Début", "Date Fin", "Montant", "Nom Client", "Email Client", "Statut"};
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
            row.createCell(1).setCellValue(contrat.getTypeContrat().toString());
            row.createCell(2).setCellValue(contrat.getDateDebutContrat().toString());
            row.createCell(3).setCellValue(contrat.getDateFinContrat().toString());
            row.createCell(4).setCellValue(contrat.getMontantContrat());
            row.createCell(5).setCellValue(contrat.getNomClient());
            row.createCell(6).setCellValue(contrat.getEmailClient());
            row.createCell(7).setCellValue(contrat.getStatusContrat());

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
    private void genererContratPDF(ActionEvent event) {
        // Récupérer le contrat sélectionné
        Contrat contratSelectionne = tableView.getSelectionModel().getSelectedItem();
        if (contratSelectionne == null) {
            showAlert("Aucun contrat sélectionné", "Veuillez sélectionner un contrat pour générer le PDF.", Alert.AlertType.WARNING);
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
