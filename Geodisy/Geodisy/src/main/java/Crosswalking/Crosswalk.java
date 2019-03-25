/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crosswalking;

import Dataverse.DataverseJavaObject;

import java.util.LinkedList;

/**
 *
 * @author pdante
 */
public interface Crosswalk {
    //TODO create interface for metadata crosswalks
    public void convertDJO(LinkedList<DataverseJavaObject> records);
}
