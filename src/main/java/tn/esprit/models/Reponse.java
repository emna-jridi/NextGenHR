package tn.esprit.models;

public class Reponse {
private int idReponse, idQuestion;
private String text_response ;
private boolean est_correcte ;


public Reponse() {}
    public Reponse(int idReponse, int idQuestion, String text_response , boolean est_correcte) {
    this.idReponse = idReponse;
    this.idQuestion = idQuestion;
    this.text_response = text_response;
    this.est_correcte = est_correcte;

    }
    public Reponse(int idQuestion, String text_response , boolean est_correcte) {
        this.idQuestion = idQuestion;
        this.text_response = text_response;
        this.est_correcte = est_correcte;

    }

    public int getIdReponse() {
        return idReponse;
    }

    public void setIdReponse(int idReponse) {
        this.idReponse = idReponse;
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getText_response() {
        return text_response;
    }

    public void setText_response(String text_response) {
        this.text_response = text_response;
    }

    public boolean isEst_correcte() {
        return est_correcte;
    }

    public void setEst_correcte(boolean est_correcte) {
        this.est_correcte = est_correcte;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "idReponse=" + idReponse +
                ", idQuestion=" + idQuestion +
                ", text_response='" + text_response + '\'' +
                ", est_correcte=" + est_correcte +
                '}'
                 + "\n";
    }
}
