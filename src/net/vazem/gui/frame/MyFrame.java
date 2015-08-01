package net.vazem.gui.frame;



import net.vazem.file.SourceData;
import net.vazem.gui.listener.ComboBoxListener;
import net.vazem.gui.listener.EndListener;
import net.vazem.gui.listener.OpenFileListener;
import net.vazem.gui.listener.PushingListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyFrame extends JFrame {

    private String[] aHeaderColumn;

    private File source;

    private File output;

    public MyFrame(String title){
        super(title);
        createGUI();
    }



    private void createGUI(){
       // drawBtnOpenFileAndDelim();
        Map<JButton,PushingListener> mBtnAndListener = new LinkedHashMap<JButton, PushingListener>();
        Map<JComboBox<String>,ComboBoxListener> mComboBoxAndListener = new LinkedHashMap<JComboBox<String>,ComboBoxListener>();
        
        setSource(openFile(JFileChooser.FILES_ONLY));
        SourceData sourceData = new SourceData(source);
        aHeaderColumn = sourceData.getAllColumnNames(true,";");
        String[] dataTypes = new String[]{"Int","Double", "String"};

        //add button and checkbox for each column
        int yPos = 35;
        for(String title : aHeaderColumn) {
            JButton btn = new JButton(title);
            btn.setBackground(Color.WHITE);
            btn.setBounds(90, yPos, 185, 30);
            btn.addActionListener(new PushingListener());

            JComboBox comboBox = new JComboBox(dataTypes);
            comboBox.setBounds(280,yPos,185,30);
            comboBox.addActionListener(new ComboBoxListener());
            mComboBoxAndListener.put(comboBox,(ComboBoxListener) comboBox.getActionListeners()[0]);
            mBtnAndListener.put(btn, (PushingListener) btn.getActionListeners()[0]);

            yPos +=30;

        }

        JTextField conditionField = new JTextField();
        conditionField.setBounds(470,35,250,30);
        getContentPane().add(conditionField);
        getContentPane().revalidate();

        getContentPane().setLayout(null);
        for(JButton btn : mBtnAndListener.keySet()){
            getContentPane().add(btn);
            getContentPane().revalidate();
        }
        for(JComboBox<String> comboBox : mComboBoxAndListener.keySet()){
            getContentPane().add(comboBox);
            getContentPane().revalidate();
        }

        JButton compBtn = new JButton("End");
        compBtn.setBounds(0,100,85,30);
        compBtn.addActionListener(new EndListener(mBtnAndListener, mComboBoxAndListener,getSource(),conditionField,getOutput()));
        getContentPane().add(compBtn);
        getContentPane().revalidate();
        setOutput(openFile(JFileChooser.SAVE_DIALOG));
    }


    private void  drawBtnOpenFileAndDelim(){
        JTextField delimField = new JTextField(";",4);
        delimField.setBounds(270, 35, 75, 30);
        getContentPane().add(delimField);
        getContentPane().revalidate();



        JButton btnOpenFile = new JButton();
        btnOpenFile.setBounds(0,0,185,30);
        btnOpenFile.addActionListener(new OpenFileListener());
        getContentPane().add(btnOpenFile);
        getContentPane().revalidate();
    }

    private File openFile(int mode){
        File file = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(mode);
        int ret = fileChooser.showDialog(null, "Открыть файл");
        if(ret == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        }
        return file;
    }


    public File getSource() {
        return source;
    }

    public void setSource(File source) {
        this.source = source;
    }

    public File getOutput() {
        return output;
    }

    public void setOutput(File output) {
        this.output = output;
    }
}