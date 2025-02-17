
package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.models.Salle;
import tn.esprit.services.ServiceSalle;

import java.util.List;

public class SallesController {

    @FXML private TextField CapacitéId;
    @FXML private TextField ReferenceSalle;
    @FXML private CheckBox checkBoxDisponible;
    @FXML private CheckBox checkBoxIndisponible;
    @FXML private TableView<Salle> tableSalle;
    @FXML private TableColumn<Salle, String> columnRefSalle;
    @FXML private TableColumn<Salle, Integer> columnCapacite;
    @FXML private TableColumn<Salle, String> columnTypeSalle;
    @FXML private ChoiceBox<String> TypeSalle;
    @FXML private TableColumn<Salle, String> columnDisponibilite;

    private ObservableList<Salle> salleList;
    private ServiceSalle serviceSalle;
    private Salle salleSelectionnee;

    public SallesController() {
        serviceSalle = new ServiceSalle();
    }
@FXML
    public void initialize() {
        // Configure la colonne de la table
        configureTableColumns();

        // Initialiser la liste des salles
        salleList = FXCollections.observableArrayList();
        tableSalle.setItems(salleList);

        // Ajouter le listener de sélection
        addSelectionListener();

        // Configurer les cases à cocher
        configureCheckBoxes();

        // Ajouter les types de salle au ChoiceBox
        ObservableList<String> roomTypes = FXCollections.observableArrayList("Formation", "Réunion", "Détente", "Jeu", "Conférence");
        TypeSalle.setItems(roomTypes);  // Ajout des types dans le ChoiceBox
    }

    private void configureTableColumns() {
        columnRefSalle.setCellValueFactory(new PropertyValueFactory<>("refSalle"));
        columnCapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        columnTypeSalle.setCellValueFactory(new PropertyValueFactory<>("typeSalle"));
        columnDisponibilite.setCellValueFactory(new PropertyValueFactory<>("disponibilite"));
    }

    private void addSelectionListener() {
        tableSalle.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                salleSelectionnee = newSelection;
                updateTextFields();
            }
        });
    }

    private void configureCheckBoxes() {
        checkBoxDisponible.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                checkBoxIndisponible.setSelected(false);
            }
        });

        checkBoxIndisponible.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                checkBoxDisponible.setSelected(false);
            }
        });
    }

    private void updateTextFields() {
        ReferenceSalle.setText(salleSelectionnee.getRefSalle());
        CapacitéId.setText(String.valueOf(salleSelectionnee.getCapacite()));
        TypeSalle.setValue(salleSelectionnee.getTypeSalle());
        if (salleSelectionnee.getDisponibilite().equals("Disponible")) {
            checkBoxDisponible.setSelected(true);
            checkBoxIndisponible.setSelected(false);
        } else {
            checkBoxDisponible.setSelected(false);
            checkBoxIndisponible.setSelected(true);
        }
    }

    @FXML
    void AfficherSalle(ActionEvent event) {
        List<Salle> salles = serviceSalle.getAll();
        salleList.clear();
        salleList.addAll(salles);
    }

    @FXML
    void AjouterSalle(ActionEvent event) {
        String refSalle = ReferenceSalle.getText().trim();
        String capaciteText = CapacitéId.getText().trim();
        String typeSalle = TypeSalle.getValue();
        String disponibilite = checkBoxDisponible.isSelected() ? "Disponible" : "Indisponible";

        if (isValidSalleInput(refSalle, capaciteText, typeSalle)) {
            int capacite = Integer.parseInt(capaciteText);
            if (serviceSalle.existsByRef(refSalle)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La référence de la salle existe déjà !");
                return;
            }

            Salle salle = new Salle(refSalle, capacite, typeSalle, disponibilite);
            if (serviceSalle.add(salle)) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "La salle a été ajoutée avec succès !");
                clearTextFields();
                AfficherSalle(event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de la salle. Veuillez réessayer.");
            }
        }
    }

    @FXML
    void ModifierSalle(ActionEvent event) {
        if (salleSelectionnee != null) {
            String refSalle = ReferenceSalle.getText().trim();
            String capaciteText = CapacitéId.getText().trim();
            String typeSalle = TypeSalle.getValue(); // Récupérer la valeur du ChoiceBox
            String disponibilite = checkBoxDisponible.isSelected() ? "Disponible" : "Indisponible";

            if (isValidSalleInput(refSalle, capaciteText, typeSalle)) {
                int capacite = Integer.parseInt(capaciteText);
                if (!refSalle.equals(salleSelectionnee.getRefSalle()) && serviceSalle.existsByRef(refSalle)) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "La référence de la salle existe déjà !");
                    return;
                }

                salleSelectionnee.setRefSalle(refSalle);
                salleSelectionnee.setCapacite(capacite);
                salleSelectionnee.setTypeSalle(typeSalle); // Utiliser la valeur du ChoiceBox
                salleSelectionnee.setDisponibilite(disponibilite);

                if (serviceSalle.update(salleSelectionnee)) {
                    tableSalle.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "La salle a été modifiée avec succès !");
                    clearTextFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la modification de la salle.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez sélectionner une salle à modifier !");
        }
    }

    @FXML
    void SupprimerSalle(ActionEvent event) {
        Salle selectedSalle = tableSalle.getSelectionModel().getSelectedItem();
        if (selectedSalle != null) {
            if (serviceSalle.delete(selectedSalle.getIdSalle())) {
                salleList.remove(selectedSalle);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "La salle a été supprimée avec succès !");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression de la salle");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez sélectionner une salle à supprimer");
        }
    }

    private boolean isValidSalleInput(String refSalle, String capaciteText, String typeSalle) {
        if (refSalle.isEmpty() || capaciteText.isEmpty() || typeSalle.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Tous les champs sont obligatoires !");
            return false;
        }

        try {
            int capacite = Integer.parseInt(capaciteText);
            if (capacite <= 0) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "La capacité doit être un nombre positif !");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez entrer une capacité valide !");
            return false;
        }

        return true;
    }

    private void clearTextFields() {
        ReferenceSalle.clear();
        CapacitéId.clear();
        TypeSalle.setValue(null); // Réinitialiser le ChoiceBox
        checkBoxDisponible.setSelected(false);
        checkBoxIndisponible.setSelected(false);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
