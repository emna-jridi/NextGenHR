package test;

import services.ServiceUser;
import utils.DBConnection;
import entities.User;
import entities.User.Role; // ✅ Import de l'énumération Role

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // ✅ Vérifier la connexion à la base de données
        DBConnection dbConnection = DBConnection.getInstance();
        if (dbConnection.getCon() != null) {
            System.out.println("✅ Connection status: Connected");

            // ✅ Initialisation du service utilisateur
            ServiceUser su = new ServiceUser();

            // ✅ Ajout d'un utilisateur classique (Employé par défaut)
            User newUser = new User(1, "Doe", "John", LocalDate.of(1990, 5, 15),
                    "123 Main St", "123456789", "john.doe@example.com", Role.EMPLOYE);
            su.add(newUser);
            System.out.println("✅ Utilisateur (Employé) ajouté avec succès !");

            // ✅ Ajout d'un autre Employé
            User newEmploye = new User(2, "Martin", "Alice", LocalDate.of(1992, 6, 10),
                    "456 Oak St", "987654321", "alice.martin@example.com", Role.EMPLOYE);
            su.add(newEmploye);
            System.out.println("✅ Employé ajouté avec succès !");

            // ✅ Ajout d'un ResponsableRH
            User newRH = new User(3, "Dupont", "Sophie", LocalDate.of(1985, 3, 20),
                    "789 Pine St", "112233445", "sophie.dupont@example.com", Role.RESPONSABLE_RH);
            su.add(newRH);
            System.out.println("✅ Responsable RH ajouté avec succès !");

            // ✅ Affichage de la liste des utilisateurs
            List<User> users = su.getAll();
            System.out.println("\n📋 Liste des utilisateurs :");
            for (User u : users) {
                System.out.println(u);
            }

            // ✅ Mise à jour d'un utilisateur
            int idToUpdate = 3;
            boolean foundForUpdate = false;
            for (User u : users) {
                if (u.getIdUser() == idToUpdate) {
                    u.setNomUser("Smith");
                    u.setPrenomUser("Jane");
                    u.setDateNaissanceUser(LocalDate.of(1995, 8, 20));
                    u.setAdresseUser("456 Elm St");
                    u.setTelephoneUser("987654321");
                    u.setEmailUser("jane.smith@example.com");
                    u.setRole(Role.RESPONSABLE_RH); // 🔥 Mise à jour du rôle

                    su.update(u);
                    System.out.println("✅ Utilisateur avec ID " + idToUpdate + " mis à jour !");
                    foundForUpdate = true;
                    break;
                }
            }
            if (!foundForUpdate) {
                System.out.println("❌ ID " + idToUpdate + " non trouvé pour mise à jour !");
            }

            // ✅ Suppression d'un utilisateur
            int idToDelete = 1;
            boolean foundForDelete = false;
            for (User u : su.getAll()) {
                if (u.getIdUser() == idToDelete) {
                    su.delete(idToDelete);
                    System.out.println("🗑️ Utilisateur avec ID " + idToDelete + " supprimé !");
                    foundForDelete = true;
                    break;
                }
            }
            if (!foundForDelete) {
                System.out.println("❌ ID " + idToDelete + " non trouvé pour suppression !");
            }

        } else {
            System.out.println("❌ Connection status: Failed");
        }
    }
}
