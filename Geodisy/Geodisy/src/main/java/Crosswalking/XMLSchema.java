package Crosswalking;

import Crosswalking.XML.XMLTools.XMLDocObject;
import Dataverse.SourceJavaObject;

import java.util.List;

public interface XMLSchema extends MetadataSchema{
    XMLDocObject generateXML(SourceJavaObject sJO);
}
