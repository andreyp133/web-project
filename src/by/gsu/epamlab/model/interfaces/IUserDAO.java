package by.gsu.epamlab.model.interfaces;

import by.gsu.epamlab.model.User;
import by.gsu.epamlab.model.exceptions.DaoException;

public interface IUserDAO {
    User getUser(String login, String password) throws DaoException;
    void addUser(String login, String password) throws DaoException;
}