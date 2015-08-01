package net.vazem.gui.listener;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ComboBoxListener implements ActionListener{

    private String selectedValue = "Int";

    public void actionPerformed(ActionEvent event){
        JComboBox<String> comboBox = (JComboBox<String>) event.getSource();
        setSelectedValue((String) comboBox.getSelectedItem());
    }


    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }
}
