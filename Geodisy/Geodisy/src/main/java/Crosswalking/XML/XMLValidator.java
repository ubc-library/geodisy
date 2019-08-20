package Crosswalking.XML;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import java.io.File;
import BaseFiles.GeoLogger;
import org.xml.sax.SAXException;
import java.io.IOException;

import static Crosswalking.XML.XMLStrings.ISO_19115_VALIDATION_FILE_PATH;

public class XMLValidator {
    GeoLogger logger =  new GeoLogger(this.getClass());

    public boolean validateXML(File fileName) {
        File schemaFile = new File(ISO_19115_VALIDATION_FILE_PATH); // etc.

        Source xmlFile = new StreamSource(fileName);
        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
            return true;
        } catch (SAXException e) {
            return false;
        } catch (IOException e) {
            logger.error("Something went wrong validating the file: " + fileName);
            return false;
        }
    }
}
