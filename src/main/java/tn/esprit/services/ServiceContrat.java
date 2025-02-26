package tn.esprit.services;

import tn.esprit.interfaces.IServices;
import tn.esprit.models.Contrat;
import tn.esprit.models.Service;
import tn.esprit.models.TypeContrat;
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

        String qry = "INSERT INTO `contrat`(`typeContrat`, `dateDebutContrat`, `dateFinContrat`, `statusContrat`, `montantContrat`, `nomClient`, `emailClient`) VALUES (?,?,?,?,?,?,?)";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            //conversion de l'énumération typeContrat en une chaîne.
            pstm.setString(1, contrat.getTypeContrat().toString());
            pstm.setDate(2, java.sql.Date.valueOf(contrat.getDateDebutContrat()));
            pstm.setDate(3, java.sql.Date.valueOf(contrat.getDateFinContrat()));
            pstm.setString(4, contrat.getStatusContrat());
            pstm.setInt(5, contrat.getMontantContrat());
            pstm.setString(6, contrat.getNomClient());
            pstm.setString(7, contrat.getEmailClient());

            pstm.executeUpdate();

            System.out.println("Contrat ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du contrat : " + e.getMessage());
        }
    }


    //Afficher les contrats existants//
    @Override
    public List<Contrat> getAll() {
        List<Contrat> contrats = new ArrayList<>();
        String qry = "SELECT * FROM `contrat`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry); //contient les résultats de la requête

            while (rs.next()) {
                Contrat c = new Contrat();
                c.setIdContrat(rs.getInt("idContrat"));
                // Conversion de la chaîne en TypeContrat
                String typeContratStr = rs.getString("typeContrat");
                c.setTypeContrat(TypeContrat.valueOf(typeContratStr));
                c.setDateDebutContrat(rs.getDate("dateDebutContrat").toLocalDate());
                c.setDateFinContrat(rs.getDate("dateFinContrat").toLocalDate());
                c.setStatusContrat(rs.getString("statusContrat"));
                c.setMontantContrat(rs.getInt("montantContrat"));
                c.setNomClient(rs.getString("nomClient"));
                c.setEmailClient(rs.getString("emailClient"));

                contrats.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des contrats : " + e.getMessage());
        }

        return contrats;
    }


