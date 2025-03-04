package tn.esprit.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import tn.esprit.models.Candidature;

import tn.esprit.models.Offreemploi;
import tn.esprit.models.Statut;
import tn.esprit.services.ServiceCandidature;
import tn.esprit.services.ServiceOffre;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CandidaturesarchivéesController {
    @FXML
    private TableView<Candidature> tableacceptées;

    @FXML
    private TableView<Candidature> tabledisqualifiées;
    @FXML
    private TableColumn<Candidature, String> CVaccep;

    @FXML
    private TableColumn<Candidature, String> Dateaccep;

    @FXML
    private TableColumn<Candidature, String> Lettreaccep;

    @FXML
    private TableColumn<Candidature, String> Offre;

    @FXML
    private TableColumn<Candidature, String> Offreaccep;

    @FXML
    private TableColumn<Candidature, String> Statut;

    @FXML
    private TableColumn<Candidature, String> Statutaccep;

    @FXML
    private TableColumn<Candidature, String> cv;

    @FXML
    private TableColumn<Candidature, String> date;

    @FXML
    private TableColumn<Candidature, String> email;

    @FXML
    private TableColumn<Candidature, String> emailaccep;

    @FXML
    private TableColumn<Candidature, String> lettre;

    @FXML
    private TableColumn<Candidature, String> nom;

    @FXML
    private TableColumn<Candidature, String> nomaccep;

    @FXML
    private TableColumn<Candidature, String> prenom;

    @FXML
    private TableColumn<Candidature, String> prenomaccep;
    @FXML
    private TableColumn<Candidature, String> telephone;

    @FXML
    private TableColumn<Candidature, String> telephoneaccep;

    @FXML
    private CheckBox triarchives;



    private ObservableList<Candidature> acceptéesList = FXCollections.observableArrayList();
    private ObservableList<Candidature> disqualifiéesList = FXCollections.observableArrayList();


    private final ServiceCandidature serviceCandidature = new ServiceCandidature();
    private final ServiceOffre serviceOffre = new ServiceOffre();

    public void initialize() {
        List<Candidature> acceptéesList = serviceCandidature.getByStatut(tn.esprit.models.Statut.acceptée);
        List<Candidature> disqualifiéesList = serviceCandidature.getByStatut(tn.esprit.models.Statut.disqualifiée);

        System.out.println("Acceptées list size: " + acceptéesList.size());
        System.out.println("Disqualifiées list size: " + disqualifiéesList.size());


        ObservableList<Candidature> acceptéesObservable = FXCollections.observableArrayList(acceptéesList);
        ObservableList<Candidature> disqualifiéesObservable = FXCollections.observableArrayList(disqualifiéesList);
        nomaccep.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        prenomaccep.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
        emailaccep.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        telephoneaccep.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelephone()));
        Dateaccep.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateCandidature().toString()));
        CVaccep.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCvUrl()));
        Lettreaccep.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLettreMotivation()));
        Statutaccep.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatut().name()));



        nom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        prenom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
        email.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        telephone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelephone()));
        date.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateCandidature().toString()));
        cv.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCvUrl()));
        lettre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLettreMotivation()));
        Statut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatut().name()));

        tableacceptées.setItems(acceptéesObservable);
        tabledisqualifiées.setItems(disqualifiéesObservable);
    }
    Comparator<Candidature> triParDateCandidature = Comparator.comparing(Candidature::getDateCandidature);

    @FXML
    void triarchives(ActionEvent event) {
        if (triarchives.isSelected()) {
        List<Candidature> acceptéesList = serviceCandidature.getByStatut(tn.esprit.models.Statut.acceptée);
        List<Candidature> disqualifiéesList = serviceCandidature.getByStatut(tn.esprit.models.Statut.disqualifiée);
        acceptéesList.sort(triParDateCandidature);
        disqualifiéesList.sort(triParDateCandidature);
        ObservableList<Candidature> acceptéesObservable = FXCollections.observableArrayList(acceptéesList);
        ObservableList<Candidature> disqualifiéesObservable = FXCollections.observableArrayList(disqualifiéesList);
        tableacceptées.setItems(acceptéesObservable);
        tabledisqualifiées.setItems(disqualifiéesObservable);
        } else {
            List<Candidature> acceptéesList = serviceCandidature.getByStatut(tn.esprit.models.Statut.acceptée);
            List<Candidature> disqualifiéesList = serviceCandidature.getByStatut(tn.esprit.models.Statut.disqualifiée);
            ObservableList<Candidature> acceptéesObservable = FXCollections.observableArrayList(acceptéesList);
            ObservableList<Candidature> disqualifiéesObservable = FXCollections.observableArrayList(disqualifiéesList);
            tableacceptées.setItems(acceptéesObservable);
            tabledisqualifiées.setItems(disqualifiéesObservable);
        }
    }



}



