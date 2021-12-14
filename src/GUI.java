//CSC 2910 OOP | Caleb Collar | FTP System | QuickFile FTP Client GUI
//Imports
import com.formdev.flatlaf.FlatLaf;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import com.formdev.flatlaf.IntelliJTheme; //Swing theme.
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.util.List;

/**
 * This is the GUI class for the QuickFile simple FTP system.
 * @author Caleb
 * @version 1.0
 */
public class GUI extends javax.swing.JFrame {
    private static String HOST = null;
    private final String defaultTheme = "\\themes\\NightOwl.json"; 
    private final Color bgColor = Color.decode("#010E1A");
    private String usingStrategy = null; //Default strategy.
    
    public GUI(String hostIP) {
        HOST = hostIP;
        initTheme(); //Changes swing's theme. (Specified in local json)
        setIcon(); //Sets runtime icon to custom.
        initComponents(); //Creates GUI components.
        setSizeAndVisibility(); //Sets up initial GUI visibilty.
        setDropArea(); //Adds drag and drop.
        System.out.println("System ready...");
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

    private void menuRefreshActionPerformed(java.awt.event.ActionEvent evt) {  
        FlatLaf.updateUI();
    }

    private void menuInfoActionPerformed(java.awt.event.ActionEvent evt) {       
        String versionText = "QuickFile Client GUI v1.1";
        String infoText = "-Drag and drop files to copy to server's remote or local location.\n"
                +"-Use 'handling' menu to alter the way copied files are handled after transfer.\n"
                +"-Use 'view' menu to switch the theme of the application.\n"
                +"\nCreated by Caleb A. Collar for OOP.\n";       
        JOptionPane.showMessageDialog(this, infoText, versionText, JOptionPane.INFORMATION_MESSAGE); 
    }

    private void viewNightOwlActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            //UIManager.setLookAndFeel(new FlatDarkLaf());
            IntelliJTheme.setup(GUI.class.getResourceAsStream("\\themes\\NightOwl.json"));           
            this.getContentPane().setBackground(Color.decode("#010E1A"));
            FlatLaf.updateUI();
            
            
        } catch (Exception ex) {
            System.out.println( "Issue loading selected theme file." );
        }
    }

    private void viewOceanicActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            IntelliJTheme.setup(GUI.class.getResourceAsStream("\\themes\\Oceanic.json"));           
            this.getContentPane().setBackground(Color.decode("#1E272C"));
            FlatLaf.updateUI();
            
            
        } catch (Exception ex) {
            System.out.println( "Issue loading selected theme file." );
        }
    }

    private void viewDeepActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            IntelliJTheme.setup(GUI.class.getResourceAsStream("\\themes\\Deep.json"));           
            this.getContentPane().setBackground(Color.decode("#090B10"));
            FlatLaf.updateUI();
            
            
        } catch (Exception ex) {
            System.out.println( "Issue loading selected theme file." );
        }
    }

    private void viewSolarizedLiteActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            IntelliJTheme.setup(GUI.class.getResourceAsStream("\\themes\\SolarizedLight.json"));           
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
  
    //Sets up the custom Swing theme from referenced json using flatlaf library.
    private void initTheme(){       
        try {
            IntelliJTheme.setup(GUI.class.getResourceAsStream(defaultTheme));           
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
        this.setVisible(true); //Make frame visible.
    }
    
    //Method call to GUI constructor for user input and data display.
    public static void launchGUI(String hostIP){
        java.awt.EventQueue.invokeLater(() -> {
            new GUI(hostIP);
        });
    }
    
    //Listens for file drag and drops.
    private void setDropArea() {
        dropZone.setDropTarget(new DropTarget() {
            @Override
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>)evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    evt.dropComplete(true);
                    for (File file : droppedFiles) { 
                        //Handle dropped file.
                        System.out.println("File dropped. Initiate transfer...");
                        System.out.println(file.getCanonicalFile()+" -> "+"@"+HOST);
                        Client thisClient = new Client();
                        thisClient.FormConnections(HOST);
                        if(usingStrategy != null){
                            thisClient.setStrategy(usingStrategy);
                        }
                        thisClient.SendFile(file.getCanonicalPath());
                        thisClient.TerminateConnections();
                        System.out.println("System ready...");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    
    //Swing variables for components.
    javax.swing.JMenuBar ClientMenuBar;
    javax.swing.JLabel bgLabel;
    javax.swing.JPanel dragNDropPanel;
    javax.swing.JTextArea dropZone;
    javax.swing.ButtonGroup groupHandling;
    javax.swing.ButtonGroup groupView;
    javax.swing.JMenuItem menuExit;
    javax.swing.JMenuItem menuRefresh;
    javax.swing.JMenu menuFile;
    javax.swing.JPopupMenu.Separator menuFileSeperator1;
    javax.swing.JMenu menuHandling;
    javax.swing.JMenuItem menuInfo;
    javax.swing.JMenu menuView;
    javax.swing.JRadioButtonMenuItem strategyAll;
    javax.swing.JRadioButtonMenuItem strategyFilter;
    javax.swing.JRadioButtonMenuItem strategyUnZip;
    javax.swing.JRadioButtonMenuItem viewDeep;
    javax.swing.JRadioButtonMenuItem viewNightOwl;
    javax.swing.JRadioButtonMenuItem viewOceanic;
    javax.swing.JRadioButtonMenuItem viewSolarizedLite;

    //Initiates swing components for GUI.
    private void initComponents() {

        groupHandling = new javax.swing.ButtonGroup();
        groupView = new javax.swing.ButtonGroup();
        dragNDropPanel = new javax.swing.JPanel();
        dropZone = new javax.swing.JTextArea();
        bgLabel = new javax.swing.JLabel();
        ClientMenuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuInfo = new javax.swing.JMenuItem();
        menuFileSeperator1 = new javax.swing.JPopupMenu.Separator();
        menuExit = new javax.swing.JMenuItem();
        menuRefresh = new javax.swing.JMenuItem();
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

        getContentPane().add(dragNDropPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 20, 560, 270));

        menuFile.setText("File");

        menuInfo.setText("Info");
        menuInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuInfoActionPerformed(evt);
            }
        });
        menuFile.add(menuInfo);

        menuRefresh.setText("Refresh");
        menuRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRefreshActionPerformed(evt);
            }
        });
        //menuFile.add(menuRefresh);
        menuFile.add(menuFileSeperator1);

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
}