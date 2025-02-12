package tn.esprit.Services;

import tn.esprit.Models.Candidature;
import tn.esprit.interfaces.IService;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCandidature implements IService<Candidature> {

    private Connection cnx ;
    public ServiceCandidature() {
        // Récupère la connexion à la base de données via MyDatabase
        cnx = MyDataBase.getInstance().getCnx();
    }


    @Override
    public void add(Candidature candidature) {
        String qry = "INSERT INTO candidature (dateCandidature, statut, cvUrl, lettreMotivation) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setTimestamp(1, Timestamp.valueOf(candidature.getDateCandidature())); // Convertir LocalDateTime en Timestamp
            pstm.setString(2, candidature.getStatut());
            pstm.setString(3, candidature.getCvUrl());
            pstm.setString(4, candidature.getLettreMotivation());

            pstm.executeUpdate();
            System.out.println("Candidature ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la candidature : " + e.getMessage());
        }

    }

    @Override
    public List<Candidature> getAll() {

            List<Candidature> candidatures = new ArrayList<>();
            String qry = "SELECT * FROM candidature"; // Requête SQL pour récupérer toutes les candidatures

            try {
                // Créer une instruction (Statement) pour exécuter la requête
                Statement stm = cnx.createStatement();
                ResultSet rs = stm.executeQuery(qry); // Exécuter la requête et obtenir un ResultSet

                // Parcourir le ResultSet et remplir la liste des candidatures
                while (rs.next()) {
                    Candidature c = new Candidature();
                    c.setId(rs.getInt("id")); // Récupérer l'id
                    c.setDateCandidature(rs.getTimestamp("dateCandidature").toLocalDateTime()); // Récupérer la date de candidature
                    c.setStatut(rs.getString("statut")); // Récupérer le statut
                    c.setCvUrl(rs.getString("cvUrl")); // Récupérer l'URL du CV
                    c.setLettreMotivation(rs.getString("lettreMotivation")); // Récupérer la lettre de motivation

                    // Ajouter la candidature à la liste
                    candidatures.add(c);
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la récupération des candidatures : " + e.getMessage());
            }

            return candidatures; // Retourner la liste des candidatures
        }



    @Override
    public void update(Candidature candidature) {
        String qry = "UPDATE candidature SET dateCandidature=?, statut=?, cvUrl=?, lettreMotivation=? WHERE id=?";
        try {
            // Préparer la requête SQL avec les paramètres
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setTimestamp(1, Timestamp.valueOf(candidature.getDateCandidature()));  // Conversion LocalDateTime to Timestamp
            pstm.setString(2, candidature.getStatut());
            pstm.setString(3, candidature.getCvUrl());
            pstm.setString(4, candidature.getLettreMotivation());
            pstm.setInt(5, candidature.getId());  // Assurer que l'id est bien passé

            // Exécution de la mise à jour
            pstm.executeUpdate();
            System.out.println("Candidature mise à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la candidature : " + e.getMessage());
        }
    }



    @Override
    public void delete(int id) {
        String qry = "DELETE FROM candidature WHERE id=?";
        try {
            // Préparer la requête SQL avec les paramètres
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);  // L'ID de la candidature à supprimer

            // Exécution de la suppression
            pstm.executeUpdate();
            System.out.println("Candidature supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la candidature : " + e.getMessage());
        }
    }


}
