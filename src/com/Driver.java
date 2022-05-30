package com;


//CSC 2910 OOP | Caleb Collar | FTP System | Main driver method.

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * This is the driver for the QuickFile simple FTP system.
 * @author Caleb
 * @version 1.0
 */
public class Driver 
{
    private static List<String> addresses = new ArrayList<>();
    public static void main(String[] args) 
    {
        System.out.println("QuickFile FTP");
        StartNetworking();
        StartGUI();
    }

    private static void StartNetworking() {
        try {
            Communicator.send(8888,"224.0.0.0");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> {
            try {
                addresses.add(Communicator.receive(8888,"224.0.0.0"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private static void StartGUI() {
        new Thread(() -> {
            new GUI();
            //GUI.launchGUI();
        }).start();
    }

    public static List<String> getAddressList(){
        return addresses;
    }
}