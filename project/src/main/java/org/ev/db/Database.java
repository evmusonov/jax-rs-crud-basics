package org.ev.db;

import com.sun.tools.classfile.Type;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.ev.domain.tasks.Task;
import org.json.JSONObject;

import java.sql.*;
import java.util.List;

public class Database {
    // JDBC URL, username and password of MySQL server
    private static final String url = "jdbc:postgresql://postgres/test";
    private static final String user = "postgres";
    private static final String password = "root";
    private static final String driver = "org.postgresql.Driver";

    // JDBC variables for opening and managing connection
    private static Connection con;

    public static List<Task> select(String query, Class classType) throws SQLException, ClassNotFoundException {
        establishConnection();

        QueryRunner run = new QueryRunner();
        ResultSetHandler<List<Task>> h = new BeanListHandler<>(classType);
        return run.query(
                con, query, h);
    }

    public static int execute(String query, Object[] objects) throws SQLException, ClassNotFoundException {
        establishConnection();

        QueryRunner run = new QueryRunner();
        return run.execute(con, query, objects);
    }

    public static void closeConnection() throws SQLException {
        con.close();
    }

    private static void establishConnection() throws SQLException, ClassNotFoundException {
        //Class.forName("com.mysql.cj.jdbc.Driver");
        Class.forName(driver);
        // opening database connection to MySQL server
        con = DriverManager.getConnection(url, user, password);
    }
}
