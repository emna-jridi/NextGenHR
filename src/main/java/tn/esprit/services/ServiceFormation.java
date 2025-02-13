package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Formation;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFormation implements IService<Formation>
{
    private Connection cnx ;

    public ServiceFormation(){
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Formation formation) {
        String qry ="INSERT INTO `formation`( `NomFormation`, `ThemeFormation`, `date`) VALUES (?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1,formation.getNomFormation());
pstm.setString(2,formation.getThemeFormation());
            pstm.setDate(3, formation.getDateFormation());
            pstm.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public List<Formation> getAll() {
        List<Formation> formations = new ArrayList<>();
            String qry ="SELECT * FROM `formation`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){

                Formation f = new Formation();
                f.setNomFormation(rs.getString("NomFormation"));
                f.setThemeFormation(rs.getString("ThemeFormation"));
                f.setDateFormation(rs.getDate("date"));

                formations.add(f);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());


        }
return formations;
    }

    @Override
    public void update(Formation formation) {
        String qry = "UPDATE `formation` SET `IdFormation`=?,`NomFormation`=?,`ThemeFormation`=?,`date`=? WHERE `idFormation` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, formation.getIdFormation());
            pstm.setString(2, formation.getNomFormation());
            pstm.setString(3, formation.getThemeFormation());
            pstm.setDate(4, formation.getDateFormation());
            pstm.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

    }

    @Override
    public void delete(Formation formation) {
String qry = "DELETE FROM `formation` WHERE `idFormation` = ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, formation.getIdFormation());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

    }
}
