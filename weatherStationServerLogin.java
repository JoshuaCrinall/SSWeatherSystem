/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.weathersystem;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;  
import java.util.Scanner; 

public class weatherStationServerLogin {
        
    private static int amountOfAccounts() {
        LinkedList<String> usernameAndPasswords = storeUsernamesAndPasswords();
        int amountOfAccounts = usernameAndPasswords.size();
        return amountOfAccounts;
    }
    
     private static LinkedList<String> storeUsernamesAndPasswords() {
        LinkedList<String> storeUsernamesAndPasswords = new LinkedList<>(); 
        try {
            File usernamesAndPasswords = new File("accounts.txt");
            try (Scanner readFile = new Scanner(usernamesAndPasswords)) {
                while (readFile.hasNextLine()) {
                    String username = readFile.nextLine();
                    storeUsernamesAndPasswords.add(username);
                    String password = readFile.nextLine();
                    storeUsernamesAndPasswords.add(password);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find file accounts.txt");
        }
        return storeUsernamesAndPasswords;
    }
    
    public static boolean userAuthorization(LinkedList<String> clientAccount) {

        LinkedList<String> usernameAndPasswords = storeUsernamesAndPasswords();
        int amountOfAccounts = amountOfAccounts(); 
        
        for (int i = 0; i < amountOfAccounts; i++) {
            String username = usernameAndPasswords.get(i); 
            i++;
            String password = usernameAndPasswords.get(i);
            if (clientAccount.get(0).equals(username)) {
                if (clientAccount.get(1).equals(password)) {
                    System.out.println("Correct Username and Password");
                    return true;
                }
            }    
        }
        System.out.println("Incorrect Username and Password");
        return false;
    }
    
    private static void disconnect() {
        // Clear Unique ID and Remove it From the List
    }
}

