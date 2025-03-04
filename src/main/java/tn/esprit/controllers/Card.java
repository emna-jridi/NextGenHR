package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import tn.esprit.models.Formation;

public class Card {
    @FXML
    private AnchorPane cardRoot;
    @FXML
    private ImageView imageFormation;
    @FXML
    private Label nomFormation;

    @FXML
    private Label themeFormation;

    @FXML
    private Label description;

    @FXML
    private Label dateFormation;

    @FXML
    private Label niveauDifficulte;

    public void setFormationData(String imageUrl, String nom, String theme, String desc, String date, String niveau) {
        imageFormation.setImage(new Image(imageUrl));
        nomFormation.setText(nom);
        themeFormation.setText(theme);
        description.setText(desc);
        dateFormation.setText(date);
        niveauDifficulte.setText(niveau);
    }
}
