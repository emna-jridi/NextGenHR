package tn.esprit.services;

import tn.esprit.interfaces.IServices;
import tn.esprit.models.Contrat;
import tn.esprit.models.ModePaiement;
import tn.esprit.models.Service;
import tn.esprit.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ServiceContrat implements IServices<Contrat> {

    //gérer la connexion à la base de données
    private Connection cnx;

    // initialise la connexion à la base de données
    public ServiceContrat() {
        cnx = MyDatabase.getInstance().getCnx();
    }



    //Ajouter Contrat//
    @Override
    public void add(Contrat contrat) {
        if (!validateContrat(contrat)) {
            return;
        }

        String qry = "INSERT INTO `contrat`( `dateDebutContrat`, `dateFinContrat`, `statusContrat`, `montantContrat`, `nomClient`, `emailClient`, `telephoneClient`, `modePaiement`) VALUES (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);
            pstm.setDate(1, java.sql.Date.valueOf(contrat.getDateDebutContrat()));
            pstm.setDate(2, java.sql.Date.valueOf(contrat.getDateFinContrat()));
            pstm.setString(3, contrat.getStatusContrat());
            pstm.setInt(4, contrat.getMontantContrat());
            pstm.setString(5, contrat.getNomClient());
            pstm.setString(6, contrat.getEmailClient());
            pstm.setString(7, contrat.getTelephoneClient());
            pstm.setString(8, contrat.getModeDePaiement().name());

            pstm.executeUpdate();

            // Obtenir l'ID du contrat généré
            ResultSet rs = pstm.getGeneratedKeys();
            int contratId = 0;
            if (rs.next()) {
                contratId = rs.getInt(1);
            }

            // Ajouter les services associés au contrat dans la table de jointure
            for (Service service : contrat.getServices()) {
                String insertServiceQry = "INSERT INTO `contrat_services` (`contrat_id`, `service_id`) VALUES (?, ?)";
                PreparedStatement ps = cnx.prepareStatement(insertServiceQry);
                ps.setInt(1, contratId);
                ps.setInt(2, service.getIdService());
                ps.executeUpdate();
            }

            System.out.println("Contrat et services associés ajoutés avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du contrat : " + e.getMessage());
        }
    }



    //Afficher les contrats existants et ses services//
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
                c.setDateDebutContrat(rs.getDate("dateDebutContrat").toLocalDate());
                c.setDateFinContrat(rs.getDate("dateFinContrat").toLocalDate());
                c.setStatusContrat(rs.getString("statusContrat"));
                c.setMontantContrat(rs.getInt("montantContrat"));
                c.setNomClient(rs.getString("nomClient"));
                c.setEmailClient(rs.getString("emailClient"));
                c.setTelephoneClient(rs.getString("telephoneClient"));

                // Conversion du mode de paiement (String → Enum)
                String modePaiementStr = rs.getString("modePaiement");
                if (modePaiementStr != null) {
                    c.setModeDePaiement(ModePaiement.valueOf(modePaiementStr));
                }


                // Récupérer les services associés à ce contrat
                String serviceQry = "SELECT s.* FROM `services` s " +
                        "JOIN `contrat_services` cs ON s.idService = cs.service_id " +
                        "WHERE cs.contrat_id = ?";
                PreparedStatement pstm = cnx.prepareStatement(serviceQry);
                pstm.setInt(1, c.getIdContrat());
                ResultSet serviceRs = pstm.executeQuery();

                List<Service> services = new ArrayList<>();
                while (serviceRs.next()) {
                    Service service = new Service();
                    service.setIdService(serviceRs.getInt("idService"));
                    service.setNomService(serviceRs.getString("nomService"));
                    service.setDescriptionService(serviceRs.getString("descriptionService"));
                    services.add(service);
                }
                c.setServices(services);
                contrats.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des contrats : " + e.getMessage());
        }

        return contrats;
    }






    //Mettre à jour un contrat//
    @Override
    public void update(Contrat contrat) {
        String qry = "UPDATE `contrat` SET `dateDebutContrat` = ?, `dateFinContrat` = ?, `statusContrat` = ?, `montantContrat` = ?, `nomClient` = ?, `emailClient` = ?, `telephoneClient` = ?, `modePaiement` = ? WHERE `idContrat` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setDate(1, java.sql.Date.valueOf(contrat.getDateDebutContrat()));
            pstm.setDate(2, java.sql.Date.valueOf(contrat.getDateFinContrat()));
            pstm.setString(3, contrat.getStatusContrat());
            pstm.setInt(4, contrat.getMontantContrat());
            pstm.setString(5, contrat.getNomClient());
            pstm.setString(6, contrat.getEmailClient());
            pstm.setString(7, contrat.getTelephoneClient());
            // Ajouter le mode de paiement (Enum → String)
            pstm.setString(8, contrat.getModeDePaiement().name());
            pstm.setInt(9, contrat.getIdContrat());

            pstm.executeUpdate();

            // Mettre à jour les services associés
            String deleteServicesQry = "DELETE FROM `contrat_services` WHERE `contrat_id` = ?";
            PreparedStatement deleteStmt = cnx.prepareStatement(deleteServicesQry);
            deleteStmt.setInt(1, contrat.getIdContrat());
            deleteStmt.executeUpdate();

            // Ajouter les nouveaux services
            for (Service service : contrat.getServices()) {
                String insertServiceQry = "INSERT INTO `contrat_services` (`contrat_id`, `service_id`) VALUES (?, ?)";
                PreparedStatement insertStmt = cnx.prepareStatement(insertServiceQry);
                insertStmt.setInt(1, contrat.getIdContrat());
                insertStmt.setInt(2, service.getIdService());
                insertStmt.executeUpdate();
            }

            System.out.println("Contrat et services mis à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du contrat : " + e.getMessage());
        }
    }




    //supprimer contrat//
    @Override
    public void delete(int idContrat) {
        // 1. Supprimer d'abord les services associés à ce contrat dans la table de jointure
        String deleteServicesQry = "DELETE FROM `contrat_services` WHERE `contrat_id` = ?";

        try {
            // Supprimer les services associés au contrat
            PreparedStatement deleteStmt = cnx.prepareStatement(deleteServicesQry);
            deleteStmt.setInt(1, idContrat);
            deleteStmt.executeUpdate();

            // 2. Ensuite, supprimer le contrat de la table `contrat`
            String deleteContratQry = "DELETE FROM `contrat` WHERE `idContrat` = ?";
            PreparedStatement pstm = cnx.prepareStatement(deleteContratQry);
            pstm.setInt(1, idContrat);
            pstm.executeUpdate();

            System.out.println("Contrat et services associés supprimés avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du contrat et de ses services : " + e.getMessage());
        }
    }



