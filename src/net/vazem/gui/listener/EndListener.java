package net.vazem.gui.listener;


import net.vazem.sql.Connector;
import org.relique.jdbc.csv.CsvDriver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

public class EndListener implements ActionListener {

    private Map<JButton,PushingListener> map;

    private File source;

    public EndListener(Map<JButton,PushingListener> map, File file){
        this.map = map;
        this.source = file;
    }

    public void actionPerformed(ActionEvent event){
        StringBuilder sWhatSearch = new StringBuilder("");
        for(Map.Entry<JButton,PushingListener> entry : map.entrySet()){
            if(entry.getValue().isSTATE()){
                sWhatSearch.append(entry.getKey().getText()).append(',');
            }
        }
        if(sWhatSearch.length() != 0){
            sWhatSearch.setLength(sWhatSearch.length()-1);
        }
        System.out.println(sWhatSearch.toString());
        executeQuery(sWhatSearch.toString());

    }

    private void executeQuery(String columns){
        String separator = ";";
        Properties props = new Properties();
            props.put("separator",separator);
        try {
            Class.forName("org.relique.jdbc.csv.CsvDriver");
        } catch (ClassNotFoundException e) {

        }
        Connection conn = new Connector().getConnection(source,props);
        if(conn != null){
            try {
                Statement statement = conn.createStatement();
                String tableName = source.getName().replace(".csv", "");
                String query = "SELECT " + columns +" FROM " + tableName;
                //query += " WHERE COLUMN2 > 2";
                ResultSet resultSet = statement.executeQuery(query);
                CsvDriver.writeToCsv(resultSet, System.out, false);
                conn.close();
            } catch (SQLException e){

            }
        }
    }
}