package tn.esprit.services;

import tn.esprit.models.*;
import tn.esprit.interfaces.IService;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCandidature{

    private Connection cnx ;
    public ServiceCandidature() {

        cnx = MyDatabase.getInstance().getCnx();
    }

    public void ajouter(Candidature candidature) {
        String qry = "INSERT INTO candidature (dateCandidature, statut, cvUrl, lettreMotivation, offreId, Nom, Prenom, email, telephone) VALUES (?, ?, ?, ?, ?, ? , ? ,?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setTimestamp(1, Timestamp.valueOf(candidature.getDateCandidature()));
            pstm.setString(2, candidature.getStatut().name());
            pstm.setString(3, candidature.getCvUrl());
            pstm.setString(4, candidature.getLettreMotivation());
            if (candidature.getOffreemploi() != null) {
                pstm.setInt(5, candidature.getOffreemploi().getId());
            } else {
                pstm.setNull(5, java.sql.Types.INTEGER);
            }
            pstm.setString(6, candidature.getNom());
            pstm.setString(7, candidature.getPrenom());
            pstm.setString(8, candidature.getEmail());
            pstm.setString(9, candidature.getTelephone());
            pstm.executeUpdate();
            System.out.println("Candidature ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la candidature : " + e.getMessage());
        }
    }




    public List<Candidature> getAll() {

            List<Candidature> candidatures = new ArrayList<>();
        String qry = "SELECT * FROM candidature c JOIN offreemploi o ON c.offreId = o.id";
            try {
                Statement stm = cnx.createStatement();
                ResultSet rs = stm.executeQuery(qry);
                while (rs.next()) {
                    Candidature candidature = new Candidature();
                    candidature.setId(rs.getInt("id"));
                    candidature.setNom(rs.getString("nom"));
                    candidature.setPrenom(rs.getString("prenom"));
                    candidature.setEmail(rs.getString("email"));
                    candidature.setTelephone(rs.getString("telephone"));
                    candidature.setStatut(Statut.valueOf(rs.getString("statut")));
                    candidature.setCvUrl(rs.getString("cvUrl"));
                    candidature.setLettreMotivation(rs.getString("lettreMotivation"));
                    candidature.setDateCandidature(rs.getTimestamp("dateCandidature").toLocalDateTime());
                    int offreId = rs.getInt("offreId");
                    Offreemploi offre = new Offreemploi();
                    offre.setId(offreId);
                    offre.setTitre(rs.getString("titre"));
                    offre.setDescription(rs.getString("description"));
                    offre.setCompetences(rs.getString("competences"));
                    offre.setExperiencerequise(experience.valueOf(rs.getString("experiencerequise")));
                    offre.setNiveaulangues(Niveaulangues.valueOf(rs.getString("niveaulangues")));
                    offre.setTypecontrat(TypeContrat.valueOf(rs.getString("typecontrat")));
                    offre.setNiveauEtudes(Niveauetudes.valueOf(rs.getString("niveauEtudes")));
                    candidature.setOffreemploi(offre);
                    candidatures.add(candidature);

                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la récupération des candidatures : " + e.getMessage());
            }
            return candidatures;
        }




    public void updatee (Candidature candidature, Offreemploi offre) {
        String qry = "UPDATE candidature SET dateCandidature=?, statut=?, cvUrl=?, lettreMotivation=?, offreId=?, Nom=?, Prenom=?, email=?, telephone=? WHERE id=?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setTimestamp(1, Timestamp.valueOf(candidature.getDateCandidature()));
            pstm.setString(2, candidature.getStatut().name());
            pstm.setString(3, candidature.getCvUrl());
            pstm.setString(4, candidature.getLettreMotivation());
            pstm.setInt(5, offre.getId());
            pstm.setString(6, candidature.getNom());
            pstm.setString(7, candidature.getPrenom());
            pstm.setString(8, candidature.getEmail());
            pstm.setString(9, candidature.getTelephone());
            pstm.setInt(10, candidature.getId());
            pstm.executeUpdate();
            System.out.println("Candidature mise à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la candidature : " + e.getMessage());
        }
    }







    public Candidature getbyid(int id) {
        String qry = "SELECT * FROM candidature WHERE id = ?";
        Candidature candidature = null;

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                candidature = new Candidature();
                candidature.setId(rs.getInt("id"));
                candidature.setDateCandidature(rs.getTimestamp("dateCandidature").toLocalDateTime());
                candidature.setStatut(Statut.valueOf(rs.getString("statut")));
                candidature.setCvUrl(rs.getString("cvUrl"));
                candidature.setLettreMotivation(rs.getString("lettreMotivation"));
                candidature.setNom(rs.getString("Nom"));
                candidature.setPrenom(rs.getString("Prenom"));
                candidature.setEmail(rs.getString("email"));
                candidature.setTelephone(rs.getString("telephone"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la candidature : " + e.getMessage());
        }
        return candidature;
    }
    public void delete(int id) {

        String qry = "DELETE FROM candidature WHERE id=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            pstm.executeUpdate();
            System.out.println("Candidature supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la candidature : " + e.getMessage());
        }
    }
    public void update(Candidature candidature) {
        String qry = "UPDATE candidature SET dateCandidature=?, statut=?, cvUrl=?, lettreMotivation=?, offreId=?, Nom=?, Prenom=?, email=?, telephone=? WHERE id=?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setTimestamp(1, Timestamp.valueOf(candidature.getDateCandidature()));
            pstm.setString(2, candidature.getStatut().name());
            pstm.setString(3, candidature.getCvUrl());
            pstm.setString(4, candidature.getLettreMotivation());

            if (candidature.getOffreemploi() != null) {
                pstm.setInt(5, candidature.getOffreemploi().getId());
            } else {
                pstm.setNull(5, java.sql.Types.INTEGER);
            }

            pstm.setString(6, candidature.getNom());
            pstm.setString(7, candidature.getPrenom());
            pstm.setString(8, candidature.getEmail());
            pstm.setString(9, candidature.getTelephone());
            pstm.setInt(10, candidature.getId());

            int rowsUpdated = pstm.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Candidature mise à jour avec succès !");
            } else {
                System.out.println("Aucune candidature trouvée avec cet ID !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la candidature : " + e.getMessage());
        }
    }
    public List<Candidature> getByStatut(Statut statut) {
        String qry = "SELECT * FROM candidature WHERE statut = ?";
        List<Candidature> candidatures = new ArrayList<>();

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, statut.name()); // Récupère le statut en chaîne (enum)
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Candidature candidature = new Candidature();
                candidature.setId(rs.getInt("id"));
                candidature.setDateCandidature(rs.getTimestamp("dateCandidature").toLocalDateTime());
                candidature.setStatut(Statut.valueOf(rs.getString("statut")));
                candidature.setCvUrl(rs.getString("cvUrl"));
                candidature.setLettreMotivation(rs.getString("lettreMotivation"));
                candidature.setNom(rs.getString("Nom"));
                candidature.setPrenom(rs.getString("Prenom"));
                candidature.setEmail(rs.getString("email"));
                candidature.setTelephone(rs.getString("telephone"));

                candidatures.add(candidature);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des candidatures par statut : " + e.getMessage());
        }

        return candidatures;
    }



}








