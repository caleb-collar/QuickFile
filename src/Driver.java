//CSC 2910 OOP | Caleb Collar | FTP System | Main driver method.
/**
 * This is the driver for the QuickFile simple FTP system.
 * @author Caleb
 * @version 1.0
 */
public class Driver 
{ 
    private static ServerFinder hostFinder = new ServerFinder();
    public static void main(String[] args) 
    { 
        System.out.println("QuickFile FTP Client");
        FindTheHost();  
    } 

    private static void FindTheHost() {
        System.out.println("INFO: Attempting to locate host on the local subnet...");
        String hostIP = hostFinder.GetHostAddress();
        System.out.println("The host appears to be located at: "+hostIP);
        GUI.launchGUI(hostIP);
    }
    
    private static void StartServer() {
        new Thread(()-> {
            try {
                //Server.StartServer();
            } catch (Exception ex) {
                System.out.println("INFO: Did not start server, either one is already running or the port '54321' is bound.");
            }
        }).start();
    }
}