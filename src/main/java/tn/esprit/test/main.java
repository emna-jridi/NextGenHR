package tn.esprit.test;

import tn.esprit.models.ReservationBureau;
import tn.esprit.models.Teletravail;
import tn.esprit.services.ServiceRservationBureau;
import tn.esprit.services.ServiceTeletravail;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.time.LocalDate;

public class main {
    public static void main(String[] args) {
        ServiceTeletravail teletravailService = new ServiceTeletravail();
        ServiceRservationBureau reservationService = new ServiceRservationBureau();

        // Test Télétravail
        Teletravail tt = new Teletravail(1, LocalDate.now(), LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 5), "En attente", "Projet important");
        teletravailService.add(tt);
        System.out.println("Télétravail ajouté : " + teletravailService.getById(tt.getIdTeletravail()));

        tt.setStatutTT("Approuvé");
        teletravailService.update(tt);
        System.out.println("Télétravail mis à jour : " + teletravailService.getById(tt.getIdTeletravail()));

        teletravailService.delete(tt.getIdTeletravail());
        System.out.println("Télétravail supprimé : " + teletravailService.getById(tt.getIdTeletravail()));

        // Test Réservation Bureau
        ReservationBureau res = new ReservationBureau(2, 101, LocalDate.now(), LocalTime.now(), "Confirmée");
        reservationService.add(res);
        System.out.println("Réservation ajoutée : " + reservationService.getById(res.getIdReservation()));

        res.setStatutReservation("Annulée");
        reservationService.update(res);
        System.out.println("Réservation mise à jour : " + reservationService.getById(res.getIdReservation()));

        reservationService.delete(res.getIdReservation());
        System.out.println("Réservation supprimée : " + reservationService.getById(res.getIdReservation()));
    }
}
