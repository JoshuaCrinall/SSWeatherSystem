/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.weathersystem;

import java.io.IOException;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherStationUserClient {
    
    static LinkedList<String> UsernamesAndPasswords = new LinkedList<>();
    private Socket socket;
    String clientID;

    
    private boolean connectToServer() {
        String host = "127.0.0.1";
        int portNumber = 9091;
        try {
            socket = new Socket(host, portNumber);
            System.out.println("Connected to Server");
            return true;
        } catch (IOException e) {
            System.out.println("Can't connect to server. Please try again");
            return false;
        }       
    }
    
        private void getClientID(String input) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(input);
         while(m.find()) {
           clientID = m.group();
         }
    }


    public static void main(String[]args) throws InterruptedException, IOException {
        WeatherStationUserClient a = new WeatherStationUserClient();
        loginPanel loginPanel = new loginPanel();
        userClient userClient = new userClient();
        loginPanel.setVisible(true);
        while (true) {
            TimeUnit.SECONDS.sleep(1);
            if (!UsernamesAndPasswords.isEmpty()) {
                System.out.println("Connecting to Server");
                // Connects to the Server
                if (a.connectToServer() == true) {
                    
                    // Sends Account Details to Server
                    PrintWriter out = new PrintWriter(a.socket.getOutputStream(), true);
                    out.println(UsernamesAndPasswords.get(0));
                    out.println(UsernamesAndPasswords.get(1));
                    out.println("USER");
                    out.flush();
                    
                    // Delete Login Details from List
                    UsernamesAndPasswords.clear();
                    
                    // Wait to See If Server Accepts the Request
                    // Read From Client 
                    InputStream input = a.socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    String line;  
                    
                    while ((line = reader.readLine()) != null) {
                     System.out.println(line);

                        if (line.contains("Username & Password valid")) {
                            loginPanel.setVisible(false);
                            userClient.setVisible(true);
                            a.getClientID(line);
                            break;
                        } else if ("Incorrect".contains(line)) {
                            System.out.println("Incorrect Details");
                            break;
                        }
                    }    
                } else {
                    System.out.println("Quitting Application due to Error");
                    loginPanel.dispose();
                    break;
                }
            }
        }
    }
}       