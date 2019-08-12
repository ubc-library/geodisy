package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONJournalFieldClasses;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJSONFieldClasses.MetadataType;
import Dataverse.DataverseJSONFieldClasses.MetadataWSimple;
import Dataverse.DataverseJavaObject;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static Dataverse.DVFieldNameStrings.*;

public class JournalFields extends MetadataWSimple {
    private String journalArticleType;
    private List<JournalVolIssue> journalVolIssues;


    private GeoLogger logger = new GeoLogger(JournalFields.class);

    public JournalFields(DataverseJavaObject djo) {
        this.djo = djo;
        this.doi = djo.getDOI();
        this.journalArticleType = "";
        this.journalVolIssues = new LinkedList<>();
    }

    //placeholder JournalField
    public JournalFields() {
        this.journalArticleType = "";
        this.journalVolIssues = new LinkedList<>();
    }

    @Override
    public void setFields(JSONObject jo) {
        String fieldName = jo.getString(TYPE_NAME);
        Object o = jo.get(VAL);
        if(fieldName.equals(JOURNAL_ARTICLE_TYPE)){
            journalArticleType = (String) o;
        } else if(fieldName.equals(JOURNAL_VOLUME_ISSUE)){
            for(Object obj: (JSONArray) o) {
                JSONObject jObj = (JSONObject) obj;
                Object subObject = jObj.get(VAL);
                JournalVolIssue jVI = new JournalVolIssue();
                jVI.setFields((JSONArray) subObject);
                journalVolIssues.add(jVI);
            }
        }else
            logger.error("Tried to set field \"" + fieldName + "\"");

    }

    @Override
    public List getListField(String fieldName) {
        if(fieldName.equals(JOURNAL_VOLUME_ISSUE))
            return journalVolIssues;
        logger.error("Tried to get invalid list field \"" + fieldName + "\"");
        return new LinkedList();
    }

    @Override
    public String getDoi() {
        return doi;
    }

    @Override
    public void setDoi(String doi) {
        this.doi = doi;
    }


    public String getField(String fieldName){
        if(fieldName.equals(JOURNAL_ARTICLE_TYPE))
            return journalArticleType;
        logger.error("Tried to get a field, " + fieldName +", that either isn't a String or isn't a real field");
        return "Error getting value from Journal Fields";
    }
}
