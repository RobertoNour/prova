package net;

import exceptions.PasswordException;
import hangman.GameResult;

import java.io.IOException;
import java.util.Scanner;

public class ClientPlayer {
    String separator;
    GameResult status ;
    Scanner tastiera;
    Boolean loginStatus;

    public ClientPlayer() {
        this.separator = "&";
        this.status = null;
        this.tastiera = new Scanner(System.in);
        this.loginStatus = false;
    }

    public Boolean getLoginStatus() {
        return loginStatus;
    }

    public void translatemessage(String message) {
        String[] tokens = message.split(this.separator);
        String secretWord = null;
        switch (message.charAt(0)) {
            case 'O':
                this.status = GameResult.OPEN;
                int rem = Integer.parseInt(tokens[1]);
                int failedAttempts = Integer.parseInt(tokens[2]);
                String knownLetters = tokens[3];
                System.out.print("\n" + rem + " tentativi rimasti\n");
                System.out.println(this.gameRepresentation(failedAttempts));
                System.out.println(knownLetters);
                break;

            case 'F':
                this.status = GameResult.FAILED;
                secretWord = tokens[1];
                printBanner("Hai perso!  La parola da indovinare era '" +
                secretWord + "'");
                break;

            case 'S':
                this.status = GameResult.SOLVED;
                secretWord = tokens[1];
                printBanner("Hai indovinato!   (" + secretWord + ")");
                break;
        }
    }
    private String gameRepresentation(int a) {

        String s = "   ___________\n  /       |   \n  |       ";
        s += (a == 0 ? "\n" : "O\n");
        s += "  |     " + (a == 0 ? "\n" : (a < 5
                ? "  +\n"
                : (a == 5 ? "--+\n" : "--+--\n")));
        s += "  |       " + (a < 2 ? "\n" : "|\n");
        s += "  |      " + (a < 3 ? "\n" : (a == 3 ? "/\n" : "/ \\\n"));
        s += "  |\n================\n";
        return s;
    }

    private void printBanner(String message) {
        System.out.println("");
        for (int i = 0; i < 80; i++)
            System.out.print("*");
        System.out.println("\n***  " + message);
        for (int i = 0; i < 80; i++)
            System.out.print("*");
        System.out.println("\n");
    }

    private void printBannerInfo() {
        System.out.println("**************************************************\n");
    }

    public GameResult getStatus() {
        return status;
    }

    public char chooseLetter() {
        for (;;) {
            System.out.print("Inserisci una lettera: ");
            String line = null;
            line = tastiera.nextLine();
            if (line.length() == 1 && Character.isLetter(line.charAt(0))) {
                return line.charAt(0);
            } else {
                System.out.println("Lettera non valida.");
            }
        }
    }

    public String welcome(){
        String message = null;
        Boolean passFlag = false;
        System.out.println("Benvenuto!\nSei già registrato?");
        String answer = tastiera.nextLine();
        if(answer.equals("si")){
            printBannerInfo();
            message = "R";
            System.out.println("Digitare nome utente");
            message = message+ separator + tastiera.nextLine();
            printBannerInfo();
            System.out.println("Digitare la password numerica");
            message = message+ separator + tastiera.nextLine();
            printBannerInfo();
        }
        else {
            message = "U";
            System.out.println("Digitare nome utente valido per la registrazione");
            message = message+ separator + tastiera.nextLine();
            printBannerInfo();
           while(!passFlag){
               System.out.println("Digitare una nuova password numerica(4 cifre)");
               String pass = tastiera.nextLine();
               try{
                   if(pass.length() != 4){
                       throw new  PasswordException("La password deve avere 4 cifre!!");
                   }
                   else {
                        passFlag = true;
                   }
               }
               catch (PasswordException e){
                   System.out.println(e.getMessage());
               }
               message = message+ separator + pass;
           }
            printBannerInfo();
        }
        return message;
    }

    public void translateAnswer(String answer){
        String [] tokens = answer.split(separator);
        switch (answer.charAt(0)) {
            case 'R':
                if(tokens[1].equals("S")){
                    System.out.println("Login eseguito con successo! Buona partita!");
                    printBannerInfo();
                    this.loginStatus = true;
                }
                else if(tokens[1].equals("EP")){
                    System.out.println("Password errata!!");
                    printBannerInfo();
                    this.loginStatus = false;
                }
                else {
                    System.out.println("Nome errato!!");
                    printBannerInfo();
                    this.loginStatus = false;
                }
                break;
            case 'U':
                if(tokens[1].equals("S")){
                    System.out.println("Registrazione e Login eseguiti con successo! Buona partita!");
                    printBannerInfo();
                    this.loginStatus = true;
                }
                else {
                    System.out.println("Nome utente non disponibile provare con un altro!");
                    printBannerInfo();
                    this.loginStatus = false;
                }
                break;
        }
    }

}
