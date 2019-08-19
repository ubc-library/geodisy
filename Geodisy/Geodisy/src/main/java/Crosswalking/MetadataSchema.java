/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crosswalking;

import Crosswalking.XML.XMLDocument;
import Dataverse.SourceJavaObject;


import java.util.List;

/**
 * Interface for the output metadata schema side of a crosswalk
 * @author pdante
 */
public interface MetadataSchema {
    //TODO create interface for metadata crosswalks
    public List<XMLDocument> generateXML(List<SourceJavaObject> dJOs);

    void crosswalk(SourceJavaObject sJO);
}
