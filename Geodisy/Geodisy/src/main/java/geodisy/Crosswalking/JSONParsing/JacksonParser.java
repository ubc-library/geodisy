/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geodisy.Crosswalking.JSONParsing;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 *
 * @author pdante
 */
public class JacksonParser implements JSONParser{
    //TODO create Jackson JSON parser for reading and creating JSON objects
    
    private void JSONToMap (String filePath){
        try{
        byte[] mapData = Files.readAllBytes(Paths.get(filePath));
        Map<String,String> myMap = new HashMap<String, String>();

        //ObjectMapper objectMapper = new ObjectMapper();
        //myMap = objectMapper.readValue(mapData, HashMap.class);
        System.out.println("Map is: "+myMap);
        }catch(IOException e)
        {}
        
        
    }
}
