package by.gsu.epamlab.model.interfaces;

import by.gsu.epamlab.model.Task;
import by.gsu.epamlab.model.database.TaskAction;
import by.gsu.epamlab.model.exceptions.DaoException;

import java.sql.Date;
import java.util.List;

public interface ITaskDAO {
    List<Task> getTasks(String userId, String queryEnds) throws DaoException;
    void addTask(String userId, String task, Date date, String fileName) throws DaoException;
    void tasksAction(String[] tasksId, TaskAction action) throws DaoException;
    void addFileName(String fileName, String taskId) throws DaoException;
    String getFileName(String taskId) throws DaoException;
    void deleteFileName(String taskId) throws DaoException;
    void tasksDestroy(String[] tasksId, String idUser, String path, boolean isDestroyAll) throws DaoException;
}