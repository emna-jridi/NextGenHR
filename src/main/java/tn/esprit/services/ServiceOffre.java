package tn.esprit.services;

import tn.esprit.models.*;
import tn.esprit.interfaces.IService;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceOffre{
    private Connection cnx ;
    public ServiceOffre(){

        cnx = MyDatabase.getInstance().getCnx();
    }
    public void add(Offreemploi offreEmploi) {
        String qry = "INSERT INTO offreemploi (titre, description,experiencerequise,niveauEtudes, competences,typecontrat,localisation,niveaulangues, dateCreation, dateExpiration) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);

            pstm.setString(1, offreEmploi.getTitre());
            pstm.setString(2, offreEmploi.getDescription());
            pstm.setString(3, offreEmploi.getExperiencerequise().name());
            pstm.setString(4,offreEmploi.getNiveauEtudes().name());
            pstm.setString(5, offreEmploi.getCompetences());
            pstm.setString(6, offreEmploi.getTypecontrat().name());
            pstm.setString(7, offreEmploi.getLocalisation());
            pstm.setString(8, offreEmploi.getNiveaulangues().name());


            pstm.setTimestamp(9, Timestamp.valueOf(offreEmploi.getDateCreation()));
            pstm.setTimestamp(10, Timestamp.valueOf(offreEmploi.getDateExpiration()));


            pstm.executeUpdate();
            System.out.println("Offre d'emploi ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Offreemploi> getAll() {
        String qry = "SELECT * FROM offreemploi";
        List<Offreemploi> offres = new ArrayList<>();
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {

            while (rs.next()) {
                Offreemploi offre = new Offreemploi();
                offre.setId(rs.getInt("id"));
                offre.setTitre(rs.getString("titre"));
                offre.setDescription(rs.getString("description"));

                // Vérification de null pour les champs qui peuvent être null
                String experienceRequise = rs.getString("experiencerequise");
                if (experienceRequise != null) {
                    offre.setExperiencerequise(experience.valueOf(experienceRequise));
                }

                String niveauEtudes = rs.getString("niveauEtudes");
                if (niveauEtudes != null) {
                    offre.setNiveauEtudes(Niveauetudes.valueOf(niveauEtudes));
                }
                String typecontrat = rs.getString("typecontrat");
                if (typecontrat != null) {
                    offre.setTypecontrat((TypeContrat.valueOf(typecontrat)) );
                }


                offre.setCompetences(rs.getString("competences"));

                offre.setLocalisation(rs.getString("localisation"));

                String niveaulangues = rs.getString("niveaulangues");
                if (niveaulangues != null) {
                    offre.setNiveaulangues(Niveaulangues.valueOf(niveaulangues));
                }

                // Vérification de null pour les dates
                if (rs.getTimestamp("dateCreation") != null) {
                    offre.setDateCreation(rs.getTimestamp("dateCreation").toLocalDateTime());
                }

                if (rs.getTimestamp("dateExpiration") != null) {
                    offre.setDateExpiration(rs.getTimestamp("dateExpiration").toLocalDateTime());
                }


                offres.add(offre);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return offres;
    }


    public void update(Offreemploi offreEmploi) {

            String qry = "UPDATE offreemploi SET titre=?, description=?, experiencerequise=?, niveauEtudes=?, competences=?, typecontrat=?, localisation=?, niveaulangues=?, dateCreation=?, dateExpiration=? WHERE id=?";
            try {

                PreparedStatement pstm = cnx.prepareStatement(qry);
                pstm.setString(1, offreEmploi.getTitre());
                pstm.setString(2, offreEmploi.getDescription());
                pstm.setString(3, offreEmploi.getExperiencerequise() != null ? offreEmploi.getExperiencerequise().name() : null);
                pstm.setString(4, offreEmploi.getNiveauEtudes() != null ? offreEmploi.getNiveauEtudes().name() : null);
                pstm.setString(5, offreEmploi.getCompetences());
                pstm.setString(6, offreEmploi.getTypecontrat() != null ? offreEmploi.getTypecontrat().name() : null);
                pstm.setString(7, offreEmploi.getLocalisation());
                pstm.setString(8, offreEmploi.getNiveaulangues().name());
                pstm.setTimestamp(9, Timestamp.valueOf(offreEmploi.getDateCreation()));
                pstm.setTimestamp(10, Timestamp.valueOf(offreEmploi.getDateExpiration()));


                pstm.setInt(11, offreEmploi.getId());
                pstm.executeUpdate();

                System.out.println("Offre d'emploi mise à jour avec succès !");
            } catch (SQLException e) {
                System.out.println("Erreur lors de la mise à jour de l'offre d'emploi : " + e.getMessage());
            }
         // Ajouter cette ligne si vous travaillez dans une transaction manuelle

    }



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

                offreemploi.setTitre(rs.getString("titre"));
                offreemploi.setDescription(rs.getString("description"));
                offreemploi.setTitre(rs.getString("experiencerequise"));
                offreemploi.setCompetences(rs.getString("competences"));
                offreemploi.setLocalisation(rs.getString("localisation"));
                offreemploi.setExperiencerequise(experience.valueOf(rs.getString("experiencerequise")));
                offreemploi.setNiveauEtudes(Niveauetudes.valueOf(rs.getString("niveauEtudes")));
                offreemploi.setNiveaulangues(Niveaulangues.valueOf(rs.getString("niveaulangues")));
                offreemploi.setTypecontrat(TypeContrat.valueOf(rs.getString("typecontrat")));
                offreemploi.setDateCreation(rs.getTimestamp("dateCreation").toLocalDateTime());
                offreemploi.setDateExpiration(rs.getTimestamp("dateExpiration").toLocalDateTime());


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

            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'offre : " + e.getMessage());
        }
        return offreEmploi;
    }


    public void updatee(Candidature candidature, Offreemploi offre){};
}








