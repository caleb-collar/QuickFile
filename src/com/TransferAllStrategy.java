package com;

//CSC 2910 OOP | Caleb Collar | FTP System | QuickFile default strategy.
/**
 * This is the default strategy for the QuickFile simple FTP system.
 * @author Caleb
 * @version 1.0
 */
public class TransferAllStrategy implements FileHandlingStrategy{
    @Override
    public void HandleFiles(String path) {
        System.out.println("HANDLING STRATEGY: Transfer all files no additional handling.");
    }
}
