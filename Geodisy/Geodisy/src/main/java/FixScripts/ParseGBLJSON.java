package FixScripts;

import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

public class ParseGBLJSON {
    String json;

    public JSONObject readJSON(File file) throws IOException {
        return getJSONObject(new String(Files.readAllBytes(Paths.get(file.getPath()))));
    }

    JSONObject getJSONObject(String jsonString){
        JSONObject json = new JSONObject(jsonString);
        return json;
    }

    boolean isGeospatial(JSONObject json){
        String slug = json.getString("layer_slug_s");
        int slugLength = slug.length();
        boolean firstNumber = false;
        for(int i = slugLength-1; i>0; i--){
            if(firstNumber && slug.substring(i,i+1).equals("v")||slug.substring(i,i+1).equals("r"))
                return true;
            else if (firstNumber && !Pattern.matches("\\d",slug.substring(i,i+1)))
                return false;
            if(Pattern.matches("\\d",slug.substring(i,i+1)))
                firstNumber = true;
        }
        return false;
    }

    String getDOI(JSONObject json){
        String doi = json.getString("dc_identifier_s");
        int header = doi.indexOf("https://doi.org/");
        int start;
        if(header==-1)
            start = 23;
        else
            start = 16;
        doi = doi.substring(start);
        return doi;
    }
    String getDBID(JSONObject json){
        String resources = json.getString("dct_references_s");
        JSONObject jsonResource = new JSONObject(resources);
        String keys = "";
        for(String s: jsonResource.keySet()){
            keys += " ," + s;
        }
        String dbID = jsonResource.getString("http://schema.org/downloadUrl");
        return dbID.substring(58);
    }
    String getGeoserverLabel(JSONObject json){
        String slug = json.getString("layer_slug_s");
        slug = slug.substring(10);
        return slug;
    }

    String[] getBoundingBoxNumberAndType(JSONObject json){
        String[] answer = new String[2];
        String slug = json.getString("layer_slug_s");
        int length = slug.length();
        String number = "";
        for(int i = length-1; i>=0; i--){
            String letter = String.valueOf(slug.charAt(i));
            try {
                Integer.parseInt(letter);
            }catch (NumberFormatException e){
                if(number.length()>0) {
                    answer[0] = letter;
                    answer[1] = number;
                    break;
                }
            }
            number = letter + number;
        }
        return answer;
    }

}
