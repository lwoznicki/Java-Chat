/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.javachat;

import static com.mycompany.javachat.MulticastClient.datagramSocket;
import static com.mycompany.javachat.MulticastClient.nickname;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import javax.swing.JOptionPane;

/**
 *
 * @author Luke
 */
class Client implements Runnable{
  
     Client() {
         try {
             MulticastClient.datagramSocket = new DatagramSocket();
             MulticastClient.multicastSocket = new MulticastSocket(4446);           
             MulticastClient.address = InetAddress.getByName("230.0.0.2");
             MulticastClient.multicastSocket.joinGroup(MulticastClient.address);
         } catch (Exception exc) {
             JOptionPane.showMessageDialog(new ChatApp(), "Error, client has not joined to group." + exc);
         }
    }

    void newUserInfo() {
        String infoUserLog = "# " + nickname + " zalogowal sie. #";
        byte bytebuffer[] = infoUserLog.getBytes();
        try {
            InetAddress group = InetAddress.getByName("230.0.0.2");
            DatagramPacket packet = new DatagramPacket(bytebuffer, bytebuffer.length, group, 4446);
            datagramSocket.send(packet);
        } catch (Exception e) {
            System.err.println("Error has occured while sending a message!");
        }
    }

    @Override
    public void run() {
        Thread threadOnlineUsersList = new Thread(new OnlineUsersList());

        threadOnlineUsersList.start();
        Thread threadControlOnlineUsersList = new Thread(new ReceiveOnlineStatus());
        threadControlOnlineUsersList.start();
        newUserInfo();
        while (true) {
            try {
                DatagramPacket datagramPacket;
                byte[] byteBuffer = new byte[256];
                datagramPacket = new DatagramPacket(byteBuffer, byteBuffer.length);
                MulticastClient.multicastSocket.receive(datagramPacket);
                String received = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                MulticastClient.jTextArea1.setText(MulticastClient.jTextArea1.getText() + received + "\n");
                MulticastClient.jTextAreaMessage.setText("");
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

}
