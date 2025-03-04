package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import tn.esprit.models.ReservationSalle;
import tn.esprit.models.Salle;
import tn.esprit.services.ServiceReservationSalle;
import tn.esprit.services.ServiceSalle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import java.util.List;

public class RhController {

    @FXML
    private TableView<ReservationSalle> reservationsTable;
    @FXML
    private TableColumn<ReservationSalle, String> columnNomEmploye;
    @FXML
    private TableColumn<ReservationSalle, String> columnSalle;
    @FXML
    private TableColumn<ReservationSalle, String> columnDateReservation;
    @FXML
    private TableColumn<ReservationSalle, String> columnStatut;

    @FXML
    private TableView<Salle> sallesOccupeesTable;
    @FXML
    private TableColumn<Salle, String> columnRefSalle;
    @FXML
    private TableColumn<Salle, Integer> columnCapacite;
    @FXML
    private TableColumn<Salle, String> columnTypeSalle;

    private final ServiceReservationSalle serviceReservationSalle = new ServiceReservationSalle();
    private final ServiceSalle serviceSalle = new ServiceSalle();


    @FXML
    void exporterReservations(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            exporterReservationsVersExcel(file.getAbsolutePath());
        }
    }

    @FXML
    void exporterSallesOccupees(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            exporterSallesOccupeesVersExcel(file.getAbsolutePath());
        }
    }

    public void initialize() {
        // Initialiser les colonnes pour la table des réservations
        columnNomEmploye.setCellValueFactory(new PropertyValueFactory<>("IdEmploye"));
        columnSalle.setCellValueFactory(new PropertyValueFactory<>("IdSalle"));
        columnDateReservation.setCellValueFactory(new PropertyValueFactory<>("dateReservation"));
        columnStatut.setCellValueFactory(new PropertyValueFactory<>("statutReservation"));

        // Initialiser les colonnes pour la table des salles occupées
        columnRefSalle.setCellValueFactory(new PropertyValueFactory<>("refSalle"));
        columnCapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        columnTypeSalle.setCellValueFactory(new PropertyValueFactory<>("typeSalle"));

        // Afficher les réservations et salles occupées
        afficherReservations();
        afficherSallesOccupees();
    }

    private void afficherReservations() {
        List<ReservationSalle> reservations = serviceReservationSalle.getAll();
        reservationsTable.getItems().setAll(reservations);
    }

    private void afficherSallesOccupees() {
        List<Salle> salles = serviceSalle.getAll();
        sallesOccupeesTable.getItems().setAll(salles);
    }
    public void exporterReservationsVersExcel(String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Réservations");

            // Créer l'en-tête
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Identifiant Employé");
            headerRow.createCell(1).setCellValue("Salle");
            headerRow.createCell(2).setCellValue("Date Réservation");
            headerRow.createCell(3).setCellValue("Statut");

            // Remplir les données
            List<ReservationSalle> reservations = serviceReservationSalle.getAll();
            int rowNum = 1;
            for (ReservationSalle reservation : reservations) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(reservation.getIdEmploye());
                row.createCell(1).setCellValue(reservation.getIdSalle());
                row.createCell(2).setCellValue(reservation.getDateReservation().toString());
                row.createCell(3).setCellValue(reservation.getStatutReservation());
            }

            // Écrire le fichier
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            showAlert(AlertType.INFORMATION, "Succès", "Les réservations ont été exportées avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur lors de l'exportation des réservations.");
        }
    }

    public void exporterSallesOccupeesVersExcel(String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Salles Occupées");

            // Créer l'en-tête
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Référence Salle");
            headerRow.createCell(1).setCellValue("Capacité");
            headerRow.createCell(2).setCellValue("Type Salle");

            // Remplir les données
            List<Salle> salles = serviceSalle.getAll();
            int rowNum = 1;
            for (Salle salle : salles) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(salle.getRefSalle());
                row.createCell(1).setCellValue(salle.getCapacite());
                row.createCell(2).setCellValue(salle.getTypeSalle());
            }

            // Écrire le fichier
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            showAlert(AlertType.INFORMATION, "Succès", "Les salles occupées ont été exportées avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur lors de l'exportation des salles occupées.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
