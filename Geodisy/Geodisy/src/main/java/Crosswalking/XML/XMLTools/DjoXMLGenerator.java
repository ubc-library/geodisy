package Crosswalking.XML.XMLTools;

import Dataverse.DataverseJavaObject;
import org.w3c.dom.Element;

public abstract class DjoXMLGenerator {
    DataverseJavaObject djo;

    public abstract XMLDocObject generateXMLFile();
    protected abstract Element getRoot();
}
