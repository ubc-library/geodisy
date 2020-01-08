package Crosswalking.GeoBlacklightJson;

import BaseFiles.GeoLogger;
import BaseFiles.HTTPCaller;
import Crosswalking.XML.XMLTools.JGit;

import java.io.IOException;

import static BaseFiles.GeodisyStrings.GEOCOMBINE;
import static BaseFiles.GeodisyStrings.MOVE_METADATA;

public class GeoCombine {
    GeoLogger logger;
    public GeoCombine() {
        logger = new GeoLogger(this.getClass());
    }

    public void index(){
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", MOVE_METADATA);

        Process p = null;
        try {
            System.out.println("Moving metadata");
            p = processBuilder.start();
            p.waitFor();
            p.destroy();
            System.out.println("Calling Geocombine");
            processBuilder.command("bash", "-c", GEOCOMBINE);
            p.waitFor();
            p.destroy();
        } catch (IOException | InterruptedException e) {
            logger.error("Something went wrong calling GeoCombine to index files");
        }
    }

    public void updateOpenGeoMetadata(String fileName, JGit jgit){
        String filePath = jgit.getOpenGeoLocalFilePath(fileName);
        //TODO uncomment once I've got JGit working
        //jgit.addXMLFileToIndex(filePath + fileName + ".json");
    }
}
