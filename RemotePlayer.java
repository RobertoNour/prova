package net;

import hangman.Game;
import hangman.Hangman;
import hangman.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RemotePlayer extends Player implements Runnable{
    private PrintWriter out;
    private BufferedReader in;
    private String name;
    private ListUsers list;
    private boolean loginStatus;

    public RemotePlayer(Socket socket, ListUsers list) throws IOException {
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.name = null;
        this.list = list;
        this.loginStatus = false;
    }


    @Override
    public char chooseLetter(Game game) {
       String letter = null;
        try{
           letter = in.readLine();
       }
       catch (IOException e){
           e.printStackTrace();
           System.exit(1);
       }
       return letter.charAt(0);
    }



    @Override
    public void update(Game game) {
        String message = null;
        String separator = "&";
        switch(game.getResult()) {
            case FAILED:
                message = "F"+separator+game.getSecretWord();
                out.println(message);

                break;
            case SOLVED:
                message = "S"+separator+game.getSecretWord();
                out.println(message);

                break;
            case OPEN:
                int rem = Game.MAX_FAILED_ATTEMPTS - game.countFailedAttempts();
                message = "O"+separator+rem+separator+game.countFailedAttempts()+separator+game.getKnownLetters() ;
                out.println(message);

                break;
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        try {
            while(loginStatus==false) {
                String message = this.translateMessage(in.readLine());
                out.println(message);
            }
                Hangman game = new Hangman();
                game.playGame(this);

        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }


    public String translateMessage(String message){
        String [] tokens = message.split("&");
        String answer = null;
        switch (message.charAt(0)) {
            case 'R':
                if((this.list.getUsers().containsKey(tokens[1]))) {
                    if(Integer.parseInt(tokens[2]) == this.list.getUsers().get(tokens[1])){
                        answer = "R&"+"S";
                        this.loginStatus = true;
                    }
                    else {
                        answer = "R&"+"EP";
                    }
                }
                else{
                    answer = "R&" + "EN";
                }
                break;
            case 'U':
                if(this.list.getUsers().containsKey(tokens[1])) {
                    answer = "U&" + "EN";
                }
                else{
                    answer = "U&" + "S";
                    this.loginStatus = true;
                    this.list.getUsers().put(tokens[1], Integer.parseInt(tokens[2]));
                }
                break;
        }
        return answer;
    }
}


