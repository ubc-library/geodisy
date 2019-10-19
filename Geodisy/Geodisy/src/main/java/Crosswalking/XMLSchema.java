package Crosswalking;

import Crosswalking.XML.XMLTools.XMLDocObject;
import Dataverse.SourceJavaObject;

import java.util.List;

public interface XMLSchema extends MetadataSchema{
    List<XMLDocObject> generateXML(List<SourceJavaObject> dJOs);
}
