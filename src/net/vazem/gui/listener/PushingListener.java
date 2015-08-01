package net.vazem.gui.listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PushingListener implements ActionListener {

    private boolean STATE = false;

    public void actionPerformed(ActionEvent event) {
        JButton btn = (JButton) event.getSource();
        if(!isSTATE()) {
            setSTATE(true);
            btn.setBackground(Color.ORANGE);
        } else {
            setSTATE(false);
            btn.setBackground(Color.WHITE);
        }


    }


    public boolean isSTATE() {
        return STATE;
    }

    public void setSTATE(boolean STATE) {
        this.STATE = STATE;
    }
}