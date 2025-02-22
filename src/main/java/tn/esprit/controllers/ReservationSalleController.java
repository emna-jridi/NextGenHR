package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.models.ReservationSalle;
import tn.esprit.models.Salle;
import tn.esprit.services.ServiceReservationSalle;
import tn.esprit.services.ServiceSalle;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservationSalleController {

    @FXML private ListView<HBox> sallesListView;
    @FXML private TextField idEmployeField;
    @FXML private DatePicker datePicker;
    @FXML private TextField dureeField;
    @FXML private Button btnReserver;

    private final ServiceSalle serviceSalle = new ServiceSalle();
    private final ServiceReservationSalle serviceReservation = new ServiceReservationSalle();

    private Salle selectedSalle;

    @FXML
    public void initialize() {
        loadSallesDisponibles();
    }

    private void loadSallesDisponibles() {
        List<Salle> salles = serviceSalle.getAll();
        ObservableList<HBox> salleCards = FXCollections.observableArrayList();

        for (Salle salle : salles) {
            if ("Disponible".equals(salle.getDisponibilite())) {
                HBox salleCard = createSalleCard(salle);
                salleCards.add(salleCard);
            }
        }

        sallesListView.setItems(salleCards);
    }

    private HBox createSalleCard(Salle salle) {
        Label salleRefLabel = new Label(salle.getRefSalle());
        salleRefLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label capaciteLabel = new Label("Capacité: " + salle.getCapacite());

        Button selectButton = new Button("Sélectionner");
        selectButton.setOnAction(event -> selectSalle(salle));

        HBox salleCard = new HBox(20, salleRefLabel, capaciteLabel, selectButton);
        salleCard.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 10px; -fx-border-radius: 5px; -fx-spacing: 15px;");

        return salleCard;
    }

    private void selectSalle(Salle salle) {
        this.selectedSalle = salle;
        showAlert(Alert.AlertType.INFORMATION, "Salle sélectionnée", "Vous avez choisi : " + salle.getRefSalle());
    }

    @FXML
    private void reserverSalle() {
        try {
            if (selectedSalle == null) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez sélectionner une salle !");
                return;
            }

            int idEmploye = Integer.parseInt(idEmployeField.getText().trim());
            LocalDate dateReservation = datePicker.getValue();
            LocalTime dureeReservation = LocalTime.parse(dureeField.getText().trim());

            if (dateReservation == null || dureeReservation == null) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Tous les champs sont obligatoires !");
                return;
            }

            ReservationSalle reservation = new ReservationSalle(idEmploye, selectedSalle.getIdSalle(), dateReservation, dureeReservation, "Confirmée");

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
        datePicker.setValue(null);
        dureeField.clear();
        selectedSalle = null;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
