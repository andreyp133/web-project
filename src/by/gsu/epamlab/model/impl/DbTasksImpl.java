package by.gsu.epamlab.model.impl;

import by.gsu.epamlab.Constants;
import by.gsu.epamlab.model.Task;
import by.gsu.epamlab.model.database.DbConnection;
import by.gsu.epamlab.model.database.TaskAction;
import by.gsu.epamlab.model.exceptions.DaoException;
import by.gsu.epamlab.model.factories.FileFactory;
import by.gsu.epamlab.model.interfaces.IFileDAO;
import by.gsu.epamlab.model.interfaces.ITaskDAO;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static by.gsu.epamlab.model.database.Queries.*;

public class DbTasksImpl implements ITaskDAO {
    private static final int ID_INDEX = 1;
    private static final int USER_ID_INDEX = 1;
    private static final int TASK_INDEX = 2;
    private static final int DATE_INDEX = 3;
    private static final int FILE_NAME_INDEX = 4;
    private static final int TASK_ID_INDEX = 1;
    private static final int TASK_ID_DELETE_INDEX = 1;
    private static final String KEY_TEXT = "text";

    @Override
    public List<Task> getTasks(String userId,
                               String queryEnds) throws DaoException {
        List<Task> tasks = new ArrayList<Task>();
        Connection connection = null;
        PreparedStatement pr = null;
        ResultSet rs = null;
        try{
            connection = DbConnection.getConnection();
            pr = connection.prepareStatement(GET_QUERY_BEGINS + queryEnds);
            pr.setString(USER_ID_INDEX, userId);
            rs = pr.executeQuery();
            while (rs.next()){
                tasks.add(new Task(rs.getString(Constants.KEY_ID_TASK),
                        rs.getString(KEY_TEXT),
                        rs.getDate(Constants.KEY_DATE),
                        rs.getString(Constants.KEY_FILE_NAME)));
            }
            return tasks;
        } catch (NamingException | SQLException e){
            throw new DaoException(e);
        } finally {
            try{
                if(rs != null){ rs.close(); }
                if(pr != null){ pr.close(); }
                if(connection != null){ connection.close(); }
            } catch (SQLException e){
                throw new DaoException(e);
            }
        }
    }

    @Override
    public void addTask(String userId, String task,
                        Date date, String fileName) throws DaoException {
        if(task.equals("")){
            throw new DaoException("Field with new task is empty");
        }
        Connection connection = null;
        PreparedStatement pr = null;
        try{
            connection = DbConnection.getConnection();
            pr = connection.prepareStatement(INSERT_QUERY);
            pr.setString(USER_ID_INDEX, userId);
            pr.setString(TASK_INDEX, task);
            pr.setDate(DATE_INDEX, date);
            pr.setString(FILE_NAME_INDEX, fileName);
            pr.execute();
        } catch (NamingException | SQLException e){
            throw new DaoException(e);
        } finally {
            closeResources(connection, pr);
        }
    }

    @Override
    public void tasksAction(String[] tasksId,
                            TaskAction action) throws DaoException{
        Connection connection = null;
        PreparedStatement pr = null;
        try{
            connection = DbConnection.getConnection();
            if(tasksId == null){
                throw new DaoException("You didn't choose any tasks");
            }
            for(String taskId : tasksId){
                pr = connection.prepareStatement(
                        action == TaskAction.DELETE ? UPDATE_DELETE_QUERY :
                        action == TaskAction.RESTORE ? UPDATE_RESTORE_QUERY :
                        action == TaskAction.FIX ? UPDATE_FIX_QUERY :
                                UPDATE_UNFIX_QUERY);
                pr.setString(TASK_ID_INDEX, taskId);
                pr.execute();
            }
        } catch (NamingException | SQLException e){
            throw new DaoException(e);
        } finally {
            closeResources(connection, pr);
        }
    }

    @Override
    public void tasksDestroy(String[] tasksId, String idUser, String path,
                             boolean isDestroyAll) throws DaoException {
        Connection connection = null;
        try{
            connection = DbConnection.getConnection();
            if(isDestroyAll){
                destroyAllOrNot(connection, idUser,
                        SELECT_FILE_NAME_DESTROY_ALL_QUERY,
                        DELETE_ALL_TASK_QUERY, path);
            } else {
                if(tasksId == null){
                    throw new DaoException("You didn't choose any tasks");
                }
                for(String taskId : tasksId){
                    destroyAllOrNot(connection, taskId,
                            SELECT_FILE_NAME_DESTROY_QUERY,
                            DELETE_TASK_QUERY, path);
                }
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

    @Override
    public void addFileName(String fileName,
                            String taskId) throws DaoException{
        Connection connection = null;
        PreparedStatement pr = null;
        try{
            connection = DbConnection.getConnection();
            pr = connection.prepareStatement(ADD_FILE_NAME_QUERY);
            pr.setString(1, fileName);
            pr.setString(2, taskId);
            pr.execute();
        } catch (NamingException | SQLException e){
            throw new DaoException(e);
        } finally {
            closeResources(connection, pr);
        }
    }

    @Override
    public String getFileName(String taskId) throws DaoException{
        Connection connection = null;
        PreparedStatement pr = null;
        try {
            connection = DbConnection.getConnection();
            pr = connection.prepareStatement(GET_FILE_NAME_QUERY);
            pr.setString(ID_INDEX, taskId);
            ResultSet rs = pr.executeQuery();
            String fileName = Constants.EMPTY_STRING;
            while (rs.next()) {
                fileName = rs.getString(Constants.KEY_FILE_NAME);
            }
            rs.close();
            return fileName;
        } catch (NamingException | SQLException e){
            throw new DaoException(e);
        } finally {
            closeResources(connection, pr);
        }
    }

    @Override
    public void deleteFileName(String taskId) throws DaoException {
        Connection connection = null;
        PreparedStatement pr = null;
        try{
            connection = DbConnection.getConnection();
            pr = connection.prepareStatement(DELETE_FILE_NAME_QUERY);
            pr.setString(ID_INDEX, taskId);
            pr.execute();
        } catch (NamingException | SQLException e){
            throw new DaoException(e);
        } finally {
            closeResources(connection, pr);
        }
    }

    private void destroyAllOrNot(
            Connection connection, String id,
            String queryOne, String queryTwo, String path) throws DaoException{
        PreparedStatement pr = null;
        IFileDAO fileImpl = FileFactory.getClassFromFactory();
        path = FileImpl.getPathToDir(path);
        try{
            pr = connection.prepareStatement(queryOne);
            pr.setString(ID_INDEX, id);
            ResultSet rs = pr.executeQuery();
            while(rs.next()){
                try{
                    fileImpl.delete(rs.getString(Constants.KEY_FILE_NAME), path);
                }catch (DaoException e){
                    e.printStackTrace();
                }
            }
            rs.close();
            pr.close();

            pr = connection.prepareStatement(queryTwo);
            pr.setString(TASK_ID_DELETE_INDEX, id);
            pr.execute();
        } catch (SQLException e){
            throw new DaoException(e);
        } finally {
            try{
                if(pr != null){
                    pr.close();
                }
            } catch (SQLException e){
                throw new DaoException(e);
            }
        }
    }

    private void closeResources(Connection connection,
                                PreparedStatement pr) throws DaoException{
        try{
            if(pr != null){ pr.close(); }
            if(connection != null){ connection.close(); }
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }
}