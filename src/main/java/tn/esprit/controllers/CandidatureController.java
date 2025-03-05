package tn.esprit.controllers;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import tn.esprit.models.Candidature;
import tn.esprit.models.Offreemploi;
import tn.esprit.models.Statut;
import tn.esprit.services.ServiceCandidature;
import tn.esprit.services.ServiceMailCandidature;
import tn.esprit.services.ServiceOffre;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;


public class CandidatureController {

    @FXML
    private CheckBox tricandidature;


    @FXML
    private ListView<Candidature> listcandidats;

    @FXML
    private ComboBox<Offreemploi> listeoffres;
















    @FXML
    private ChoiceBox<Statut> statutchoice;

    @FXML
    private AnchorPane contentPane;

    private Candidature selectedcand;

    private final ServiceCandidature serviceCandidature = new ServiceCandidature();
    private final ServiceOffre serviceOffre = new ServiceOffre();



    @FXML
    public void initialize() {
        loadCandidatures();

        statutchoice.getItems().setAll(Arrays.asList(Statut.values()));
        listcandidats.setOnMouseClicked(event -> {
            selectedcand = listcandidats.getSelectionModel().getSelectedItem();
            if (selectedcand != null) {

                statutchoice.setValue(selectedcand.getStatut());

            }
        });

    }

    void loadCandidatures() {
        List<Candidature> candidatures = serviceCandidature.getAll();
        List<Candidature> candidaturesEnCours = candidatures.stream()
                .filter(c -> c.getStatut() == Statut.En_cours)
                .collect(Collectors.toList());

        ObservableList<Candidature> candidaturesAffichees = FXCollections.observableArrayList(candidaturesEnCours);
        listcandidats.setItems(candidaturesAffichees);
    }

    /*void loadOffres() {
        List<Offreemploi> offres = serviceOffre.getAll();
        ObservableList<Offreemploi> offresAffichees = FXCollections.observableArrayList(offres);
        listeoffres.setItems(offresAffichees);
    }*/
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




    @FXML
    void Modifier(ActionEvent event) {
        Candidature selectedCandidature = listcandidats.getSelectionModel().getSelectedItem();
        if (selectedCandidature == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Aucune offre sélectionnée pour modification.");
            return;
        }

        String oldStatut = selectedCandidature.getStatut().name();
        String newStatut = statutchoice.getValue().name();

        if (newStatut.equals(oldStatut)) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Aucune modification détectée.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Êtes-vous sûr de vouloir modifier le statut de cette candidature ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            selectedCandidature.setStatut(Statut.valueOf(newStatut));
            serviceCandidature.update(selectedCandidature);

            if (newStatut.equals("acceptée") || newStatut.equals("disqualifiée")) {
                listcandidats.getItems().remove(selectedCandidature);
            }
            loadCandidatures();

            ServiceMailCandidature serviceEmail = new ServiceMailCandidature();
            String destinataire = selectedCandidature.getEmail();
            String sujet = "Mise à jour de votre candidature";
            String contenu = "";

            if (newStatut.equals("acceptée")) {
                contenu = "Madame/Monsieur " + selectedCandidature.getPrenom() + ",\n\n"
                        + "Nous avons le plaisir de vous informer que votre candidature a été retenue.\n\n"
                        + "Félicitations ! Nous vous invitons à prendre contact avec notre équipe RH.\n\n"
                        + "Cordialement,\nL'équipe Ressources Humaines\n";
            } else if (newStatut.equals("disqualifiée")) {
                contenu = "Madame/Monsieur " + selectedCandidature.getPrenom() + ",\n\n"
                        + "Nous regrettons de vous informer que votre candidature n'a pas été retenue.\n\n"
                        + "Nous vous souhaitons beaucoup de succès pour l'avenir.\n\n"
                        + "Cordialement,\nL'équipe Ressources Humaines\n";
            }

            serviceEmail.sendEmail(destinataire, sujet, contenu);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Statut modifié avec succès.");
        }
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




    private void clearFields() {

        statutchoice.setValue(null);

    }


    @FXML
    void Afficher(ActionEvent event) {
        List<Candidature> candidatures = serviceCandidature.getAll();
        List<Candidature> candidaturesEnCours = candidatures.stream()
                .filter(c -> c.getStatut() == Statut.En_cours)
                .collect(Collectors.toList());
        ObservableList<Candidature> candidaturesAffichees = FXCollections.observableArrayList(candidaturesEnCours);
        listcandidats.setItems(candidaturesAffichees);
    }


