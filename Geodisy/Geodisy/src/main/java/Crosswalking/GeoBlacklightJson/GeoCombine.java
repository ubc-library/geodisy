package Crosswalking.GeoBlacklightJson;

import BaseFiles.HTTPCaller;
import Crosswalking.XML.XMLTools.JGit;

public class GeoCombine {
    HTTPCaller caller;

    public GeoCombine() {
        caller = new HTTPCombineCaller();
    }

    public void index(){
        caller.callHTTP("SOLR_URL=http://geo.frdr.ca:8983/solr/collection bundle exec rake geocombine:index");
    }

    public void updateOpenGeoMetadata(String fileName, JGit jgit){
        String filePath = jgit.getOpenGeoLocalFilePath(fileName);
        //TODO uncomment once I've got JGit working
        //jgit.addXMLFileToIndex(filePath + fileName + ".json");
    }
}
