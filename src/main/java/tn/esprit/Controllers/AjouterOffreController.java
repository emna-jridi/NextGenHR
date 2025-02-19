package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import tn.esprit.Models.Offreemploi;
import tn.esprit.Services.ServiceOffre;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AjouterOffreController {

    ServiceOffre serviceOffre = new ServiceOffre();

    @FXML
    private TextField txtTitre;
    @FXML
    private TextField txtcandidatures;

    @FXML
    private TextField txtcompetences;

    @FXML
    private DatePicker DateExpiration;

    @FXML
    private DatePicker txtDateCreation;

    @FXML
    private TextField txtdescription;

    @FXML
    private TextField txtexperience;

    @FXML
    private TextField txtlocalisation;

    @FXML
    private TextField txtniveauetudes;

    @FXML
    private TextField txtnivlangues;

    @FXML
    private TextField txtstatut;

    @FXML
    private TextField txttypecontrat;

    @FXML
    private ListView<Offreemploi> listOffre;


    private Offreemploi selectedOffre;

    @FXML
    void ajouteroffre(ActionEvent event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (txtTitre.getText().isEmpty() || txtcompetences.getText().isEmpty() ||
                txtniveauetudes.getText().isEmpty() || txtexperience.getText().isEmpty()) {
            System.out.println("Tous les champs obligatoires doivent être remplis !");
            return;
        }
        LocalDateTime dateCreation = txtDateCreation.getValue().atStartOfDay();
        LocalDateTime dateExpiration = DateExpiration.getValue().atStartOfDay();
        Offreemploi offreemploi = new Offreemploi(
                Integer.parseInt(txtcandidatures.getText()),
                txtTitre.getText(),
                txtdescription.getText(),
                txtexperience.getText(),
                txtniveauetudes.getText(),
                txtcompetences.getText(),
                txttypecontrat.getText(),
                txtlocalisation.getText(),
                txtnivlangues.getText(),
                dateCreation,
                dateExpiration,
                txtstatut.getText(),
                null //
        );
        serviceOffre.add(offreemploi);


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
        listOffre.setOnMouseClicked(event -> {
            selectedOffre = listOffre.getSelectionModel().getSelectedItem();
            if (selectedOffre != null) {
                System.out.println("Offre sélectionnée : " + selectedOffre.getTitre());//testi
                txtTitre.setText(selectedOffre.getTitre());
                txtdescription.setText(selectedOffre.getDescription());
                DateExpiration.setValue(selectedOffre.getDateExpiration().toLocalDate());
                txtexperience.setText(selectedOffre.getExperiencerequise());
                txtniveauetudes.setText(selectedOffre.getNiveauEtudes());
                txtcompetences.setText(selectedOffre.getCompetences());
                txttypecontrat.setText(selectedOffre.getTypecontrat());
                txtlocalisation.setText(selectedOffre.getLocalisation());
                txtnivlangues.setText(selectedOffre.getNiveaulangues());
                txtDateCreation.setValue(selectedOffre.getDateCreation().toLocalDate());
                txtstatut.setText(selectedOffre.getStatut());
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
            selectedOffre.setExperiencerequise(txtexperience.getText());
            selectedOffre.setNiveauEtudes(txtniveauetudes.getText());
            selectedOffre.setCompetences(txtcompetences.getText());
            selectedOffre.setTypecontrat(txttypecontrat.getText());
            selectedOffre.setLocalisation(txtlocalisation.getText());
            selectedOffre.setNiveaulangues(txtnivlangues.getText());
            selectedOffre.setDateCreation(txtDateCreation.getValue().atStartOfDay());
            selectedOffre.setStatut(txtstatut.getText());
            serviceOffre.update(selectedOffre);
            loadOffres();
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

    }}


