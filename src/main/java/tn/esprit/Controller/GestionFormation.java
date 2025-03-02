package tn.esprit.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import tn.esprit.models.Formation;
import tn.esprit.services.ServiceFormation;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GestionFormation implements Initializable {

    @FXML private FlowPane formationsContainer;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button resetButton;

    @FXML private CheckBox devCheckBox;
    @FXML private CheckBox managementCheckBox;
    @FXML private CheckBox comCheckBox;
    @FXML private CheckBox rhCheckBox;
    @FXML private CheckBox dureeCourteCheckBox;
    @FXML private CheckBox dureeMoyenneCheckBox;
    @FXML private CheckBox dureeLongueCheckBox;
    @FXML private CheckBox debutantCheckBox;
    @FXML private CheckBox intermediaireCheckBox;
    @FXML private CheckBox avanceCheckBox;

    private List<Formation> allFormations = new ArrayList<>();
    private List<Formation> filteredFormations = new ArrayList<>();

    private ServiceFormation serviceFormation = new ServiceFormation();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFormationsFromDatabase();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilters();
        });
        displayFormations(allFormations);
    }

    private void loadFormationsFromDatabase() {
        // Utiliser votre service pour charger les formations
        allFormations = serviceFormation.getAll();
        filteredFormations = new ArrayList<>(allFormations);
    }

    @FXML
    private void searchFormations() {
        applyFilters();
    }

    @FXML
    private void resetFilters() {
        devCheckBox.setSelected(false);
        managementCheckBox.setSelected(false);
        comCheckBox.setSelected(false);
        rhCheckBox.setSelected(false);
        dureeCourteCheckBox.setSelected(false);
        dureeMoyenneCheckBox.setSelected(false);
        dureeLongueCheckBox.setSelected(false);
        debutantCheckBox.setSelected(false);
        intermediaireCheckBox.setSelected(false);
        avanceCheckBox.setSelected(false);
        searchField.clear();
        filteredFormations = new ArrayList<>(allFormations);
        displayFormations(filteredFormations);
    }

    @FXML
    private void applyFilters() {
        List<Formation> result = new ArrayList<>(allFormations);
        String searchText = searchField.getText().toLowerCase().trim();
        if (!searchText.isEmpty()) {
            result = result.stream()
                    .filter(f -> f.getNomFormation().toLowerCase().contains(searchText) ||
                            f.getDescription().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
        }
        if (devCheckBox.isSelected() || managementCheckBox.isSelected() ||
                comCheckBox.isSelected() || rhCheckBox.isSelected()) {

            result = result.stream()
                    .filter(f -> (devCheckBox.isSelected() && f.getThemeFormation().equals("Développement")) ||
                            (managementCheckBox.isSelected() && f.getThemeFormation().equals("Management")) ||
                            (comCheckBox.isSelected() && f.getThemeFormation().equals("Communication")) ||
                            (rhCheckBox.isSelected() && f.getThemeFormation().equals("Ressources Humaines")))
                    .collect(Collectors.toList());
        }
        if (dureeCourteCheckBox.isSelected() || dureeMoyenneCheckBox.isSelected() ||
                dureeLongueCheckBox.isSelected()) {

            result = result.stream()
                    .filter(f -> (dureeCourteCheckBox.isSelected() && f.getDuree() < 5) ||
                            (dureeMoyenneCheckBox.isSelected() && f.getDuree() >= 5 && f.getDuree() <= 10) ||
                            (dureeLongueCheckBox.isSelected() && f.getDuree() > 10))
                    .collect(Collectors.toList());
        }
        if (debutantCheckBox.isSelected() || intermediaireCheckBox.isSelected() ||
                avanceCheckBox.isSelected()) {

            result = result.stream()
                    .filter(f -> (debutantCheckBox.isSelected() && f.getNiveauDifficulte().equals("Débutant")) ||
                            (intermediaireCheckBox.isSelected() && f.getNiveauDifficulte().equals("Intermédiaire")) ||
                            (avanceCheckBox.isSelected() && f.getNiveauDifficulte().equals("Avancé")))
                    .collect(Collectors.toList());
        }

        filteredFormations = result;
        displayFormations(filteredFormations);
    }

    private void displayFormations(List<Formation> formations) {
        formationsContainer.getChildren().clear();

        for (Formation formation : formations) {
            try {
                VBox card = createFormationCard(formation);
                formationsContainer.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (formations.isEmpty()) {
            Label noResult = new Label("Aucune formation ne correspond à vos critères");
            noResult.getStyleClass().add("no-result");
            formationsContainer.getChildren().add(noResult);
        }
    }

    private VBox createFormationCard(Formation formation) {
        VBox card = new VBox();
        card.getStyleClass().add("formation-card");
        card.setPrefWidth(250);
        card.setMaxWidth(250);
        card.setSpacing(10);

        ImageView imageView = new ImageView();
        try {
            if (formation.getImageUrl() != null && !formation.getImageUrl().isEmpty()) {
                Image image = new Image(formation.getImageUrl());
                imageView.setImage(image);
            } else {
                // Image par défaut
                Image defaultImage = new Image(getClass().getResourceAsStream("/images/default-formation.png"));
                imageView.setImage(defaultImage);
            }
        } catch (Exception e) {
            Image defaultImage = new Image(getClass().getResourceAsStream("/images/default-formation.png"));
            imageView.setImage(defaultImage);
        }

        imageView.setFitWidth(250);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        Label titleLabel = new Label(formation.getNomFormation());
        titleLabel.getStyleClass().add("formation-title");
        titleLabel.setWrapText(true);
        HBox infoBox = new HBox(10);
        Label categoryLabel = new Label(formation.getThemeFormation()); // Utiliser statut comme catégorie
        categoryLabel.getStyleClass().add("formation-category");
        Label levelLabel = new Label(formation.getNiveauDifficulte());
        levelLabel.getStyleClass().add("formation-level");
        infoBox.getChildren().addAll(categoryLabel, levelLabel);
        Label descLabel = new Label(getShortDescription(formation.getDescription(), 100));
        descLabel.getStyleClass().add("formation-description");
        descLabel.setWrapText(true);
        HBox detailsBox = new HBox(20);
        Label durationLabel = new Label(formation.getDuree() + " jours");
        durationLabel.getStyleClass().add("formation-duration");
        Label dateLabel = new Label(formation.getDateFormation().toString());
        dateLabel.getStyleClass().add("formation-date");
        detailsBox.getChildren().addAll(durationLabel, dateLabel);
        // Bouton pour voir les détails
//        Button detailsButton = new Button("Voir détails");
//        detailsButton.getStyleClass().add("details-button");
//        detailsButton.setMaxWidth(Double.MAX_VALUE);
       // detailsButton.setOnAction(e -> openFormationDetails(formation));

        // Ajouter tous les éléments à la carte
        card.getChildren().addAll(imageView, titleLabel, infoBox, descLabel, detailsBox);

        return card;
    }
    private String getShortDescription(String description, int maxLength) {
        if (description == null) {
            return "";
        }
        if (description.length() <= maxLength) {
            return description;
        }
        return description.substring(0, maxLength) + "...";
    }
//    private void openFormationDetails(Formation formation) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FormationDetailsView.fxml"));
//            Parent root = loader.load();
//
//            FormationDetailsController controller = loader.getController();
//            controller.setFormation(formation);
//
//            // Ouvrir dans une nouvelle fenêtre ou remplacer la vue actuelle
//            // Selon votre design d'application
//
//        } catch (IOException e) {
//            showAlert("Erreur", "Impossible d'ouvrir les détails de la formation.");
//            e.printStackTrace();
//        }
//    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}