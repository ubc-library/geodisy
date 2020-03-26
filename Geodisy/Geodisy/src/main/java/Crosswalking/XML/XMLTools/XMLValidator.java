package Crosswalking.XML.XMLTools;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import java.io.File;
import BaseFiles.GeoLogger;
import org.xml.sax.*;

import java.io.IOException;



public class XMLValidator {
    GeoLogger logger =  new GeoLogger(this.getClass());

    public boolean validateXML(String fileName) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);

            SAXParser parser = factory.newSAXParser();
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                    "http://www.w3.org/2001/XMLSchema");

            XMLReader reader = parser.getXMLReader();
            reader.setErrorHandler(
                    new ErrorHandler() {
                        public void warning(SAXParseException e) throws SAXException {
                            System.out.println("WARNING: " + e.getMessage()); // do nothing
                        }

                        public void error(SAXParseException e) throws SAXException {
                            System.out.println("ERROR: " + e.getMessage());
                            throw e;
                        }

                        public void fatalError(SAXParseException e) throws SAXException {
                            System.out.println("FATAL: " + e.getMessage());
                            throw e;
                        }
                    }
            );
            reader.parse(new InputSource(fileName));
            return true;
        } catch (ParserConfigurationException pce) {
            logger.error("Something went wrong with the parser configuration with file: " + fileName);
            return false;
        } catch (IOException io) {
            logger.error("Something went wrong with an IO exception for file: " + fileName);
            return false;
        } catch (SAXException se) {
            logger.warn("XML file " + fileName + " could not be validated.");
            return false;
        }
    }
}
