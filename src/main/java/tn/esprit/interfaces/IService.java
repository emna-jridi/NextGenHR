package tn.esprit.interfaces;

import tn.esprit.Models.Candidature;
import tn.esprit.Models.Offreemploi;

import java.util.List;

public interface IService<T> {
    void add(T entity);

     List<T> getAll();

    void update(T entity);

    /* @Override
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
     }*/
    void updatee(Candidature candidature, int offreId);

    void delete(int id);
    T getbyid(int id);

    void ajout(Candidature candidature, int offreId);

    public void delete(int id, int offreId);

}
