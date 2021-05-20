package Crosswalking;


import BaseFiles.GeoLogger;
import Dataverse.SourceJavaObject;
import _Strings.GeodisyStrings;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static _Strings.GeodisyStrings.GEODISY_PATH_ROOT;

public class Crosswalk implements CrosswalkInterface {
    GeoLogger logger = new GeoLogger(this.getClass().toString());
    @Override
    public void convertSJO(SourceJavaObject record)  {
        XMLSchema metadata = new ISO_19139();
        metadata.generateXML(record);
        String path = GeodisyStrings.replaceSlashes(GEODISY_PATH_ROOT + GeodisyStrings.removeHTTPSAndReplaceAuthority(record.getPID()) + "/iso19139.xml");
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            logger.error("Something went wrong trying to delete " + path);
        }
        System.out.println("Finished Creating XML ZIP file");

    }
}
