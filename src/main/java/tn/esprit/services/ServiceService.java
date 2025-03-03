package tn.esprit.services;

import tn.esprit.interfaces.IServices;
import tn.esprit.models.Contrat;
import tn.esprit.models.Service;
import tn.esprit.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceService implements IServices<Service> {

    private Connection cnx;
    public ServiceService() {
        cnx = MyDatabase.getInstance().getCnx();
    }




    //ajouter un service//
    @Override
    public void add(Service service) {

        if (!validateService(service)) {
            return;
        }

        String qry = "INSERT INTO services (NomService, DescriptionService, TypeService, DateDebutService, DateFinService, StatusService) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, service.getNomService());
            pstm.setString(2, service.getDescriptionService());
            pstm.setString(3, service.getTypeService());
            pstm.setDate(4, java.sql.Date.valueOf(service.getDateDebutService()));
            pstm.setDate(5, java.sql.Date.valueOf(service.getDateFinService()));
            pstm.setString(6, service.getStatusService());
            //pstm.setInt(7, service.getIdContrat());

            pstm.executeUpdate();

            System.out.println("Service ajouté avec succés !");

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du service : " + e.getMessage());
        }
    }



    //afficher les services//
    @Override
    public List<Service> getAll() {

        List<Service> services = new ArrayList<>();

        String qry = "SELECT * FROM services";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Service s = new Service();
                s.setIdService(rs.getInt("IdService"));
                s.setNomService(rs.getString("NomService"));
                s.setDescriptionService(rs.getString("DescriptionService"));
                s.setTypeService(rs.getString("TypeService"));
                s.setDateDebutService(rs.getDate("DateDebutService").toLocalDate());
                s.setDateFinService(rs.getDate("DateFinService").toLocalDate());
                s.setStatusService(rs.getString("StatusService"));
                //s.setIdContrat(rs.getInt("IdContrat"));

                services.add(s);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des services : " + e.getMessage());
        }
        return services;
    }



    //mettre à jour service//
    @Override
    public void update(Service service) {

        /*if (!validateService(service)) {
            return;
        }*/

        String qry = "UPDATE services SET NomService = ?, DescriptionService = ?, TypeService = ?, DateDebutService = ?, DateFinService = ?, StatusService = ? WHERE IdService = ?";

        try {

            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, service.getNomService());
            pstm.setString(2, service.getDescriptionService());
            pstm.setString(3, service.getTypeService());
            pstm.setDate(4, java.sql.Date.valueOf(service.getDateDebutService()));
            pstm.setDate(5, java.sql.Date.valueOf(service.getDateFinService()));
            pstm.setString(6, service.getStatusService());
            //pstm.setInt(7, service.getIdContrat());
            pstm.setInt(7, service.getIdService());

            pstm.executeUpdate();

            System.out.println("Service mis à jour !");

        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du service : " + e.getMessage());
        }
    }


    //supprimer service//
    @Override
    public void delete(int idService) {
        // Supprimer d'abord la relation dans contrat_services
        String deleteContractServiceQry = "DELETE FROM contrat_services WHERE idService = ?";

        try {
            // Supprimer la relation dans contrat_services
            PreparedStatement deleteContractServiceStmt = cnx.prepareStatement(deleteContractServiceQry);
            deleteContractServiceStmt.setInt(1, idService);
            deleteContractServiceStmt.executeUpdate();

            // Puis supprimer le service de la table services
            String deleteServiceQry = "DELETE FROM services WHERE IdService = ?";
            PreparedStatement pstm = cnx.prepareStatement(deleteServiceQry);
            pstm.setInt(1, idService);
            pstm.executeUpdate();

            System.out.println("Service et relation avec contrat supprimés avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du service : " + e.getMessage());
        }
    }




    // Récupérer un service par son ID
    public Service getById(int id) {

        String qry = "SELECT * FROM services WHERE IdService = ?";

        try {
            Connection conn = MyDatabase.getInstance().getCnx();
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Service service = new Service();
                service.setIdService(rs.getInt("IdService"));
                service.setNomService(rs.getString("NomService"));
                service.setDescriptionService(rs.getString("DescriptionService"));
                service.setTypeService(rs.getString("TypeService"));
                service.setDateDebutService(rs.getDate("DateDebutService").toLocalDate());
                service.setDateFinService(rs.getDate("DateFinService").toLocalDate());
                service.setStatusService(rs.getString("StatusService"));
                //service.setIdContrat(rs.getInt("IdContrat"));
                return service;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du service : " + e.getMessage());
        }
        return null;
    }






    // rechercher des services par nom de service
    public List<Service> getServicesByName(String nomService) {

        List<Service> services = new ArrayList<>();

        String qry = "SELECT * FROM services WHERE NomService LIKE ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, "%" + nomService + "%");
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Service service = new Service();
                service.setIdService(rs.getInt("IdService"));
                service.setNomService(rs.getString("NomService"));
                service.setDescriptionService(rs.getString("DescriptionService"));
                service.setTypeService(rs.getString("TypeService"));
                service.setDateDebutService(rs.getDate("DateDebutService").toLocalDate());
                service.setDateFinService(rs.getDate("DateFinService").toLocalDate());
                service.setStatusService(rs.getString("StatusService"));
                //service.setIdContrat(rs.getInt("IdContrat"));
                services.add(service);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche des services : " + e.getMessage());
        }
        return services;
    }





    // Tri des services par type de service
    public List<Service> sortServicesByType() {

        List<Service> services = new ArrayList<>();

        String qry = "SELECT * FROM services ORDER BY TypeService ASC";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Service s = new Service();
                s.setIdService(rs.getInt("IdService"));
                s.setNomService(rs.getString("NomService"));
                s.setDescriptionService(rs.getString("DescriptionService"));
                s.setTypeService(rs.getString("TypeService"));
                s.setDateDebutService(rs.getDate("DateDebutService").toLocalDate());
                s.setDateFinService(rs.getDate("DateFinService").toLocalDate());
                s.setStatusService(rs.getString("StatusService"));
                //s.setIdContrat(rs.getInt("IdContrat"));

                services.add(s);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du tri des services par type : " + e.getMessage());
        }
        return services;
    }





    // Filtrer les services actifs (non expirés)
    public List<Service> filterActiveServices() {

        List<Service> activeServices = new ArrayList<>();

        String qry = "SELECT * FROM `services` WHERE `StatusService` = 'Actif'";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Service s = new Service();
                s.setIdService(rs.getInt("IdService"));
                s.setNomService(rs.getString("NomService"));
                s.setDescriptionService(rs.getString("DescriptionService"));
                s.setTypeService(rs.getString("TypeService"));
                s.setDateDebutService(rs.getDate("DateDebutService").toLocalDate());
                s.setDateFinService(rs.getDate("DateFinService").toLocalDate());
                s.setStatusService(rs.getString("StatusService"));
                //s.setIdContrat(rs.getInt("IdContrat"));

                activeServices.add(s);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du filtrage des services actifs : " + e.getMessage());
        }
        return activeServices;
    }






    private boolean validateService(Service service) {

        if (service.getNomService() == null || service.getNomService().trim().isEmpty()) {
            System.out.println("Erreur : Le nom de service est obligatoire !");
            return false;
        }

        if (service.getDescriptionService() == null || service.getDescriptionService().trim().isEmpty()) {
            System.out.println("Erreur : La description de service est obligatoire !");
            return false;
        }

        if (service.getTypeService() == null || service.getTypeService().trim().isEmpty()) {
            System.out.println("Erreur : Le type de service est obligatoire !");
            return false;
        }

        if (service.getDateDebutService() == null || service.getDateFinService() == null) {
            System.out.println("Erreur : Les dates de sébut et de fin sont obligatoires !");
            return false;
        }

        if (service.getDateDebutService().isAfter(service.getDateFinService())) {
            System.out.println("Erreur : La date de début ne peut pas être après la date de fin. !");
            return false;
        }


        if (service.getStatusService() == null || service.getStatusService().trim().isEmpty()) {
            System.out.println("Erreur : Le statut de service est obligatoire");
            return false;
        }

        if (!service.getStatusService().equalsIgnoreCase("Actif") && !service.getStatusService().equalsIgnoreCase("Inactif")) {
            System.out.println("Erreur : Le statut de service doit etre actif ou inactif !");
            return false;
        }

        /*if (service.getIdContrat() <= 0) {
            System.out.println("Erreur : L'id du contrat doit étre un nombre positif !");
            return false;
        }*/
        return true;
    }











}
