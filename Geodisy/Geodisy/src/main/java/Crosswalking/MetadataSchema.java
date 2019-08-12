/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crosswalking;

import Dataverse.SourceJavaObject;

/**
 * Interface for the output metadata schema side of a crosswalk
 * @author pdante
 */
public interface MetadataSchema {
    //TODO create interface for metadata crosswalks
    public void generateXML(SourceJavaObject s);

    void crosswalk(SourceJavaObject sJO);
}
