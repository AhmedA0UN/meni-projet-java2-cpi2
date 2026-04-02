package projet.dao;

import java.util.List;


public interface IDao<T> {
    boolean  insert(T obj);
    boolean  update(T obj);
    boolean  delete(int id);
    T        findById(int id);
    List<T>  findAll();
}