/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package d9l2;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mostafa
 */
public class ChatHandle extends Thread
{
        DataInputStream DIS;
        PrintStream PS;
        static Vector<ChatHandle> clientVector = new Vector<ChatHandle>();
        
        
        public ChatHandle(Socket cs)
        {
          
            try {
                DIS = new DataInputStream(cs.getInputStream());
                PS = new PrintStream(cs.getOutputStream());
                clientVector.add(this);
                this.start();
            } catch (IOException ex) {
                Logger.getLogger(ChatHandle.class.getName()).log(Level.SEVERE, null, ex);
            }  
        }
        
        @Override
        public void run()
        {
            while(true)
            {
                try {
                    //System.out.println("test3");
                    String str = DIS.readLine();
                    //System.out.println("test4");
                    //System.out.println(str);
                    sendMassageToAll(str);
                } catch (IOException ex) {
                    Logger.getLogger(ChatHandle.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
         public void sendMassageToAll(String msg)
        {
            for(ChatHandle ch : clientVector)
            {
                ch.PS.println(msg + " "  + ch.getId());
                //System.out.println(msg);
            }
        }
}
