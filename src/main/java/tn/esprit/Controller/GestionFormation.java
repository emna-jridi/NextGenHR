package tn.esprit.Controller;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import tn.esprit.models.Formation;
import tn.esprit.services.ServiceFormation;

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
    @FXML private Pagination pagination;
    private List<Formation> allFormations = new ArrayList<>();
    private List<Formation> filteredFormations = new ArrayList<>();
    private final int FORMATIONS_PER_PAGE = 6;

    private ServiceFormation serviceFormation = new ServiceFormation();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFormationsFromDatabase();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilters();
        });
        displayFormations(allFormations);
        setupPagination();
    }

    private void loadFormationsFromDatabase() {
        // Utiliser votre service pour charger les formations
        allFormations = serviceFormation.getAll();
        filteredFormations = new ArrayList<>(allFormations);
    }

    private void setupPagination() {
        // Calculer le nombre de pages nécessaires
        int pageCount = (int) Math.ceil((double) filteredFormations.size() / FORMATIONS_PER_PAGE);
        pagination.setPageCount(pageCount);

        // Définir le gestionnaire de pages
        pagination.setPageFactory(this::createPage);

        // Assurer que la page actuelle est valide
        if (pagination.getCurrentPageIndex() >= pageCount) {
            pagination.setCurrentPageIndex(0);
        }
    }
    private Node createPage(int pageIndex) {
        // Calculer l'index de début et de fin pour cette page
        int fromIndex = pageIndex * FORMATIONS_PER_PAGE;
        int toIndex = Math.min(fromIndex + FORMATIONS_PER_PAGE, filteredFormations.size());

        // Obtenir les formations pour cette page
        List<Formation> pageFormations = filteredFormations.subList(fromIndex, toIndex);

        // Afficher ces formations
        formationsContainer.getChildren().clear();

        for (Formation formation : pageFormations) {
            try {
                VBox card = createFormationCard(formation);
                formationsContainer.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return formationsContainer;
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
       // setupPagination();
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
       // card.setPadding(new Insets(10));
        VBox.setMargin(card, new Insets(0, 0, 0, 15));
        ImageView imageView = new ImageView();

        try {
            if (formation.getImageUrl() != null && !formation.getImageUrl().isEmpty()) {
                // Pour les chemins locaux (images uploadées)
                if (formation.getImageUrl().startsWith("resources/") || formation.getImageUrl().startsWith("/resources/")) {
                    // Utiliser le fichier local
                    Image image = new Image("file:" + formation.getImageUrl());
                    imageView.setImage(image);
                } else {
                    // Pour les URL web
                    Image image = new Image(formation.getImageUrl());
                    imageView.setImage(image);
                }
            } else {
                // Image par défaut
                Image defaultImage = new Image(getClass().getResourceAsStream("/images/default-formation.png"));
                imageView.setImage(defaultImage);
            }
        } catch (Exception e) {
            System.out.println("Erreur de chargement d'image: " + e.getMessage());
            // Image par défaut en cas d'erreur
            Image defaultImage = new Image(getClass().getResourceAsStream("/images/default-formation.png"));
            imageView.setImage(defaultImage);
        }

        card.setOnMouseClicked(event -> {
            openFormationLink(formation);
        });
        card.setStyle("-fx-cursor: hand;");

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
    private void openFormationLink(Formation formation) {
        if (formation.getLien_formation() != null && !formation.getLien_formation().isEmpty()) {
            try {
                // Ouvrir le lien dans le navigateur par défaut
                java.awt.Desktop.getDesktop().browse(new java.net.URI(formation.getLien_formation()));
            } catch (Exception e) {
                showAlert("Erreur", "Impossible d'ouvrir le lien : " + formation.getLien_formation());
                e.printStackTrace();
            }
        } else {
            showAlert("Information", "Aucun lien disponible pour cette formation.");
        }
    }
}