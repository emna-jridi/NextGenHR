package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Teletravail;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceTeletravail implements IService<Teletravail> {
    private Connection cnx;

    public ServiceTeletravail() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    //Ajouter Teletravail
    @Override
    public void add(Teletravail teletravail) {
        String qry = "INSERT INTO `teletravail` (`IdEmploye`, `DateDemandeTT`, `DateDebutTT`, `DateFinTT`, `StatutTT`, `RaisonTT`) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, teletravail.getIdEmploye());
            pstm.setDate(2, Date.valueOf(teletravail.getDateDemandeTT()));
            pstm.setDate(3, Date.valueOf(teletravail.getDateDebutTT()));
            pstm.setDate(4, Date.valueOf(teletravail.getDateFinTT()));
            pstm.setString(5, teletravail.getStatutTT());
            pstm.setString(6, teletravail.getRaisonTT());

            pstm.executeUpdate();
            System.out.println("Télétravail ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du télétravail : " + e.getMessage());
        }
    }


    //Afficher Teletravail
    @Override
    public List<Teletravail> getAll() {
        List<Teletravail> teletravails = new ArrayList<>();
        String qry = "SELECT * FROM `teletravail`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Teletravail t = new Teletravail();
                t.setIdTeletravail(rs.getInt("IdTeletravail"));
                t.setIdEmploye(rs.getInt("IdEmploye"));
                t.setDateDemandeTT(rs.getDate("DateDemandeTT").toLocalDate());
                t.setDateDebutTT(rs.getDate("DateDebutTT").toLocalDate());
                t.setDateFinTT(rs.getDate("DateFinTT").toLocalDate());
                t.setStatutTT(rs.getString("StatutTT"));
                t.setRaisonTT(rs.getString("RaisonTT"));

                teletravails.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des télétravails : " + e.getMessage());
        }

        return teletravails;
    }

    //Collecter Teletravail
    public Teletravail getById(int id) {
        String sql = "SELECT * FROM Teletravail WHERE idTeletravail = ?";
        Teletravail tt = null;

        try (Connection conn = MyDatabase.getInstance().getCnx();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                tt = new Teletravail(
                        rs.getInt("idEmploye"),
                        rs.getDate("dateDemandeTT").toLocalDate(),
                        rs.getDate("dateDebutTT").toLocalDate(),
                        rs.getDate("dateFinTT").toLocalDate(),
                        rs.getString("statutTT"),
                        rs.getString("raisonTT")
                );
                tt.setIdTeletravail(rs.getInt("idTeletravail")); // Important pour l'update
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du télétravail : " + e.getMessage());
        }

        return tt;
    }
    // mise a jour teletravail
    public void update(Teletravail teletravail) {
        String sql = "UPDATE Teletravail SET idEmploye = ?, dateDemandeTT = ?, dateDebutTT = ?, dateFinTT = ?, statutTT = ?, raisonTT = ? WHERE idTeletravail = ?";

        try (Connection conn = MyDatabase.getInstance().getCnx();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teletravail.getIdEmploye());
            pstmt.setDate(2, java.sql.Date.valueOf(teletravail.getDateDemandeTT()));
            pstmt.setDate(3, java.sql.Date.valueOf(teletravail.getDateDebutTT()));
            pstmt.setDate(4, java.sql.Date.valueOf(teletravail.getDateFinTT()));
            pstmt.setString(5, teletravail.getStatutTT());
            pstmt.setString(6, teletravail.getRaisonTT());
            pstmt.setInt(7, teletravail.getIdTeletravail()); // Condition de mise à jour

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Télétravail mis à jour avec succès !");
            } else {
                System.out.println("Aucun télétravail trouvé avec l'ID : " + teletravail.getIdTeletravail());
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du télétravail : " + e.getMessage());
        }
    }


// Supprimer Teletravail
    public void delete(Teletravail teletravail) {
        String sql = "DELETE FROM Teletravail WHERE idTeletravail = ?";

        try (Connection conn = MyDatabase.getInstance().getCnx(); // 🔑 Assure-toi d'utiliser getCnx()
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teletravail.getIdTeletravail());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Télétravail supprimé avec succès !");
            } else {
                System.out.println("Aucun télétravail trouvé avec l'ID : " + teletravail.getIdTeletravail());
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du télétravail : " + e.getMessage());
        }
    }

}