package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String fileName = "datasetFiles/10.5072/FK2/WQLIQD10_5072_FK2_WQLIQDr1.tif";
        String geoserverName = "10.5072/FK2/WQLIQD/10_5072_FK2_WQLIQDr1";
        String call = "curl -u admin:geoserver" + " -XPOST -H \"Content-type:image/tiff\" --data-binary @/home/centos/Geodisy/" + fileName.replace("/.","/") + "  http://localhost:8080/geoserver/rest/workspaces/geodisy/" + geoserverName + "/file.geotiff";
        ProcessBuilder processBuilder= new ProcessBuilder();
        processBuilder.command("bash", "-c", call);
        try {
            processBuilder.start();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}