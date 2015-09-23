package net.vazem.gui.listener;


import net.vazem.sql.Connector;
import org.relique.jdbc.csv.CsvDriver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class EndListener implements ActionListener {

    private Map<JButton,PushingListener> mButtonColumn;

    private Map<JComboBox<String>,ComboBoxListener> mComboBoxColumn;

    private File source;

    private File output;

    private JTextField conditionFiled;

    private String separator;

    private JTextField format;

    public EndListener(Map<JButton,PushingListener> mButtonColumn,
                       Map<JComboBox<String>,ComboBoxListener> mComboBoxColumn,
                       File file,
                       JTextField textField,
                       File output,
                       String separator,
                       JTextField format){
        this.mButtonColumn = mButtonColumn;
        this.source = file;
        this.mComboBoxColumn = mComboBoxColumn;
        this.conditionFiled = textField;
        this.output = output;
        this.separator = separator;
        this.format = format;
    }

    public void actionPerformed(ActionEvent event){
        StringBuilder sWhatSearch = new StringBuilder("");
        Map<JComboBox<String>,Boolean> mStateButton= new LinkedHashMap<JComboBox<String>,Boolean>();
        int nuboiIter = 0;
        List<JComboBox<String>> lComboxBox = new LinkedList<JComboBox<String>>(mComboBoxColumn.keySet());
        for(Map.Entry<JButton,PushingListener> entry : mButtonColumn.entrySet()){
            if(entry.getValue().isSTATE()){
                sWhatSearch.append(entry.getKey().getText()).append(',');
                mStateButton.put(lComboxBox.get(nuboiIter),true);
            } else {
                mStateButton.put(lComboxBox.get(nuboiIter),false);
            }
            nuboiIter++;

        }

        if(sWhatSearch.length() != 0){
            sWhatSearch.setLength(sWhatSearch.length()-1);
        }
        StringBuilder sDataTypeProp = new StringBuilder("");
        for(Map.Entry<JComboBox<String>,ComboBoxListener> entry : mComboBoxColumn.entrySet()){
            if(mStateButton.get(entry.getKey())) {
                sDataTypeProp.append(entry.getValue().getSelectedValue()).append(',');
            }
        }
        if(sDataTypeProp.length() != 0){
            sDataTypeProp.setLength(sDataTypeProp.length()-1);
        }

        executeQuery(sWhatSearch.toString(),sDataTypeProp.toString(),conditionFiled.getText());

    }

    private void executeQuery(String columns,String dataTypeProp,String condition){
        String sDateOrTime = "dateFormat";
        Properties props = new Properties();
            props.put("separator",separator);
            props.put("columnTypes",dataTypeProp);
        if(!format.getText().equals("")) {
            if(dataTypeProp.indexOf("Time") != -1) {
                sDateOrTime = "timeFormat";
            }
            if(dataTypeProp.indexOf("Timestamp") != -1) {
                sDateOrTime = "timestampFormat";
            }

            props.put(sDateOrTime, format.getText());
        }
            props.put("timeZoneName",java.util.TimeZone.getDefault().getID());
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
                if(!condition.equals("")) {
                    query += " WHERE " + condition;
                }
                ResultSet resultSet = statement.executeQuery(query);
                PrintStream ps = new PrintStream(output.getAbsoluteFile() + "\\output.csv");
                CsvDriver.writeToCsv(resultSet, ps, false);
                conn.close();
                ps.close();
            } catch (SQLException e){

            } catch (IOException e){

            }


        }
    }
}