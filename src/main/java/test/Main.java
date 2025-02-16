package test;

import entities.Reunion;
import entities.conge;
import services.ServiceConge;
import services.ServiceReunion;
import utils.DBConnection;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Vérifier la connexion à la base de données
        DBConnection dbConnection = DBConnection.getInstance();
        if (dbConnection.getCon() != null) {
            System.out.println("✅ Connection status: Connected");

            // Créer une instance de ServiceConge
            ServiceConge sp = new ServiceConge();
            /*
            // 🔹 Ajouter un nouvel objet Conge
            conge newConge = new conge(0, "Vacances", new Date(), new Date(), "En attente");
            sp.add(newConge);
            System.out.println("✅ Congé ajouté avec succès !");

            // 🔹 Récupérer et afficher tous les congés
            List<conge> conges = sp.getAll();
            System.out.println("📋 Liste des congés :");
            for (conge c : conges) {
                System.out.println("🆔 ID: " + c.getId() + ", Type: " + c.getType_conge() +
                        ", Début: " + c.getDate_debut() + ", Fin: " + c.getDate_fin() +
                        ", Statut: " + c.getStatus());
            }

            // 🔹 Mise à jour d'un congé
            int idToUpdate = 3;
            boolean foundForUpdate = false;
            for (conge c : conges) {
                if (c.getId() == idToUpdate) {
                    c.setType_conge("Congé Annuel");
                    c.setDate_debut(new Date());
                    c.setDate_fin(new Date(System.currentTimeMillis() + 86400000L * 5));
                    c.setStatus("Approuvé");
                    sp.update(c);
                    System.out.println("✅ Congé avec ID " + idToUpdate + " mis à jour !");
                    foundForUpdate = true;
                    break;
                }
            }

            if (!foundForUpdate) {
                System.out.println("❌ ID " + idToUpdate + " non trouvé pour mise à jour !");
            }

            // 🔹 Suppression d'un congé
            int idToDelete = 4;
            boolean foundForDelete = false;
            for (conge c : sp.getAll()) {
                if (c.getId() == idToDelete) {
                    sp.delete(c);
                    System.out.println("🗑️ Congé avec ID " + idToDelete + " supprimé !");
                    foundForDelete = true;
                    break;
                }
            }
            if (!foundForDelete) {
                System.out.println("❌ ID " + idToDelete + " non trouvé pour suppression !");
            }

            // ******************** CRUD Réunion ********************

            // Créer une instance de ServiceReunion
            ServiceReunion sr = new ServiceReunion();

            // 🔹 Ajouter une réunion
            Reunion newReunion = new Reunion(0, "Réunion stratégique", "Discussion des objectifs", new Date(), "Stratégie");
            sr.add(newReunion);
            System.out.println("✅ Réunion ajoutée avec succès !");

            // 🔹 Récupérer et afficher toutes les réunions
            List<Reunion> reunions = sr.getAll();
            System.out.println("📋 Liste des réunions :");
            for (Reunion r : reunions) {
                System.out.println("🆔 ID: " + r.getId() + ", Sujet: " + r.getTitre() +
                        ", Date: " + r.getDate() + ", Type: " + r.getType() +
                        ", Description: " + r.getDescription());
            }

            // 🔹 Mise à jour d'une réunion
            int idReunionToUpdate = 2;
            boolean foundReunionForUpdate = false;
            for (Reunion r : reunions) {
                if (r.getId() == idReunionToUpdate) {
                    r.setTitre("Réunion d'équipe");
                    r.setDate(new Date());
                    r.setDescription("Révision des progrès");
                    r.setType("Opérationnelle");
                    sr.update(r);
                    System.out.println("✅ Réunion avec ID " + idReunionToUpdate + " mise à jour !");
                    foundReunionForUpdate = true;
                    break;
                }
            }
            if (!foundReunionForUpdate) {
                System.out.println("❌ ID " + idReunionToUpdate + " non trouvé pour mise à jour !");
            }

            // 🔹 Suppression d'une réunion
            int idReunionToDelete = 3;
            boolean foundReunionForDelete = false;
            for (Reunion r : sr.getAll()) {
                if (r.getId() == idReunionToDelete) {
                    sr.delete(r);
                    System.out.println("🗑️ Réunion avec ID " + idReunionToDelete + " supprimée !");
                    foundReunionForDelete = true;
                    break;
                }
            }
            if (!foundReunionForDelete) {
                System.out.println("❌ ID " + idReunionToDelete + " non trouvé pour suppression !");
            }


             */
            ////////////CONGEE
            LocalDate datedebut = LocalDate.of(2025, 3, 15);
            LocalDate datefin = LocalDate.of(2026, 10, 20);
            conge conge1 = new conge("long",datedebut,datefin,"tttttttt");
            conge conge2 = new conge("short",datedebut,datefin,"test");
            sp.add(conge1);
            sp.add(conge2);
           // sp.delete(5);
            sp.update(conge1,3);
            System.out.println(sp.getAll());

            ////////////reunion
            LocalDate Date = LocalDate.of(2025, 3, 15);
            LocalDate Date2 = LocalDate.of(2025, 7, 27);
            Reunion Reunion1 = new Reunion("sbeh","aaaaaaaaa",Date,"urgent");
            Reunion Reunion2 = new Reunion("lil","bbbbbbbbb",Date,"non-urgent");
            ServiceReunion ServiceReunion = new ServiceReunion();
            //ServiceReunion.add(Reunion1);
            ServiceReunion.add(Reunion2);
            //ServiceReunion.delete(3);
            ServiceReunion.update(Reunion1,5);
            System.out.println(ServiceReunion.getAll());





        } else {
            System.out.println("❌ Connection status: Not connected");
        }
    }
}
