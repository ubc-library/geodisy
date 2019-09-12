package BaseFiles;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.IOException;

import static BaseFiles.GeodisyStrings.COUNTRY_VALS;


public class GetAltCountryNames {
    GeoLogger logger;

    public GetAltCountryNames() {
        logger = new GeoLogger(this.getClass());
    }

    public void getAltNames() {
        try {
            File inputFile = new File(COUNTRY_VALS);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("country");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (eElement.getElementsByTagName("AltName").getLength()>0)
                        continue;
                    String name = getElement(eElement,"countryName");
                    Geonames geo = new Geonames();
                    String countryFullString = geo.callGeonames(name, "PCL*");
                    if(countryFullString.isEmpty()){
                        for(int i = 0; i<5; i++){
                            countryFullString = geo.callGeonames(name, "PCL*");
                            if(!countryFullString.isEmpty())
                                break;
                        }
                        continue;
                    }
                    if(countryFullString.contains("<totalResultsCount>0</totalResultsCount>"))
                        countryFullString = geo.callGeonames(name, "TERR");
                    System.out.println(name + ": " + countryFullString.substring(0,15));
                    Document countryFullDoc = geo.getXMLDoc(countryFullString);
                    countryFullDoc.getDocumentElement().normalize();
                    Element root = countryFullDoc.getDocumentElement();
                    Element altName = doc.createElement("AltName");
                    altName.setTextContent(getElement(root,"alternateNames"));
                    eElement.appendChild(altName);
                    System.out.println();
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(COUNTRY_VALS));
            transformer.transform(domSource, streamResult);
        }catch (ParserConfigurationException | SAXException | IOException | TransformerException pce){
            logger.error("Something went wrong trying to add alternative names to " + COUNTRY_VALS);
        }
    }


    private String getElement(Element element, String tagName){
        return element.getElementsByTagName(tagName).item(0).getTextContent();
    }
}
