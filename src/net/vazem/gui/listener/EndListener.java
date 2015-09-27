package net.vazem.gui.listener;


import net.vazem.sql.Connector;
import org.apache.commons.lang3.StringUtils;

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

    private Boolean isHeader;

    public EndListener(Map<JButton,PushingListener> mButtonColumn,
                       Map<JComboBox<String>,ComboBoxListener> mComboBoxColumn,
                       File file,
                       JTextField textField,
                       File output,
                       String separator,
                       JTextField format,
                       Boolean isHeader){
        this.mButtonColumn = mButtonColumn;
        this.source = file;
        this.mComboBoxColumn = mComboBoxColumn;
        this.conditionFiled = textField;
        this.output = output;
        this.separator = separator;
        this.format = format;
        this.isHeader = isHeader;
    }

    public void actionPerformed(ActionEvent event){
        StringBuilder aliasColumn = new StringBuilder("");
        StringBuilder nativeColumn = new StringBuilder("");
        Map<JComboBox<String>,Boolean> mStateButton= new LinkedHashMap<>();
        Map<String,String> COLUMN_ALIAS = new LinkedHashMap<>();
        int nuboiIter = 1;
        List<JComboBox<String>> lComboxBox = new LinkedList<>(mComboBoxColumn.keySet());
        List<Integer> numHeaders = new LinkedList<>();
        for(Map.Entry<JButton,PushingListener> entry : mButtonColumn.entrySet()){
            if(entry.getValue().isSTATE()){
                nativeColumn.append("").append(entry.getKey().getText().trim()).append(',');
                aliasColumn.append("").append("COLUMN").append(nuboiIter).append(',');
                COLUMN_ALIAS.put(entry.getKey().getText().trim(), "COLUMN" + nuboiIter);
                mStateButton.put(lComboxBox.get(nuboiIter-1),true);
                numHeaders.add(nuboiIter);
            } else {
                mStateButton.put(lComboxBox.get(nuboiIter-1),false);
            }
            nuboiIter++;
        }


        /*
        for(Integer i : numHeaders){
            aliasColumn.append("COLUMN").append(i).append(',');
        }
        isHeader = false;
         */
        if(aliasColumn.length() != 0){
            aliasColumn.setLength(aliasColumn.length()-1);
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

        executeQuery(aliasColumn.toString(),sDataTypeProp.toString(),conditionFiled.getText(),COLUMN_ALIAS);

    }
//
    private void executeQuery(String columns,String dataTypeProp,String condition,Map<String,String> map){
        Boolean isNotHeader = isHeader;
        Properties props = new Properties();
            props.put("skipLeadingLines","1");
            props.put("separator",separator);
            props.put("columnTypes",dataTypeProp);
            props.put("ignoreNonParseableLines","true");
            props.put("charset","UTF-8");
            props.put("suppressHeaders","true");

        if(!format.getText().equals("")) {
            String sDateOrTime = "dateFormat";
            if(dataTypeProp.indexOf("Timestamp") != -1) {
                sDateOrTime = "timestampFormat";
            }
            if(dataTypeProp.indexOf("Time") != -1) {
                sDateOrTime = "timeFormat";
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
                String query = "SELECT " + columns + " FROM " + tableName;
                if(!condition.equals("")) {
                    for(Map.Entry<String,String> entry : map.entrySet()){
                        condition = condition.replace(entry.getKey(),entry.getValue());
                    }
                    query += " WHERE " + condition;
                }
                ResultSet resultSet = statement.executeQuery(query);

                getData(resultSet, columns.split(","),map,dataTypeProp);
                conn.close();

            } catch (SQLException e){
                System.err.println(e.getMessage());
            }


        }
    }

    private void getData(ResultSet set,String[] columns,Map<String,String> map,String types){
        String[] typeColumn = types.split(",");
        try{
            PrintStream ps = new PrintStream(output.getAbsoluteFile() + "\\output.csv");

            if(isHeader){
                String header = StringUtils.join(map.keySet(), separator);
                ps.println(header);
            }

            while(set.next()){
                for(int i=0;i<columns.length;i++){
                   String type = typeColumn[i].trim();
                    if(type.equals("String")){
                        ps.print(set.getString(columns[i]));
                    }
                    if(type.equals("Int")){
                        ps.print(set.getInt(columns[i]));
                    }
                    if(type.equals("Double")){
                        ps.print(set.getDouble(columns[i]));
                    }
                    if(type.equals("Date")){
                        ps.print(set.getDate(columns[i]));
                    }
                    if(type.equals("Time")){
                        ps.print(set.getTime(columns[i]));
                    }
                    if(type.equals("Timestamp")){
                        ps.print(set.getTimestamp(columns[i]));
                    }
                    if(i != columns.length-1) {
                        ps.print(";");
                    }

                }
                ps.println();
            }
            ps.close();
        } catch (SQLException e){
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


}