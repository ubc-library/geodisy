package BaseFiles;

import Crosswalking.JSONParsing.DataverseParser;
import Dataverse.DataverseAPI;
import Dataverse.DataverseJavaObject;
import Dataverse.GDALTranslate;
import Dataverse.SourceJavaObject;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static BaseFiles.GeodisyStrings.*;

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

    public void run(){
        dvURL = SANDBOX_DV_URL;
        doi = "doi:10.5072/FK2/SAUHWD";
        JSONObject jo = getJSON(doi);
        DataverseJavaObject djo = parseDJO(jo);
        djo.downloadFiles();
        DataverseAPI api = new DataverseAPI(dvURL);
        djo = api.generateBoundingBox(djo);
        String localDoi = djo.getDOI();
        DataverseAPI d = new DataverseAPI(djo.getServer());
        d.crosswalkRecord(djo);
        String stop = "Place to pause program";
    }

    public void run(String doi){
        //download
        long startTime = Calendar.getInstance().getTimeInMillis();
        String doiPathed = replaceSlashes(doi.substring(4).replace(".","/"));
        dvURL = SANDBOX_DV_URL;
        this.doi = doi;
        JSONObject jo = getJSON(doi);
        DataverseJavaObject djo = parseDJO(jo);
        djo.downloadFiles();

        //transform to tiff
        Calendar beginningEnd =  Calendar.getInstance();
        Long total = beginningEnd.getTimeInMillis()-startTime;
        System.out.println("Finished a download run at: " + beginningEnd.getTime() + " after " + total + " milliseconds");
        Long middle = beginningEnd.getTimeInMillis();
        String path = DATASET_FILES_PATH + doiPathed + "/";
        int count = translateFiles(path);
        Calendar endEnd = Calendar.getInstance();
        Long end = endEnd.getTimeInMillis();
        total = end-middle;
        System.out.println("Finished Processing Transformations of " + count + " png files in " + total + " milliseconds");
        total = end-startTime;
        System.out.println("Finished total procedure in " + total + " milliseconds");

    }

    private int translateFiles(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        int count = files.length;
        GDALTranslate trans = new GDALTranslate();
        for(File f: files){
            String name = f.getName();
            if(GeodisyStrings.gdalinfoRasterExtention(name))
                trans.rasterTransformTest(path,name,true , "1");
            else if(GeodisyStrings.ogrinfoVectorExtension(name))
                trans.vectorTransformTest(path,name,true, "1");
            else
                count--;
        }
        return count;
    }
}

