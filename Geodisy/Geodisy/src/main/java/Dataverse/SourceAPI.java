/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dataverse;

import BaseFiles.API;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author pdante
 */
public abstract class SourceAPI implements API {
    abstract protected HashSet<String> searchDV();
    abstract protected LinkedList<JSONObject> downloadMetadata(HashSet<String> dIOs);
    abstract protected void downloadDatasets(String dOI);
    abstract public LinkedList<SourceJavaObject> harvest(ExistingSearches es);
}
