package tn.esprit.services;

import tn.esprit.models.Bureau;
import tn.esprit.utils.MyDatabase;
import java.sql.*;
import java.util.*;

public class ServiceBureau {
    private final Connection cnx;

    public ServiceBureau() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    public boolean existsByRef(String refBureau) {
        String sql = "SELECT COUNT(*) FROM bureau WHERE RefBureau = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setString(1, refBureau);
            ResultSet rs = pst.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // La référence existe déjà
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean add(Bureau bureau) {
        if (isValidBureau(bureau)) {
            String sql = "INSERT INTO bureau (RefBureau, Capacite, Disponibilité) VALUES (?, ?, ?)";
            try (PreparedStatement pst = cnx.prepareStatement(sql)) {
                pst.setString(1, bureau.getRefBureau());
                pst.setInt(2, bureau.getCapacite());
                pst.setString(3, bureau.getDisponibilite());
                pst.executeUpdate();
                System.out.println("Bureau ajouté avec succès !");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean update(Bureau bureau) {
        if (isValidBureau(bureau)) {
            Bureau existingBureau = getById(bureau.getIdBureau());
            if (existingBureau != null && !existingBureau.getRefBureau().equals(bureau.getRefBureau()) &&
                    existsByRef(bureau.getRefBureau())) {
                System.out.println("Erreur : La référence du bureau existe déjà !");
                return false;
            }

            String sql = "UPDATE bureau SET RefBureau = ?, Capacite = ?, Disponibilité = ? WHERE IdBureau = ?";
            try (PreparedStatement pst = cnx.prepareStatement(sql)) {
                pst.setString(1, bureau.getRefBureau());
                pst.setInt(2, bureau.getCapacite());
                pst.setString(3, bureau.getDisponibilite());
                pst.setInt(4, bureau.getIdBureau());
                pst.executeUpdate();
                System.out.println("Bureau mis à jour avec succès !");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM bureau WHERE IdBureau = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Bureau getById(int id) {
        String sql = "SELECT * FROM bureau WHERE IdBureau = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Bureau(
                        rs.getInt("IdBureau"),
                        rs.getString("RefBureau"),
                        rs.getInt("Capacite"),
                        rs.getString("Disponibilité")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Bureau> getAll() {
        List<Bureau> bureaux = new ArrayList<>();
        String sql = "SELECT * FROM bureau";
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                bureaux.add(new Bureau(
                        rs.getInt("IdBureau"),
                        rs.getString("RefBureau"),
                        rs.getInt("Capacite"),
                        rs.getString("Disponibilité")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bureaux;
    }

    private boolean isValidBureau(Bureau bureau) {
        // Vérification des champs obligatoires
        if (bureau.getRefBureau() == null || bureau.getRefBureau().trim().isEmpty() ||
                bureau.getDisponibilite() == null || bureau.getDisponibilite().trim().isEmpty()) {
            System.out.println("Erreur : Tous les champs sont obligatoires !");
            return false;
        }

        // Vérification que la capacité est un nombre valide et positif
        if (bureau.getCapacite() <= 0) {
            System.out.println("Erreur : La capacité doit être un nombre positif !");
            return false;
        }

        return true;
    }
}
