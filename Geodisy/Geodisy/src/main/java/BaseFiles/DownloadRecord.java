package BaseFiles;

import Crosswalking.JSONParsing.DataverseParser;
import Dataverse.DataverseAPI;
import Dataverse.DataverseJavaObject;
import Dataverse.GDALTranslate;
import _Strings.GeodisyStrings;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;

import static _Strings.GeodisyStrings.*;

public class DownloadRecord {
    String doi;
    String dvURL;

    private JSONObject getJSON(String doi){
        HTTPCallerGeoNames getMetadata;
        //TODO set base URL programmatically
        String baseURL = dvURL + "api/datasets/export?exporter=dataverse_json&persistentId=";
        getMetadata = new HTTPCallerGeoNames();
        String dataverseJSON = getMetadata.callHTTP(baseURL+doi);
        JSONObject jo = new JSONObject(dataverseJSON);
        return jo;
    }

    private DataverseJavaObject parseDJO(JSONObject jo){
        DataverseParser parser = new DataverseParser();
        DataverseJavaObject djo = parser.parse(jo, dvURL,true);
        return djo;
    }

    private int translateFiles(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        int count = files.length;
        GDALTranslate trans = new GDALTranslate();
        for(File f: files){
            String name = f.getName();
            if(GeodisyStrings.gdalinfoRasterExtention(name))
                trans.rasterTransformTest(path,name);
            else if(GeodisyStrings.ogrinfoVectorExtension(name))
                trans.vectorTransformTest(path,name);
            else
                count--;
        }
        return count;
    }
}

