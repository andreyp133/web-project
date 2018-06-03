package by.gsu.epamlab.model.database;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DbConnection {

    private static DataSource ds;

    public static Connection getConnection() throws NamingException, SQLException {
        if(ds == null){
            Context envCtx = (Context) (new InitialContext().lookup(
                    "java:comp/env"));
            ds = (DataSource) envCtx.lookup("jdbc/web-project");
        }
        return ds.getConnection();
    }
}