package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.*;
import tn.esprit.services.ServiceOffre;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AjouterOffreController {

    ServiceOffre serviceOffre = new ServiceOffre();

    @FXML
    private TextField txtTitre;

    @FXML
    private TextField serarch;
    @FXML
    private TextField txtcompetences;

    @FXML
    private DatePicker DateExpiration;

    @FXML
    private DatePicker txtDateCreation;

    @FXML
    private TextField txtdescription;

    @FXML
    private ChoiceBox<Niveauetudes> niveauetudeschoice;

    @FXML
    private ChoiceBox<Niveaulangues> niveaulangueschoice;

    @FXML
    private TextField txtlocalisation;



    @FXML
    private ChoiceBox<experience> experiencechoice;



    @FXML
    private ChoiceBox<TypeContrat> typecontratbox;

    @FXML
    private ListView<Offreemploi> listOffre;


    private Offreemploi selectedOffre;
    @FXML
    private CheckBox trititre;
    @FXML
    private CheckBox triniveauetudes;

    @FXML
    void ajouteroffre(ActionEvent event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Niveauetudes niveauEtudes = niveauetudeschoice.getValue();
        Niveaulangues niveauLangues = niveaulangueschoice.getValue();
        experience experience = experiencechoice.getValue();
        TypeContrat typecontrat = typecontratbox.getValue();
        LocalDateTime dateCreation = txtDateCreation.getValue().atStartOfDay();
        LocalDateTime dateExpiration = DateExpiration.getValue().atStartOfDay();
        if (dateCreation.isAfter(dateExpiration)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date de création doit être inférieure à la date d'expiration.");
            return;
        }
        if (txtTitre.getText().isEmpty() || txtdescription.getText().isEmpty() || txtcompetences.getText().isEmpty() || typecontrat == null || txtlocalisation.getText().isEmpty() ||
                niveauEtudes == null || niveauLangues == null || experience == null || dateCreation == null || dateExpiration == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Tous les champs sont obligatoires.");
            return;
        }

        Offreemploi offreemploi = new Offreemploi(
                txtTitre.getText(),
                txtdescription.getText(),
                 experience,
                 niveauEtudes,
                txtcompetences.getText(),
               typecontrat,
                txtlocalisation.getText(),
               niveauLangues,
                dateCreation,
                dateExpiration,
                null);
        serviceOffre.add(offreemploi);
        loadOffres();
        clearOffreFields();
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void Afficher(ActionEvent event) {
        List<Offreemploi> offres = serviceOffre.getAll();
        ObservableList<Offreemploi> offresAffichees = FXCollections.observableArrayList();
        offresAffichees.addAll(offres);
        listOffre.setItems(offresAffichees);

    }


    void loadOffres() {
        List<Offreemploi> offres = serviceOffre.getAll();
        ObservableList<Offreemploi> offresAffichees = FXCollections.observableArrayList(offres);
        listOffre.setItems(offresAffichees);
    }




    @FXML
    void initialize() {
        niveauetudeschoice.getItems().setAll(Niveauetudes.values());
        niveaulangueschoice.getItems().setAll(Niveaulangues.values());
        experiencechoice.getItems().setAll(experience.values());
        typecontratbox.getItems().setAll(TypeContrat.values());
        listOffre.setOnMouseClicked(event -> {
            selectedOffre = listOffre.getSelectionModel().getSelectedItem();
            if (selectedOffre != null) {
                System.out.println("Offre sélectionnée : " + selectedOffre.getTitre());
                txtTitre.setText(selectedOffre.getTitre());
                txtdescription.setText(selectedOffre.getDescription());
                DateExpiration.setValue(selectedOffre.getDateExpiration().toLocalDate());
                txtcompetences.setText(selectedOffre.getCompetences());
                txtlocalisation.setText(selectedOffre.getLocalisation());
                txtDateCreation.setValue(selectedOffre.getDateCreation().toLocalDate());
                niveauetudeschoice.setValue(selectedOffre.getNiveauEtudes());
                niveaulangueschoice.setValue(selectedOffre.getNiveaulangues());
                experiencechoice.setValue(selectedOffre.getExperiencerequise());
                typecontratbox.setValue(selectedOffre.getTypecontrat());
            }
        });
        loadOffres();
    }

    @FXML
    void Modifier(ActionEvent event) {
        if (selectedOffre != null) {
            selectedOffre.setTitre(txtTitre.getText());
            selectedOffre.setDescription(txtdescription.getText());
            selectedOffre.setDateExpiration(DateExpiration.getValue().atStartOfDay());
            experience experiencerequise = experiencechoice.getValue();
            Niveauetudes niveauEtudes = niveauetudeschoice.getValue();
            Niveaulangues niveaulangues = niveaulangueschoice.getValue();
            TypeContrat typecontrat = typecontratbox.getValue();
            if (experiencerequise != null) {
                selectedOffre.setExperiencerequise(experiencerequise);
            } else {
                System.out.println("Veuillez sélectionner une expérience requise.");
                return;
            }
            if (niveauEtudes != null) {
                selectedOffre.setNiveauEtudes(niveauEtudes);
            } else {
                System.out.println("Veuillez sélectionner un niveau d'études.");
                return;
            }

            if (niveaulangues != null) {
                selectedOffre.setNiveaulangues(niveaulangues);
            } else {
                System.out.println("Veuillez sélectionner un niveau de langue.");
                return;
            }

            if (typecontrat != null) {
                selectedOffre.setTypecontrat(typecontrat);
            } else {
                System.out.println("Veuillez sélectionner un type de contrat.");
                return;
            }

            selectedOffre.setCompetences(txtcompetences.getText());
            selectedOffre.setLocalisation(txtlocalisation.getText());
            selectedOffre.setDateCreation(txtDateCreation.getValue().atStartOfDay());
            serviceOffre.update(selectedOffre);
            loadOffres();  // Rechargement des offres
            System.out.println("Offre d'emploi modifiée avec succès.");
        } else {
            System.out.println("Veuillez sélectionner une offre à modifier.");
        }
    }









    @FXML
    void Delete(ActionEvent event) {
        Offreemploi selectedOffre = listOffre.getSelectionModel().getSelectedItem();
        if (selectedOffre != null) {
            int offreId = selectedOffre.getId();
            serviceOffre.delete(offreId);
            listOffre.getItems().remove(selectedOffre);
            loadOffres();
            System.out.println("Offre d'emploi supprimée.");
        } else {
            System.out.println("Aucune offre sélectionnée pour suppression.");
        }

    }
    private void clearOffreFields() {
        txtTitre.clear();
        txtdescription.clear();
        txtcompetences.clear();

        txtlocalisation.clear();
        niveauetudeschoice.setValue(null);
        niveaulangueschoice.setValue(null);
        experiencechoice.setValue(null);
        txtDateCreation.setValue(null);
        DateExpiration.setValue(null);
        typecontratbox.setValue(null);
    }
    Comparator<Offreemploi> triParTitre = Comparator.comparing(Offreemploi::getTitre);
    Comparator<Offreemploi> triParNiveauEtude = Comparator.comparing(Offreemploi::getNiveauEtudes);

    @FXML
    void trititre(ActionEvent event) {
        List<Offreemploi> offres = serviceOffre.getAll();
        offres.sort(triParTitre);
        ObservableList<Offreemploi> offresTriees = FXCollections.observableArrayList(offres);
        listOffre.setItems(offresTriees);
    }


    @FXML
    void triniveauetudes(ActionEvent event) {
        List<Offreemploi> offres = serviceOffre.getAll();
        Comparator<Offreemploi> triParNiveauEtude = Comparator.comparing(Offreemploi::getNiveauEtudes)
                .reversed();

        offres.sort(triParNiveauEtude);
        ObservableList<Offreemploi> offresTriees = FXCollections.observableArrayList(offres);
        listOffre.setItems(offresTriees);
    }
    @FXML
    void rechercherid(ActionEvent event) {
        List<Offreemploi> offres = serviceOffre.getAll();
        String recherche = serarch.getText().toLowerCase();
        List<Offreemploi> offresFiltrees = offres.stream()
                .filter(offre -> offre.getTitre().toLowerCase().contains(recherche))
                .collect(Collectors.toList());
        ObservableList<Offreemploi> offresTriees = FXCollections.observableArrayList(offresFiltrees);
        listOffre.setItems(offresTriees);

    }


}




