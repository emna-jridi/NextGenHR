package tn.esprit.interfaces;

import tn.esprit.Models.Candidature;
import tn.esprit.Models.Offreemploi;

import java.util.List;

public interface IService<T> {
    void add(T entity);
    List<T> getAll();

    void update(T entity);
    public void updatee(Candidature candidature, Offreemploi offre);

    void delete(int id);
    T getbyid(int id);





    void ajouter(Candidature candidature);
}
