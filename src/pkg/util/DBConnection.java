package pkg.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private Connection con;

    public Connection getConnection() {
        return con;
    }

    public DBConnection() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");

        // OSX USING DOCKER ----------------------------------------
        con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XEPDB1", "hr","hr");

        // PC (WINDOWS) --------------------------------------------
//        con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr");

    }

}