    @FXML
    void Supprimer(ActionEvent event) {
        Candidature selectedCandidature = listcandidats.getSelectionModel().getSelectedItem();
        if (selectedCandidature != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText("Suppression d'une candidature");
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette candidature ?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int candidatureId = selectedCandidature.getId();
                serviceCandidature.delete(candidatureId);
                listcandidats.getItems().remove(selectedCandidature);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Suppression réussie");
                successAlert.setHeaderText(null);
                successAlert.setContentText("La candidature a été supprimée avec succès !");
                successAlert.showAndWait();

                System.out.println("Candidature supprimée.");
                clearFields();
            }
        } else {

            Alert errorAlert = new Alert(Alert.AlertType.WARNING);
            errorAlert.setTitle("Aucune sélection");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Veuillez sélectionner une candidature à supprimer.");
            errorAlert.showAndWait();
        }
    }

    @FXML
    void exportToPDF(ActionEvent event) {
        Candidature selectedCandidature = listcandidats.getSelectionModel().getSelectedItem();
        if (selectedCandidature == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur d'export", "Aucune candidature sélectionnée");
            return;
        }

        String exportType = getExportType();

        FileChooser fileChooser = new FileChooser();

        if (exportType.equals("Candidature")) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                try (PDDocument document = new PDDocument()) {
                    PDPage page = new PDPage();
                    document.addPage(page);

                    PDPageContentStream contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                    contentStream.setNonStrokingColor(0, 51, 102);
                    contentStream.newLineAtOffset(100, 750);
                    contentStream.showText("Candidature - " + selectedCandidature.getNom() + " " + selectedCandidature.getPrenom());
                    contentStream.newLineAtOffset(0, -30);

                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.setNonStrokingColor(0, 0, 0);
                    contentStream.showText("Nom: " + selectedCandidature.getNom());
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Prénom: " + selectedCandidature.getPrenom());
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Email: " + selectedCandidature.getEmail());
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Téléphone: " + selectedCandidature.getTelephone());
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Statut: " + selectedCandidature.getStatut());
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.endText();
                    contentStream.close();

                    document.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de créer le PDF");
                }
            }
        } else if (exportType.equals("CV")) {
            String cvPath = selectedCandidature.getCvUrl();
            if (cvPath == null || cvPath.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur d'export", "Aucun CV trouvé pour cette candidature");
                return;
            }

            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File destinationFile = fileChooser.showSaveDialog(new Stage());

            if (destinationFile != null) {
                try {
                    Files.copy(Paths.get(cvPath), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "CV exporté avec succès !");
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'exporter le CV");
                }
            }
        } else if (exportType.equals("Lettre de motivation")) {
            String lettrePath = selectedCandidature.getLettreMotivation();
            if (lettrePath == null || lettrePath.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur d'export", "Aucune lettre de motivation trouvée");
                return;
            }

            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File destinationFile = fileChooser.showSaveDialog(new Stage());

            if (destinationFile != null) {
                try {
                    Files.copy(Paths.get(lettrePath), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Lettre de motivation exportée avec succès !");
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'exporter la lettre de motivation");
                }
            }
        }
    }


    private void exportCandidatureDetails(PDPageContentStream contentStream, Candidature selectedCandidature) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.setNonStrokingColor(0, 102, 204);  // Couleur bleue
        contentStream.showText("Informations personnelles");
        contentStream.newLineAtOffset(0, -20);

        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.setNonStrokingColor(0, 0, 0);  // Noir pour le texte
        contentStream.showText("Nom: " + selectedCandidature.getNom());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Prénom: " + selectedCandidature.getPrenom());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Email: " + selectedCandidature.getEmail());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Téléphone: " + selectedCandidature.getTelephone());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Statut: " + selectedCandidature.getStatut());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Date de candidature: " + selectedCandidature.getDateCandidature());
        contentStream.newLineAtOffset(0, -15);
    }

    private void exportCvDetails(PDPageContentStream contentStream, Candidature selectedCandidature) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.setNonStrokingColor(0, 102, 204);
        contentStream.showText("CV");
        contentStream.newLineAtOffset(0, -20);

        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.setNonStrokingColor(0, 0, 0);
        contentStream.showText("CV URL: " + selectedCandidature.getCvUrl());
        contentStream.newLineAtOffset(0, -15);
    }

    private void exportLettreDetails(PDPageContentStream contentStream, Candidature selectedCandidature) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.setNonStrokingColor(0, 102, 204);
        contentStream.showText("Lettre de Motivation");
        contentStream.newLineAtOffset(0, -20);

        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.setNonStrokingColor(0, 0, 0);
        contentStream.showText("Lettre de motivation: " + selectedCandidature.getLettreMotivation());
        contentStream.newLineAtOffset(0, -15);
    }

    private String getExportType() {
        List<String> options = Arrays.asList("Candidature", "CV", "Lettre de motivation");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Candidature", options);
        dialog.setTitle("Choix du type d'export");
        dialog.setHeaderText("Sélectionnez le type de document à exporter");
        dialog.setContentText("Exporter :");
        Optional<String> result = dialog.showAndWait();

        return result.orElse("Candidature");
    }

    Comparator<Candidature> triParDateCandidature = Comparator.comparing(Candidature::getDateCandidature);


    @FXML
    private List<Candidature> candidaturesOriginales = new ArrayList<>();

    @FXML
    void triercand(ActionEvent event) {
        List<Candidature> candidatures = serviceCandidature.getAll();
        List<Candidature> candidaturesEnCours = candidatures.stream()
                .filter(c -> c.getStatut() == Statut.En_cours)
                .collect(Collectors.toList());
        if (tricandidature.isSelected()) {
            candidaturesEnCours.sort(triParDateCandidature);
            ObservableList<Candidature> candidaturesTriees = FXCollections.observableArrayList(candidaturesEnCours);
            listcandidats.setItems(candidaturesTriees);
        } else {
            if (candidaturesOriginales.isEmpty()) {
                candidaturesOriginales = serviceCandidature.getAll().stream()
                        .filter(c -> c.getStatut() == Statut.En_cours)
                        .collect(Collectors.toList());
            }
            ObservableList<Candidature> candidaturesRestaurées = FXCollections.observableArrayList(candidaturesOriginales);
            listcandidats.setItems(candidaturesRestaurées);
        }
    }




    @FXML
    void archivecandidature(ActionEvent event) {
        switchView("/Candidaturesarchivées.fxml");
    }
    private void switchView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane pane = loader.load();
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), pane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            contentPane.getChildren().setAll(pane);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de " + fxmlPath);
            e.printStackTrace();
        }
    }
}








