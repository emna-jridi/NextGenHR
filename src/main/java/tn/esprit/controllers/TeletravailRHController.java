package tn.esprit.controllers;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.client.util.DateTime;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import tn.esprit.models.Teletravail;
import tn.esprit.services.GoogleCalendarService;
import tn.esprit.services.ServiceTeletravail;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

public class TeletravailRHController implements javafx.fxml.Initializable {

    @FXML
    private Button IDApprouverTT, IDRefuserTT, IDSupprimerTT, IDafficherTT;
    @FXML
    private ComboBox<String> IDchoiceEmploye;
    @FXML
    private ListView<String> affichageid;

    private final ServiceTeletravail TT = new ServiceTeletravail();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateChoiceBox();
    }

    private void updateChoiceBox() {
        List<Teletravail> teletravails = TT.getAll();
        IDchoiceEmploye.getItems().clear();

        Set<String> employeeEntries = new HashSet<>();
        for (Teletravail teletravail : teletravails) {
            if ("En attente".equals(teletravail.getStatutTT())) {
                String employeeName = TT.getEmployeeName(teletravail.getIdEmploye());
                if (employeeName != null && !employeeName.isEmpty()) {
                    String entry = employeeName + " - " + teletravail.getIdTeletravail();
                    employeeEntries.add(entry);
                }
            }
        }
        IDchoiceEmploye.getItems().addAll(employeeEntries);
    }

    @FXML
    void AfficherTTbyName(ActionEvent event) {
        String selected = IDchoiceEmploye.getSelectionModel().getSelectedItem();
        affichageid.getItems().clear();

        if (selected == null || selected.trim().isEmpty()) {
            showAlert("Erreur", "Veuillez sélectionner un employé.");
            return;
        }

        int teletravailId = extractTeletravailId(selected);
        if (teletravailId == -1) {
            showAlert("Erreur", "Format de sélection invalide.");
            return;
        }

        Teletravail demande = TT.getById(teletravailId);
        if (demande != null && "En attente".equals(demande.getStatutTT())) {
            String stats = TT.getEmployeeTTStats(demande.getIdEmploye());
            String info = String.format("Date Demande: %s | Début: %s | Fin: %s | Statut: %s | Raison: %s",
                    demande.getDateDemandeTT(), demande.getDateDebutTT(), demande.getDateFinTT(), demande.getStatutTT(), demande.getRaisonTT());
            affichageid.getItems().add(info);
        } else {
            affichageid.getItems().add("Aucune demande en attente pour cet employé.");
        }
    }

    @FXML
    void ApprouverTT(ActionEvent event) {
        processTeletravailAction("Approuvé");
    }

    @FXML
    void RefuserTT(ActionEvent event) {
        processTeletravailAction("Refusé");
    }

    @FXML
    void SupprimerTT(ActionEvent event) {
        String selected = IDchoiceEmploye.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner une demande à supprimer.");
            return;
        }

        int teletravailId = extractTeletravailId(selected);
        if (teletravailId == -1) {
            showAlert("Erreur", "Format de sélection invalide.");
            return;
        }

        Teletravail selectedDemande = TT.getById(teletravailId);
        if (selectedDemande != null && TT.delete(teletravailId)) {
            affichageid.getItems().removeIf(item -> item.contains("ID: " + teletravailId));
            showAlert("Succès", "La demande a été supprimée.");
        } else {
            showAlert("Erreur", "La demande n'a pas pu être supprimée.");
        }

        updateChoiceBox();
    }

    private void processTeletravailAction(String statut) {
        String selected = IDchoiceEmploye.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner une demande.");
            return;
        }

        int teletravailId = extractTeletravailId(selected);
        if (teletravailId == -1) {
            showAlert("Erreur", "Format de sélection invalide.");
            return;
        }

        Teletravail selectedDemande = TT.getById(teletravailId);
        if (selectedDemande != null) {
            selectedDemande.setStatutTT(statut);
            if (TT.update(selectedDemande)) {
                if ("Approuvé".equals(statut)) {
                    TT.incrementNbTTValide(selectedDemande.getIdEmploye());
                } else {
                    TT.incrementNbTTRefuse(selectedDemande.getIdEmploye());
                }

                TT.traiterDemandeTT(selectedDemande.getIdTeletravail(), statut);

                // Intégration avec Google Calendar lorsque la demande est approuvée
                if ("Approuvé".equals(statut)) {
                    try {
                        GoogleCalendarService calendarService = new GoogleCalendarService();

                        LocalDate startDate = selectedDemande.getDateDebutTT();
                        LocalDate endDate = selectedDemande.getDateFinTT();

                        // Création d'un événement Google Calendar
                        calendarService.createEvent(
                                "Télétravail approuvé: " + selectedDemande.getNomEmploye(),
                                selectedDemande.getRaisonTT(),
                                startDate.atStartOfDay().toInstant(ZoneOffset.UTC),
                                endDate.atStartOfDay().toInstant(ZoneOffset.UTC)
                        );

                        showAlert("Succès", "La demande a été approuvée et l'événement a été ajouté au calendrier.");
                        System.out.println("Événement créé avec succès.");

                    } catch (IOException | GeneralSecurityException e) {
                        showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de l'événement au calendrier : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                affichageid.getItems().removeIf(item -> item.contains("ID: " + teletravailId));
                showAlert("Succès", "La demande a été " + statut.toLowerCase() + " et un email a été envoyé.");
            } else {
                showAlert("Erreur", "La demande n'a pas pu être " + statut.toLowerCase() + ".");
            }
            updateChoiceBox();
        } else {
            showAlert("Erreur", "La demande sélectionnée n'existe pas.");
        }
    }


    private int extractTeletravailId(String selected) {
        try {
            String[] parts = selected.split(" - ");
            return Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return -1;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
