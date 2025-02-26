package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import tn.esprit.models.Candidature;
import tn.esprit.models.Offreemploi;
import tn.esprit.services.ServiceCandidature;
import tn.esprit.services.ServiceOffre;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


public class CandidatureController {

    @FXML
    private CheckBox tricandidature;

    @FXML
    private Label alertemail;

    @FXML
    private ListView<Candidature> listcandidats;

    @FXML
    private ComboBox<Offreemploi> listeoffres;


    @FXML
    private TextField txtCv;
    @FXML
    private Button importLettre;

    @FXML
    private DatePicker dateCandidature;

    @FXML
    private TextField txtEmail;

    @FXML
    private Label labeltelephone;


    @FXML
    private TextField txtLettre;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private TextField txtStatut;
    @FXML
    private Button importCV;

    @FXML
    private TextField txtTelephone;
    private Candidature selectedcand;

    private final ServiceCandidature serviceCandidature = new ServiceCandidature();
    private final ServiceOffre serviceOffre = new ServiceOffre();

    @FXML
    public void initialize() {
        txtTelephone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,8}")) { // Autorise uniquement jusqu'à 8 chiffres
                txtTelephone.setText(oldValue); // Rétablit l'ancienne valeur si dépassement
            }

            if (newValue.length() < 8) {
                txtTelephone.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                labeltelephone.setText("Le numéro doit contenir exactement 8 chiffres !");
                labeltelephone.setTextFill(javafx.scene.paint.Color.RED);
                labeltelephone.setVisible(true);
            } else {
                txtTelephone.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
                labeltelephone.setText("Numéro valide !");
                labeltelephone.setTextFill(javafx.scene.paint.Color.GREEN);
                labeltelephone.setVisible(true);
            }
        });
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                txtEmail.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                alertemail.setText("Email invalide ! Format attendu : xxxx@xxx.xx");
                alertemail.setTextFill(javafx.scene.paint.Color.RED);
                alertemail.setVisible(true);
            } else {
                txtEmail.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
                alertemail.setText("Email valide !");
                alertemail.setTextFill(javafx.scene.paint.Color.GREEN);
                alertemail.setVisible(true);
            }
        });
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
        for (Candidature c : candidatures) {
            if (c.getOffreemploi() == null) {
                System.out.println("Candidature sans offre associée : " + c.getId());
            }
        }
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
        String oldStatut = selectedCandidature.getStatut(); // Stocker l'ancien statut
        String newStatut = txtStatut.getText();


        selectedCandidature.setStatut(txtStatut.getText());
        selectedCandidature.setCvUrl(txtCv.getText());
        selectedCandidature.setLettreMotivation(txtLettre.getText());
        selectedCandidature.setNom(txtNom.getText());
        selectedCandidature.setPrenom(txtPrenom.getText());
        selectedCandidature.setEmail(txtEmail.getText());
        selectedCandidature.setTelephone(txtTelephone.getText());
        selectedCandidature.setStatut(txtStatut.getText());

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
        if (!txtEmail.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("L'email saisi est invalide. Veuillez entrer un email au format xxxx@xxx.xx");
            alert.showAndWait();
            return; // Stoppe l'ajout
        }
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
            loadCandidatures();
            loadOffres();
            candidature.setOffreemploi(serviceOffre.getbyid((selectedOffre.getId())));


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
        ObservableList<Candidature> candidaturesAffichees = FXCollections.observableArrayList(candidatures); // Ajouter les candidatures à la liste observable
        listcandidats.setItems(candidaturesAffichees); // Mettre à jour la ListView
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
    void importerCV(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tous les fichiers", "*.*"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());  // Crée une nouvelle fenêtre pour ouvrir un fichier

        if (selectedFile != null) {
            txtCv.setText(selectedFile.getAbsolutePath());  // Affiche le chemin du fichier dans ton TextField
            System.out.println("Fichier sélectionné : " + selectedFile.getAbsolutePath());
        }
    }


    @FXML
    void importerLettre(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documents Word", "*.docx", "*.doc"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tous les fichiers", "*.*"));

        // Utiliser la fenêtre principale (stage) pour ouvrir le file chooser
        Stage stage = (Stage) importLettre.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            txtLettre.setText(selectedFile.getAbsolutePath());  // Affiche le chemin du fichier dans ton TextField
            System.out.println("Fichier Lettre de Motivation sélectionné : " + selectedFile.getAbsolutePath());
        }
    }


    @FXML
    void exportToPDF(ActionEvent event) {
        Candidature selectedCandidature = listcandidats.getSelectionModel().getSelectedItem();
        if (selectedCandidature == null) {
            System.out.println("Veuillez sélectionner une candidature.");
            return;
        }

        // Créer un FileChooser pour choisir l'emplacement du fichier PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            // Créer un document PDF avec PDFBox
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(100, 750);

                // Ajouter les informations de la candidature
                contentStream.showText("Candidature : ");
                contentStream.newLineAtOffset(0, -15);  // Espacement vertical entre les lignes

                contentStream.showText("Nom: " + selectedCandidature.getNom());
                contentStream.newLineAtOffset(0, -15);

                contentStream.showText("Prénom: " + selectedCandidature.getPrenom());
                contentStream.newLineAtOffset(0, -15);

                contentStream.showText("Email: " + selectedCandidature.getEmail());
                contentStream.newLineAtOffset(0, -15);

                contentStream.showText("Téléphone: " + selectedCandidature.getTelephone());
                contentStream.newLineAtOffset(0, -15);

                contentStream.showText("CV: " + selectedCandidature.getCvUrl());
                contentStream.newLineAtOffset(0, -15);

                contentStream.showText("Lettre de motivation: " + selectedCandidature.getLettreMotivation());
                contentStream.newLineAtOffset(0, -15);

                contentStream.showText("Statut: " + selectedCandidature.getStatut());
                contentStream.newLineAtOffset(0, -15);

                contentStream.showText("Date de candidature: " + selectedCandidature.getDateCandidature());
                contentStream.newLineAtOffset(0, -15);

                contentStream.showText("Offre d'emploi: " + selectedCandidature.getOffreemploi().getTitre());
                contentStream.newLineAtOffset(0, -15);

                contentStream.showText("Description : " + selectedCandidature.getOffreemploi().getDescription());
                contentStream.newLineAtOffset(0, -15);


                contentStream.showText("Compétences " + selectedCandidature.getOffreemploi().getCompetences());
                contentStream.newLineAtOffset(0, -15);

                contentStream.showText("Experience: " + selectedCandidature.getOffreemploi().getExperiencerequise());
                contentStream.newLineAtOffset(0, -15);

                contentStream.showText("Niveau d'études: " + selectedCandidature.getOffreemploi().getNiveauEtudes());
                contentStream.newLineAtOffset(0, -15);

                contentStream.showText("Niveau Langues: " + selectedCandidature.getOffreemploi().getNiveaulangues());
                contentStream.newLineAtOffset(0, -15);

                contentStream.showText("Type Contrat: " + selectedCandidature.getOffreemploi().getTypecontrat());
                contentStream.newLineAtOffset(0, -15);

                contentStream.showText("Localisation: " + selectedCandidature.getOffreemploi().getLocalisation());
                contentStream.newLineAtOffset(0, -15);

                contentStream.showText("Date Création: " + selectedCandidature.getOffreemploi().getDateCreation());
                contentStream.newLineAtOffset(0, -15);

                contentStream.showText("Date expiration: " + selectedCandidature.getOffreemploi().getDateExpiration());
                contentStream.newLineAtOffset(0, -15);

                contentStream.endText();
                contentStream.close();


                // Sauvegarder le document PDF dans le fichier choisi par l'utilisateur
                document.save(file);
                System.out.println("Candidature exportée en PDF avec succès!");

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erreur lors de la création du PDF.");
            }
        }
    }
    Comparator<Candidature> triParDateCandidature = Comparator.comparing(Candidature::getDateCandidature);


    @FXML
    void triercand(ActionEvent event) {
        List<Candidature> candidatures = serviceCandidature.getAll();

        // Appliquer le tri par date
        candidatures.sort(triParDateCandidature);

        // Créer une ObservableList à partir des candidatures triées
        ObservableList<Candidature> candidaturesTriees = FXCollections.observableArrayList(candidatures);

        // Mettre à jour la ListView avec les candidatures triées
        listcandidats.setItems(candidaturesTriees);

    }
    }








