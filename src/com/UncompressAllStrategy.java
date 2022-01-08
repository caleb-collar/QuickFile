package com;

//CSC 2910 OOP | Caleb Collar | FTP System | QuickFile extraction strategy.
//Imports
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.IOUtils;
/**
 * This is the extraction/unpack strategy for the QuickFile simple FTP system.
 * @author Caleb
 * @version 1.0
 */
public class UncompressAllStrategy implements FileHandlingStrategy {
    @Override
    public void HandleFiles(String path) throws IOException {
        System.out.println("HANDLING STRATEGY: Extract any compressed files. Recursive.");
        try {
            RecursiveExtraction(path);
        } catch (Exception ex) {
            System.out.println("HANDLING STRATEGY: IO Error on archived file(s) or no archive found.");
        }
    }
    
    public void RecursiveExtraction(String zipfile) throws IOException {
        int buffer = 2048;
        File file = new File(zipfile);

        try (ZipFile zip = new ZipFile(file)) {
            String newPath = zipfile.substring(0, zipfile.length() - 4);
            new File(newPath).mkdir();
            Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();
            //Process each entry.
            while (zipFileEntries.hasMoreElements()) {
                ZipEntry entry = zipFileEntries.nextElement();
                String currentEntry = entry.getName();
                
                File destFile = new File(newPath, currentEntry);
                File destinationParent = destFile.getParentFile();
                
                destinationParent.mkdirs(); //Create parent directory if it is needed
                if (!entry.isDirectory()) { //Write contents.
                    BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
                    FileOutputStream fos = new FileOutputStream(destFile);
                    BufferedOutputStream dest = new BufferedOutputStream(fos, buffer);
                    IOUtils.copy(is, dest);
                    dest.flush();
                    dest.close();
                    fos.flush();
                    fos.close();                  
                    is.close();               
                }
                if (currentEntry.endsWith(".zip")) { //Recursion
                    try {
                        RecursiveExtraction(destFile.getAbsolutePath());
                    } catch (Exception ex) {
                        System.out.println("HANDLING STRATEGY: IO Error on archived file(s).");
                    }
                    destFile.delete(); //Delete interior.zip as the extracted folder is a copy.
                }
            }           
        }
    }
}