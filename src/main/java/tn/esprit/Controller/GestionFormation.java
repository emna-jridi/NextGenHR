package tn.esprit.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Formation;
import tn.esprit.services.ServiceFormation;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class GestionFormation  implements Initializable {
    IService<Formation> sf = new ServiceFormation();
    private int id;
    @FXML
//    private ChoiceBox<Formation.Statut> status;
//    @FXML
//    private ChoiceBox<Formation.NiveauDifficulte> Niveau_difficulte;
//    @FXML
    private TableColumn<Formation, Date> date;

    @FXML
    private TextArea description;

    @FXML
    private TextField lien;
    @FXML
    private TableColumn<Formation, String> nom;

    @FXML
    private TextField tfNomFormation;

    @FXML
    private TextField tfThemeFormation;

    @FXML
    private TableColumn<Formation, String> theme;

    @FXML
    private TableView<Formation> tvFormation;

    @FXML
    private DatePicker pdate;

    private ObservableList<Formation> lf;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        // Check if ChoiceBoxes are properly initialized
//        if (Niveau_difficulte != null && status != null) {
//            // Initialize the ChoiceBoxes with their values
//            Niveau_difficulte.getItems().addAll(Formation.NiveauDifficulte.values());
//            status.getItems().addAll(Formation.Statut.values());
//
//            // Set default values
//            Niveau_difficulte.setValue(Formation.NiveauDifficulte.DEBUTANT);
//            status.setValue(Formation.Statut.ACTIVE);
//        } else {
//            System.err.println("ChoiceBoxes not properly initialized from FXML!");
//        }

        nom.setCellValueFactory(new PropertyValueFactory<>("nomFormation"));
        theme.setCellValueFactory(new PropertyValueFactory<>("themeFormation"));
        date.setCellValueFactory(new PropertyValueFactory<>("dateFormation"));
       rafraichirTableView();
    }

    @FXML
    void ajouterFormation(ActionEvent event) {

        if (tfNomFormation.getText().trim().isEmpty() || tfThemeFormation.getText().trim().isEmpty() || pdate.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        if (!tfNomFormation.getText().matches("[a-zA-Z0-9_]+") || !tfThemeFormation.getText().matches("[a-zA-Z0-9_]+")) {
            showAlert("Erreur", "Le nom et le thème de la formation doivent contenir  des lettres et des chiffres  .");
            return;
        }

        LocalDate today = LocalDate.now();
        if (pdate.getValue().isBefore(today)) {
            showAlert("Erreur", "La date de la formation doit être supérieure à la date d'aujourd'hui.");
            return;
        }
        Formation formation = new Formation();
        formation.setNomFormation(tfNomFormation.getText());
        formation.setThemeFormation(tfThemeFormation.getText());
        formation.setDateFormation(Date.valueOf(pdate.getValue()));
        formation.setLien_formation(lien.getText());
        formation.setDescription(description.getText());
//        formation.setNiveauDifficulte(Niveau_difficulte.getValue());
//        formation.setStatut(status.getValue());
        sf.add(formation);
        System.out.println(formation);
        rafraichirTableView();
        clearFields();
        showAlert("Succès", "Formation ajoutée avec succès !");
    }

    @FXML
    void getData(MouseEvent event) {
        Formation selectedFormation = tvFormation.getSelectionModel().getSelectedItem();
        if (selectedFormation == null) {
            showAlert("Erreur", "Veuillez sélectionner une formation !");
            return;
        }
        id = selectedFormation.getIdFormation();
        tfNomFormation.setText(selectedFormation.getNomFormation());
        tfThemeFormation.setText(selectedFormation.getThemeFormation());
        pdate.setValue(selectedFormation.getDateFormation().toLocalDate());
        description.setText(selectedFormation.getDescription());
        lien.setText(selectedFormation.getLien_formation());
//        Niveau_difficulte.setValue(selectedFormation .getNiveauDifficulte());
//        status.setValue(selectedFormation.getStatut());
    }

    @FXML
    void effacerFormation(ActionEvent event) {
        Formation selectedFormation = tvFormation.getSelectionModel().getSelectedItem();
        if (selectedFormation == null) {
            showAlert("Erreur", "Veuillez sélectionner une formation à supprimer !");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer la formation");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette formation ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            sf.delete(selectedFormation);
            rafraichirTableView();
            clearFields();
            showAlert("Succès", "Formation supprimée avec succès !");
        }
    }

    @FXML
    void modifierFormation(ActionEvent event) {
        if (tfNomFormation.getText().isEmpty() || tfThemeFormation.getText().isEmpty() || pdate.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        if (!tfNomFormation.getText().matches("[a-zA-Z]+") || !tfThemeFormation.getText().matches("[a-zA-Z]+")) {
            showAlert("Erreur", "Le nom et le thème de la formation doivent contenir uniquement des lettres.");
            return;
        }

        LocalDate today = LocalDate.now();
        if (pdate.getValue().isBefore(today)) {
            showAlert("Erreur", "La date de la formation doit être supérieure à la date d'aujourd'hui.");
            return;
        }
        Formation selectedFormation = tvFormation.getSelectionModel().getSelectedItem();
        if (selectedFormation == null) {
            showAlert("Erreur", "Veuillez sélectionner une formation à modifier !");
            return;
        }
        selectedFormation.setNomFormation(tfNomFormation.getText());
        selectedFormation.setThemeFormation(tfThemeFormation.getText());
        selectedFormation.setDateFormation(Date.valueOf(pdate.getValue()));
        selectedFormation.setDescription(description.getText());
        selectedFormation.setLien_formation(lien.getText());
//        selectedFormation.setNiveauDifficulte(Niveau_difficulte.getValue());
//        selectedFormation.setStatut(status.getValue());
        sf.update(selectedFormation);
        rafraichirTableView();
        clearFields();
        showAlert("Succès", "Formation modifiée avec succès !");
    }

    private void rafraichirTableView() {
        lf = FXCollections.observableList(sf.getAll());
        tvFormation.setItems(lf);
    }

    private void clearFields() {
        tfNomFormation.clear();
        tfThemeFormation.clear();
        pdate.setValue(null);
        lien.clear();
        description.clear();

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}





