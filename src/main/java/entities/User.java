//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package entities;

import java.time.LocalDate;

public class User {
    private int idUser;
    private String nomUser;
    private String prenomUser;
    private LocalDate dateNaissanceUser;
    private String adresseUser;
    private String telephoneUser;
    private String emailUser;
    private String password;
    private Role role;
    private boolean isActive;

    public User() {
        this.role = User.Role.EMPLOYE;
        this.isActive = true;
    }

    public User(int idUser, String nomUser, String prenomUser, LocalDate dateNaissanceUser, String adresseUser, String telephoneUser, String emailUser, String password, Role role, boolean isActive) {
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

    public User(int idUser, String nomUser, String prenomUser, LocalDate dateNaissanceUser, String adresseUser, String telephoneUser, String emailUser, String password) {
        this(idUser, nomUser, prenomUser, dateNaissanceUser, adresseUser, telephoneUser, emailUser, password, User.Role.EMPLOYE, true);
    }

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

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
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

    public String toString() {
        int var10000 = this.idUser;
        return "User{idUser=" + var10000 + ", nomUser='" + this.nomUser + "', prenomUser='" + this.prenomUser + "', dateNaissanceUser=" + String.valueOf(this.dateNaissanceUser) + ", adresseUser='" + this.adresseUser + "', telephoneUser='" + this.telephoneUser + "', emailUser='" + this.emailUser + "', password='****', role=" + this.role.getDbValue() + ", isActive=" + this.isActive + "}";
    }

    public static enum Role {
        RESPONSABLE_RH("ResponsableRH"),
        EMPLOYE("Employe");

        private final String dbValue;

        private Role(String dbValue) {
            this.dbValue = dbValue;
        }

        public String getDbValue() {
            return this.dbValue;
        }

        public static Role fromDbValue(String dbValue) {
            Role[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                Role role = var1[var3];
                if (role.dbValue.equalsIgnoreCase(dbValue)) {
                    return role;
                }
            }

            throw new IllegalArgumentException("Valeur de rôle inconnue : " + dbValue);
        }
    }
}
