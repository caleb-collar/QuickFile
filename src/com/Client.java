package com;

//CSC 2910 OOP | Caleb Collar | FTP System | QuickFile Client
//Imports
import java.io.*;
import java.net.*;
import javax.swing.JProgressBar;
import javax.swing.event.ChangeListener;
import org.apache.commons.io.IOUtils;

/**
 * This is the client class for the QuickFile simple FTP system.
 * @author Caleb
 * @version 1.0
 */
public class Client{

    public Socket client = null;
    public DataInputStream dis = null;
    public DataOutputStream dos = null;
    public FileInputStream fis = null;
    public FileOutputStream fos = null;
    public BufferedReader br = null;
    public String inputFromUser = "";
    public Boolean connected = false;
    private String writeDirectory = ".\\";
    private final Integer PORT = 54321;
    
    public void FormConnections(String HOST) {       
        try {
            InputStreamReader isr = new InputStreamReader(System.in);
            br = new BufferedReader(isr);
            client = new Socket(HOST, PORT);
            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());
            connected = true;
        } catch (Exception e) {
            System.out.println("Unable to connect to server @"+HOST+":"+PORT);
        }   
    }
    
    /*
    while (connected) {
            try {              
                SendFile(filename);
                connected = false;                  
            } catch (Exception e) {
                System.out.println("Some error occured.");
            }
        }
    */
    
    public void setStrategy(String strategy){
        try {     
            dos.writeUTF("STRATEGY_CHANGE");
            dos.writeUTF(strategy);   
            //System.out.println("USING STRATEGY: "+strategy);        
        } catch (Exception e) {

        }
    } 
    
    public InetAddress getIP(){
        return client.getInetAddress();
    }
    
    public void setDownloadLocation(String directory){
        writeDirectory = directory;
         try {     
            dos.writeUTF("DOWNLOAD_LOCATION");
            dos.writeUTF(writeDirectory);   
            //System.out.println("USING DIRECTORY: "+writeDirectory);        
        } catch (Exception e) {

        }
    }
    
    public void SendFile(String filename) {      
        try {     
            File file;
            file = new File(filename);
            if (file.isFile()) {
                fis = new FileInputStream(file);
                dos.writeUTF("FILE_SEND_FROM_CLIENT");                
                dos.writeUTF(filename);             
                IOUtils.copy(fis, dos);
                dos.flush();
                fis.close();
                dos.close();              
                System.out.println("File send successful.");
            } else {
                System.out.println("404 File not found.");
            }
        } catch (Exception e) {

        }
    }

    public void ReceiveFile(int update) {
        try {
            String filename = "", filedata = "";
            //
            filename = br.readLine();
            dos.writeUTF("DOWNLOAD_FILE");
            dos.writeUTF(filename);
            filedata = dis.readUTF();
            if (filedata.equals("")) {
                System.out.println("No Such File");
            } else {
                fos = new FileOutputStream(writeDirectory+filename);
                fos.write(filedata.getBytes());
                fos.close();
            }
        } catch (Exception e) {
            
        }
    }

    private String HandleFilePath(String filename) {
        int index = filename.lastIndexOf('\\');
        String name = filename.substring(index+1);
        String finalPath = writeDirectory+name;
        return finalPath;
    }
}