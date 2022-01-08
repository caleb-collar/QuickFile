package com;

//CSC 2910 OOP | Caleb Collar | FTP System | QuickFile strategy interface.
//Imports
import java.io.IOException;
/**
 * This is the strategy interface for the QuickFile simple FTP system.
 * @author Caleb
 * @version 1.0
 */
public interface FileHandlingStrategy {
    public void HandleFiles(String path) throws IOException;
}
