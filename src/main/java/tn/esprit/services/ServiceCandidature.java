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

   /* public void ajouter(Candidature candidature) {
        String qry = "INSERT INTO candidature (dateCandidature, statut, cvUrl, lettreMotivation, offreId, Nom, Prenom, email, telephone ) VALUES (?, ?, ?, ?, ?, ? , ? ,?, ?)";
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
    }*/
   public void ajouter(Candidature candidature) {
       String qry = "INSERT INTO candidature (dateCandidature, statut, cvUrl, lettreMotivation, offreId, nom, prenom, email, telephone, candidat_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
       try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
           // Ajouter la date de candidature
           pstm.setTimestamp(1, Timestamp.valueOf(candidature.getDateCandidature()));

           // Ajouter le statut de la candidature
           pstm.setString(2, candidature.getStatut().name());

           // Ajouter l'URL du CV
           pstm.setString(3, candidature.getCvUrl());

           // Ajouter la lettre de motivation
           pstm.setString(4, candidature.getLettreMotivation());

           // Ajouter l'ID de l'offre (si disponible)
           if (candidature.getOffreemploi() != null) {
               pstm.setInt(5, candidature.getOffreemploi().getId());
           } else {
               pstm.setNull(5, java.sql.Types.INTEGER);
           }

           // Ajouter les informations du candidat
           pstm.setString(6, candidature.getNom());
           pstm.setString(7, candidature.getPrenom());
           pstm.setString(8, candidature.getEmail());
           pstm.setString(9, candidature.getTelephone());

           // Récupérer l'ID du candidat connecté depuis le SessionManager
           User connectedUser = SessionManager.getConnectedUser();


           if (connectedUser != null && connectedUser.getIdUser() > 0) {
               pstm.setInt(10, connectedUser.getIdUser());  // Ajouter l'ID du candidat valide
           } else {
               System.out.println("Erreur : Aucun utilisateur valide connecté.");
               return;
           }
           System.out.println("Utilisateur connecté : " + connectedUser);

           if (connectedUser != null) {
               // Ajouter l'ID du candidat connecté dans la colonne candidat_id
               pstm.setInt(10, connectedUser.getIdUser());  // Assurez-vous que la méthode getIdUser() existe dans l'objet User
           } else {
               // Si aucun utilisateur n'est connecté, gérer la situation de manière appropriée
               System.out.println("Aucun candidat connecté.");
               return;
           }
           System.out.println("ID de l'utilisateur connecté : " + connectedUser.getIdUser());


           // Exécuter la requête d'insertion
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
   /* public List<Candidature> getCandidaturesByUserId(int userId) {
        List<Candidature> candidatures = new ArrayList<>();
        String query = "SELECT c.*, o.titre AS offreTitre " + // On récupère tout de `candidature` et seulement `titre` de `offreemploi`
                "FROM candidature c " +
                "JOIN offreemploi o ON c.offreId = o.id " +
                "WHERE c.candidat_id = ?";

        ;

        try (Connection con = MyDatabase.getInstance().getCnx();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Candidature candidature = new Candidature();
                    // Récupérer les données de la candidature depuis ResultSet et ajouter à la liste
                    candidature.setId(rs.getInt("id"));
                    candidature.setDateCandidature(rs.getTimestamp("dateCandidature").toLocalDateTime());
                    candidature.setStatut(Statut.valueOf(rs.getString("Statut")));
                    candidature.setCvUrl(rs.getString("cvUrl"));
                    candidature.setLettreMotivation(rs.getString("lettreMotivation"));
                    candidature.setNom(rs.getString("Nom"));
                    candidature.setPrenom(rs.getString("Prenom"));
                    candidature.setEmail(rs.getString("email"));
                    candidature.setTelephone(rs.getString("telephone"));
                    Offreemploi offre = new Offreemploi();
                    offre.setTitre(rs.getString("offreTitre")); // On ne récupère que le titre
                    candidature.setOffreemploi(offre);

                    candidatures.add(candidature);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Les candidatures : " + candidatures.toString());

        return candidatures;
    }*/
   public List<Candidature> getCandidaturesByUserId(int userId) {
       List<Candidature> candidatures = new ArrayList<>();
       String query = "SELECT c.*, o.titre AS offreTitre " +
               "FROM candidature c " +
               "JOIN offreemploi o ON c.offreId = o.id " +
               "WHERE c.candidat_id = ?";

       Connection con = null;
       PreparedStatement pst = null;
       ResultSet rs = null;

       try {
           con = MyDatabase.getInstance().getCnx(); // Récupération de la connexion
           pst = con.prepareStatement(query);
           pst.setInt(1, userId); // Remplacement du `?` par la valeur `userId`
           rs = pst.executeQuery();

           while (rs.next()) {
               Candidature candidature = new Candidature();
               candidature.setId(rs.getInt("id"));
               candidature.setDateCandidature(rs.getTimestamp("dateCandidature").toLocalDateTime());
               candidature.setStatut(Statut.valueOf(rs.getString("statut")));
               candidature.setCvUrl(rs.getString("cvUrl"));
               candidature.setLettreMotivation(rs.getString("lettreMotivation"));
               candidature.setNom(rs.getString("nom"));
               candidature.setPrenom(rs.getString("prenom"));
               candidature.setEmail(rs.getString("email"));
               candidature.setTelephone(rs.getString("telephone"));

               Offreemploi offre = new Offreemploi();
               offre.setTitre(rs.getString("offreTitre")); // On ne récupère que le titre
               candidature.setOffreemploi(offre);

               candidatures.add(candidature);
           }
       } catch (SQLException e) {
           System.out.println("Erreur lors de la récupération des candidatures : " + e.getMessage());
       } finally {
           // Fermeture des ressources
           try {
               if (rs != null) rs.close();
               if (pst != null) pst.close();
           } catch (SQLException e) {
               System.out.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
           }
           return candidatures;
       }};



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
    /*public List<Candidature> getByStatut(Statut statut) {
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
    }*/
    public List<Candidature> getByStatut(Statut statut) {
        String qry = "SELECT * FROM candidature c JOIN offreemploi o ON c.offreId = o.id WHERE c.statut = ?";
        List<Candidature> candidatures = new ArrayList<>();

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, statut.name()); // Convertir l'énumération en chaîne
            ResultSet rs = pstm.executeQuery();

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

                // Récupération des informations de l'offre associée
                Offreemploi offre = new Offreemploi();
                offre.setId(rs.getInt("offreId"));
                offre.setTitre(rs.getString("titre"));
                offre.setDescription(rs.getString("description"));
                offre.setCompetences(rs.getString("competences"));
                offre.setExperiencerequise(experience.valueOf(rs.getString("experiencerequise")));
                offre.setNiveaulangues(Niveaulangues.valueOf(rs.getString("niveaulangues")));
                offre.setTypecontrat(TypeContrat.valueOf(rs.getString("typecontrat")));
                offre.setNiveauEtudes(Niveauetudes.valueOf(rs.getString("niveauEtudes")));

                candidature.setOffreemploi(offre); // Associer l'offre à la candidature
                candidatures.add(candidature);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des candidatures par statut : " + e.getMessage());
        }

        return candidatures;
    }





}








