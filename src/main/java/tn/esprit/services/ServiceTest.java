package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Formation;
import tn.esprit.models.Test;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceTest implements IService <Test>{

    private Connection cnx ;

    public ServiceTest() {
        cnx = MyDatabase.getInstance().getCnx();
    }
    @Override
    public void add(Test test) {
        String qry = "INSERT INTO `test`(`date`, `DureeTest`, `ScoreTest`, `IdEmploye`, `TypeTest`) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setDate(1, test.getDate());
            pstm.setString(2, test.getTime());
            pstm.setInt(3, test.getScore());
            pstm.setInt(4, test.getIdEmploye());
    pstm.setString(5, String.valueOf(test.getTypeTest()));
            pstm.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }

    @Override
    public List<Test> getAll() {
        List<Test> tests = new ArrayList<>();
        String qry = "SELECT * FROM `test`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()){
                Test test = new Test();
                test.setDate(rs.getDate("date"));
                test.setTime(rs.getString("DureeTest"));
                test.setScore(rs.getInt("ScoreTest"));
                test.setIdEmploye(rs.getInt("IdEmploye"));
                test.setTypeTest(Test.TypeTest.valueOf(rs.getString("typeTest")));
                tests.add(test);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
return tests;
    }

    @Override
    public void update(Test test) {
String qry ="UPDATE `test` SET `IdTest`=?,`date`=?,`DureeTest`=?,`ScoreTest`=?,`IdEmploye`=?,`TypeTest`=? WHERE  `IdTest`= ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, test.getIdTest());

            pstm.setDate(2, test.getDate());
            pstm.setString(3, test.getTime());
            pstm.setInt(4, test.getScore());
            pstm.setInt(5, test.getIdEmploye());
            pstm.setString(6, String.valueOf(test.getTypeTest()));
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());        }
    }

    @Override
    public void delete(Test test) {
        String qry = "DELETE FROM `test` WHERE `IdTest`=?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, test.getIdTest());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }
}
