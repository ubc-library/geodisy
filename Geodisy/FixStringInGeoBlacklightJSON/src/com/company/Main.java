package com.company;

import java.io.File;

public class Main {
	//Convert geoblacklight.json, ISO19115.xml, and ISO19115.zip to ISO19139
	//Add jar to folder with all the metadata and run. It will recurse through all the folders
    public static void main(String[] args) {
    	String start = "";
    	String end = "";
    	for(int i = 0; i< args.length;i++){
    		if (i==0)
    			start = args[i];
    		if(i==1)
    			end = args[i];
		}
    	System.out.println("Got here");
    	System.out.println(start + " " + end);
		File folder = new File(System.getProperty("user.dir"));
		System.out.println(folder.getName());
		/*ChangeISO changeISO = new ChangeISO();
		changeISO.alterXMLFiles(folder);
		changeISO.alterJSON(folder);*/
        if(!end.equals("")) {
			ChangeDownload changeDownload = new ChangeDownload();
			changeDownload.alterJSON(folder, start, end);
		}
    }
}
