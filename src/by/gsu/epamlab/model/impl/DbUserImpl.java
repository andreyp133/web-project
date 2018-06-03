package by.gsu.epamlab.model.impl;

import by.gsu.epamlab.Constants;
import by.gsu.epamlab.model.User;
import by.gsu.epamlab.model.database.DbConnection;
import by.gsu.epamlab.model.exceptions.DaoException;
import by.gsu.epamlab.model.exceptions.InvalidLoginOrPasswordException;
import by.gsu.epamlab.model.interfaces.IUserDAO;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static by.gsu.epamlab.model.database.Queries.*;

public class DbUserImpl implements IUserDAO {

    private static final int LOGIN_INDEX = 1;
    private static final int PASSWORD_INDEX = 2;
    public static final String KEY_USER_ID = "user_id";

    @Override
    public User getUser(String login, String password) throws DaoException {
        if(login == null && password == null){
            throw new InvalidLoginOrPasswordException(
                    Constants.EX_MESSAGE_INVALID_LOGIN_OR__PASSWORD);
        }
        if(login.equals(Constants.EMPTY_STRING)){
            throw new InvalidLoginOrPasswordException(
                    Constants.EX_MESSAGE_EMPTY_LOGIN);
        }
        if(password.equals(Constants.EMPTY_STRING)){
            throw new InvalidLoginOrPasswordException(
                    Constants.EX_MESSAGE_EMPTY_PASSWORD);
        }
        Connection connection = null;
        try{
            connection = DbConnection.getConnection();
            PreparedStatement pr = connection.prepareStatement(
                    SELECT_LOGIN_AND_PASS_QUERY);
            pr.setString(LOGIN_INDEX, login);
            ResultSet rs = pr.executeQuery();
            if(!rs.next()){
                throw new InvalidLoginOrPasswordException(
                        Constants.EX_MESSAGE_INVALID_LOGIN_OR__PASSWORD);
            }
            if(rs.getString(Constants.KEY_PASSWORD).equals(password)){
                return new User(rs.getString(KEY_USER_ID), login);
            } else {
                throw new InvalidLoginOrPasswordException(
                        Constants.EX_MESSAGE_INVALID_LOGIN_OR__PASSWORD);
            }
        } catch (NamingException | SQLException e){
            throw new DaoException(e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
            } catch (SQLException e){
                throw new DaoException(e);
            }
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
        Connection connection = null;
        try{
            connection = DbConnection.getConnection();
            PreparedStatement pr = connection.prepareStatement(
                    SELECT_LOGIN_QUERY);
            pr.setString(LOGIN_INDEX, login);
            synchronized (this){
                ResultSet rs = pr.executeQuery();
                if(rs.next()){
                    throw new InvalidLoginOrPasswordException(
                            Constants.EX_MESSAGE_USER_EXIST);
                }
                rs.close();
                pr.close();
                pr = connection.prepareStatement(INSERT_USER_QUERY);
                pr.setString(LOGIN_INDEX, login);
                pr.setString(PASSWORD_INDEX, password);
                pr.execute();
                pr.close();
            }
        } catch (NamingException | SQLException e){
            throw new DaoException(e);
        } finally {
            try{
                if(connection != null){
                    connection.close();
                }
            } catch (SQLException e){
                throw new DaoException(e);
            }
        }
    }
}