//récupérer les services par contrat id
    public List<Service> getServicesByContratId(int contratId) {
        List<Service> services = new ArrayList<>();
        String serviceQry = "SELECT s.* FROM `services` s " +
                "JOIN `contrat_services` cs ON s.idService = cs.service_id " +
                "WHERE cs.contrat_id = ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(serviceQry);
            pstm.setInt(1, contratId);
            ResultSet serviceRs = pstm.executeQuery();

            while (serviceRs.next()) {
                Service service = new Service();
                service.setIdService(serviceRs.getInt("idService"));
                service.setNomService(serviceRs.getString("nomService"));
                service.setDescriptionService(serviceRs.getString("descriptionService"));
                services.add(service);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des services : " + e.getMessage());
        }

        return services;
    }





    //récupérer un contrat par son id
    public Contrat getById(int id) {

        String qry = "SELECT * FROM `contrat` WHERE `idContrat` = ?";

        try {

            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Contrat contrat = new Contrat();
                contrat.setIdContrat(rs.getInt("idContrat"));
                contrat.setDateDebutContrat(rs.getDate("dateDebutContrat").toLocalDate());
                contrat.setDateFinContrat(rs.getDate("dateFinContrat").toLocalDate());
                contrat.setStatusContrat(rs.getString("statusContrat"));
                contrat.setMontantContrat(rs.getInt("montantContrat"));
                contrat.setNomClient(rs.getString("nomClient"));
                contrat.setEmailClient(rs.getString("emailClient"));
                contrat.setTelephoneClient(rs.getString("telephoneClient"));
                // Récupérer le mode de paiement (Enum)
                String modePaiementStr = rs.getString("modePaiement");
                if (modePaiementStr != null) {
                    contrat.setModeDePaiement(ModePaiement.valueOf(modePaiementStr));
                }

                return contrat;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du contrat : " + e.getMessage());
        }
        return null;
    }







    //Recherche d’un contrat par nom de client
    public List<Contrat> searchByClientName(String nomClient) {

        List<Contrat> contrats = new ArrayList<>();

        String qry = "SELECT * FROM `contrat` WHERE `nomClient` LIKE ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, "%" + nomClient + "%");
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Contrat c = new Contrat();
                c.setIdContrat(rs.getInt("idContrat"));
                c.setDateDebutContrat(rs.getDate("dateDebutContrat").toLocalDate());
                c.setDateFinContrat(rs.getDate("dateFinContrat").toLocalDate());
                c.setStatusContrat(rs.getString("statusContrat"));
                c.setMontantContrat(rs.getInt("montantContrat"));
                c.setNomClient(rs.getString("nomClient"));
                c.setEmailClient(rs.getString("emailClient"));
                c.setTelephoneClient(rs.getString("telephoneClient"));
                // Récupérer le mode de paiement (Enum)
                String modePaiementStr = rs.getString("modePaiement");
                if (modePaiementStr != null) {
                    c.setModeDePaiement(ModePaiement.valueOf(modePaiementStr));
                }

                contrats.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche : " + e.getMessage());
        }
        return contrats;
    }









    private boolean validateContrat(Contrat contrat) {

        if (contrat.getDateDebutContrat() == null || contrat.getDateFinContrat() == null) {
            System.out.println("Erreur : Les dates de début et de fin du contrat sont obligatoires !");
            return false;
        }
        if (contrat.getDateFinContrat().isBefore(contrat.getDateDebutContrat())) {
            System.out.println("Erreur : La date de fin doit être après la date de début !");
            return false;
        }

        if (contrat.getMontantContrat() < 0) {
            System.out.println("Erreur : Le montant doit être positif !");
            return false;
        }
        if (contrat.getNomClient() == null || contrat.getNomClient().isEmpty()) {
            System.out.println("Erreur : Le nom du client est obligatoire !");
            return false;
        }
        if (!contrat.getEmailClient().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            System.out.println("Erreur : Email invalide !");
            return false;
        }

        return true;
    }







}
