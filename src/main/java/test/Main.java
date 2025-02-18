package test;

import services.ServiceUser;
import utils.DBConnection;
import entities.User;
import entities.User.Role;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // ✅ Vérification de la connexion à la base de données
        DBConnection dbConnection = DBConnection.getInstance();
        if (dbConnection.getCon() != null) {
            System.out.println("✅ Connection status: Connected");

            // ✅ Initialisation du service utilisateur
            ServiceUser su = new ServiceUser();

            try {
                // ✅ Ajout d'un utilisateur Employé
                User newUser = new User();
                newUser.setNomUser("Doe");
                newUser.setPrenomUser("John");
                newUser.setDateNaissanceUser(LocalDate.of(1990, 5, 15));
                newUser.setAdresseUser("123 Main St");
                newUser.setTelephoneUser("123456789");  // Vérifier la longueur ici
                newUser.setEmailUser("john.doe@example.com");
                newUser.setPassword("password123");
                newUser.setRole(Role.EMPLOYE);

                // Vérification de la longueur du téléphone avant l'ajout
                if (isValidPhoneNumber(newUser.getTelephoneUser())) {
                    su.add(newUser);
                    System.out.println("✅ Utilisateur (Employé) ajouté avec succès !");
                } else {
                    System.out.println("❌ Numéro de téléphone invalide pour John Doe !");
                }

                // ✅ Ajout d'un autre Employé
                User newEmploye = new User();
                newEmploye.setNomUser("Martin");
                newEmploye.setPrenomUser("Alice");
                newEmploye.setDateNaissanceUser(LocalDate.of(1992, 6, 10));
                newEmploye.setAdresseUser("456 Oak St");
                newEmploye.setTelephoneUser("987654321");  // Vérifier la longueur ici
                newEmploye.setEmailUser("alice.martin@example.com");
                newEmploye.setPassword("password456");
                newEmploye.setRole(Role.EMPLOYE);

                // Vérification de la longueur du téléphone avant l'ajout
                if (isValidPhoneNumber(newEmploye.getTelephoneUser())) {
                    su.add(newEmploye);
                    System.out.println("✅ Employé ajouté avec succès !");
                } else {
                    System.out.println("❌ Numéro de téléphone invalide pour Alice Martin !");
                }

                // ✅ Ajout d'un ResponsableRH
                User newRH = new User();
                newRH.setNomUser("Dupont");
                newRH.setPrenomUser("Sophie");
                newRH.setDateNaissanceUser(LocalDate.of(1985, 3, 20));
                newRH.setAdresseUser("789 Pine St");
                newRH.setTelephoneUser("112233445");  // Vérifier la longueur ici
                newRH.setEmailUser("sophie.dupont@example.com");
                newRH.setPassword("adminpass");
                newRH.setRole(Role.RESPONSABLE_RH);

                // Vérification de la longueur du téléphone avant l'ajout
                if (isValidPhoneNumber(newRH.getTelephoneUser())) {
                    su.add(newRH);
                    System.out.println("✅ Responsable RH ajouté avec succès !");
                } else {
                    System.out.println("❌ Numéro de téléphone invalide pour Sophie Dupont !");
                }

                // ✅ Ajout d'un utilisateur en utilisant le constructeur par défaut
                User anotherUser = new User();
                anotherUser.setNomUser("Dupont");
                anotherUser.setPrenomUser("Jean");
                anotherUser.setDateNaissanceUser(LocalDate.of(1988, 2, 14));
                anotherUser.setAdresseUser("321 Birch St");
                anotherUser.setTelephoneUser("999888777");  // Vérifier la longueur ici
                anotherUser.setEmailUser("jean.dupont@example.com");
                anotherUser.setPassword("password123");
                anotherUser.setRole(Role.EMPLOYE);

                // Vérification de la longueur du téléphone avant l'ajout
                if (isValidPhoneNumber(anotherUser.getTelephoneUser())) {
                    su.add(anotherUser);
                    System.out.println("✅ Autre utilisateur (Employé) ajouté avec succès !");
                } else {
                    System.out.println("❌ Numéro de téléphone invalide pour Jean Dupont !");
                }

                // ✅ Affichage de la liste des utilisateurs
                List<User> users = su.getAll();
                System.out.println("\n📋 Liste des utilisateurs :");
                for (User u : users) {
                    System.out.println(u);
                }

                // ✅ Mise à jour d'un utilisateur (avec changement de mot de passe)
                int idToUpdate = 3;
                User userToUpdate = su.getById(idToUpdate);
                if (userToUpdate != null) {
                    userToUpdate.setNomUser("Smith");
                    userToUpdate.setPrenomUser("Jane");
                    userToUpdate.setDateNaissanceUser(LocalDate.of(1995, 8, 20));
                    userToUpdate.setAdresseUser("456 Elm St");
                    userToUpdate.setTelephoneUser("987654321");
                    userToUpdate.setEmailUser("jane.smith@example.com");
                    userToUpdate.setPassword("newpassword789");
                    userToUpdate.setRole(Role.RESPONSABLE_RH);

                    su.update(userToUpdate);
                    System.out.println("✅ Utilisateur avec ID " + idToUpdate + " mis à jour !");
                } else {
                    System.out.println("❌ ID " + idToUpdate + " non trouvé pour mise à jour !");
                }

                // ✅ Suppression d'un utilisateur
                int idToDelete = 1;
                User userToDelete = su.getById(idToDelete);
                if (userToDelete != null) {
                    su.delete(idToDelete);
                    System.out.println("🗑️ Utilisateur avec ID " + idToDelete + " supprimé !");
                } else {
                    System.out.println("❌ ID " + idToDelete + " non trouvé pour suppression !");
                }

            } catch (Exception e) {
                System.out.println("❌ Erreur : " + e.getMessage());
            }
        } else {
            System.out.println("❌ Connection status: Failed");
        }
    }

    // Méthode pour vérifier la validité du numéro de téléphone (10 chiffres)
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("\\d{10}");
    }
}
