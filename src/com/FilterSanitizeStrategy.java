package com;

//CSC 2910 OOP | Caleb Collar | FTP System | QuickFile filtering strategy.
//Imports
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
/**
 * This is the strategy meant to filter certain files.
 * The filter/sanitize method is really only good for archives since directories
 * can't be sent as serialized data without being sent as a file.
 * @author Caleb
 * @version 1.0
 */
public class FilterSanitizeStrategy implements FileHandlingStrategy {

    @Override
    public void HandleFiles(String path) {
        System.out.println("HANDLING STRATEGY: Filter files from ignore list.");
        try {
            FileHandlingStrategy unCompressFirst = new UncompressAllStrategy();
            unCompressFirst.HandleFiles(path);
            if (path.contains(".zip")){
                path = path.replaceAll(".zip", "");
            }
            System.out.println("DEBUG: new path "+path);
            filterSanitize(path);
        } catch (IOException ex) {  
            System.out.println("HANDLING STRATEGY: IO Error on file(s).");
        }
    }

    public void filterSanitize(String path) throws IOException {
        File thisFile = new File(path);
        File ignoreList = new File(".ignoreExtensions");
        System.out.println("Ignore file type list located at "+ignoreList.getCanonicalPath());
        List<String> ignoreExtensions = new ArrayList<>();
        //Get extensions to sanitize.
        LineIterator it = FileUtils.lineIterator(ignoreList, "UTF-8");
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                if (line.charAt(0) == '.'){
                    ignoreExtensions.add(line);
                }
            }
        } finally {
            it.close();
        }
        //Check the extensions.
        for (String extension : ignoreExtensions){
            System.out.println("HANDLING STRATEGY: Ignoring "+extension);
            if (thisFile.isDirectory()){ //Recursive
                for (final File fileEntry : thisFile.listFiles()) {
                    //System.out.println("DEBUG: Contains "+fileEntry);
                    if (fileEntry.isDirectory()) {
                        filterSanitize(fileEntry.getAbsolutePath());
                    } else if (fileEntry.isFile() && fileEntry.getName().contains(extension)){
                        if (!fileEntry.delete()) {
                            throw new IOException("Not able to delete file: " + fileEntry.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }
}