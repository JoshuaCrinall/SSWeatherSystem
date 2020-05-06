package com.mycompany.weathersystem;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

// Allows us to add more nodes to a linked list
class weatherData {
    double temperature;
    double humidity;
    double positioning;
    
    public weatherData(double a, double b, double c) {
        temperature = a; humidity = b; positioning = c;
    }
}

public class weatherStationServer {
    // Getting the username and password sent from the client
    static LinkedList<String> usernamesAndPasswordsFromClient = new LinkedList<>();
    
    // Used to make sure we don't have duplicate IDs
    LinkedList<Integer> uniqueID = new LinkedList<>();
    
    // Used to store clients connected
    LinkedList<Integer> userClients = new LinkedList<>();
    LinkedList<Integer> stationClients = new LinkedList<>();
    
    // Locations containing the data of the weather (temperature, humidity, positioning) - ADD More
    LinkedList<weatherData> nottingham = new LinkedList<>();
    LinkedList<weatherData> birmingham = new LinkedList<>();
    
    public static void main(String[]args) throws IOException {
        weatherStationServer server = new weatherStationServer();
        server.acceptConnection();
    }
    
    // Get User Clients Connected
    private int amountOfUserClientsConnected() {
        return userClients.size();
    }
    
    // Get Station Clients Connected
    private int amountOfStationClientsConnected() {
        return stationClients.size();
    }
    
     // Generate a unique ID for the Clients
    private int generateID() {
      Random ran = new Random(); 
      int id = ran.nextInt(1000);
      
      while (uniqueID.contains(id)) {
          id = ran.nextInt(1000);
      }
               
      return id;
    }
    
    // Used for Generating 4 doubles. generateData(0.00 100.00); will return 4 doubles between 0.00 and 100.00)
    private double generateData(double rangeMin, double rangeMax) {
        Random ran = new Random();
        double data = rangeMin + (rangeMax - rangeMin) * ran.nextDouble();  
        return data;
    }
    
    // Starts the Server on localhost 9091 and checks the username and password from the client
    private void acceptConnection() {
        
        boolean userClient = false;
        boolean stationClient = false;
        
        try {
            int portNumber = 9091;
            ServerSocket server = new ServerSocket(portNumber);
            System.out.println("Server started on Port " + portNumber);

            while(true) {
                Socket clientSocket = server.accept();
                weatherStationServer thread = new weatherStationServer();
                
                // Read From Client 
                InputStream input = clientSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line;  
                
                while ((line = reader.readLine()) != null) {
                    usernamesAndPasswordsFromClient.add(line);
                    
                    if (usernamesAndPasswordsFromClient.size() == 3) {
                        
                        // Generates unique ID and adds it to the list
                        int id = generateID();
                        uniqueID.add(id);
                        
                        // Find Out if the client is a User or Station client
                        String clientType = usernamesAndPasswordsFromClient.get(2);
                            switch(clientType) {
                                
                                case "USER":
                                    userClients.add(id);
                                    userClient = true;
                                    break;
                                    
                                case "STATION":
                                    stationClients.add(id);
                                    stationClient = true;
                                    break;                  
                            }
                            
                            // Login if User Client
                            if (userClient == true) {
                                // Correct Username and Password
                                if (weatherStationServerLogin.userAuthorization(usernamesAndPasswordsFromClient) == true) {
                                    // Sends Account Details to Server
                                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                                    out.println(id + " " + "Username & Password valid");
                                    out.flush();
                                    
                                // Incorrect Username or Password
                                } else {
                                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                                    out.println("Incorrect");
                                    out.flush();
                                }
                                
                                userClient = false;   
                                
                            } else if (stationClient == true) {
                                stationClient = false;
                            }
                    }
                }
                // Starts Thread
                addThreads(); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static private synchronized void addThreads(){
        weatherStationThread thread = new weatherStationThread();
        thread.start();
    }
}