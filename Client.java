package net;
import hangman.Game;
import hangman.GameResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        String hostName = "127.0.0.1";
        //10.87.96.163
        int port = 8080;
        try {
            String message = null;
            Socket clientSocket = new Socket(hostName, port);
            System.out.println("Connected to host");
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ClientPlayer cp = new ClientPlayer();
            while(cp.getLoginStatus()== false) {
                out.println(cp.welcome());
                cp.translateAnswer(in.readLine());
            }
            message = in.readLine();
            cp.translatemessage(message);
            while(cp.getStatus() == GameResult.OPEN){
                char letter = cp.chooseLetter();
                out.println(letter);
                message = in.readLine();
                cp.translatemessage(message);
            }
            clientSocket.close();


        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }


}
