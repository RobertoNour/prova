/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import hangman.Hangman;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class HangmanServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int port = 8080;

        try {
            ArrayList <Thread> processes = new ArrayList<>();
            ServerSocket serverSocket = new ServerSocket(port);
            ListUsers users = new ListUsers();
            users.getUsers().put("orso", 234);
            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                Thread t = new Thread(new RemotePlayer(socket, users) );

                processes.add(t);
                t.start();

            }
        }
        catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}


// TODO
/*
    eccezioni
    obbligo password numerica
*/