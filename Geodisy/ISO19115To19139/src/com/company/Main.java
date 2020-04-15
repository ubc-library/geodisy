package com.company;

import java.io.File;

public class Main {

    public static void main(String[] args) {
    	System.out.println("Got here");
		File folder = new File(System.getProperty("user.dir"));
		System.out.println(folder.getName());
		ChangeISO changeISO = new ChangeISO();
		changeISO.alterXMLFiles(folder);
		changeISO.alterJSON(folder);
    }
}
