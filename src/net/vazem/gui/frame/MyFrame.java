package net.vazem.gui.frame;


import net.vazem.file.SourceData;
import net.vazem.gui.listener.ComboBoxListener;
import net.vazem.gui.listener.EndListener;
import net.vazem.gui.listener.PushingListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyFrame extends JFrame {

    private String[] aHeaderColumn;

    private File source;

    private String separator;

    public MyFrame(String title){
        super(title);
        createFileOpenBtn();

    }

    private void createFileOpenBtn(){
        getContentPane().setLayout(null);
        final JTextField sepField = new JTextField();
        sepField.setBounds(470,35,250,30);
        getContentPane().add(sepField);
        getContentPane().revalidate();
        JButton btn = new JButton("Open File");
        btn.setBackground(Color.WHITE);
        btn.setBounds(90, 400, 185, 30);
        btn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sepField.getText().trim().length() > 0) {
                    setSource(openFile(JFileChooser.FILES_ONLY));
                    setSeparator(sepField.getText());
                    createGUI();
                    getContentPane().repaint();
                } else {

                }
            }
        });
        getContentPane().add(btn);
        getContentPane().revalidate();
    }

    private void createGUI(){
        Map<JButton,PushingListener> mBtnAndListener = new LinkedHashMap<JButton, PushingListener>();
        Map<JComboBox<String>,ComboBoxListener> mComboBoxAndListener = new LinkedHashMap<JComboBox<String>,ComboBoxListener>();

        SourceData sourceData = new SourceData(source);
        aHeaderColumn = sourceData.getAllColumnNames(true,getSeparator());
        String[] dataTypes = new String[]{"Int","Double", "String"};
        File output = new File(source.getParent());

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
        conditionField.setBounds(470,70,250,30);
        getContentPane().add(conditionField);
        getContentPane().revalidate();

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
        compBtn.addActionListener(new EndListener(mBtnAndListener, mComboBoxAndListener,getSource(),conditionField,output,getSeparator()));
        getContentPane().add(compBtn);
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


    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}