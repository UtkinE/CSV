package net.vazem.sql;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connector {

    public Connection getConnection(File source,Properties props){
        if(source.exists()) {
            try {
                Class.forName("org.relique.jdbc.csv.CsvDriver");
                String connName = "jdbc:relique:csv:" + source.getParent();
                return DriverManager.getConnection(connName, props);
            } catch (SQLException e) {
                System.out.println("Conn error");
            } catch (ClassNotFoundException e) {
                System.out.println("Class not find");
            }
        } else {
            System.out.println("File dont open");
        }
        return null;
    }


}
