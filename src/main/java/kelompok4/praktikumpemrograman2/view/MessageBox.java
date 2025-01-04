/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok4.praktikumpemrograman2.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.event.*;
/**
 *
 * @author Afghi
 */
public class MessageBox extends JFrame {
     
    JLabel lbMessage;
    MessageBox(String title,String message){
        this.setLayout(null);
        this.setTitle(title);
        // Initialization of object of "JButton" class.
        
        lbMessage = new JLabel(message);
         
        // Setting Bounds of a JButton.
        
        lbMessage.setBounds(20,5,390,140);
        lbMessage.setSize(380, 130); 
        //"this" keyword in java refers to current object.
        // Adding JButton on JFrame.
        
        this.add(lbMessage);
         
        // Adding Listener toJButton.
        
        this.setBounds(200,100,400,150);
        this.setAlwaysOnTop(true);
    }
     
}
