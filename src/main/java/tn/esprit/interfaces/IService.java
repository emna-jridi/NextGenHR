package tn.esprit.interfaces;

import java.util.List;

public interface IService<T> {  // <T> pour rendre l'interface générique
    void add(T entity);  // Ajouter une entité de type T

   List<T> getAll();  // Récupérer toutes les entités de type T

    void update(T entity);  // Mettre à jour une entité de type T

 void delete(int id);
}
