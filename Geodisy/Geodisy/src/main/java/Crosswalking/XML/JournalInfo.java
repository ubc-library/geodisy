package Crosswalking.XML;

import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONJournalFieldClasses.JournalFields;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Element;

public class JournalInfo extends SubElement  {
    JournalFields journalFields;
    String doi;

    public JournalInfo(DataverseJavaObject djo, XMLDocument doc, Element root) {
        super(djo, doc, root);
        journalFields = djo.getJournalFields();
    }

    @Override
    public Element getFields() {
        return null;
    }

}
