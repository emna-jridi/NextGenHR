package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import tn.esprit.models.Teletravail;
import tn.esprit.services.ServiceTeletravail;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;

public class TeletravailEmController implements Initializable {

    @FXML private ListView<Teletravail> IdList; // Liste des demandes de télétravail
    @FXML private DatePicker Id_Date_Fin;
    @FXML private TextField Id_Identifiant;
    @FXML private TextArea Id_Raison;
    @FXML private DatePicker Id_date_debut;

    private ServiceTeletravail TTService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TTService = new ServiceTeletravail();

        // Configurer le DatePicker de la date de début
        configurerDateDebutPicker();

        // Configurer le DatePicker de la date de fin
        configurerDateFinPicker();

        // Définir la façon d'afficher les éléments dans la ListView
        IdList.setCellFactory(param -> new ListCell<Teletravail>() {
            @Override
            protected void updateItem(Teletravail tt, boolean empty) {
                super.updateItem(tt, empty);
                if (empty || tt == null) {
                    setText(null);
                } else {
                    setText(formatTeletravail(tt)); // Affichage formaté de la demande
                }
            }
        });

        // Ajouter un écouteur de sélection d'élément
        IdList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                remplirChamps(newValue);
            }
        });
    }

    // Configurer le DatePicker de la date de début
    private void configurerDateDebutPicker() {
        Id_date_debut.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        LocalDate minDate = LocalDate.now().plusDays(3); // 3 jours après aujourd'hui
                        DayOfWeek day = date.getDayOfWeek();

                        if (date.isBefore(minDate) || day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                            setDisable(true); // Désactiver les dates antérieures et les week-ends
                            setStyle("-fx-background-color: #ffc0cb;"); // Style pour les dates désactivées
                        }
                    }
                };
            }
        });

        // Mettre à jour le DatePicker de la date de fin lorsque la date de début change
        Id_date_debut.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                configurerDateFinPicker();
            }
        });
    }

    // Configurer le DatePicker de la date de fin
    private void configurerDateFinPicker() {
        Id_Date_Fin.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        LocalDate dateDebut = Id_date_debut.getValue();
                        if (dateDebut != null) {
                            LocalDate minDate = dateDebut;
                            LocalDate maxDate = getNextWorkingDay(dateDebut, 1); // Calculer la date max en jours ouvrables
                            DayOfWeek day = date.getDayOfWeek();

                            if (date.isBefore(minDate) || date.isAfter(maxDate) || day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                                setDisable(true); // Désactiver les dates hors de la plage et les week-ends
                                setStyle("-fx-background-color: #ffc0cb;"); // Style pour les dates désactivées
                            }
                        }
                    }
                };
            }
        });
    }

    // Fonction utilitaire pour calculer la prochaine date ouvrable (en excluant les week-ends)
    private LocalDate getNextWorkingDay(LocalDate startDate, int days) {
        LocalDate date = startDate;
        int addedDays = 0;

        while (addedDays < days) {
            date = date.plusDays(1);
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                addedDays++;
            }
        }
        return date;
    }

    // Affiche les demandes de télétravail pour un employé donné
    @FXML
    void AfficherTT(ActionEvent event) {
        String identifiant = Id_Identifiant.getText().trim();

        if (identifiant.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un identifiant valide.");
            return;
        }

        int idEmploye = parseId(identifiant);
        if (idEmploye == -1) return;

        List<Teletravail> demandes = TTService.getTeletravailByEmploye(idEmploye);
        if (demandes.isEmpty()) {
            showAlert("Info", "Aucune demande trouvée pour cet employé.");
            IdList.getItems().clear();
        } else {
            IdList.getItems().setAll(demandes);
        }
    }

    // Modifier une demande de télétravail
    @FXML
    void ModifierTT(ActionEvent event) {
        if (!validateFields()) return;

        Teletravail teletravail = IdList.getSelectionModel().getSelectedItem();
        if (teletravail == null) {
            showAlert("Erreur", "Veuillez sélectionner une demande à modifier.");
            return;
        }

        LocalDate dateDebut = Id_date_debut.getValue();
        LocalDate dateFin = Id_Date_Fin.getValue();

        // Vérifier si la date de début est un vendredi
        if (dateDebut.getDayOfWeek().getValue() == 5) { // 5 = Vendredi
            // Ignorer samedi et dimanche
            LocalDate lundi = dateDebut.plusDays(3); // Prochain lundi
            LocalDate mardi = dateDebut.plusDays(4); // Mardi
            if (dateFin.isBefore(lundi) || dateFin.isAfter(mardi)) {
                showAlert("Erreur", "Si la date de début est un vendredi, la date de fin doit être lundi ou mardi.");
                return;
            }
        } else {
            // Validation normale (2 jours maximum)
            long duree = ChronoUnit.DAYS.between(dateDebut, dateFin);
            if (duree > 2) {
                showAlert("Erreur", "La durée de la demande ne peut pas dépasser 2 jours consécutifs.");
                return;
            }
        }

        // Mettre à jour l'objet avec les nouvelles valeurs
        teletravail.setRaisonTT(Id_Raison.getText().trim());
        teletravail.setDateDebutTT(dateDebut);
        teletravail.setDateFinTT(dateFin);

        if (TTService.update(teletravail)) {
            showAlert("Succès", "Demande de télétravail modifiée avec succès.");
            AfficherTT(new ActionEvent()); // Rafraîchir la liste
        } else {
            showAlert("Erreur", "Erreur lors de la modification de la demande.");
        }
    }

    @FXML
    void AjouterTT(ActionEvent event) {
        if (!validateFields()) return;

        int idEmploye = parseId(Id_Identifiant.getText().trim());
        if (idEmploye == -1) return;

        LocalDate dateDebut = Id_date_debut.getValue();
        LocalDate dateFin = Id_Date_Fin.getValue();

        // Vérifier si la date de début est un vendredi
        if (dateDebut.getDayOfWeek().getValue() == 5) { // 5 = Vendredi
            // Ignorer samedi et dimanche
            LocalDate lundi = dateDebut.plusDays(3); // Prochain lundi
            LocalDate mardi = dateDebut.plusDays(4); // Mardi
            if (dateFin.isBefore(lundi) || dateFin.isAfter(mardi)) {
                showAlert("Erreur", "Si la date de début est un vendredi, la date de fin doit être lundi ou mardi.");
                return;
            }
        } else {
            // Validation normale (2 jours maximum)
            long duree = ChronoUnit.DAYS.between(dateDebut, dateFin);
            if (duree > 2) {
                showAlert("Erreur", "La durée de la demande ne peut pas dépasser 2 jours consécutifs.");
                return;
            }
        }

        Teletravail teletravail = new Teletravail();
        teletravail.setIdEmploye(idEmploye);
        teletravail.setRaisonTT(Id_Raison.getText().trim());
        teletravail.setDateDemandeTT(LocalDate.now());
        teletravail.setDateDebutTT(dateDebut);
        teletravail.setDateFinTT(dateFin);
        teletravail.setStatutTT("En attente");

        if (TTService.add(teletravail)) {
            showAlert("Succès", "Demande de télétravail ajoutée avec succès.");
            AfficherTT(new ActionEvent()); // Rafraîchir la liste
        } else {
            showAlert("Erreur", "Erreur lors de l'ajout de la demande.");
        }
    }

    // Remplir les champs avec les données de l'objet Teletravail sélectionné
    private void remplirChamps(Teletravail teletravail) {
        Id_Identifiant.setText(String.valueOf(teletravail.getIdEmploye()));
        Id_Raison.setText(teletravail.getRaisonTT());
        Id_date_debut.setValue(teletravail.getDateDebutTT());
        Id_Date_Fin.setValue(teletravail.getDateFinTT());
    }

    // Afficher une alerte d'information
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Fonction utilitaire pour valider les champs du formulaire
    private boolean validateFields() {
        if (Id_Identifiant.getText().trim().isEmpty() || Id_Raison.getText().trim().isEmpty() ||
                Id_date_debut.getValue() == null || Id_Date_Fin.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return false;
        }
        return true;
    }

    // Fonction utilitaire pour parser l'identifiant de l'employé
    private int parseId(String identifiant) {
        try {
            return Integer.parseInt(identifiant);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'identifiant doit être un nombre.");
            return -1;
        }
    }

    // Retourne un format texte de l'objet Teletravail pour l'affichage dans la ListView
    private String formatTeletravail(Teletravail tt) {
        return "ID: " + tt.getIdTeletravail() + " | " + tt.getRaisonTT() + " (" +
                tt.getDateDebutTT() + " - " + tt.getDateFinTT() + ")" + "|" + tt.getStatutTT();
    }

    @FXML
    void trierParId(ActionEvent event) {
        List<Teletravail> demandesTriees = TTService.getAllSortedById();
        IdList.getItems().clear();
        IdList.getItems().addAll(demandesTriees);
    }
}