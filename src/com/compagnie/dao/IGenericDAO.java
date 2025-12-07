package com.compagnie.dao;

import java.sql.SQLException;
import java.util.List;

public interface IGenericDAO<T, ID> {

    T chercher(ID id) throws SQLException;
    boolean ajouter(T entity) throws SQLException;
    boolean modifier(T entity) throws SQLException;
    boolean supprimer(ID id) throws SQLException;

    List<T> listerTous() throws SQLException;
}