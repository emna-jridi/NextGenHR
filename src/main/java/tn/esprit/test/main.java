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
            scanner.nextLine(); // Consommer la ligne restante

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

    // Menu pour gérer les télétravails
    private static void menuTeletravail(Scanner scanner, ServiceTeletravail service) {
        System.out.println("\nGestion des Télétravails");
        System.out.println("1. Ajouter");
        System.out.println("2. Consulter par ID");
        System.out.println("3. Modifier");
        System.out.println("4. Supprimer");
        System.out.print("Choisissez une option : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer la ligne restante

        switch (choix) {
            case 1:
                Teletravail teletravail = new Teletravail(3, LocalDate.now(), LocalDate.of(2025, 2, 15), LocalDate.of(2025, 2, 16), "Approuvé", "Réunion à distance");
                if (service.add(teletravail)) {
                    System.out.println("Ajout réussi.");
                } else {
                    System.out.println("Echec de l'ajout.");
                }
                break;
            case 2:
                System.out.print("ID : ");
                int idT = scanner.nextInt();
                Teletravail teletravailFound = service.getById(idT);
                if (teletravailFound == null) {
                    System.out.println("Aucun télétravail trouvé pour cet ID.");
                } else {
                    System.out.println(teletravailFound);
                }
                break;
            case 3:
                System.out.print("ID : ");
                int idUpdate = scanner.nextInt();
                scanner.nextLine(); // Consommer la ligne restante
                Teletravail teletravailUpdate = service.getById(idUpdate);
                if (teletravailUpdate != null) {
                    teletravailUpdate.setStatutTT("En attente");
                    if (service.update(teletravailUpdate)) {
                        System.out.println("Mise à jour réussie.");
                    } else {
                        System.out.println("Echec de la mise à jour.");
                    }
                } else {
                    System.out.println("Télétravail introuvable.");
                }
                break;
            case 4:
                System.out.print("ID : ");
                int idDelete = scanner.nextInt();
                if (service.delete(idDelete)) {
                    System.out.println("Suppression réussie.");
                } else {
                    System.out.println("Impossible de supprimer ce télétravail.");
                }
                break;
            default:
                System.out.println("Option invalide.");
        }
    }

    // Menu pour gérer les réservations de bureaux
    private static void menuReservationBureau(Scanner scanner, ServiceReservationBureau service) {
        System.out.println("\nGestion des Réservations");
        System.out.println("1. Ajouter");
        System.out.println("2. Consulter par ID");
        System.out.println("3. Modifier");
        System.out.println("4. Supprimer");
        System.out.print("Choisissez une option : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer la ligne restante

        switch (choix) {
            case 1:
                ReservationBureau reservation = new ReservationBureau(3, 1, LocalDate.of(2025, 2, 12), LocalTime.of(2, 0), "Approuvé");
                if (service.add(reservation)) {
                    System.out.println("Ajout réussi.");
                } else {
                    System.out.println("Echec de l'ajout.");
                }
                break;
            case 2:
                System.out.print("ID : ");
                int idR = scanner.nextInt();
                ReservationBureau reservationFound = service.getById(idR);
                if (reservationFound != null) {
                    System.out.println(reservationFound);
                } else {
                    System.out.println("Réservation introuvable.");
                }
                break;
            case 3:
                System.out.print("ID : ");
                int idUpdate = scanner.nextInt();
                ReservationBureau rb = service.getById(idUpdate);
                if (rb != null) {
                    rb.setStatutReservation("Annulé");
                    if (service.update(rb)) {
                        System.out.println("Mise à jour réussie.");
                    } else {
                        System.out.println("Echec de la mise à jour.");
                    }
                } else {
                    System.out.println("Réservation introuvable.");
                }
                break;
            case 4:
                System.out.print("ID : ");
                int idDelete = scanner.nextInt();
                if (service.delete(idDelete)) {
                    System.out.println("Suppression réussie.");
                } else {
                    System.out.println("Impossible de supprimer cette réservation.");
                }
                break;
            default:
                System.out.println("Option invalide.");
        }
    }

    // Menu pour gérer les bureaux
    private static void menuBureau(Scanner scanner, ServiceBureau service) {
        System.out.println("\nGestion des Bureaux");
        System.out.println("1. Ajouter");
        System.out.println("2. Afficher tous");
        System.out.println("3. Modifier");
        System.out.println("4. Supprimer");
        System.out.print("Choisissez une option : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer la ligne restante

        switch (choix) {
            case 1:
                Bureau bureau = new Bureau("105", 10, "Disponible");
                if (service.add(bureau)) {
                    System.out.println("Ajout réussi.");
                } else {
                    System.out.println("Echec de l'ajout.");
                }
                break;
            case 2:
                List<Bureau> bureaux = service.getAll();
                if (!bureaux.isEmpty()) {
                    bureaux.forEach(System.out::println);
                } else {
                    System.out.println("Aucun bureau trouvé.");
                }
                break;
            case 3:
                System.out.print("ID : ");
                int idUpdate = scanner.nextInt();
                Bureau bureauUpdate = service.getById(idUpdate);
                if (bureauUpdate != null) {
                    bureauUpdate.setCapacite(12);
                    if (service.update(bureauUpdate)) {
                        System.out.println("Mise à jour réussie.");
                    } else {
                        System.out.println("Echec de la mise à jour.");
                    }
                } else {
                    System.out.println("Bureau introuvable.");
                }
                break;
            case 4:
                System.out.print("ID : ");
                int idDelete = scanner.nextInt();
                if (service.delete(idDelete)) {
                    System.out.println("Suppression réussie.");
                } else {
                    System.out.println("Impossible de supprimer ce bureau.");
                }
                break;
            default:
                System.out.println("Option invalide.");
        }
    }
}
