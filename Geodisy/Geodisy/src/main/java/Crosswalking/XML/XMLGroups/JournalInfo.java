package Crosswalking.XML.XMLGroups;

import Crosswalking.XML.XMLTools.SubElement;
import Crosswalking.XML.XMLTools.XMLDocObject;
import Crosswalking.XML.XMLTools.XMLStack;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONJournalFieldClasses.JournalFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONJournalFieldClasses.JournalVolIssue;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Element;

import java.util.List;

import static BaseFiles.GeodisyStrings.CHARACTER;
import static Dataverse.DVFieldNameStrings.*;

public class JournalInfo extends SubElement {
    JournalFields journalFields;
    List<JournalVolIssue> journalVolIssues;
    String doi;

    public JournalInfo(DataverseJavaObject djo, XMLDocObject doc, Element root) {
        super(djo, doc, root);
        //Uncomment if we decide to include Journal Fields metadata block
        // journalFields = djo.getJournalFields();
        journalVolIssues = journalFields.getListField(JOURNAL_VOLUME_ISSUE);
    }

    @Override
    public Element getFields() {

        stack.push(root);
        Element levelK = doc.createMDBElement("additionalDocumentation");
        stack.push(levelK);
        Element levelL = doc.createMDBElement("CI_Citation");
        XMLStack innerStack = new XMLStack();
        String journal;
        for(JournalVolIssue jvl: journalVolIssues){
            innerStack.push(levelL);
            innerStack.push(doc.createMDBElement("otherCitationDetails"));
            journal = "Volume: " + jvl.getField(JOURNAL_VOLUME);
            if(!jvl.getField(JOURNAL_ISSUE).isEmpty())
                journal += ", Issue: " + jvl.getField(JOURNAL_ISSUE);
            if(!jvl.getField(JOURNAL_PUB_DATE).isEmpty())
                journal += ", Publication Date: " + jvl.getField(JOURNAL_PUB_DATE);
            if(!jvl.getField(JOURNAL_ARTICLE_TYPE).isEmpty())
                journal += ", Article Type: " + jvl.getField(JOURNAL_ARTICLE_TYPE);
            levelL = innerStack.zip(doc.addGCOVal(journal,CHARACTER));
        }
        root = stack.zip(levelL);
        return root;
    }
}
