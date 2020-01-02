package com.mycompany.javachat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Luke
 */
import java.io.*;
import static java.lang.Thread.sleep;
import java.net.*;
import java.util.*;
public class OnlineUsersList implements Runnable {

    DatagramSocket datagramSocketOUL;

    OnlineUsersList() {
        try {
            datagramSocketOUL = new DatagramSocket();
        } catch (SocketException exc) {
            System.err.println("Error has occured while creating DatagramSocket" + exc);
        }
    }

    @Override
    public void run() {
        while(true)
        { 
            try{
                byte[] byteBufferNickname;
                byteBufferNickname = MulticastClient.nickname.getBytes();
                InetAddress iNetAddress = InetAddress.getByName("230.0.0.2");
                DatagramPacket datagramPacketNickname = new DatagramPacket(byteBufferNickname, byteBufferNickname.length, iNetAddress, 5000);
                datagramSocketOUL.send(datagramPacketNickname);  
                try{
                    sleep((long)(Math.random() * 1000));
                }
                catch(Exception e){
                    System.err.println("Error has occured in sleep function" + e);
                }
            }
            catch (IOException e) {
               System.err.println("Error has occured while sending bytes" + e);
               datagramSocketOUL.close();
            }
        }
    }
}

class ReceiveOnlineStatus implements Runnable {

    InetAddress address = null;
    MulticastSocket socket = null;
    public static ArrayList onlineUsersName = new ArrayList();

    ReceiveOnlineStatus() {
        try {
            socket = new MulticastSocket(5000);
            address = InetAddress.getByName("230.0.0.2");
            socket.joinGroup(address);
        } catch (Exception e) {
            System.err.println("Error in receiving online status" + e);
        }
    }

    @Override
    public void run() {
        onlineUsersName = new ArrayList();

        while (true) {
            try {
                DatagramPacket packet;
                byte[] byteBuffer = new byte[256];
                packet = new DatagramPacket(byteBuffer, byteBuffer.length);
                socket.receive(packet);

                String name = new String(packet.getData(), 0, packet.getLength());
                if (name.equals("OFFLINE")) {
                    onlineUsersName = new ArrayList();
                }

                if (!onlineUsersName.contains(name) && !name.equals("OFFLINE")) {
                    onlineUsersName.add(name);

                    if (MulticastClient.jTextArea3.getText().equals("")) {
                        MulticastClient.jTextArea3.setText(name);
                    } else {
                        MulticastClient.jTextArea3.setText("");
                        for (Object obj : onlineUsersName) {
                            MulticastClient.jTextArea3.setText(MulticastClient.jTextArea3.getText() + obj.toString() + "\n");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("error in receive online status class" + e);
            }
        }
    }

}
