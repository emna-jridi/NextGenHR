package tn.esprit.Services;

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

        // Liste pour stocker les résultats
        List<Offreemploi> offres = new ArrayList<>();

        try {
            // Créer une déclaration SQL
            Statement stm = cnx.createStatement();

            // Exécuter la requête et récupérer les résultats
            ResultSet rs = stm.executeQuery(qry);

            // Parcourir les résultats et remplir la liste
            while (rs.next()) {
                Offreemploi offre = new Offreemploi();
                offre.setId(rs.getInt("id"));  // L'ID de l'offre
                offre.setTitre(rs.getString("titre"));  // Titre de l'offre
                offre.setDescription(rs.getString("description"));  // Description de l'offre
                offre.setExperiencerequise(rs.getString("experiencerequise"));  // Expérience requise
                offre.setNiveauEtudes(rs.getString("niveauEtudes"));  // Niveau d'études requis
                offre.setCompetences(rs.getString("competences"));  // Compétences requises
                offre.setTypecontrat(rs.getString("typecontrat"));  // Type de contrat
                offre.setLocalisation(rs.getString("localisation"));  // Localisation
                offre.setNiveaulangues(rs.getString("niveaulangues"));  // Niveau des langues
                offre.setDateCreation(rs.getTimestamp("dateCreation").toLocalDateTime());  // Date de création
                offre.setDateExpiration(rs.getTimestamp("dateExpiration").toLocalDateTime());  // Date d'expiration
                offre.setStatut(rs.getString("statut"));  // Statut de l'offre
                offre.setCandidaturesrecues(rs.getInt("candidaturesrecues")); // Candidatures reçues (par exemple)

                // Ajouter l'offre à la liste
                offres.add(offre);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Retourner la liste des offres
        return offres;
    }

    @Override
    public void update(Offreemploi offreEmploi) {

            String qry = "UPDATE offreemploi SET titre=?, description=?, experiencerequise=?, niveauEtudes=?, competences=?, typecontrat=?, localisation=?, niveaulangues=?, dateCreation=?, dateExpiration=?, statut=?, candidaturesrecues=? WHERE id=?";
            try {
                // Préparer la requête SQL avec les paramètres
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
                pstm.setInt(13, offreEmploi.getId()); // Assurer que l'id est bien passé

                // Exécution de la mise à jour
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
                pstm.setInt(1, id); // On passe l'id de l'offre à supprimer

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
    }







