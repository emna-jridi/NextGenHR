package org.example.services;

import org.example.interfaces.IServices;
import org.example.models.Contrat;
import org.example.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceContrat implements IServices<Contrat> {
    private Connection cnx;

    public ServiceContrat() {
        cnx = MyDatabase.getInstance().getCnx();
    }



    //Ajouter Contrat//
    @Override
    public void add(Contrat contrat) {
        String qry = "INSERT INTO `contrat`(`typeContrat`, `dateDebutContrat`, `dateFinContrat`, `statusContrat`, `montantContrat`, `nomClient`, `emailClient`, `idService`) VALUES (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, contrat.getTypeContrat());
            pstm.setDate(2, new java.sql.Date(contrat.getDateDebutContrat().getTime()));
            pstm.setDate(3, new java.sql.Date(contrat.getDateFinContrat().getTime()));
            pstm.setString(4, contrat.getStatusContrat());
            pstm.setInt(5, contrat.getMontantContrat());
            pstm.setString(6, contrat.getNomClient());
            pstm.setString(7, contrat.getEmailClient());
            pstm.setInt(8, contrat.getIdService());

            pstm.executeUpdate();
            System.out.println("Contrat ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du contrat : " + e.getMessage());
        }
    }


    //Afficher les contrats//
    @Override
    public List<Contrat> getAll() {
        List<Contrat> contrats = new ArrayList<>();
        String qry = "SELECT * FROM `contrat`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Contrat c = new Contrat();
                        c.setIdContrat(rs.getInt("idContrat"));
                        c.setTypeContrat(rs.getString("typeContrat"));
                        c.setDateDebutContrat(rs.getDate("dateDebutContrat"));
                        c.setDateFinContrat(rs.getDate("dateFinContrat"));
                        c.setStatusContrat(rs.getString("statusContrat"));
                        c.setMontantContrat(rs.getInt("montantContrat"));
                        c.setNomClient(rs.getString("nomClient"));
                        c.setEmailClient(rs.getString("emailClient"));
                        c.setIdService(rs.getInt("idService"));

                contrats.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des contrats : " + e.getMessage());
        }

        return contrats;
    }



    //Mettre à jour contrat//
    @Override
    public void update(Contrat contrat) {
        String qry = "UPDATE `contrat` SET `typeContrat` = ?, `dateDebutContrat` = ?, `dateFinContrat` = ?, `statusContrat` = ?, `montantContrat` = ?, `nomClient` = ?, `emailClient` = ?, `idService` = ? WHERE `idContrat` = ?";
        try {
            Connection conn = MyDatabase.getInstance().getCnx();
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, contrat.getTypeContrat());
            pstm.setDate(2, new java.sql.Date(contrat.getDateDebutContrat().getTime()));
            pstm.setDate(3, new java.sql.Date(contrat.getDateFinContrat().getTime()));
            pstm.setString(4, contrat.getStatusContrat());
            pstm.setInt(5, contrat.getMontantContrat());
            pstm.setString(6, contrat.getNomClient());
            pstm.setString(7, contrat.getEmailClient());
            pstm.setInt(8, contrat.getIdService());
            pstm.setInt(9, contrat.getIdContrat());

            pstm.executeUpdate();
            System.out.println("Contrat mis à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du contrat : " + e.getMessage());
        }
    }



    //supprimer contrat//
    @Override
    public void delete(Contrat contrat) {
        String qry = "DELETE FROM `contrat` WHERE `idContrat` = ?";
        try {
            Connection conn = MyDatabase.getInstance().getCnx();
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, contrat.getIdContrat());
            pstm.executeUpdate();
            System.out.println("Contrat supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du contrat : " + e.getMessage());
        }
    }



//récupérer un contrat par son id
    public Contrat getById(int id) {
        String qry = "SELECT * FROM `contrat` WHERE `idContrat` = ?";
        try {
            Connection conn = MyDatabase.getInstance().getCnx();
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Contrat contrat = new Contrat();
                contrat.setIdContrat(rs.getInt("idContrat"));
                contrat.setTypeContrat(rs.getString("typeContrat"));
                contrat.setDateDebutContrat(rs.getDate("dateDebutContrat"));
                contrat.setDateFinContrat(rs.getDate("dateFinContrat"));
                contrat.setStatusContrat(rs.getString("statusContrat"));
                contrat.setMontantContrat(rs.getInt("montantContrat"));
                contrat.setNomClient(rs.getString("nomClient"));
                contrat.setEmailClient(rs.getString("emailClient"));
                contrat.setIdService(rs.getInt("idService"));

                return contrat;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du contrat : " + e.getMessage());
        }
        return null; // Retourne null si le contrat n'existe pas
    }

}
