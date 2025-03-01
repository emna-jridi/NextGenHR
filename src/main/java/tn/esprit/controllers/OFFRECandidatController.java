package tn.esprit.controllers;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import tn.esprit.models.Offreemploi;
import tn.esprit.services.ServiceOffre;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class OFFRECandidatController {
    private ServiceOffre serviceOffre = new ServiceOffre();






        @FXML
        private TableView<Offreemploi> tableoffres;

    @FXML
    private TableColumn<Offreemploi, String> compétenceid;

    @FXML
    private TableColumn<Offreemploi, String> datecreationid;

    @FXML
    private TableColumn<Offreemploi, String> dateexpirationid;
    @FXML
    private AnchorPane contentPanee;

    @FXML
    private TableColumn<Offreemploi, String> descriptionid;

    @FXML
    private TableColumn<Offreemploi, String> experienceid;

    @FXML
    private TableColumn<Offreemploi, Integer> joursrestantsid;

    @FXML
    private TableColumn<Offreemploi, String> localisationid;

    @FXML
    private TableColumn<Offreemploi, String> niveauetudesid;

    @FXML
    private TableColumn<Offreemploi, String> niveaulangueid;

    @FXML
    private TableColumn<Offreemploi, String> titreid;

    @FXML
    private TableColumn<Offreemploi, String> typecontratid;



    @FXML
    public void initialize() {
        titreid.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionid.setCellValueFactory(new PropertyValueFactory<>("description"));
        datecreationid.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        dateexpirationid.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        compétenceid.setCellValueFactory(new PropertyValueFactory<>("competences"));
        experienceid.setCellValueFactory(new PropertyValueFactory<>("experiencerequise"));
        localisationid.setCellValueFactory(new PropertyValueFactory<>("localisation"));
        niveauetudesid.setCellValueFactory(new PropertyValueFactory<>("niveauEtudes"));
        niveaulangueid.setCellValueFactory(new PropertyValueFactory<>("niveaulangues"));
        typecontratid.setCellValueFactory(new PropertyValueFactory<>("typecontrat"));
        joursrestantsid.setCellValueFactory(cellData -> {
            Offreemploi offre = cellData.getValue();
            LocalDate today = LocalDate.now();

            if (offre.getDateExpiration() != null) {
                LocalDate expirationDate = offre.getDateExpiration().toLocalDate();
                long joursRestants = ChronoUnit.DAYS.between(today, expirationDate);
                return new SimpleIntegerProperty((int) joursRestants).asObject();
            } else {
                return new SimpleIntegerProperty(0).asObject();
            }
        });
        joursrestantsid.setCellFactory(column -> new TableCell<Offreemploi, Integer>() {
            @Override
            protected void updateItem(Integer joursRestants, boolean empty) {
                super.updateItem(joursRestants, empty);

                if (empty || joursRestants == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(joursRestants.toString());

                    // Appliquer la couleur en fonction de la valeur
                    if (joursRestants < 5) {
                        setStyle("-fx-background-color: red; -fx-text-fill: white;");
                    } else if (joursRestants >= 5 && joursRestants <= 10) {
                        setStyle("-fx-background-color: orange; -fx-text-fill: black;");
                    } else {
                        setStyle("-fx-background-color: green; -fx-text-fill: white;");
                    }
                }
            }
        });
        loadOffres();
    }



    private void loadOffres() {
        List<Offreemploi> offres = serviceOffre.getAll();
        ObservableList<Offreemploi> offresAffichees = FXCollections.observableArrayList(offres);
        tableoffres.setItems(offresAffichees);
    }

    private void switchView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane pane = loader.load();

            // Ajout d'une animation de transition
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), pane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            contentPanee.getChildren().setAll(pane);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de " + fxmlPath);
            e.printStackTrace();
        }
    }


    @FXML
    void postulermaintenant(ActionEvent event) {
        switchView("/DemandeCandidature.fxml");
    }


    }



