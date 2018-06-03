package by.gsu.epamlab.model.database;

public class Queries {
    public static final String GET_QUERY_BEGINS =
            "SELECT idTask, text, date, fileName FROM tasks ";
    public static final String INSERT_QUERY =
            "INSERT INTO tasks (idUser, text, date, fixed, bined, fileName) " +
                    "VALUES(?, ?, ?, false, false, ?);";
    public static final String UPDATE_FIX_QUERY =
            "UPDATE tasks SET fixed = true WHERE idTask = ?;";
    public static final String UPDATE_UNFIX_QUERY =
            "UPDATE tasks SET fixed = false WHERE idTask = ?;";
    public static final String UPDATE_DELETE_QUERY =
            "UPDATE tasks SET bined = true WHERE idTask = ?;";
    public static final String UPDATE_RESTORE_QUERY =
            "UPDATE tasks SET bined = false WHERE idTask = ?;";
    public static final String DELETE_TASK_QUERY =
            "DELETE FROM tasks WHERE idTask = ?;";
    public static final String DELETE_ALL_TASK_QUERY =
            "DELETE FROM tasks WHERE idUser = ? and bined = true;";
    public static final String ADD_FILE_NAME_QUERY =
            "UPDATE tasks SET fileName = ? WHERE idTask = ?;";
    public static final String DELETE_FILE_NAME_QUERY =
            "UPDATE tasks SET fileName = '' WHERE idTask = ?;";
    public static final String SELECT_FILE_NAME_DESTROY_QUERY =
            "SELECT fileName FROM tasks WHERE idTask = ?;";
    public static final String SELECT_FILE_NAME_DESTROY_ALL_QUERY =
            "SELECT fileName FROM tasks WHERE idUser = ? and bined = true;";
    public static final String GET_FILE_NAME_QUERY =
            "SELECT fileName FROM tasks WHERE idTask = ?;";
    public static final String SELECT_LOGIN_QUERY =
            "SELECT login FROM users WHERE login = ?;";
    public static final String SELECT_LOGIN_AND_PASS_QUERY =
            "SELECT user_id, password FROM users WHERE login = ?;";
    public static final String INSERT_USER_QUERY =
            "INSERT INTO users(login, password) VALUES(?, ?);";
}