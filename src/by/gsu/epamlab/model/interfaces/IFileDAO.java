package by.gsu.epamlab.model.interfaces;

import by.gsu.epamlab.model.exceptions.DaoException;

import javax.servlet.http.Part;

public interface IFileDAO {
    String upload(Part part, String path) throws DaoException;
    void delete(String fileName, String path) throws DaoException;
}