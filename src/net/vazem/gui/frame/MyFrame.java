package net.vazem.gui.frame;



import net.vazem.file.SourceData;
import net.vazem.gui.listener.EndListener;
import net.vazem.gui.listener.OpenFileListener;
import net.vazem.gui.listener.PushingListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFrame extends JFrame {

    private String[] aHeaderColumn;

    private File source;

    public MyFrame(String title){
        super(title);
        createGUI();
    }

    private Map<JButton,PushingListener> map = new HashMap<JButton, PushingListener>();


    private void createGUI(){
        int yPos = 35;
        drawBtnOpenFileAndDelim();
        List<JButton> listButton = new ArrayList<JButton>();
        openFile();
        SourceData sourceData = new SourceData(source);
        aHeaderColumn = sourceData.getAllColumnNames(true,";");
        for(String title : aHeaderColumn) {
            JButton btn = new JButton(title);
            btn.setBackground(Color.WHITE);
            btn.setBounds(90, yPos, 185, 30); yPos +=30;
            btn.addActionListener(new PushingListener());
            listButton.add(btn);
            map.put(btn, (PushingListener) btn.getActionListeners()[0]);

        }

        getContentPane().setLayout(null);
        for(JButton btn : listButton){
            getContentPane().add(btn);
            getContentPane().revalidate();
        }

        JButton compBtn = new JButton("End");
        compBtn.setBounds(0,100,85,30);
        compBtn.addActionListener(new EndListener(map,getSource()));
        getContentPane().add(compBtn);
        getContentPane().revalidate();



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

    private void openFile(){
        File file = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int ret = fileChooser.showDialog(null, "Открыть файл");
        if(ret == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        }
        setSource(file);
    }


    public File getSource() {
        return source;
    }

    public void setSource(File source) {
        this.source = source;
    }

}