package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String doi = "10\\5072\\FK2\\KZRG9F";
        String DATASET_FILES_PATH = "/home/centos/temp/testMerge/";
        String mergeTiles = "python /usr/gdal30/bin/gdal_merge.py -init 255 -o " + doi.replace("\\","_") + ".tif -of GTiff -v " + DATASET_FILES_PATH + "\\*.png";
        ProcessBuilder processBuilder= new ProcessBuilder();
        processBuilder.command("bash", "-c",mergeTiles);

        Process p = null;
        try {
            p = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        p.destroy();

    }
}
