package by.gsu.epamlab.model.impl;

import by.gsu.epamlab.Constants;
import by.gsu.epamlab.model.User;
import by.gsu.epamlab.model.exceptions.DaoException;
import by.gsu.epamlab.model.exceptions.InvalidLoginOrPasswordException;
import by.gsu.epamlab.model.interfaces.IUserDAO;

import java.util.HashMap;
import java.util.Map;

public class HardcodeUserImpl implements IUserDAO {

    private static Map<String, String> users = new HashMap<String, String>();

    @Override
    public User getUser(String login, String password) throws DaoException {
        if(login.equals(Constants.EMPTY_STRING)){
            throw new InvalidLoginOrPasswordException(
                    Constants.EX_MESSAGE_EMPTY_LOGIN);
        }
        if(password.equals(Constants.EMPTY_STRING)){
            throw new InvalidLoginOrPasswordException(
                    Constants.EX_MESSAGE_EMPTY_PASSWORD);
        }
        if(users.containsKey(login)){
            if(users.get(login).equals(password)){
                return new User(login);
            } else {
                throw new InvalidLoginOrPasswordException(
                        Constants.EX_MESSAGE_INVALID_LOGIN_OR__PASSWORD);
            }
        } else {
            throw new InvalidLoginOrPasswordException(
                    Constants.EX_MESSAGE_INVALID_LOGIN_OR__PASSWORD);
        }
    }

    @Override
    public void addUser(String login, String password) throws DaoException {
        if(login.equals(Constants.EMPTY_STRING)){
            throw new InvalidLoginOrPasswordException(
                    Constants.EX_MESSAGE_EMPTY_LOGIN);
        }
        if(password.equals(Constants.EMPTY_STRING)){
            throw new InvalidLoginOrPasswordException(
                    Constants.EX_MESSAGE_EMPTY_PASSWORD);
        }
        synchronized (users){
            if(users.containsKey(login)){
                throw new DaoException(
                        Constants.EX_MESSAGE_USER_EXIST);
            } else {
                users.put(login, password);
            }
        }
    }
}