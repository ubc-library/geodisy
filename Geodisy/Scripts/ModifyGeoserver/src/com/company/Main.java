package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	// write your code here
        String GEOSERVER_USERNAME = "admin";
        String GEOSERVER_PASSWORD = "geoserver";
        String geoserverlabel = "geodisy:10_5072_fk2_zwav7zv2";
        String filename = "over180PDC_1TUKUB";
        String modifyName = "curl -v -u "+ GEOSERVER_USERNAME + ":" + GEOSERVER_PASSWORD + " -XPOST -H \"Content-type: text/xml\" -d \"<GeoServerLayer><enabled>true</enabled><name>" + geoserverlabel.toLowerCase() + "</name><title>" + filename + "</title></GeoServerLayer>\"  \"http://localhost:8080/geoserver/gwc/rest/layers/" + geoserverlabel.toLowerCase() +".xml\"";

        ProcessBuilder processBuilder= new ProcessBuilder();
        processBuilder.command("bash", "-c",modifyName);

        Process p = null;
        try {
            p = processBuilder.start();
            p.waitFor();
            p.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
