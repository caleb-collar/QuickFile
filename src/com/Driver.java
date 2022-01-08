package com;


//CSC 2910 OOP | Caleb Collar | FTP System | Main driver method.
/**
 * This is the driver for the QuickFile simple FTP system.
 * @author Caleb
 * @version 1.0
 */
public class Driver 
{ 
    public static void main(String[] args) 
    { 
        System.out.println("QuickFile FTP");
        StartServer();
        StartClient();
    } 

    private static void StartClient() {
        new Thread(() -> {
            new GUI();
            //GUI.launchGUI();
        }).start();
    }
    
    private static void StartServer() {
        new Thread(() -> {
            new Server();
        }).start();
    }
}