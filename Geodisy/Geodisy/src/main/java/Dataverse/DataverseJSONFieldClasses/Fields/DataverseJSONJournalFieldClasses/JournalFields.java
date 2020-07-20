package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONJournalFieldClasses;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJSONFieldClasses.MetadataSimple;
import Dataverse.DataverseJavaObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static _Strings.DVFieldNameStrings.*;

public class JournalFields extends MetadataSimple {
    private String journalArticleType;
    private List<JournalVolIssue> journalVolIssues;


    private GeoLogger logger = new GeoLogger(JournalFields.class);

    public JournalFields(DataverseJavaObject djo) {
        this.djo = djo;
        this.doi = djo.getPID();
        this.journalArticleType = "";
        this.journalVolIssues = new LinkedList<>();
    }

    //placeholder JournalField
    public JournalFields() {
        this.journalArticleType = "";
        this.journalVolIssues = new LinkedList<>();
    }

    @Override
    public JournalFields setFields(JSONObject jo) {
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
        return this;
    }

    @Override
    public List getListField(String fieldName) {
        if(fieldName.equals(JOURNAL_VOLUME_ISSUE))
            return journalVolIssues;
        logger.error("Tried to get invalid list field \"" + fieldName + "\"");
        return new LinkedList();
    }

    @Override
    public String getPID() {
        return doi;
    }

    @Override
    public void setPID(String doi) {
        this.doi = doi;
    }


    public String getField(String fieldName){
        if(fieldName.equals(JOURNAL_ARTICLE_TYPE))
            return journalArticleType;
        logger.error("Tried to get a field, " + fieldName +", that either isn't a String or isn't a real field");
        return "Error getting value from Journal Fields";
    }
}
