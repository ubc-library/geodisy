package com.company;

import java.io.File;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) {
	// write your code here
        ClassLoader loader = Main.class.getClassLoader();
        File fileStart = null;
        try {
            fileStart = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        File file = new File(fileStart.getParent());
        System.out.println("Program called at " + file.getAbsolutePath());
        FixShpFolder fsf = new FixShpFolder();
        fsf.fix(file);
    }

}
