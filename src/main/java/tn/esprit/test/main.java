package tn.esprit.test;

import tn.esprit.models.Teletravail;
import tn.esprit.services.ServiceTeletravail;
import tn.esprit.models.ReservationBureau;
import tn.esprit.services.ServiceReservationBureau;
import java.time.LocalDate;
import java.time.LocalTime;
import tn.esprit.models.Bureau;
import tn.esprit.services.ServiceBureau;

import java.util.List;

///////////////// TEST CRUD TELETRAVAIL /////////////////////
/*
public class main {
    public static void main(String[] args) {
        ServiceTeletravail service = new ServiceTeletravail();

        // Ajouter un télétravail
        Teletravail teletravail1 = new Teletravail(1, LocalDate.of(2025, 2, 12), LocalDate.of(2025, 2, 15), LocalDate.of(2025, 2, 16), "Approuvé", "Réunion importante à distance");
        boolean ajoutResult = service.add(teletravail1);
        System.out.println("Ajout du télétravail : " + (ajoutResult ? "Réussi" : "Échoué"));

        // Récupérer un télétravail par ID
        Teletravail retrievedTeletravail = service.getById(10);
        System.out.println("Télétravail récupéré : " + (retrievedTeletravail != null ? retrievedTeletravail : "Introuvable"));

        // Mettre à jour un télétravail
        if (retrievedTeletravail != null) {
            retrievedTeletravail.setRaisonTT("En attente");
            retrievedTeletravail.setRaisonTT("Réunion à distance et tâches urgentes");
            boolean updateResult = service.update(retrievedTeletravail);
            System.out.println("Mise à jour du télétravail : " + (updateResult ? "Réussi" : "Échoué"));
        }

        // Supprimer un télétravail
        boolean deleteResult = service.delete(service.getById(11).getIdTeletravail());
        System.out.println("Suppression du télétravail : " + (deleteResult ? "Réussi" : "Échoué"));

        // Vérifier si le télétravail a bien été supprimé
        Teletravail deletedTeletravail = service.getById(11);
        if (deletedTeletravail == null) {
            System.out.println("Télétravail supprimé avec succès.");
        } else {
            System.out.println("Erreur : Le télétravail n'a pas été supprimé.");
        }

        // Récupérer tous les télétravails
        System.out.println("\nListe de tous les télétravails :");
        for (Teletravail tt : service.getAll()) {
            System.out.println(tt);
        }
    }
}
 */

///////////////// TEST CRUD RESERVATION BUREAU /////////////////////


/*
public class main {

    public static void main(String[] args) {
        ServiceReservationBureau service = new ServiceReservationBureau();

        // Créer une nouvelle réservation
        ReservationBureau reservation1 = new ReservationBureau(1, 101, LocalDate.of(2025, 2, 12), LocalTime.of(2, 0), "Approuvé");
        boolean addResult = service.add(reservation1);
        System.out.println("Ajout de la réservation : " + (addResult ? "Réussi" : "Échoué"));

        // Récupérer une réservation par ID
        ReservationBureau retrievedReservation = service.getById(10);
        System.out.println("Réservation récupérée : " + retrievedReservation);

        // Mettre à jour une réservation
        retrievedReservation.setStatutReservation("Annulé");
        boolean updateResult = service.update(retrievedReservation);
        System.out.println("Mise à jour de la réservation : " + (updateResult ? "Réussi" : "Échoué"));

        // Supprimer une réservation
        boolean deleteResult = service.delete(service.getById(21).getIdReservation());
        System.out.println("Suppression de la réservation : " + (deleteResult ? "Réussi" : "Échoué"));

        // Vérifier si la réservation a été supprimée
        ReservationBureau deletedReservation = service.getById(reservation1.getIdReservation());
        if (deletedReservation == null) {
            System.out.println("Réservation supprimée avec succès.");
        } else {
            System.out.println("Erreur : La réservation n'a pas été supprimée.");
        }
    }
}
*/

///////////////// TEST CRUD BUREAUX /////////////////////

/*
public class main {
    public static void main(String[] args) {
        ServiceBureau serviceBureau = new ServiceBureau();

        // Ajouter un bureau
        Bureau bureau1 = new Bureau("Bureau1", 10, "Disponible");
        serviceBureau.add(bureau1);
        System.out.println("Bureau ajouté : " + bureau1);

        // Récupérer tous les bureaux
        List<Bureau> bureaux = serviceBureau.getAll();
        System.out.println("Liste des bureaux : " + bureaux);

        // Mettre à jour un bureau
        serviceBureau.getById(3).setCapacite(12);
        serviceBureau.update(serviceBureau.getById(3));
        System.out.println("Bureau mis à jour : " + bureau1);

        // Supprimer un bureau
        serviceBureau.delete(serviceBureau.getById(2).getIdBureau());
        System.out.println("Bureau supprimé : " + bureau1);
    }
}
*/