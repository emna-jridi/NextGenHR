package tn.esprit.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int idUser;
    private String nomUser;
    private String prenomUser;
    private LocalDate dateNaissanceUser;
    private String adresseUser;
    private String telephoneUser;
    private String emailUser;
    private String password;
    private ROLE role;
    private boolean isActive;
    private List<Candidature> candidatures;

   /* public User(int userId, String email, String role) {
    }*/

    public User(int userId, String nom, String prenom, String emailDB, String telephone, String role, boolean b) {
    }

    public List<Candidature> getCandidatures() {
        return candidatures;
    }

    public void setCandidatures(List<Candidature> candidatures) {
        this.candidatures = candidatures;
    }

    public User() {
       this.role= role;
        this.isActive = true;
        this.candidatures = new ArrayList<>();
    }

    public User(int idUser, String nomUser, String prenomUser, LocalDate dateNaissanceUser, String adresseUser, String telephoneUser, String emailUser, String password, ROLE role, boolean isActive) {
        this.idUser = idUser;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.dateNaissanceUser = dateNaissanceUser;
        this.adresseUser = adresseUser;
        this.telephoneUser = telephoneUser;
        this.emailUser = emailUser;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
    }
    public User(int idUser, String nomUser, String prenomUser, String emailUser, String telephoneUser, ROLE role, boolean isActive) {
        this.idUser = idUser;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.emailUser = emailUser;
        this.telephoneUser = telephoneUser;
        this.role = role;
        // Convertir String en Enum
        this.isActive = isActive;
    }


    /*public User(int idUser, String nomUser, String prenomUser, LocalDate dateNaissanceUser, String adresseUser, String telephoneUser, String emailUser, String password) {
        this(idUser, nomUser, prenomUser, dateNaissanceUser, adresseUser, telephoneUser, emailUser, password, , true);
    }*/

    public int getIdUser() {
        return this.idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNomUser() {
        return this.nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getPrenomUser() {
        return this.prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }

    public LocalDate getDateNaissanceUser() {
        return this.dateNaissanceUser;
    }

    public void setDateNaissanceUser(LocalDate dateNaissanceUser) {
        this.dateNaissanceUser = dateNaissanceUser;
    }

    public String getAdresseUser() {
        return this.adresseUser;
    }

    public void setAdresseUser(String adresseUser) {
        this.adresseUser = adresseUser;
    }

    public String getTelephoneUser() {
        return this.telephoneUser;
    }

    public void setTelephoneUser(String telephoneUser) {
        this.telephoneUser = telephoneUser;
    }

    public String getEmailUser() {
        return this.emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public ROLE getRole() {
        return this.role;
    }

    public void setRole(ROLE role) {
        this.role = role;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    @Override
    public String toString() {
        return "User{idUser=" + idUser + ", nomUser='" + nomUser + "', prenomUser='" + prenomUser + "', dateNaissanceUser=" + dateNaissanceUser +
                ", adresseUser='" + adresseUser + "', telephoneUser='" + telephoneUser + "', emailUser='" + emailUser +
                "', password='****', role=" + role + ", isActive=" + isActive + "}";
    }


}
