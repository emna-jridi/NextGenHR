package tn.esprit.services;

import tn.esprit.models.Salle;
import tn.esprit.utils.MyDatabase;
import java.sql.*;
import java.util.*;

public class ServiceSalle {
    private final Connection cnx;

    public ServiceSalle() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    public boolean existsByRef(String refSalle) {
        String sql = "SELECT COUNT(*) FROM salle WHERE RefSalle = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setString(1, refSalle);
            ResultSet rs = pst.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // La référence existe déjà
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean add(Salle salle) {
        if (isValidSalle(salle)) {
            String sql = "INSERT INTO salle (RefSalle, Capacite, TypeSalle, Disponibilité) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pst = cnx.prepareStatement(sql)) {
                pst.setString(1, salle.getRefSalle());
                pst.setInt(2, salle.getCapacite());
                pst.setString(3, salle.getTypeSalle());
                pst.setString(4, salle.getDisponibilite());
                pst.executeUpdate();
                System.out.println("Salle ajoutée avec succès !");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean update(Salle salle) {
        if (isValidSalle(salle)) {
            Salle existingSalle = getById(salle.getIdSalle());
            if (existingSalle != null && !existingSalle.getRefSalle().equals(salle.getRefSalle()) &&
                    existsByRef(salle.getRefSalle())) {
                System.out.println("Erreur : La référence de la salle existe déjà !");
                return false;
            }

            String sql = "UPDATE salle SET RefSalle = ?, Capacite = ?, TypeSalle = ?, Disponibilité = ? WHERE IdSalle = ?";
            try (PreparedStatement pst = cnx.prepareStatement(sql)) {
                pst.setString(1, salle.getRefSalle());
                pst.setInt(2, salle.getCapacite());
                pst.setString(3, salle.getTypeSalle());
                pst.setString(4, salle.getDisponibilite());
                pst.setInt(5, salle.getIdSalle());
                pst.executeUpdate();
                System.out.println("Salle mise à jour avec succès !");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM salle WHERE IdSalle = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Salle getById(int id) {
        String sql = "SELECT * FROM salle WHERE IdSalle = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Salle(
                        rs.getInt("IdSalle"),
                        rs.getString("RefSalle"),
                        rs.getInt("Capacite"),
                        rs.getString("TypeSalle"),
                        rs.getString("Disponibilité")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Salle> getAll() {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM salle";
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                salles.add(new Salle(
                        rs.getInt("IdSalle"),
                        rs.getString("RefSalle"),
                        rs.getInt("Capacite"),
                        rs.getString("TypeSalle"),
                        rs.getString("Disponibilité")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salles;
    }

    private boolean isValidSalle(Salle salle) {
        // Vérification des champs obligatoires
        if (salle.getRefSalle() == null || salle.getRefSalle().trim().isEmpty() ||
                salle.getTypeSalle() == null || salle.getTypeSalle().trim().isEmpty() ||
                salle.getDisponibilite() == null || salle.getDisponibilite().trim().isEmpty()) {
            System.out.println("Erreur : Tous les champs sont obligatoires !");
            return false;
        }

        // Vérification que la capacité est un nombre valide et positif
        if (salle.getCapacite() <= 0) {
            System.out.println("Erreur : La capacité doit être un nombre positif !");
            return false;
        }

        return true;
    }


    // Ajouter la méthode getIdByRef
    public int getIdByRef(String refSalle) {
        int idSalle = -1;
        String query = "SELECT idSalle FROM Salle WHERE refSalle = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, refSalle);  // Remplace le paramètre de la requête avec la référence de la salle
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                idSalle = rs.getInt("idSalle");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idSalle;
    }
}