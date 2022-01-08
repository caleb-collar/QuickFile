package com;

//CSC 2910 OOP | Caleb Collar | FTP System | QuickFile client threads extension.
//Imports
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import org.apache.commons.io.IOUtils;

/**
 * This is an extended version of Thread for the QuickFile simple FTP system.
 * @author Caleb
 * @version 1.0
 */
class ClientThread extends Thread {

    private Socket client = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private FileInputStream fis = null;
    private FileOutputStream fos = null;
    private File file = null;
    //private String defaultDirectory = ".\\"; //Output of files.
    
    public ClientThread(Socket c) {
        try {
            client = c;
            dis = new DataInputStream(c.getInputStream());
            dos = new DataOutputStream(c.getOutputStream());
        } catch (Exception e) {

        }
    }

    @Override
    public void run() {
        //Strategies
        FileHandlingStrategy transferAll = new TransferAllStrategy();
        FileHandlingStrategy uncompressAll = new UncompressAllStrategy();
        FileHandlingStrategy filterSanitize = new FilterSanitizeStrategy();
        FileHandlingStrategy fileHandlingStrategy = transferAll; //Default
        String downloadLocation = ""; //Default
        //Exit flag
        Boolean exit = false;
        while (!exit) {
            try {
                String input = dis.readUTF();
                String fileMethod = "", filename = "", filedata = "";
                byte[] data;
                switch (input) { //Handle protocol.
                    case "STRATEGY_CHANGE":
                        fileMethod = dis.readUTF();
                        switch (fileMethod) { //Handle file handling method.
                            case "TRANSFER":
                                System.out.println("Client @"+client.getInetAddress().getHostAddress()+" requests strategy change to transfer all.");
                                fileHandlingStrategy = transferAll;
                                break;
                            case "UNCOMPRESS":
                                System.out.println("Client @"+client.getInetAddress().getHostAddress()+" requests strategy change to uncompress all.");
                                fileHandlingStrategy = uncompressAll;
                                break;
                            case "FILTER":
                                System.out.println("Client @"+client.getInetAddress().getHostAddress()+" requests strategy change to filtering.");
                                fileHandlingStrategy = filterSanitize;
                                break;
                            default:
                                System.out.println("Unknown handling method request from client. Using default.");                           
                                break;
                        }
                        System.out.println("Done...\nReady.");
                        break;
                    case "DOWNLOAD_LOCATION":
                        downloadLocation = dis.readUTF()+"\\";
                        switch (downloadLocation) {//Handle download location as string.
                            case "":
                                System.out.println("Unknown download location request from client. Using default.");                           
                                break;              
                            default:
                                System.out.println("Client @"+client.getInetAddress().getHostAddress()+" requests directory change to "+downloadLocation);                           
                                break;
                        }
                        System.out.println("Done...\nReady.");
                        break;
                    case "FILE_SEND_FROM_CLIENT":
                        filename = dis.readUTF();
                        String path = downloadLocation+HandleFilePath(filename);
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        System.out.println("Client @"+client.getInetAddress().getHostAddress()+" -> "+path+" "+timestamp);
                        fos = new FileOutputStream(path);
                        IOUtils.copy(dis, fos);
                        System.out.println("System successfully wrote the file to "+path);
                        fos.close();
                        exit = true;
                        fileHandlingStrategy.HandleFiles(path); //Strategy implementation to handle file transfers.
                        System.out.println("Done...\nReady.");
                        break;
                    case "DOWNLOAD_FILE":
                        filename = dis.readUTF();
                        file = new File(filename);
                        if (file.isFile()) {
                            fis = new FileInputStream(file);
                            data = new byte[fis.available()];
                            fis.read(data);
                            filedata = new String(data);
                            fis.close();
                            dos.writeUTF(filedata);
                            exit = true;
                        } else {
                            dos.writeUTF(""); // NO FILE FOUND
                            exit = true;
                        }   break;
                    default:
                        System.out.println("Unknown Request");
                        exit = true;
                        break;
                }
            } catch (Exception e) {
               exit = true;
            }
        }      
    }
    
    private String HandleFilePath(String filename) {
        int index = filename.lastIndexOf('\\');
        String name = filename.substring(index+1);
        String finalPath = name;
        return finalPath;
    }
}