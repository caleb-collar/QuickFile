//CSC 2910 OOP | Caleb Collar | FTP System | QuickFile Server
//Imports
import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.Timestamp;

/**
 * This is the server class for the QuickFile simple FTP system.
 * @author Caleb
 * @version 1.0
 */
class Server {

    private ServerSocket server = null;
    private Socket client = null;
    private final Integer PORT = 54321;
    private final List<Thread> myThreads = new ArrayList<>();
    
    public static void main(String[] arg) {
        Server s = new Server();
        s.FormConnections();
    }

    public void FormConnections() {
        try {
            server = new ServerSocket(PORT);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            System.out.println("QuickFile FTP Server is running... "+timestamp);
            System.out.println("The main adapter reports the server ip is "+InetAddress.getLocalHost().getHostAddress());
            while (true) {
                client = server.accept();
                System.out.println("Incoming Connection...");
                ClientThread ct = new ClientThread(client);                
                ct.start();
                timestamp = new Timestamp(System.currentTimeMillis());
                myThreads.add(ct);                
                System.out.println("Success. "+timestamp);                              
                
            }         
        } catch (IOException e) {
            System.out.println("IO exception...");
        }
    }
}