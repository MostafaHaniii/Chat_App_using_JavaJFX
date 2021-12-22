/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package d9l2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mostafa
 */
public class ServerMulti {
    
    ServerSocket serverSocket;
    
    public ServerMulti()
    {
        try {
            serverSocket = new ServerSocket(5005);
            while (true)
            {
                Socket s = serverSocket.accept();
                new ChatHandle(s);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerMulti.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main (String [] args)
    {
           new ServerMulti();
    }
}
