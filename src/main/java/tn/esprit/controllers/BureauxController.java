package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.models.Bureau;
import tn.esprit.services.ServiceBureau;

import java.util.List;

public class BureauxController {

    @FXML private TextField CapacitéId;
    @FXML private TextField DisponibilitéB;
    @FXML private TextField ReferenceB;
    @FXML private TableView<Bureau> tableBureau;
    @FXML private TableColumn<Bureau, String> columnRefBureau;
    @FXML private TableColumn<Bureau, Integer> columnCapacite;
    @FXML private TableColumn<Bureau, String> columnDisponibilite;

    private ObservableList<Bureau> bureauList;
    private ServiceBureau serviceBureau;
    private Bureau bureauSelectionne;

    public BureauxController() {
        serviceBureau = new ServiceBureau();
    }

    @FXML
    public void initialize() {
        configureTableColumns();
        bureauList = FXCollections.observableArrayList();
        tableBureau.setItems(bureauList);
        addSelectionListener();
    }

    private void configureTableColumns() {
        columnRefBureau.setCellValueFactory(new PropertyValueFactory<>("refBureau"));
        columnCapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        columnDisponibilite.setCellValueFactory(new PropertyValueFactory<>("disponibilite"));
    }

    private void addSelectionListener() {
        tableBureau.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                bureauSelectionne = newSelection;
                updateTextFields();
            }
        });
    }

    private void updateTextFields() {
        ReferenceB.setText(bureauSelectionne.getRefBureau());
        CapacitéId.setText(String.valueOf(bureauSelectionne.getCapacite()));
        DisponibilitéB.setText(bureauSelectionne.getDisponibilite());
    }

    @FXML
    void AfficherBureau(ActionEvent event) {
        List<Bureau> bureaux = serviceBureau.getAll();
        bureauList.clear();
        bureauList.addAll(bureaux);
    }

    @FXML
    void AjouterBureau(ActionEvent event) {
        String refBureau = ReferenceB.getText().trim();
        String capaciteText = CapacitéId.getText().trim();
        String disponibilite = DisponibilitéB.getText().trim();

        if (isValidBureauInput(refBureau, capaciteText, disponibilite)) {
            int capacite = Integer.parseInt(capaciteText);
            if (serviceBureau.existsByRef(refBureau)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La référence du bureau existe déjà !");
                return;
            }

            Bureau bureau = new Bureau(refBureau, capacite, disponibilite);
            if (serviceBureau.add(bureau)) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Le bureau a été ajouté avec succès !");
                clearTextFields();
                AfficherBureau(event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du bureau. Veuillez réessayer.");
            }
        }
    }

    @FXML
    void ModifierBureau(ActionEvent event) {
        if (bureauSelectionne != null) {
            String refBureau = ReferenceB.getText().trim();
            String capaciteText = CapacitéId.getText().trim();
            String disponibilite = DisponibilitéB.getText().trim();

            if (isValidBureauInput(refBureau, capaciteText, disponibilite)) {
                int capacite = Integer.parseInt(capaciteText);
                if (!refBureau.equals(bureauSelectionne.getRefBureau()) && serviceBureau.existsByRef(refBureau)) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "La référence du bureau existe déjà !");
                    return;
                }

                bureauSelectionne.setRefBureau(refBureau);
                bureauSelectionne.setCapacite(capacite);
                bureauSelectionne.setDisponibilite(disponibilite);

                if (serviceBureau.update(bureauSelectionne)) {
                    tableBureau.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Le bureau a été modifié avec succès !");
                    clearTextFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la modification du bureau.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez sélectionner un bureau à modifier !");
        }
    }

    @FXML
    void SupprimerBureau(ActionEvent event) {
        Bureau selectedBureau = tableBureau.getSelectionModel().getSelectedItem();
        if (selectedBureau != null) {
            if (serviceBureau.delete(selectedBureau.getIdBureau())) {
                bureauList.remove(selectedBureau);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Le bureau a été supprimé avec succès !");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression du bureau");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez sélectionner un bureau à supprimer");
        }
    }

    private boolean isValidBureauInput(String refBureau, String capaciteText, String disponibilite) {
        if (refBureau.isEmpty() || capaciteText.isEmpty() || disponibilite.isEmpty()) {
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
        ReferenceB.clear();
        CapacitéId.clear();
        DisponibilitéB.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