//Afficher les contrats avec ces services
    /*@Override
    public List<Contrat> getAll() {
        List<Contrat> contrats = new ArrayList<>();
        String qry = "SELECT * FROM contrat";

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

                // Récupérer les services associés à ce contrat
                String serviceQry = "SELECT * FROM services WHERE idContrat = ?";
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
    }*/




    //Mettre à jour un contrat//
    @Override
    public void update(Contrat contrat) {

        /*if (!validateContrat(contrat)) {
            return;
        }*/

        String qry = "UPDATE `contrat` SET `typeContrat` = ?, `dateDebutContrat` = ?, `dateFinContrat` = ?, `statusContrat` = ?, `montantContrat` = ?, `nomClient` = ?, `emailClient` = ? WHERE `idContrat` = ?";
        try {

            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, contrat.getTypeContrat().name());
            pstm.setDate(2, java.sql.Date.valueOf(contrat.getDateDebutContrat()));
            pstm.setDate(3, java.sql.Date.valueOf(contrat.getDateFinContrat()));
            pstm.setString(4, contrat.getStatusContrat());
            pstm.setInt(5, contrat.getMontantContrat());
            pstm.setString(6, contrat.getNomClient());
            pstm.setString(7, contrat.getEmailClient());
            //pstm.setInt(8, contrat.getIdService());
            pstm.setInt(8, contrat.getIdContrat());

            pstm.executeUpdate();

            System.out.println("Contrat mis à jour avec succès !");

        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du contrat : " + e.getMessage());
        }
    }



    //supprimer contrat//
    @Override
    public void delete(int idContrat) {

        /*if (getById(contrat.getIdContrat()) == null) {
            System.out.println("Erreur : Le contrat n'existe pas !");
            return;
        }*/

        String qry = "DELETE FROM `contrat` WHERE `idContrat` = ?";

        try {

            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, idContrat);

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

            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Contrat contrat = new Contrat();
                contrat.setIdContrat(rs.getInt("idContrat"));
                // Handling the enum conversion (assuming it's stored as String in the database)
                String typeContratStr = rs.getString("typeContrat");
                if (typeContratStr != null) {
                    contrat.setTypeContrat(TypeContrat.valueOf(typeContratStr)); // Convert to enum
                }
                contrat.setDateDebutContrat(rs.getDate("dateDebutContrat").toLocalDate());
                contrat.setDateFinContrat(rs.getDate("dateFinContrat").toLocalDate());
                contrat.setStatusContrat(rs.getString("statusContrat"));
                contrat.setMontantContrat(rs.getInt("montantContrat"));
                contrat.setNomClient(rs.getString("nomClient"));
                contrat.setEmailClient(rs.getString("emailClient"));
                //contrat.setIdService(rs.getInt("idService"));

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
                // Handling the enum conversion (assuming it's stored as String in the database)
                String typeContratStr = rs.getString("typeContrat");
                if (typeContratStr != null) {
                    c.setTypeContrat(TypeContrat.valueOf(typeContratStr)); // Convert to enum
                }
                c.setDateDebutContrat(rs.getDate("dateDebutContrat").toLocalDate());
                c.setDateFinContrat(rs.getDate("dateFinContrat").toLocalDate());
                c.setStatusContrat(rs.getString("statusContrat"));
                c.setMontantContrat(rs.getInt("montantContrat"));
                c.setNomClient(rs.getString("nomClient"));
                c.setEmailClient(rs.getString("emailClient"));
                //c.setIdService(rs.getInt("idService"));

                contrats.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche : " + e.getMessage());
        }
        return contrats;
    }





    /*//Tri des contrats par montant par ordre décroissant ou décroissant
    public List<Contrat> sortByMontant(boolean asc) {

        List<Contrat> contrats = new ArrayList<>();

        String order = asc ? "ASC" : "DESC";

        String qry = "SELECT * FROM `contrat` ORDER BY `montantContrat` " + order;

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Contrat c = new Contrat();
                c.setIdContrat(rs.getInt("idContrat"));
                // Handling the enum conversion (assuming it's stored as String in the database)
                String typeContratStr = rs.getString("typeContrat");
                if (typeContratStr != null) {
                    c.setTypeContrat(TypeContrat.valueOf(typeContratStr)); // Convert to enum
                }
                c.setDateDebutContrat(rs.getDate("dateDebutContrat").toLocalDate());
                c.setDateFinContrat(rs.getDate("dateFinContrat").toLocalDate());
                c.setStatusContrat(rs.getString("statusContrat"));
                c.setMontantContrat(rs.getInt("montantContrat"));
                c.setNomClient(rs.getString("nomClient"));
                c.setEmailClient(rs.getString("emailClient"));
                //c.setIdService(rs.getInt("idService"));

                contrats.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du tri : " + e.getMessage());
        }
        return contrats;
    }*/






    //Filtrer les contrats actifs (non expirés)
    /*public List<Contrat> filterActiveContracts() {

        List<Contrat> activeContracts = new ArrayList<>();

        String qry = "SELECT * FROM `contrat` WHERE `statusContrat` = 'Actif'";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Contrat c = new Contrat();
                c.setIdContrat(rs.getInt("idContrat"));
                // Handle enum conversion for 'typeContrat'
                String typeContratStr = rs.getString("typeContrat");
                if (typeContratStr != null) {
                    c.setTypeContrat(TypeContrat.valueOf(typeContratStr)); // Convert to enum
                }
                c.setDateDebutContrat(rs.getDate("dateDebutContrat").toLocalDate());
                c.setDateFinContrat(rs.getDate("dateFinContrat").toLocalDate());
                c.setStatusContrat(rs.getString("statusContrat"));
                c.setMontantContrat(rs.getInt("montantContrat"));
                c.setNomClient(rs.getString("nomClient"));
                c.setEmailClient(rs.getString("emailClient"));
                //c.setIdService(rs.getInt("idService"));

                activeContracts.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du filtrage des contrats actifs : " + e.getMessage());
        }
        return activeContracts;
    }*/
























    //récupérer un contrat avec ses services (jointure)
    /*public List<Service> getServicesByContrat(int idContrat) {
        List<Service> services = new ArrayList<>();
        String query = "SELECT s.idService, s.nomService, s.descriptionService, s.typeService, s.dateDebutService, s.dateFinService, s.statusService " +
                "FROM services s " +
                "INNER JOIN contrat c ON c.idContrat = s.idContrat " +
                "WHERE c.idContrat = ?";

        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, idContrat);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Service service = new Service();
                service.setIdService(rs.getInt("idService"));
                service.setNomService(rs.getString("nomService"));
                service.setDescriptionService(rs.getString("descriptionService"));
                service.setTypeService(rs.getString("typeService"));
                service.setDateDebutService(rs.getDate("dateDebutService").toLocalDate());
                service.setDateFinService(rs.getDate("dateFinService").toLocalDate());
                service.setStatusService(rs.getString("statusService"));

                services.add(service);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des services : " + e.getMessage());
        }
        return services;
    }*/





    private boolean validateContrat(Contrat contrat) {
        if (contrat.getTypeContrat() == null ) {
            System.out.println("Erreur : Le type de contrat est obligatoire !");
            return false;
        }
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
