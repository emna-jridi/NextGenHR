package tn.esprit.models;

import java.time.LocalDate;

public class Candidat {
    private int idCandidat;
    private String nomCandidat;
    private String prenomCandidat;
    private LocalDate dateNaissanceCandidat;
    private String adresseCandidat;
    private String telephoneCandidat;
    private String emailCandidat;





    public Candidat() {};

    public Candidat( String nomCandidat, String prenomCandidat, LocalDate dateNaissanceCandidat, String adresseCandidat, String telephoneCandidat, String emailCandidat) {
        this.nomCandidat = nomCandidat;
        this.prenomCandidat = prenomCandidat;
        this.dateNaissanceCandidat = dateNaissanceCandidat;
        this.adresseCandidat = adresseCandidat;
        this.telephoneCandidat = telephoneCandidat;
        this.emailCandidat = emailCandidat;
    }

    public int getIdCandidat() {
        return idCandidat;
    }

    public void setIdCandidat(int idCandidat) {
        this.idCandidat = idCandidat;
    }

    public String getNomCandidat() {
        return nomCandidat;
    }

    public void setNomCandidat(String nomCandidat) {
        this.nomCandidat = nomCandidat;
    }

    public String getPrenomCandidat() {
        return prenomCandidat;
    }

    public void setPrenomCandidat(String prenomCandidat) {
        this.prenomCandidat = prenomCandidat;
    }

    public LocalDate getDateNaissanceCandidat() {
        return dateNaissanceCandidat;
    }

    public void setDateNaissanceCandidat(LocalDate dateNaissanceCandidat) {
        this.dateNaissanceCandidat = dateNaissanceCandidat;
    }

    public String getAdresseCandidat() {
        return adresseCandidat;
    }

    public void setAdresseCandidat(String adresseCandidat) {
        this.adresseCandidat = adresseCandidat;
    }

    public String getTelephoneCandidat() {
        return telephoneCandidat;
    }

    public void setTelephoneCandidat(String telephoneCandidat) {
        this.telephoneCandidat = telephoneCandidat;
    }

    public String getEmailCandidat() {
        return emailCandidat;
    }

    public void setEmailCandidat(String emailCandidat) {
        this.emailCandidat = emailCandidat;
    }
}
