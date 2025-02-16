package tn.esprit.interfaces;

import tn.esprit.models.Teletravail;

import java.util.List;

public interface IService<T> {

    boolean add(T t);

    List<T> getAll();

    boolean update(T t);

    boolean delete(T t);

    Teletravail getById(int id);

}
