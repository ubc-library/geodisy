package tests.DataverseTestCases;


import BaseFiles.Geodisy;
import Crosswalking.JSONParsing.DataverseParser;
import Dataverse.DataverseJavaObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public abstract class DataverseTests {
    protected String doi;
    protected Logger logger = LogManager.getLogger(DataverseTests.class);
    protected DataverseJavaObject dJO;

    //https://206-12-90-131.cloud.computecanada.ca/api/datasets/export?exporter=dataverse_json&persistentId=doi:10.5072/FK2/QZIPVK

    protected String getDataverseString(){
        String answer = "https://206-12-90-131.cloud.computecanada.ca/api/datasets/export?exporter=dataverse_json&persistentId=";
        answer += getURL(doi);
        return answer;
    }

    String getURL(String doi){
            doi.replaceAll(":","%3A");
            doi.replaceAll("/","%2F");
            return doi;

    }
    public DataverseJavaObject dataverseCallTest(){
        String dataverseCall = getDataverseString();
        String dataverse = call(dataverseCall);
        JSONObject jo = new JSONObject(dataverse);
        DataverseParser dataverseParser = new DataverseParser();
        dJO = dataverseParser.parse(jo);
        return dJO;
    }

    protected String call(String dataverseCall) {
        URL url;
        try{
            url = new URL(dataverseCall);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            return getContent(con);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    protected String getContent(HttpsURLConnection con){
        StringBuilder stringBuilder = new StringBuilder();
        if(con!=null){

            try {

                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(con.getInputStream()));

                String input;

                while ((input = br.readLine()) != null){
                    stringBuilder.append(input);
                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return stringBuilder.toString();

    }
}
