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
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GestionFormationRh  implements Initializable {
    IService<Formation> sf = new ServiceFormation();
    private int id;
    @FXML
    private ChoiceBox<String> status;
    @FXML
    private ChoiceBox<String> Niveau_difficulte;
    @FXML
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
    private ChoiceBox tfThemeFormation;

    @FXML
    private TableColumn<Formation, String> theme;

    @FXML
    private TableView<Formation> tvFormation;
    @FXML
    private Button search;
    @FXML
    private DatePicker pdate;
    @FXML
    private TextField tfsearch;
    @FXML
    private TextField duree;

    @FXML
    private TextField imageUrl;



    private ObservableList<Formation> lf;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
// Initialiser les ChoiceBox
        Niveau_difficulte.getItems().addAll("Débutant", "Intermédiaire", "Avancé");
        tfThemeFormation.getItems().addAll("Développement", "Ressources Humaines", "Management", "Communication");

        // Définir des valeurs par défaut
        Niveau_difficulte.setValue("Débutant");
        tfThemeFormation.setValue("Développement"); // Valeur par défaut


        // Configurer les colonnes de la TableView
        nom.setCellValueFactory(new PropertyValueFactory<>("nomFormation"));
        theme.setCellValueFactory(new PropertyValueFactory<>("themeFormation"));
        date.setCellValueFactory(new PropertyValueFactory<>("dateFormation"));

        // Rafraîchir la TableView
        rafraichirTableView();
    }

    @FXML
    void ajouterFormation(ActionEvent event) {
        if (tfNomFormation.getText().trim().isEmpty() || tfThemeFormation.getValue() == null || pdate.getValue() == null ||duree.getText().trim().isEmpty() || imageUrl.getText().trim().isEmpty() || lien.getText().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérifier que le nom de la formation contient uniquement des lettres et des chiffres
        if (!tfNomFormation.getText().matches("[a-zA-Z0-9_]+")) {
            showAlert("Erreur", "Le nom de la formation doit contenir uniquement des lettres et des chiffres.");
            return;
        }
        if (!lien.getText().matches("\"^(https?://)?([\\\\da-z.-]+)\\\\.([a-z.]{2,6})([/\\\\w .-]*)*/?$\"")) {
            showAlert("Erreur", "Le lien n'est pas valide !");
            return;
        }
        if (!imageUrl.getText().matches("^(https?://)?([\\\\da-z.-]+)\\\\.([a-z.]{2,6})([/\\\\w .-]*)*\\\\.(jpg|jpeg|png|gif)$\"")) {
            showAlert("Erreur", "L'url de l'image  n'est pas valide !");
            return;
        }


        // Vérifier que la date est valide
        LocalDate today = LocalDate.now();
        if (pdate.getValue().isBefore(today)) {
            showAlert("Erreur", "La date de la formation doit être supérieure à la date d'aujourd'hui.");
            return;
        }

        // Créer une nouvelle formation
        Formation formation = new Formation();
        formation.setNomFormation(tfNomFormation.getText());
        formation.setThemeFormation(tfThemeFormation.getValue().toString()); // Récupérer la valeur du ChoiceBox
        formation.setDateFormation(Date.valueOf(pdate.getValue()));
        formation.setLien_formation(lien.getText());
        formation.setDescription(description.getText());
        formation.setNiveauDifficulte(Niveau_difficulte.getValue());


        // Ajouter la formation
        sf.add(formation);
        System.out.println(formation);

        // Rafraîchir la TableView et effacer les champs
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

        // Remplir les champs avec les données de la formation sélectionnée
        id = selectedFormation.getIdFormation();
        tfNomFormation.setText(selectedFormation.getNomFormation());
        tfThemeFormation.setValue(selectedFormation.getThemeFormation()); // Définir la valeur du ChoiceBox
        pdate.setValue(selectedFormation.getDateFormation().toLocalDate());
        description.setText(selectedFormation.getDescription());
        lien.setText(selectedFormation.getLien_formation());
        Niveau_difficulte.setValue(selectedFormation.getNiveauDifficulte());

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
        // Vérifier que tous les champs sont remplis
        if (tfNomFormation.getText().isEmpty() || tfThemeFormation.getValue() == null || pdate.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérifier que le nom de la formation contient uniquement des lettres et des chiffres
        if (!tfNomFormation.getText().matches("[a-zA-Z0-9_]+")) {
            showAlert("Erreur", "Le nom de la formation doit contenir uniquement des lettres et des chiffres.");
            return;
        }

        // Vérifier que la date est valide
        LocalDate today = LocalDate.now();
        if (pdate.getValue().isBefore(today)) {
            showAlert("Erreur", "La date de la formation doit être supérieure à la date d'aujourd'hui.");
            return;
        }

        // Récupérer la formation sélectionnée
        Formation selectedFormation = tvFormation.getSelectionModel().getSelectedItem();
        if (selectedFormation == null) {
            showAlert("Erreur", "Veuillez sélectionner une formation à modifier !");
            return;
        }

        // Mettre à jour les propriétés de la formation
        selectedFormation.setNomFormation(tfNomFormation.getText());
        selectedFormation.setThemeFormation(tfThemeFormation.getValue().toString());
        selectedFormation.setDateFormation(Date.valueOf(pdate.getValue()));
        selectedFormation.setDescription(description.getText());
        selectedFormation.setLien_formation(lien.getText());
        selectedFormation.setNiveauDifficulte(Niveau_difficulte.getValue());
        selectedFormation.setImageUrl(imageUrl.getText());
        selectedFormation.setDuree(Integer.parseInt(duree.getText()));




        // Mettre à jour la formation dans la base de données
        sf.update(selectedFormation);

        // Rafraîchir la TableView et effacer les champs
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
        tfThemeFormation.setValue(null);
        pdate.setValue(null);
        lien.clear();
        description.clear();

    }
    @FXML
    void search(ActionEvent event) {

        String searchTerm = tfsearch.getText();
        List<Formation> allFormations = sf.getAll();
        List<Formation> filteredFormations = searchList(searchTerm, allFormations);
        lf = FXCollections.observableList(filteredFormations);
        tvFormation.setItems(lf);
    }

    private List<Formation> searchList(String searchNom, List<Formation> listOfFormations) {

        List<String> searchNomArray = Arrays.asList(searchNom.trim().split(" "));


        return listOfFormations.stream()
                .filter(formation -> {

                    return searchNomArray.stream().allMatch(word ->
                            formation.getNomFormation().toLowerCase().contains(word.toLowerCase()));
                })
                .collect(Collectors.toList());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

