package Crosswalking.XML;

import BaseFiles.GeoLogger;

public class GeoCombine {
    ProcessBuilder processBuilder = new ProcessBuilder();
    GeoLogger logger = new GeoLogger(this.getClass());

    public void call() {
        processBuilder.command("bash","bundle exec rake geocombine:pull[ca.ubc]");
    }

    public void generateGeoBlacklightXML(String fileName){
        ProcessBuilder processBuilder = new ProcessBuilder();
        JGit jgit = new JGit();
        String filePath = jgit.getFilePath(fileName);
        processBuilder.command("bash","GeoCombine::Iso19139.new(" + filePath + fileName + ".xml).to_geoblacklight.to_json");
    }

}
