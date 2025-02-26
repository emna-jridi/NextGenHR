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
            return; // Empêche l'ajout si des champs sont vides
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
                null //
        );
        serviceOffre.add(offreemploi);
        loadOffres();
        clearOffreFields();
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Pas de texte d'en-tête
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
                System.out.println("Offre sélectionnée : " + selectedOffre.getTitre());//testi
                txtTitre.setText(selectedOffre.getTitre());
                txtdescription.setText(selectedOffre.getDescription());
                DateExpiration.setValue(selectedOffre.getDateExpiration().toLocalDate());
             //   txtexperience.setText(selectedOffre.getExperiencerequise());
              //  txtniveauetudes.setText(selectedOffre.getNiveauEtudes());
                txtcompetences.setText(selectedOffre.getCompetences());

                txtlocalisation.setText(selectedOffre.getLocalisation());
               // txtnivlangues.setText(selectedOffre.getNiveaulangues());
                txtDateCreation.setValue(selectedOffre.getDateCreation().toLocalDate());
                niveauetudeschoice.setValue(selectedOffre.getNiveauEtudes()); // Assurez-vous que c'est de type Niveauetudes
                niveaulangueschoice.setValue(selectedOffre.getNiveaulangues()); // Assurez-vous que c'est de type Niveaulangues
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

            // Affectation des énumérations en s'assurant que les valeurs sélectionnées sont correctes
            experience experiencerequise = experiencechoice.getValue();  // Énumération experience
            Niveauetudes niveauEtudes = niveauetudeschoice.getValue();   // Énumération Niveauetudes
            Niveaulangues niveaulangues = niveaulangueschoice.getValue(); // Énumération Niveaulangues
            TypeContrat typecontrat = typecontratbox.getValue();         // Énumération TypeContrat

            // Vérification des valeurs de chaque énumération pour éviter une valeur null
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

            // Mise à jour de l'offre
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
        niveauetudeschoice.setValue(null); // Réinitialiser la valeur du choix
        niveaulangueschoice.setValue(null); // Réinitialiser la valeur du choix
        experiencechoice.setValue(null); // Réinitialiser la valeur du choix
        txtDateCreation.setValue(null); // Réinitialiser la date de création
        DateExpiration.setValue(null);// Réinitialiser la date d'expiration
        typecontratbox.setValue(null);
    }
    Comparator<Offreemploi> triParTitre = Comparator.comparing(Offreemploi::getTitre);
    Comparator<Offreemploi> triParNiveauEtude = Comparator.comparing(Offreemploi::getNiveauEtudes);

    @FXML
    void trititre(ActionEvent event) {
        List<Offreemploi> offres = serviceOffre.getAll();
        offres.sort(triParTitre); // Appliquer le tri par titre
        ObservableList<Offreemploi> offresTriees = FXCollections.observableArrayList(offres);
        listOffre.setItems(offresTriees); // Mettre à jour la ListView
    }


    @FXML
    void triniveauetudes(ActionEvent event) {
        List<Offreemploi> offres = serviceOffre.getAll();

        // Comparateur qui trie du plus élevé au plus bas en inversant l'ordre
        Comparator<Offreemploi> triParNiveauEtude = Comparator.comparing(Offreemploi::getNiveauEtudes)
                .reversed(); // Inverser l'ordre pour du plus élevé au plus bas

        offres.sort(triParNiveauEtude); // Appliquer le tri
        ObservableList<Offreemploi> offresTriees = FXCollections.observableArrayList(offres);
        listOffre.setItems(offresTriees); // Mettre à jour la ListView
    }

    @FXML
    void rechercherid(ActionEvent event) {
        List<Offreemploi> offres = serviceOffre.getAll();

        // Récupère le texte de la recherche
        String recherche = serarch.getText().toLowerCase(); // Convertir en minuscules pour ignorer la casse

        // Filtrer les offres en fonction du titre
        List<Offreemploi> offresFiltrees = offres.stream()
                .filter(offre -> offre.getTitre().toLowerCase().contains(recherche))
                .collect(Collectors.toList());

        // Mettre à jour la ListView avec les offres filtrées
        ObservableList<Offreemploi> offresTriees = FXCollections.observableArrayList(offresFiltrees);
        listOffre.setItems(offresTriees); // Mettre à jour la ListView

    }


}




