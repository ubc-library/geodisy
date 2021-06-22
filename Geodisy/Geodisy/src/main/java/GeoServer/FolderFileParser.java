package GeoServer;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordFile;
import _Strings.GeodisyStrings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Stack;

public class FolderFileParser {
    GeoLogger logger = new GeoLogger(this.getClass());

    public LinkedList<DataverseRecordFile> openFolders(File folder, String dirPath, DataverseJavaObject djo, DataverseRecordFile drf){
        LinkedList<DataverseRecordFile> recs = new LinkedList<>();
        File[] files = folder.listFiles();
        for(File f: files){
            if(f.isDirectory()) {
                recs = openFolders(f, dirPath, djo, drf);
            }
            else if(f.getName().toLowerCase().endsWith(".zip")) {
                recs.addAll(unzip(f, dirPath, drf));
            }
            else if(f.getName().toLowerCase().endsWith(".tab"))
                recs.add(convertTab(f, dirPath,f.getName(),drf));
            else if(GeodisyStrings.fileToAllow(f.getName())) {
                DataverseRecordFile tempFile = new DataverseRecordFile(drf);
                tempFile.setTranslatedTitle(f.getName());
                recs.add(tempFile);
            }
        }
        return recs;
    }

    public DataverseRecordFile convertTab(File f, String dirPath, String name, DataverseRecordFile drf){
        String newName = convertFromTabToCSV(f, dirPath, name);
        DataverseRecordFile d = new DataverseRecordFile(drf);
        d.setTranslatedTitle(newName);
        return d;
    }

    public String convertFromTabToCSV(File inputFile, String dirPath, String title) {
        String fileName = title.endsWith(".tab")? title.replace(".tab",".csv"):title+".csv";
        File outputFile = new File(dirPath + fileName);
        BufferedReader br = null;
        FileWriter writer = null;
        try {
            String line;
            br = new BufferedReader(new FileReader(inputFile));
            writer = (new FileWriter(outputFile));
            while ((line = br.readLine()) != null) {
                writer.write(line.replace("\t", ","));
                writer.write("\n");
            }
        } catch (FileNotFoundException e) {
            logger.error("Tried to convert an non-existant .tab file: " + title);
        } catch (IOException e) {
            logger.error("Something went wrong when converting a .tab file to .csv: " + title);
        }catch(OutOfMemoryError e){
            logger.warn("Tab was too big to convert to csv, maybe check? Path = " +dirPath + " And title = " + title);
        }
        finally {
            try{
                br.close();
                writer.close();
            }
            catch(IOException|NullPointerException d){
                logger.error("Something went wrong when converting a .tab file to .csv when closing br: " + title);
            }
        }
        try {
            Files.deleteIfExists(Paths.get(inputFile.getAbsolutePath()));
        } catch (IOException e) {
            logger.error("Something went wrong trying to delete " + inputFile.getAbsolutePath());
        }
        return fileName;
    }

    public LinkedList<DataverseRecordFile> unzip(File f, String dirPath, DataverseRecordFile drf){
        LinkedList<DataverseRecordFile> drfs = new LinkedList<>();
        String filePath = GeodisyStrings.replaceSlashes(f.getAbsolutePath());
        Unzip zip = new Unzip();
        try {
            Long start = Calendar.getInstance().getTimeInMillis();
            System.out.println("Unzipping file " + f.getName());
            drfs = zip.unzip(filePath, dirPath, drf);
            System.out.println(Calendar.getInstance().getTimeInMillis()-start + " milliseconds to unzip " + f.getName());
        }catch (NullPointerException e){
            logger.error("Got an null pointer exception, something clearly went wrong with unzipping " + filePath);
        }
        return drfs;
    }
}
