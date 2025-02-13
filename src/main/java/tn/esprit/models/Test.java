package tn.esprit.models;

import java.sql.Time;
import java.sql.Date;

public class Test {
private int idTest, score , idEmploye ;
private Date date;
private String time;
    public enum TypeTest {
        TECHNIQUE,
        PSYCHOTECHNIQUE
    }
    private TypeTest typeTest;

    public Test() {}
    public Test(int idTest, int score, int idEmploye, Date date, String time , TypeTest typeTest) {
        this.idTest = idTest;
        this.score = score;
        this.idEmploye = idEmploye;
        this.date = date;
        this.time = time;
        this.typeTest = typeTest;
    }
    public Test( int score, int idEmploye, Date date, String time , TypeTest typeTest) {
        this.score = score;
        this.idEmploye = idEmploye;
        this.date = date;
        this.time = time;
        this.typeTest = typeTest;
    }

    public int getIdTest() {
        return idTest;
    }

    public void setIdTest(int idTest) {
        this.idTest = idTest;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(int idEmploye) {
        this.idEmploye = idEmploye;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TypeTest getTypeTest() {
        return typeTest;
    }

    public void setTypeTest(TypeTest typeTest) {
        this.typeTest = typeTest;
    }

    @Override
    public String toString() {
        return "Test{" +
                "idTest=" + idTest +
                ", score=" + score +
                ", idEmploye=" + idEmploye +
                ", date=" + date +
                ", time=" + time +
                ", typeTest=" + typeTest +
                '}';
    }
}
