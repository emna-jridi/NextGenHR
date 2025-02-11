package tn.esprit.test;

import tn.esprit.models.Teletravail;
import tn.esprit.services.ServiceTeletravail;
import tn.esprit.models.ReservationBureau;
import tn.esprit.services.ServiceRservationBureau;
import java.sql.Date;
import java.sql.Time;
import tn.esprit.utils.MyDatabase;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*ServiceTeletravail service = new ServiceTeletravail();

        // Ajouter un télétravail
        //Teletravail tt = new Teletravail(1, LocalDate.now(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), "En attente", "Raisons personnelles");
        //service.add(tt);

        // Récupérer tous les télétravails
        List<Teletravail> teletravails = service.getAll();
        for (Teletravail t : teletravails) {
            System.out.println(t);
        }
        // Supposons que nous voulons mettre à jour le télétravail avec l'ID 1
        /*Teletravail tt = service.getById(2); // Récupérer le télétravail existant
        if (tt != null) {
            tt.setStatutTT("Approuvé");
            tt.setRaisonTT("Changement de projet");

            service.update(tt); // Mettre à jour le télétravail
        } else {
            System.out.println("Télétravail non trouvé !");
        }
        Teletravail tt2 = service.getById(2); // Récupérer le télétravail existant
        if (tt2 != null) {

            service.delete(tt2); //supprimer le télétravail
        } else {
            System.out.println("Télétravail non trouvé !");
        }

    }*/


        ServiceRservationBureau service = new ServiceRservationBureau();

        /*// ✅ Ajouter une réservation
        ReservationBureau newReservation = new ReservationBureau(1, 2, Date.valueOf("2025-02-20"), Time.valueOf("10:00:00"), "Confirmée");
        service.add(newReservation);
*/
        // ✅ Récupérer toutes les réservations
        System.out.println("Liste des réservations après ajout : ");
        service.getAll().forEach(r -> System.out.println(r));

        // ✅ Mettre à jour une réservation
        ReservationBureau updatedReservation = new ReservationBureau(1, 2, Date.valueOf("2025-02-21"), Time.valueOf("11:00:00"), "Annulée");
        updatedReservation.setIdReservation(3); // Assurez-vous que l'ID est correct
        service.update(updatedReservation);

        // ✅ Supprimer une réservation
        service.delete(5);
    }
}
