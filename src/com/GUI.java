package com;

//CSC 2910 OOP | Caleb Collar | FTP System | QuickFile FTP Client GUI
//Imports
import com.formdev.flatlaf.FlatLaf;
import java.io.*;
import javax.swing.*;
import com.formdev.flatlaf.IntelliJTheme; //Swing theme.
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * This is the GUI class for the QuickFile simple FTP system.
 * @author Caleb
 * @version 1.0
 */
public class GUI extends javax.swing.JFrame {
    //private static String HOST = null;
    private List<String> hostIPs = new ArrayList<>();
    private final String defaultTheme = "/themes/NightOwl.json";
    private final Color bgColor = Color.decode("#010E1A");
    private String usingStrategy = null, usingLocation = null; //Default strategy and this client's download location.
    private static int hostCount = 0;
    private static ServerFinder hostFinder = new ServerFinder();
    
    public GUI() {
        initTheme(); //Changes swing's theme. (Specified in local json)
        setIcon(); //Sets runtime icon to custom.
        initComponents(); //Creates GUI components.
        setSizeAndVisibility(); //Sets up initial GUI visibilty.
        setDropArea(); //Adds drag and drop.
        localHostList();
        System.out.println("System ready...");
    }
    
    public void updateHostList(List<String> IPs){
        hostIPs = IPs;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        groupHandling = new javax.swing.ButtonGroup();
        groupView = new javax.swing.ButtonGroup();
        dragNDropPanel = new javax.swing.JPanel();
        uploadProgressBar = new javax.swing.JProgressBar();
        searchProgressBar = new javax.swing.JProgressBar();
        dropZone = new javax.swing.JTextArea();
        bgLabel = new javax.swing.JLabel();
        numConnections = new javax.swing.JLabel();
        ClientMenuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuInfo = new javax.swing.JMenuItem();
        menuFileSeperator1 = new javax.swing.JPopupMenu.Separator();
        menuRefreshConnections = new javax.swing.JMenuItem();
        menuDownloadLocation = new javax.swing.JMenuItem();
        menuFileSeperator2 = new javax.swing.JPopupMenu.Separator();
        menuExit = new javax.swing.JMenuItem();
        menuHandling = new javax.swing.JMenu();
        strategyAll = new javax.swing.JRadioButtonMenuItem();
        strategyUnZip = new javax.swing.JRadioButtonMenuItem();
        strategyFilter = new javax.swing.JRadioButtonMenuItem();
        menuView = new javax.swing.JMenu();
        viewNightOwl = new javax.swing.JRadioButtonMenuItem();
        viewOceanic = new javax.swing.JRadioButtonMenuItem();
        viewDeep = new javax.swing.JRadioButtonMenuItem();
        viewSolarizedLite = new javax.swing.JRadioButtonMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 330));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dragNDropPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        dragNDropPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        uploadProgressBar.setOrientation(1);
        uploadProgressBar.setEnabled(false);
        dragNDropPanel.add(uploadProgressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(549, -460, 10, 1200));
        dragNDropPanel.add(searchProgressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 185, 240, -1));

        dropZone.setEditable(false);
        dropZone.setColumns(20);
        dropZone.setRows(5);
        dropZone.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        dropZone.setOpaque(false);
        dragNDropPanel.add(dropZone, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 560, 270));

        bgLabel.setFont(new java.awt.Font("Ubuntu Mono", 0, 24)); // NOI18N
        bgLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bgLabel.setText("Drag and Drop Files Here");
        bgLabel.setToolTipText("");
        bgLabel.setEnabled(false);
        dragNDropPanel.add(bgLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 560, 270));

        numConnections.setFont(new java.awt.Font("Ubuntu Mono", 0, 18)); // NOI18N
        numConnections.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numConnections.setText("Connections: 0");
        numConnections.setToolTipText("");
        numConnections.setEnabled(false);
        dragNDropPanel.add(numConnections, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 560, 210));

        getContentPane().add(dragNDropPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 20, 560, 270));

        menuFile.setText("File");

        menuInfo.setText("Info");
        menuInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuInfoActionPerformed(evt);
            }
        });
        menuFile.add(menuInfo);
        menuFile.add(menuFileSeperator1);

        menuRefreshConnections.setText("Refresh Connections");
        menuRefreshConnections.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRefreshConnectionsActionPerformed(evt);
            }
        });
        menuFile.add(menuRefreshConnections);

        menuDownloadLocation.setText("Download Location");
        menuDownloadLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDownloadLocationActionPerformed(evt);
            }
        });
        menuFile.add(menuDownloadLocation);
        menuFile.add(menuFileSeperator2);

        menuExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuExit.setText("Exit");
        menuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExitActionPerformed(evt);
            }
        });
        menuFile.add(menuExit);

        ClientMenuBar.add(menuFile);

        menuHandling.setText("Handling");

        groupHandling.add(strategyAll);
        strategyAll.setSelected(true);
        strategyAll.setText("Transfer All Files");
        strategyAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                strategyAllActionPerformed(evt);
            }
        });
        menuHandling.add(strategyAll);

        groupHandling.add(strategyUnZip);
        strategyUnZip.setText("Transfer & Uncompress");
        strategyUnZip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                strategyUnZipActionPerformed(evt);
            }
        });
        menuHandling.add(strategyUnZip);

        groupHandling.add(strategyFilter);
        strategyFilter.setText("Uncompress & Filter/Sanitize");
        strategyFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                strategyFilterActionPerformed(evt);
            }
        });
        menuHandling.add(strategyFilter);

        ClientMenuBar.add(menuHandling);

        menuView.setText("View");

        groupView.add(viewNightOwl);
        viewNightOwl.setSelected(true);
        viewNightOwl.setText("Night Owl");
        viewNightOwl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewNightOwlActionPerformed(evt);
            }
        });
        menuView.add(viewNightOwl);

        groupView.add(viewOceanic);
        viewOceanic.setText("Oceanic");
        viewOceanic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewOceanicActionPerformed(evt);
            }
        });
        menuView.add(viewOceanic);

        groupView.add(viewDeep);
        viewDeep.setText("Deep");
        viewDeep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewDeepActionPerformed(evt);
            }
        });
        menuView.add(viewDeep);

        groupView.add(viewSolarizedLite);
        viewSolarizedLite.setText("Solarized Light");
        viewSolarizedLite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSolarizedLiteActionPerformed(evt);
            }
        });
        menuView.add(viewSolarizedLite);

        ClientMenuBar.add(menuView);

        setJMenuBar(ClientMenuBar);

        pack();
    }

    private void menuExitActionPerformed(java.awt.event.ActionEvent evt) {
        int returnValue;
        returnValue = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exiting...", JOptionPane.YES_NO_OPTION);
        if (returnValue == JOptionPane.YES_OPTION){
            this.dispose();
        }
        else if (returnValue == JOptionPane.NO_OPTION){
            //Close dialog
        }
    }

    private void menuInfoActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane jop = new JOptionPane();        
        String versionText = "QuickFile Client GUI v1.1";
        String infoText = "-Drag and drop files to copy to server's remote or local location.\n"
                +"-Use 'handling' menu to alter the way copied files are handled after transfer.\n"
                +"-Use 'view' menu to switch the theme of the application.\n"
                +"\nCreated by Caleb A. Collar for OOP.\n";       
        jop.showMessageDialog(this, infoText, versionText, JOptionPane.INFORMATION_MESSAGE); 
    }

    private void viewNightOwlActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            InputStream themeStream = GUI.class.getResourceAsStream("/themes/NightOwl.json");
            IntelliJTheme.setup(themeStream);             
            this.getContentPane().setBackground(Color.decode("#010E1A"));
            FlatLaf.updateUI();
            
            
        } catch (Exception ex) {
            System.out.println( "Issue loading selected theme file." );
        }
    }

    private void viewOceanicActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            InputStream themeStream = GUI.class.getResourceAsStream("/themes/Oceanic.json");
            IntelliJTheme.setup(themeStream);           
            this.getContentPane().setBackground(Color.decode("#1E272C"));
            FlatLaf.updateUI();
            
            
        } catch (Exception ex) {
            System.out.println( "Issue loading selected theme file." );
        }
    }

    private void viewDeepActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            InputStream themeStream = GUI.class.getResourceAsStream("/themes/Deep.json");
            IntelliJTheme.setup(themeStream);            
            this.getContentPane().setBackground(Color.decode("#090B10"));
            FlatLaf.updateUI();
            
            
        } catch (Exception ex) {
            System.out.println( "Issue loading selected theme file." );
        }
    }

    private void viewSolarizedLiteActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            InputStream themeStream = GUI.class.getResourceAsStream("/themes/SolarizedLight.json");
            IntelliJTheme.setup(themeStream);             
            this.getContentPane().setBackground(Color.decode("#EEE8D5"));
            FlatLaf.updateUI();
            
            
        } catch (Exception ex) {
            System.out.println( "Issue loading selected theme file." );
        }
    }

    private void strategyAllActionPerformed(java.awt.event.ActionEvent evt) {
        usingStrategy = "TRANSFER"; //Sets up strategy protocol to send to server.
    }

    private void strategyUnZipActionPerformed(java.awt.event.ActionEvent evt) {
        usingStrategy = "UNCOMPRESS"; //The strategy switching takes place on the server.
    }

    private void strategyFilterActionPerformed(java.awt.event.ActionEvent evt) {
        usingStrategy = "FILTER"; //These strings are 'protocols' for which strategy to use before a file transfer.
    }

    private void menuDownloadLocationActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser(".\\"); //File chooser for download location of local server.
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showOpenDialog(this);
        if(option == JFileChooser.APPROVE_OPTION){
           usingLocation = fileChooser.getSelectedFile().toString(); //Sets download loaction.
           System.out.println("Location now set to "+usingLocation);
        }
        for (String HOST : hostIPs ){                           
            Client thisClient = new Client();
            thisClient.FormConnections(HOST);
            if(usingLocation != null && checkIsLocal(thisClient)){                    
                thisClient.setDownloadLocation(usingLocation);                           
            }
        }
    }

    private void menuRefreshConnectionsActionPerformed(java.awt.event.ActionEvent evt) {
        refreshHostList();
    }
  
    private void localHostList(){
        searchProgressBar.setVisible(true);    
        Thread searchThread = new Thread(() -> {
            System.out.println("Mapping on local adapters... ");
            hostIPs = hostFinder.GetHostAddress();
            System.out.println("Mapping WAN addresses... ");
            while (hostCount < 1){
                hostCount = 0;
                hostIPs = ServerFinder.checkSubdomains(hostIPs);
                    for (String HOST : hostIPs ){                  
                    Client thisClient = new Client();
                    thisClient.FormConnections(HOST);
                    if(!checkIsLocal(thisClient)){
                        hostCount++;
                        numConnections.setText("Connections: "+hostCount);
                        System.out.println("Connections: "+hostCount);
                    }
                }
            }
            searchProgressBar.setVisible(false);
        });
        searchThread.start();
    }
    
    private void refreshHostList(){
        searchProgressBar.setVisible(true);
        Thread searchThread = new Thread(() -> {
            if (hostCount >= 1){
                hostCount = 0;
                hostIPs = ServerFinder.checkSubdomains(hostIPs);
                for (String HOST : hostIPs ){                 
                    Client thisClient = new Client();
                    thisClient.FormConnections(HOST);
                    if(!checkIsLocal(thisClient)){
                        hostCount++;
                        numConnections.setText("Connections: "+hostCount);
                        System.out.println("Connections: "+hostCount);
                    }
                }
            } else while (hostCount < 1){
                hostCount = 0;
                hostIPs = ServerFinder.checkSubdomains(hostIPs);
                    for (String HOST : hostIPs ){                  
                    Client thisClient = new Client();
                    thisClient.FormConnections(HOST);
                    if(!checkIsLocal(thisClient)){
                        hostCount++;
                        numConnections.setText("Connections: "+hostCount);
                        System.out.println("Connections: "+hostCount);
                    }
                }
            }
            searchProgressBar.setVisible(false);
        });
        searchThread.start();
    }
    
    //Sets up the custom Swing theme from referenced json using flatlaf library.
    private void initTheme(){       
        try {
            InputStream themeStream = GUI.class.getResourceAsStream("/themes/NightOwl.json");
            IntelliJTheme.setup(themeStream);          
            UIManager.put("TitlePane.unifiedBackground", true);
            UIManager.put("MenuItem.selectionType", "underline");
            FlatLaf.updateUI(); 
        } catch (Exception ex) {
            System.out.println( "Issue loading selected theme file." );
        }    
    }
    
    //Sets the runtime icon.
    private void setIcon() {
        try{
            ImageIcon guiLogo = new ImageIcon("src\\images\\logo.png");
            setIconImage(guiLogo.getImage());
        } catch (Exception ex){
            System.err.println( "Runtime icon image not found." );
        }
    }
    
    //Sets initial size and visibility of swing objects.
    private void setSizeAndVisibility() {       
        this.setTitle ("QuickFile"); //Title of JFrame
        this.setSize (615,350); //Size of Jframe
        this.setResizable(false); //Non-resizable.
        this.getContentPane().setBackground(bgColor); //Bg color.
        this.setLocationRelativeTo(null); //Window position.
        searchProgressBar.setVisible(false); //Set search thread progress indicator visibility.
        uploadProgressBar.setVisible(false);
        searchProgressBar.setIndeterminate(true);
        uploadProgressBar.setIndeterminate(true);
        this.setVisible(true); //Make frame visible.
    }
    
    //Method call to GUI constructor for user input and data display.
    public static void launchGUI(){
        java.awt.EventQueue.invokeLater(() -> {
            new GUI();    
        });
    }
    
    //Reference local address
    private static boolean checkIsLocal(Client thisClient){
        boolean ret = false;
        try{
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while(e.hasMoreElements())
            {
                NetworkInterface n = e.nextElement();
                Enumeration<InetAddress> ee = n.getInetAddresses();
                while (ee.hasMoreElements())
                {
                    InetAddress i = ee.nextElement();    
                    //System.out.println("This Client: "+i.getHostAddress());
                    //System.out.println("Connected Server: "+thisClient.getIP().getHostAddress());
                    if (i.getHostAddress() == null ? thisClient.getIP().getHostAddress() == null : i.getHostAddress().equals(thisClient.getIP().getHostAddress())){
                        ret = true;
                    }
                }
            }
        }catch(Exception e){
            
        }
        return ret;
    }
    
    //Listens for file drag and drops.
    private void setDropArea() {
        dropZone.setDropTarget(new DropTarget() {
            @Override
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>)
                        evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : droppedFiles) { 
                        //Handle dropped file.
                        System.out.println("File dropped...");                       
                        for (String HOST : hostIPs ){                           
                            Client thisClient = new Client();
                            thisClient.FormConnections(HOST);
                            if(usingStrategy != null && !checkIsLocal(thisClient)){
                                thisClient.setStrategy(usingStrategy);                          
                            }
                            if(usingLocation != null && checkIsLocal(thisClient)){                    
                                thisClient.setDownloadLocation(usingLocation);                           
                            }
                            if(!checkIsLocal(thisClient)){
                                try {                               
                                    System.out.println(file.getCanonicalFile()+" -> "+"@"+HOST);
                                } catch (IOException ex) {
                                    
                                }
                                uploadProgressBar.setVisible(true);
                                new Thread(() -> {
                                try {
                                    thisClient.SendFile(file.getCanonicalPath());
                                } catch (IOException ex) {
                                    
                                }
                                uploadProgressBar.setVisible(false);
                                }).start();
                            }
                        }                       
                        System.out.println("System ready...");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });      
    }
    
    public GUI getGuiInstance(){
      return this;
    }
    
    // Variables declaration
    javax.swing.JMenuBar ClientMenuBar;
    javax.swing.JLabel bgLabel;
    javax.swing.JPanel dragNDropPanel;
    javax.swing.JTextArea dropZone;
    javax.swing.ButtonGroup groupHandling;
    javax.swing.ButtonGroup groupView;
    javax.swing.JMenuItem menuDownloadLocation;
    javax.swing.JMenuItem menuExit;
    javax.swing.JMenu menuFile;
    javax.swing.JPopupMenu.Separator menuFileSeperator1;
    javax.swing.JPopupMenu.Separator menuFileSeperator2;
    javax.swing.JMenu menuHandling;
    javax.swing.JMenuItem menuInfo;
    javax.swing.JMenuItem menuRefreshConnections;
    javax.swing.JMenu menuView;
    javax.swing.JLabel numConnections;
    javax.swing.JProgressBar searchProgressBar;
    javax.swing.JRadioButtonMenuItem strategyAll;
    javax.swing.JRadioButtonMenuItem strategyFilter;
    javax.swing.JRadioButtonMenuItem strategyUnZip;
    javax.swing.JProgressBar uploadProgressBar;
    javax.swing.JRadioButtonMenuItem viewDeep;
    javax.swing.JRadioButtonMenuItem viewNightOwl;
    javax.swing.JRadioButtonMenuItem viewOceanic;
    javax.swing.JRadioButtonMenuItem viewSolarizedLite;
}