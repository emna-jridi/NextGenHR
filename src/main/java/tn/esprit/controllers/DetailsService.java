package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tn.esprit.models.Service;

public class DetailsService {

    @FXML private Label lblNomService;
    @FXML private Label lbldescriptionService;
    @FXML private Label lbltypeService;
    @FXML private Label lblDateDebut;
    @FXML private Label lblDateFin;
    @FXML private Label lblStatut;

    public void setService(Service service) {
        lblNomService.setText(service.getNomService());
        lbldescriptionService.setText(service.getDescriptionService());
        lbltypeService.setText(service.getTypeService());
        lblDateDebut.setText("" + service.getDateDebutService());
        lblDateFin.setText("" + service.getDateFinService());
        lblStatut.setText(service.getStatusService());





    }
}
