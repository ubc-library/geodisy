package com.company;

import java.io.IOException;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) {
        String geoserverLabel = "10_5072_fk2_vwschav1";
        String deleteFirst = "curl -v -u admin:geoserver -X DELETE \"http://localhost:8080/geoserver/rest/workspaces/geodisy/doi" + geoserverLabel.toLowerCase() + "\"?recurse=true -H  \"accept: application/json\" -H  \"content-type: application/json\"";
        String call = "curl -v -u admin:geoserver -XPOST -H \"Content-type: text/xml\" -d \"<featureType><name>doi" + geoserverLabel.toLowerCase() + "</name><nativeCRS>EPSG:4326</nativeCRS><srs>EPSG:4326</srs><enabled>true</enabled></featureType>\" http://localhost:8080/geoserver/rest/workspaces/geodisy/datastores/" + "testvectordata" + "/featuretypes";
        ProcessBuilder processBuilder= new ProcessBuilder("bash", "-c", deleteFirst);
        Process process;
        InputStream inputStream;
        System.out.println("Delete: " + deleteFirst);
        System.out.println("Create: " + call);
        try {
            process = processBuilder.start();
            process.waitFor();
            System.out.println(process.exitValue());
            process.destroy();
        }catch (IOException | InterruptedException e) {
            System.out.println(e);
        }
        try{
            processBuilder = new ProcessBuilder("bash", "-c", call);
            process = processBuilder.start();
            process.waitFor();
            System.out.println(process.exitValue());
            process.destroy();
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        }
    }
}
