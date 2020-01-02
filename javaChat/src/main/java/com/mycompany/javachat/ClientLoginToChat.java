/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.javachat;

import javax.swing.JOptionPane;
import static com.mycompany.javachat.MulticastClient.nickname;

/**
 *
 * @author Luke
 */
public class ClientLoginToChat {
    ClientLoginToChat() { 
        nickname = JOptionPane.showInputDialog("Wpisz swoj nick:");
        int numberOfClients = 0;
        while(nickname == null || nickname.equals("") ){ 
            if(nickname == null){   
                new ChatApp().setVisible(true);
                numberOfClients++;
                break;
            }
            else if(nickname.equals("") || nickname.length() > 24){
                JOptionPane.showMessageDialog(new ChatApp(), "Nazwa musi skladac sie od 1 do 24 znakow!");
                nickname = JOptionPane.showInputDialog("Wpisz swoj nick");
            }
        }
       if(numberOfClients == 0){
           new MulticastClient().setVisible(true);
           Thread threadNewClient = new Thread(new Client());
           threadNewClient.start();
       }
    }
}

