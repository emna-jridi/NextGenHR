package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.Models.Candidature;
import tn.esprit.Models.Offreemploi;
import tn.esprit.Services.ServiceCandidature;
import tn.esprit.Services.ServiceOffre;

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



    ServiceCandidature serviceCandidature = new ServiceCandidature();
    ServiceOffre serviceOffre = new ServiceOffre();

    @FXML
    public void initialize() {
        ObservableList<Offreemploi> offres = FXCollections.observableArrayList(serviceOffre.getAll());
        listeoffres.setItems(offres);
        listeoffres.setOnAction(event -> selectionner(event));
        loadCandidatures();



    }





    @FXML
    void selectionner(ActionEvent event) {
        Offreemploi selectedOffre = listeoffres.getSelectionModel().getSelectedItem();
        if (selectedOffre != null) {
            int idOffre = selectedOffre.getId();
            utiliserIdOffre(idOffre);
        }
    }

    private void utiliserIdOffre(int id) {
        System.out.println("ID sélectionné : " + id);

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
            candidature.setTelephone(txtTelephone.getText())
            candidature.setOffreemploi(selectedOffre);
            serviceCandidature.ajouter(candidature);
            System.out.println("Candidature ajoutée avec succès !");

        } else {

            System.out.println("Veuillez sélectionner une offre.");
        }
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
        ObservableList<Candidature> candidaturesAffichees = FXCollections.observableArrayList(candidatures);
        listcandidats.setItems(candidaturesAffichees);
    }


    void loadCandidatures() {
    List<Candidature> candidatures = serviceCandidature.getAll();
    ObservableList<Candidature> candidaturesAffichees = FXCollections.observableArrayList(candidatures);
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
    @FXML
    void Modifier(ActionEvent event) {
        Candidature selectedCandidature = listcandidats.getSelectionModel().getSelectedItem();
        if (selectedCandidature != null) {
            selectedCandidature.setStatut(txtStatut.getText());
            selectedCandidature.setCvUrl(txtCv.getText());
            selectedCandidature.setLettreMotivation(txtLettre.getText());
            selectedCandidature.setNom(txtNom.getText());
            selectedCandidature.setPrenom(txtPrenom.getText());
            selectedCandidature.setEmail(txtEmail.getText());
            selectedCandidature.setTelephone(txtTelephone.getText());
            selectedCandidature.setDateCandidature(dateCandidature.getValue().atStartOfDay());
            Offreemploi selectedOffre = listeoffres.getSelectionModel().getSelectedItem();
            if (selectedOffre != null) {
                selectedCandidature.setOffreemploi(selectedOffre);
                serviceCandidature.updatee(selectedCandidature, selectedOffre);
                loadCandidatures();
                System.out.println("Candidature modifiée avec succès.");
            } else {
                System.out.println("Veuillez sélectionner une offre.");
            }
        } else {
            System.out.println("Veuillez sélectionner une candidature à modifier.");
        }

    }









}



