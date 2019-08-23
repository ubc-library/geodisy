package Crosswalking.XML.XMLTools;

import BaseFiles.GeoLogger;
import Crosswalking.XML.XMLTools.JGit;

public class GeoCombine {
    ProcessBuilder processBuilder = new ProcessBuilder();
    GeoLogger logger = new GeoLogger(this.getClass());

    public void call() {
        processBuilder.command("bash","bundle exec rake geocombine:pull[ca.ubc]");
    }

    /**
     * Creates GeoBlacklight JSON and adds file to index for Github
     * @param fileName
     * @param jgit
     */
    public void generateGeoBlacklightXML(String fileName, JGit jgit){
        ProcessBuilder processBuilder = new ProcessBuilder();
        String filePath = jgit.getXMLLocalFilePath(fileName);
        processBuilder.command("bash","GeoCombine::Iso19139.new(" + filePath + fileName + ".xml).to_geoblacklight.to_json");
        jgit.addXMLFileToIndex(filePath + fileName + ".json");
    }

}
