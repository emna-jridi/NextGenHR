package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.models.ReservationSalle;
import tn.esprit.models.Salle;
import tn.esprit.services.ServiceReservationSalle;
import tn.esprit.services.ServiceSalle;

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

}
