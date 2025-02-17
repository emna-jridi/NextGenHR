package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.ReservationSalle;
import tn.esprit.models.Salle;
import tn.esprit.services.ServiceReservationSalle;
import tn.esprit.services.ServiceSalle;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservationSalleController {

    @FXML private TextField idEmployeField;
    @FXML private ChoiceBox<String> salleChoiceBox;
    @FXML private DatePicker datePicker;
    @FXML private TextField dureeField;

    private final ServiceSalle serviceSalle = new ServiceSalle();
    private final ServiceReservationSalle serviceReservation = new ServiceReservationSalle();

    @FXML
    public void initialize() {
        loadSallesDisponibles();
    }

    private void loadSallesDisponibles() {
        List<Salle> salles = serviceSalle.getAll();
        ObservableList<String> salleRefs = FXCollections.observableArrayList();
        for (Salle salle : salles) {
            if ("Disponible".equals(salle.getDisponibilite())) {
                salleRefs.add(salle.getRefSalle());
            }
        }
        salleChoiceBox.setItems(salleRefs);
    }

    @FXML
    private void reserverSalle() {
        try {
            int idEmploye = Integer.parseInt(idEmployeField.getText().trim());
            String refSalle = salleChoiceBox.getValue();
            LocalDate dateReservation = datePicker.getValue();
            LocalTime dureeReservation = LocalTime.parse(dureeField.getText().trim());

            if (refSalle == null || dateReservation == null || dureeReservation == null) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Tous les champs sont obligatoires !");
                return;
            }

            int idSalle = serviceSalle.getIdByRef(refSalle); // Récupérer l'ID de la salle depuis la référence

            ReservationSalle reservation = new ReservationSalle(idEmploye, idSalle, dateReservation, dureeReservation, "Confirmée");

            if (serviceReservation.add(reservation)) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation effectuée avec succès !");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la réservation.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un ID valide !");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format de durée incorrect (HH:MM) !");
        }
    }

    private void clearFields() {
        idEmployeField.clear();
        salleChoiceBox.setValue(null);
        datePicker.setValue(null);
        dureeField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
