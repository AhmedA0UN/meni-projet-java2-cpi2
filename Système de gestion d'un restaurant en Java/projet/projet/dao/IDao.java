package dao;

import java.util.List;

/**
 * Interface générique DAO avec les 4 opérations CRUD.
 */
public interface IDao<T> {
    boolean  insert(T obj);
    boolean  update(T obj);
    boolean  delete(int id);
    T        findById(int id);
    List<T>  findAll();
}
