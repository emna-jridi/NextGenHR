package tn.esprit.Services;

import tn.esprit.Models.Candidature;
import tn.esprit.Models.Offreemploi;
import tn.esprit.interfaces.IService;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceOffre implements IService<Offreemploi> {
    private Connection cnx ;
    public ServiceOffre(){
        cnx = MyDataBase.getInstance().getCnx();
    }
    @Override
    public void add(Offreemploi offreEmploi) {
        String qry = "INSERT INTO offreemploi (candidaturesrecues,titre, description,experiencerequise,niveauEtudes, competences,typecontrat,localisation,niveaulangues, dateCreation, dateExpiration, statut) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, offreEmploi.getCandidaturesrecues());
            pstm.setString(2, offreEmploi.getTitre());
            pstm.setString(3, offreEmploi.getDescription());
            pstm.setString(4, offreEmploi.getExperiencerequise());
            pstm.setString(5,offreEmploi.getNiveauEtudes());
            pstm.setString(6, offreEmploi.getCompetences());
            pstm.setString(7, offreEmploi.getTypecontrat());
            pstm.setString(8, offreEmploi.getLocalisation());
            pstm.setString(9, offreEmploi.getNiveaulangues());


            pstm.setTimestamp(10, Timestamp.valueOf(offreEmploi.getDateCreation()));
            pstm.setTimestamp(11, Timestamp.valueOf(offreEmploi.getDateExpiration()));
            pstm.setString(12, offreEmploi.getStatut());

            pstm.executeUpdate();
            System.out.println("Offre d'emploi ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Offreemploi> getAll() {
        String qry = "SELECT * FROM offreemploi";
        List<Offreemploi> offres = new ArrayList<>();
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                Offreemploi offre = new Offreemploi();
                offre.setId(rs.getInt("id"));
                offre.setTitre(rs.getString("titre"));
                offre.setDescription(rs.getString("description"));
                offre.setExperiencerequise(rs.getString("experiencerequise"));
                offre.setNiveauEtudes(rs.getString("niveauEtudes"));
                offre.setCompetences(rs.getString("competences"));
                offre.setTypecontrat(rs.getString("typecontrat"));
                offre.setLocalisation(rs.getString("localisation"));
                offre.setNiveaulangues(rs.getString("niveaulangues"));
                offre.setDateCreation(rs.getTimestamp("dateCreation").toLocalDateTime());
                offre.setDateExpiration(rs.getTimestamp("dateExpiration").toLocalDateTime());
                offre.setStatut(rs.getString("statut"));
                offre.setCandidaturesrecues(rs.getInt("candidaturesrecues"));
                offres.add(offre);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return offres;
    }

    @Override
    public void update(Offreemploi offreEmploi) {

            String qry = "UPDATE offreemploi SET titre=?, description=?, experiencerequise=?, niveauEtudes=?, competences=?, typecontrat=?, localisation=?, niveaulangues=?, dateCreation=?, dateExpiration=?, statut=?, candidaturesrecues=? WHERE id=?";
            try {
                PreparedStatement pstm = cnx.prepareStatement(qry);
                pstm.setString(1, offreEmploi.getTitre());
                pstm.setString(2, offreEmploi.getDescription());
                pstm.setString(3, offreEmploi.getExperiencerequise());
                pstm.setString(4, offreEmploi.getNiveauEtudes());
                pstm.setString(5, offreEmploi.getCompetences());
                pstm.setString(6, offreEmploi.getTypecontrat());
                pstm.setString(7, offreEmploi.getLocalisation());
                pstm.setString(8, offreEmploi.getNiveaulangues());
                pstm.setTimestamp(9, Timestamp.valueOf(offreEmploi.getDateCreation()));
                pstm.setTimestamp(10, Timestamp.valueOf(offreEmploi.getDateExpiration()));
                pstm.setString(11, offreEmploi.getStatut());
                pstm.setInt(12, offreEmploi.getCandidaturesrecues());
                pstm.setInt(13, offreEmploi.getId());
                pstm.executeUpdate();
                System.out.println("Offre d'emploi mise à jour avec succès !");
            } catch (SQLException e) {
                System.out.println("Erreur lors de la mise à jour de l'offre d'emploi : " + e.getMessage());
            }
        }



    @Override
    public void delete(int id) {
            String qry = "DELETE FROM offreemploi WHERE id = ?";
            try {
                PreparedStatement pstm = cnx.prepareStatement(qry);
                pstm.setInt(1, id);

                int rowsAffected = pstm.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Offre d'emploi supprimée avec succès !");
                } else {
                    System.out.println("Aucune offre d'emploi trouvée avec cet ID.");
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la suppression de l'offre d'emploi : " + e.getMessage());
            }
        }

    @Override
    public Offreemploi getbyid(int id) {
        String qry = "SELECT * FROM offreemploi WHERE id = ?";
        Offreemploi offreemploi = null;
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                offreemploi = new Offreemploi();
                offreemploi.setId(rs.getInt("id"));
                offreemploi.setCandidaturesrecues(rs.getInt("candidaturesrecues"));
                offreemploi.setTitre(rs.getString("titre"));
                offreemploi.setDescription(rs.getString("description"));
                offreemploi.setTitre(rs.getString("experiencerequise"));
                offreemploi.setCompetences(rs.getString("competences"));
                offreemploi.setLocalisation(rs.getString("localisation"));
                offreemploi.setExperiencerequise(rs.getString("experiencerequise"));
                offreemploi.setNiveauEtudes((rs.getString("niveauEtudes")));
                offreemploi.setNiveaulangues((rs.getString("niveaulangues")));
                offreemploi.setTypecontrat(rs.getString("typecontrat"));
                offreemploi.setDateCreation(rs.getTimestamp("dateCreation").toLocalDateTime());
                offreemploi.setDateExpiration(rs.getTimestamp("dateExpiration").toLocalDateTime());
                offreemploi.setStatut(rs.getString("statut"));

            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'offred'emploi : " + e.getMessage());
        }
        return offreemploi;
    }







    public int getNbrCandidatures(int offreId) {
        String qry = "SELECT COUNT(*) AS nbrCandidatures FROM candidature WHERE offreId = ?";
        int nbrCandidatures = 0;
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, offreId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                nbrCandidatures = rs.getInt("nbrCandidatures");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du calcul du nombre de candidatures : " + e.getMessage());
        }
        return nbrCandidatures;
    }



    public Offreemploi getOffreEmploiById(int offreId) {
        String qry = "SELECT * FROM offreemploi WHERE id = ?";
        Offreemploi offreEmploi = null;
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, offreId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                offreEmploi = new Offreemploi();
                offreEmploi.setId(rs.getInt("id"));
                int nbrCandidatures = getNbrCandidatures(offreId);
                offreEmploi.setCandidaturesrecues(nbrCandidatures);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'offre : " + e.getMessage());
        }
        return offreEmploi;
    }





    @Override
    public void ajouter(Candidature candidature) {

    }


    @Override
    public void updatee(Candidature candidature, Offreemploi offre){};
}








