package tn.esprit.services;

import tn.esprit.models.User;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser {



    private Connection cnx ;

    public ServiceUser() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    /**
     * ✅ Méthode pour ajouter un utilisateur avec le rôle et le mot de passe
     */
    public void add(User user) {
        String qry = "INSERT INTO user (NomUser, PrenomUser, DateNaissanceUser, AdresseUser, TelephoneUser, EmailUser, Password, Role) VALUES (?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, user.getNomUser());
            pstm.setString(2, user.getPrenomUser());

            // 🔥 Vérification de la date de naissance avant de la convertir
            if (user.getDateNaissanceUser() != null) {
                pstm.setDate(3, java.sql.Date.valueOf(user.getDateNaissanceUser()));
            } else {
                pstm.setDate(3, null);
            }

            pstm.setString(4, user.getAdresseUser());
            pstm.setString(5, user.getTelephoneUser());
            pstm.setString(6, user.getEmailUser());
            pstm.setString(7, user.getPassword());

            // 🔥 Vérification du rôle avant de l'ajouter
            if (user.getRole() != null) {
                pstm.setString(8, user.getRole().getDbValue());
            } else {
                pstm.setString(8, null);
            }

            // 🔥 Logs pour le debug
            System.out.println("🔄 Requête SQL : " + pstm);
            pstm.executeUpdate();
            System.out.println("✅ Utilisateur ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    /**
     * ✅ Méthode pour récupérer tous les utilisateurs
     */
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String qry = "SELECT * FROM user";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                User u = new User();
                u.setIdUser(rs.getInt("ID_User"));
                u.setNomUser(rs.getString("NomUser"));
                u.setPrenomUser(rs.getString("PrenomUser"));

                // 🔥 Vérification de la date de naissance avant de la convertir
                Date dateNaissance = rs.getDate("DateNaissanceUser");
                if (dateNaissance != null) {
                    u.setDateNaissanceUser(dateNaissance.toLocalDate());
                } else {
                    u.setDateNaissanceUser(null);
                }

                u.setAdresseUser(rs.getString("AdresseUser"));
                u.setTelephoneUser(rs.getString("TelephoneUser"));
                u.setEmailUser(rs.getString("EmailUser"));
                u.setPassword(rs.getString("Password"));
                u.setRole(User.Role.fromDbValue(rs.getString("Role")));

                users.add(u);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }

        return users;
    }

    /**
     * ✅ Méthode pour mettre à jour un utilisateur
     */
    public void update(User user) {
        String qry = "UPDATE user SET NomUser=?, PrenomUser=?, DateNaissanceUser=?, AdresseUser=?, TelephoneUser=?, EmailUser=?, Password=?, Role=? WHERE ID_User=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, user.getNomUser());
            pstm.setString(2, user.getPrenomUser());

            // 🔥 Vérification de la date de naissance avant de la convertir
            if (user.getDateNaissanceUser() != null) {
                pstm.setDate(3, java.sql.Date.valueOf(user.getDateNaissanceUser()));
            } else {
                pstm.setDate(3, null);
            }

            pstm.setString(4, user.getAdresseUser());
            pstm.setString(5, user.getTelephoneUser());
            pstm.setString(6, user.getEmailUser());
            pstm.setString(7, user.getPassword());
            pstm.setString(8, user.getRole().getDbValue());
            pstm.setInt(9, user.getIdUser());

            pstm.executeUpdate();
            System.out.println("✅ Utilisateur mis à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }

    /**
     * ✅ Méthode pour supprimer un utilisateur par son ID
     */
    public void delete(int idUser) {
        String qry = "DELETE FROM user WHERE ID_User=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, idUser);

            pstm.executeUpdate();
            System.out.println("✅ Utilisateur supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }

    /**
     * ✅ Méthode pour récupérer un utilisateur par son ID
     */
    public User getById(int idUser) {
        User user = null;
        String qry = "SELECT * FROM user WHERE ID_User = ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, idUser);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setIdUser(rs.getInt("ID_User"));
                user.setNomUser(rs.getString("NomUser"));
                user.setPrenomUser(rs.getString("PrenomUser"));

                // 🔥 Vérification de la date de naissance avant de la convertir
                Date dateNaissance = rs.getDate("DateNaissanceUser");
                if (dateNaissance != null) {
                    user.setDateNaissanceUser(dateNaissance.toLocalDate());
                } else {
                    user.setDateNaissanceUser(null);
                }

                user.setAdresseUser(rs.getString("AdresseUser"));
                user.setTelephoneUser(rs.getString("TelephoneUser"));
                user.setEmailUser(rs.getString("EmailUser"));
                user.setPassword(rs.getString("Password"));
                user.setRole(User.Role.fromDbValue(rs.getString("Role")));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération de l'utilisateur par ID : " + e.getMessage());
        }

        return user; // Retourner null si l'utilisateur n'est pas trouvé
    }
}
