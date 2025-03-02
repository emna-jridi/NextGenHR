package tn.esprit.models;

import java.time.LocalDate;

public class User {
    public User(int i, String doe, String john, LocalDate of, String s, String number, String mail, String password123, Role role) {
    }

    // ✅ Énumération pour le rôle
    public enum Role {
        RESPONSABLE_RH("ResponsableRH"),
        EMPLOYE("Employe");

        private final String dbValue;

        Role(String dbValue) {
            this.dbValue = dbValue;
        }

        public String getDbValue() {
            return dbValue;
        }

        public static Role fromDbValue(String dbValue) {
            for (Role role : Role.values()) {
                if (role.dbValue.equalsIgnoreCase(dbValue)) {
                    return role;
                }
            }
            throw new IllegalArgumentException("Valeur de rôle inconnue : " + dbValue);
        }
    }

    private int idUser;
    private String nomUser;
    private String prenomUser;
    private LocalDate dateNaissanceUser;
    private String adresseUser;
    private String telephoneUser;
    private String emailUser;
    private Role role;
    private String password;
    // ✅ Constructeur par défaut
    public User() {
        this.role = Role.EMPLOYE; // Par défaut = Employé
    }

    // ✅ Constructeur complet
    public User(int idUser, String nomUser, String prenomUser, LocalDate dateNaissanceUser,
                String adresseUser, String telephoneUser, String emailUser, Role role) {
        this.idUser = idUser;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.dateNaissanceUser = dateNaissanceUser;
        this.adresseUser = adresseUser;
        this.telephoneUser = telephoneUser;
        this.emailUser = emailUser;
        this.role = role;
        this.password = password;
    }

    // ✅ Constructeur sans rôle (par défaut = Employé)
    public User(int idUser, String nomUser, String prenomUser, LocalDate dateNaissanceUser,
                String adresseUser, String telephoneUser, String emailUser) {
        this.idUser = idUser;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.dateNaissanceUser = dateNaissanceUser;
        this.adresseUser = adresseUser;
        this.telephoneUser = telephoneUser;
        this.emailUser = emailUser;
        this.role = Role.EMPLOYE;
        this.password = password;
    }

    // ✅ Getters et Setters
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getPrenomUser() {
        return prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }

    public LocalDate getDateNaissanceUser() {
        return dateNaissanceUser;
    }

    public void setDateNaissanceUser(LocalDate dateNaissanceUser) {
        this.dateNaissanceUser = dateNaissanceUser;
    }

    public String getAdresseUser() {
        return adresseUser;
    }

    public void setAdresseUser(String adresseUser) {
        this.adresseUser = adresseUser;
    }

    public String getTelephoneUser() {
        return telephoneUser;
    }

    public void setTelephoneUser(String telephoneUser) {
        this.telephoneUser = telephoneUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", nomUser='" + nomUser + '\'' +
                ", prenomUser='" + prenomUser + '\'' +
                ", dateNaissanceUser=" + dateNaissanceUser +
                ", adresseUser='" + adresseUser + '\'' +
                ", telephoneUser='" + telephoneUser + '\'' +
                ", emailUser='" + emailUser + '\'' +
                ", password='****'" +  // Sécurité : Ne pas afficher le vrai mot de passe
                ", role=" + role.getDbValue() +
                '}';
    }
}
