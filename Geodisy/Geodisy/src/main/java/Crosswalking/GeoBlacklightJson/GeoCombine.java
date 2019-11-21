package Crosswalking.GeoBlacklightJson;

import BaseFiles.HTTPCaller;
import Crosswalking.XML.XMLTools.JGit;

import static BaseFiles.GeodisyStrings.GEOCOMBINE;

public class GeoCombine {
    HTTPCaller caller;

    public GeoCombine() {
        caller = new HTTPCombineCaller();
    }

    public void index(){
        caller.callHTTP(GEOCOMBINE);
    }

    public void updateOpenGeoMetadata(String fileName, JGit jgit){
        String filePath = jgit.getOpenGeoLocalFilePath(fileName);
        //TODO uncomment once I've got JGit working
        //jgit.addXMLFileToIndex(filePath + fileName + ".json");
    }
}
