package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.Candidature;
import tn.esprit.models.Offreemploi;
import tn.esprit.services.ServiceCandidature;
import tn.esprit.services.ServiceOffre;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CandidatureController {



    @FXML
    private ListView<Candidature> listcandidats;

    @FXML
    private ComboBox<Offreemploi> listeoffres;


    @FXML
    private TextField txtCv;

    @FXML
    private DatePicker dateCandidature;

    @FXML
    private TextField txtEmail;



    @FXML
    private TextField txtLettre;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private TextField txtStatut;

    @FXML
    private TextField txtTelephone;
    private Candidature selectedcand;

    private final ServiceCandidature serviceCandidature = new ServiceCandidature();
    private final ServiceOffre serviceOffre = new ServiceOffre();

    @FXML
    public void initialize() {
        listcandidats.setOnMouseClicked(event -> {
            selectedcand = listcandidats.getSelectionModel().getSelectedItem();
            if (selectedcand != null) {
                txtNom.setText(selectedcand.getNom());
                txtPrenom.setText(selectedcand.getPrenom());
                txtEmail.setText(selectedcand.getEmail());
                txtTelephone.setText(selectedcand.getTelephone());
                txtCv.setText(selectedcand.getCvUrl());
                txtLettre.setText(selectedcand.getLettreMotivation());
                txtStatut.setText(selectedcand.getStatut());
                dateCandidature.setValue(selectedcand.getDateCandidature().toLocalDate());
                listeoffres.setValue(selectedcand.getOffreemploi());
            }
        });
        loadCandidatures();
        loadOffres();
    }

    void loadCandidatures() {
        List<Candidature> candidatures = serviceCandidature.getAll();
        ObservableList<Candidature> candidaturesAffichees = FXCollections.observableArrayList(candidatures);
        listcandidats.setItems(candidaturesAffichees);
    }

    void loadOffres() {
        List<Offreemploi> offres = serviceOffre.getAll();
        ObservableList<Offreemploi> offresAffichees = FXCollections.observableArrayList(offres);
        listeoffres.setItems(offresAffichees);
    }

    @FXML
    void Modifier(ActionEvent event) {
        Candidature selectedCandidature = listcandidats.getSelectionModel().getSelectedItem();
        if (selectedCandidature == null) {
            System.out.println("Veuillez sélectionner une candidature à modifier.");
            return;
        }
        selectedCandidature.setStatut(txtStatut.getText());
        selectedCandidature.setCvUrl(txtCv.getText());
        selectedCandidature.setLettreMotivation(txtLettre.getText());
        selectedCandidature.setNom(txtNom.getText());
        selectedCandidature.setPrenom(txtPrenom.getText());
        selectedCandidature.setEmail(txtEmail.getText());
        selectedCandidature.setTelephone(txtTelephone.getText());

        LocalDate date = dateCandidature.getValue();
        if (date != null) {
            selectedCandidature.setDateCandidature(date.atStartOfDay());
        } else {
            System.out.println("Veuillez sélectionner une date de candidature.");
            return;
        }

        Offreemploi selectedOffre = listeoffres.getSelectionModel().getSelectedItem();
        if (selectedOffre != null) {
            selectedCandidature.setOffreemploi(selectedOffre);
        } else {
            System.out.println("Veuillez sélectionner une offre.");
            return;
        }

        serviceCandidature.update(selectedCandidature);
        loadCandidatures();
        System.out.println("Candidature modifiée avec succès.");
        clearFields();
    }
    private void utiliserIdOffre(int id) {
        System.out.println("ID sélectionné : " + id);

    }

    @FXML
    void selectionner(ActionEvent event) {
        Offreemploi selectedOffre = listeoffres.getSelectionModel().getSelectedItem();
        if (selectedOffre != null) {
            int idOffre = selectedOffre.getId();
            utiliserIdOffre(idOffre);
        }
    }


    @FXML
    void Ajouter(ActionEvent event) {
        Offreemploi selectedOffre = listeoffres.getValue();

        if (selectedOffre != null) {

            Candidature candidature = new Candidature();
            candidature.setDateCandidature(LocalDateTime.now());
            candidature.setStatut(txtStatut.getText());
            candidature.setCvUrl(txtCv.getText());
            candidature.setLettreMotivation(txtLettre.getText());
            candidature.setNom(txtNom.getText());
            candidature.setPrenom(txtPrenom.getText());
            candidature.setEmail(txtEmail.getText());
            candidature.setTelephone(txtTelephone.getText());
            candidature.setOffreemploi(selectedOffre);
            serviceCandidature.ajouter(candidature);
            System.out.println("Candidature ajoutée avec succès !");

        } else {

            System.out.println("Veuillez sélectionner une offre.");
        }
        clearFields();
    }

    private void clearFields() {
        txtCv.clear();
        dateCandidature.setValue(null);
        txtEmail.clear();
        txtLettre.clear();
        txtNom.clear();
        txtPrenom.clear();
        txtStatut.clear();
        txtTelephone.clear();
    }


    @FXML
    void Afficher(ActionEvent event) {
        List<Candidature> candidatures = serviceCandidature.getAll();
        ObservableList<Candidature> candidaturesAffichees = FXCollections.observableArrayList();
        listcandidats.setItems(candidaturesAffichees);
    }
    @FXML
    void Supprimer(ActionEvent event) {
        Candidature selectedCandidature = listcandidats.getSelectionModel().getSelectedItem();

        if (selectedCandidature != null) {
            int candidatureId = selectedCandidature.getId();
            serviceCandidature.delete(candidatureId);
            listcandidats.getItems().remove(selectedCandidature);

            System.out.println("Candidature supprimée.");
        } else {
            System.out.println("Aucune candidature sélectionnée pour suppression.");
        }

    }
}



