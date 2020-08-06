package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String ADDRESS = "206-12-92-97.cloud.computecanada.ca";
        String MOVE_METADATA = "rsync -av /home/centos/Geodisy/metadata/* /var/www/" + ADDRESS + "/html/geodisy/";
        String OGM_PATH = "OGM_PATH=/var/www/206-12-92-97.cloud.computecanada.ca/html/geodisy/";
        String GEOCOMBINE = OGM_PATH + "/home/centos/geodisy/bin/bundle exec rake geocombine:index";
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("/bin/bash", "-c", MOVE_METADATA);

        Process p = null;
        try {
            System.out.println("Moving metadata");
            p = processBuilder.start();
            p.waitFor();
            p.destroy();
            /*processBuilder.command(new String[] {"/bin/bash", "-c", DELETE_DUPLICATE_META_FOLDER});
            p = processBuilder.start();
            p.waitFor();
            p.destroy();*/
            System.out.println("Calling Geocombine");
            processBuilder.command("/bin/bash", "-c", GEOCOMBINE);
            p.waitFor();
            p.destroy();
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        }
    }
}
