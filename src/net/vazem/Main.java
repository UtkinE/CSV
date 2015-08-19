package net.vazem;


import net.vazem.gui.frame.MyFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        MyFrame mainFrame = new MyFrame("Hello World");
        mainFrame.setSize(768,480);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);



    }
}
