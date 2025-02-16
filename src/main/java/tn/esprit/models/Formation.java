package tn.esprit.models;

import java.sql.Date;

public class Formation {
private int idFormation ;
private String nomFormation , themeFormation ;
private Date dateFormation ;
   // private List<Employee> employees;
    public Formation() {}
    public Formation( int idFormation,String nomFormation, String themeFormation, Date dateFormation) {
        this.nomFormation = nomFormation;
        this.themeFormation = themeFormation;
        this.dateFormation = dateFormation;
        this.idFormation = idFormation;

    }
    public Formation(String nomFormation, String themeFormation, Date dateFormation) {
        this.nomFormation = nomFormation;
        this.themeFormation = themeFormation;
        this.dateFormation = dateFormation;

    }

    public int getIdFormation() {
        return idFormation;
    }

    public void setIdFormation(int idFormation) {
        this.idFormation = idFormation;
    }

    public String getNomFormation() {
        return nomFormation;
    }

    public void setNomFormation(String nomFormation) {
        this.nomFormation = nomFormation;
    }

    public String getThemeFormation() {
        return themeFormation;
    }

    public void setThemeFormation(String themeFormation) {
        this.themeFormation = themeFormation;
    }

    public Date getDateFormation() {
        return dateFormation;
    }

    public void setDateFormation(Date dateFormation) {
        this.dateFormation = dateFormation;
    }
//    public List<Employee> getEmployees() {
//        return employees;
//    }
//
//    public void setEmployees(List<Employee> employees) {
//        this.employees = employees;
//    }


    @Override
    public String toString() {
        return "Formation{" +
                ", nomFormation='" + nomFormation + '\'' +
                ", themeFormation='" + themeFormation + '\'' +
                ", dateFormation=" + dateFormation +
                '}' + "\n";
    }
}
