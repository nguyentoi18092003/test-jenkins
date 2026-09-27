package utilities;

import java.sql.DriverManager;
import java.sql.Connection;

public class MySQLConnUtils {
    public static Connection getMySQLConnection(){
        String hostName="localhost";
        String dbName="orangeHrm5";
        String userName="root";
        String password="";
        return getMySqlConnection(hostName,dbName,userName,password);
    }
    private static Connection getMySqlConnection(String hostName,String dbName,String userName,String password) {
        Connection conn = null;
        try {
            String connectionURL = "jdbc:mysql://" + hostName + ":3307/" + dbName;
            conn = DriverManager.getConnection(connectionURL, userName, password);
        } catch (Exception e) {
            e.printStackTrace();

    }
        return conn;
}

}