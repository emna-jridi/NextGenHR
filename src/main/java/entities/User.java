package entities;

import java.time.LocalDate;

public class User {

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
    private String password;   // 🔥 Ajout de l'attribut password
    private Role role;

    // ✅ Constructeur par défaut
    public User() {
        this.role = Role.EMPLOYE; // Par défaut = Employé
    }

    // ✅ Constructeur complet avec password
    public User(int idUser, String nomUser, String prenomUser, LocalDate dateNaissanceUser,
                String adresseUser, String telephoneUser, String emailUser,
                String password, Role role) {
        this.idUser = idUser;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.dateNaissanceUser = dateNaissanceUser;
        this.adresseUser = adresseUser;
        this.telephoneUser = telephoneUser;
        this.emailUser = emailUser;
        this.password = password;   // 🔥 Initialisation du password
        this.role = role;
    }

    // ✅ Constructeur sans rôle (par défaut = Employé) avec password
    public User(int idUser, String nomUser, String prenomUser, LocalDate dateNaissanceUser,
                String adresseUser, String telephoneUser, String emailUser, String password) {
        this.idUser = idUser;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.dateNaissanceUser = dateNaissanceUser;
        this.adresseUser = adresseUser;
        this.telephoneUser = telephoneUser;
        this.emailUser = emailUser;
        this.password = password;   // 🔥 Initialisation du password
        this.role = Role.EMPLOYE;
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

    // ✅ toString() sécurisé
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
                ", password='****'" +  // 🔒 Masqué pour des raisons de sécurité
                ", role=" + role.getDbValue() +
                '}';
    }
}
