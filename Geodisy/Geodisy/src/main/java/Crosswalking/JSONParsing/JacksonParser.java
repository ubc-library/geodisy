/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crosswalking.JSONParsing;


import Crosswalking.DataverseJSON;
import com.fasterxml.jackson.databind.ObjectMapper;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


/**
 * This class is probably not getting used at all due to the Dataverse JSON structure not easily
 * working with standard JSON parsers.
 * @author pdante
 */
public class JacksonParser implements JSONParser {
    
     public JacksonParser (){
    }
     public DataverseJSON[] parseDVJSON(String filePath){
         DataverseJSON[] dvMetaObjects = new DataverseJSON[0];
         try{
             byte[] metadata = Files.readAllBytes(new File(filePath).toPath());
             int length = getNumRecords(metadata);
             dvMetaObjects = new DataverseJSON[length];
             //TODO at loop for when there are more files than will be returned in one go
             ObjectMapper objectMapper = new ObjectMapper();
             dvMetaObjects = objectMapper.readValue(metadata, DataverseJSON[].class);
             
         }catch(IOException e)
         {}
           return dvMetaObjects;  
     }

    //parse the metadata JSON to know how many records there are
    private int getNumRecords(byte[] metadata) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
