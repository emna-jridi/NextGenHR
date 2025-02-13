package tn.esprit.test;

import tn.esprit.models.*;
import tn.esprit.services.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ServiceTeletravail serviceTeletravail = new ServiceTeletravail();
        ServiceReservationBureau serviceReservationBureau = new ServiceReservationBureau();
        ServiceBureau serviceBureau = new ServiceBureau();

        while (true) {
            System.out.println("\nMENU TEST CRUD");
            System.out.println("1. Gestion des Télétravails");
            System.out.println("2. Gestion des Réservations de Bureau");
            System.out.println("3. Gestion des Bureaux");
            System.out.println("4. Quitter");
            System.out.print("Choisissez une option : ");
            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    menuTeletravail(scanner, serviceTeletravail);
                    break;
                case 2:
                    menuReservationBureau(scanner, serviceReservationBureau);
                    break;
                case 3:
                    menuBureau(scanner, serviceBureau);
                    break;
                case 4:
                    System.out.println("Fermeture du programme.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }

    private static void menuTeletravail(Scanner scanner, ServiceTeletravail service) {
        System.out.println("\nGestion des Télétravails");
        System.out.println("1. Ajouter");
        System.out.println("2. Consulter par ID");
        System.out.println("3. Modifier");
        System.out.println("4. Supprimer");
        System.out.print("Choisissez une option : ");
        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1:
                Teletravail teletravail = new Teletravail(3, LocalDate.now(), LocalDate.of(2025, 2, 15), LocalDate.of(2025, 2, 16), "Approuvé", "Réunion à distance");
                System.out.println("Ajout : " + (service.add(teletravail) ? "Réussi" : "Échoué"));
                break;
            case 2:
                System.out.print("ID : ");
                int idT = scanner.nextInt();
                System.out.println(service.getById(idT));
                break;
            case 3:
                System.out.print("ID : ");
                int idUpdate = scanner.nextInt();
                Teletravail tt = service.getById(idUpdate);
                if (tt != null) {
                    tt.setStatutTT("En attente");
                    System.out.println("Mise à jour : " + (service.update(tt) ? "Réussi" : "Échoué"));
                } else System.out.println("Introuvable.");
                break;
            case 4:
                System.out.print("ID : ");
                int idDelete = scanner.nextInt();
                System.out.println("Suppression : " + (service.delete(idDelete) ? "Réussi" : "Échoué"));
                break;
            default:
                System.out.println("Option invalide.");
        }
    }

    private static void menuReservationBureau(Scanner scanner, ServiceReservationBureau service) {
        System.out.println("\nGestion des Réservations");
        System.out.println("1. Ajouter");
        System.out.println("2. Consulter par ID");
        System.out.println("3. Modifier");
        System.out.println("4. Supprimer");
        System.out.print("Choisissez une option : ");
        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1:
                ReservationBureau reservation = new ReservationBureau(3, 1, LocalDate.of(2025, 2, 12), LocalTime.of(2, 0), "Approuvé");
                System.out.println("Ajout : " + (service.add(reservation) ? "Réussi" : "Échoué"));
                break;
            case 2:
                System.out.print("ID : ");
                int idR = scanner.nextInt();
                System.out.println(service.getById(idR));
                break;
            case 3:
                System.out.print("ID : ");
                int idUpdate = scanner.nextInt();
                ReservationBureau rb = service.getById(idUpdate);
                if (rb != null) {
                    rb.setStatutReservation("Annulé");
                    System.out.println("Mise à jour : " + (service.update(rb) ? "Réussi" : "Échoué"));
                } else System.out.println("Introuvable.");
                break;
            case 4:
                System.out.print("ID : ");
                int idDelete = scanner.nextInt();
                System.out.println("Suppression : " + (service.delete(idDelete) ? "Réussi" : "Échoué"));
                break;
            default:
                System.out.println("Option invalide.");
        }
    }

    private static void menuBureau(Scanner scanner, ServiceBureau service) {
        System.out.println("\nGestion des Bureaux");
        System.out.println("1. Ajouter");
        System.out.println("2. Afficher tous");
        System.out.println("3. Modifier");
        System.out.println("4. Supprimer");
        System.out.print("Choisissez une option : ");
        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1:
                Bureau bureau = new Bureau("103", 10, "Disponible");
                System.out.println("Ajout : " + (service.add(bureau) ? "Réussi" : "Échoué"));
                break;
            case 2:
                List<Bureau> bureaux = service.getAll();
                bureaux.forEach(System.out::println);
                break;
            case 3:
                System.out.print("ID : ");
                int idUpdate = scanner.nextInt();
                Bureau b = service.getById(idUpdate);
                if (b != null) {
                    b.setCapacite(12);
                    System.out.println("Mise à jour : " + (service.update(b) ? "Réussi" : "Échoué"));
                } else System.out.println("Introuvable.");
                break;
            case 4:
                System.out.print("ID : ");
                int idDelete = scanner.nextInt();
                System.out.println("Suppression : " + (service.delete(idDelete) ? "Réussi" : "Échoué"));
                break;
            default:
                System.out.println("Option invalide.");
        }
    }
}
