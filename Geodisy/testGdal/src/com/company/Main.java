package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	String call = "/usr/gdal30/bin/ogr2ogr -f 'ESRI Shapefile'  /home/centos/Geodisy/datasetFiles/10/5072/FK2/GFCTVC/NetCDF_GFCTVC.shp /home/centos/Geodisy/datasetFiles/10/5072/FK2/GFCTVC/NetCDF_GFCTVC.geoJSON";
        ProcessBuilder processBuilder= new ProcessBuilder();
        processBuilder.command("bash", "-c", call);
        try {
            processBuilder.start();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
