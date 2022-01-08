package com;

//CSC 2910 OOP | Caleb Collar | FTP System | QuickFile server finder.
//Imports
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * This is the server sniffer meant for the QuickFile simple FTP system.
 * @author Caleb
 * @version 1.0
 */
public class ServerFinder
{ 
    private static final int PORT = 54321; //Default port to look for server.
    private static final int TIMEOUT = 200;
    private static List<String> hostList = new ArrayList<>();
    private static Boolean connected = false;
    
    public List<String> GetHostAddress(){
        try {
            Socket clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress("127.0.0.1", PORT), TIMEOUT);
            if (clientSocket.isBound()){
                updateHost("127.0.0.1");              
                clientSocket.close();
            }                   
        } catch (IOException ex) {
            //System.out.println(targetAddr+":"+PORT+" refused.");                                               
        }
        checkSubdomains(hostList);   
        return hostList;    
    }
    
    private static void updateHost(String ip){
        hostList.add(ip);
    }
    
    public static List<String> checkSubdomains(List<String> hostList){
        int threads = Runtime.getRuntime().availableProcessors()/2;
        List<Thread> myThreads = new ArrayList<>();
        for(int i=0; i<threads; i++){
            Thread t = new Thread("" + i){
                @Override
                public void run(){
                    connected = false;
                    int searchCounter = 0;
                    String TID = getName();                                
                    
                    while (!connected && searchCounter <= 3) {
                        //System.out.println("Thread: " + TID + " searching for connection...");
                        System.out.println("Searching...");
                        Integer range;
                        range = (255/threads);
                        range += 1;
                        Integer min = range *  Integer.valueOf(TID);
                        Integer max = min + range;
                        if (max > 255){
                            max = 255;
                        }
                        //System.out.println("Checking addresses from "+min+" to "+max);
                        String address;
                        Enumeration interfaces = null;
                        try {
                            interfaces = NetworkInterface.getNetworkInterfaces();
                        } catch (SocketException ex) {
                            //
                        }
                        if (interfaces != null){
                            while(interfaces.hasMoreElements() && !connected && searchCounter <= 3)
                            {
                                NetworkInterface n = (NetworkInterface) interfaces.nextElement();
                                Enumeration ee = n.getInetAddresses();
                                while (ee.hasMoreElements() && !connected && searchCounter <= 3)
                                {
                                    InetAddress netAddress = (InetAddress) ee.nextElement();
                                    address = netAddress.getHostAddress();                       
                                    Integer subnet = address.lastIndexOf(".")+1;
                                    Integer validIP = 0;
                                    for (int i=0; i<3; i++){
                                        if(address.indexOf('.') > 0){
                                            validIP++;
                                        }
                                    }
                                    if (validIP == 3) {
                                        for (int i =min; i<=max; i++){
                                            String targetAddr = address.substring(0, subnet);
                                            targetAddr = targetAddr + Integer.toString(i);
                                            //System.out.println("Testing: "+targetAddr);                                           
                                            if(!targetAddr.contains("127.") && !hostList.contains(targetAddr)){
                                                    try {
                                                    Socket clientSocket = new Socket();
                                                    clientSocket.connect(new InetSocketAddress(targetAddr, PORT), TIMEOUT);
                                                    if (clientSocket.isBound()){
                                                        updateHost(targetAddr);
                                                        connected = true;
                                                        clientSocket.close();
                                                    }                     
                                                } catch (IOException ex) {
                                                    //System.out.println(targetAddr+":"+PORT+" refused.");                                               
                                                } 
                                            }                                                                                                                                                                 
                                        }
                                    }
                                }
                            }
                        }
                        searchCounter++;
                    }
                }
            };
            myThreads.add(t);           
        }
        //System.out.println(myThreads.size()+" threads.");
        for (Thread thread : myThreads) {
            thread.start();
        }
        for (Thread thread : myThreads) {
            try {
                //System.out.println("Joining"+thread.getName());
                thread.join();
            } catch (InterruptedException ex) {
                System.out.println("One or more threads were interrupted inappropriately...");
            }
        }
        
        System.out.println("Done...");
        return hostList;
    }
}