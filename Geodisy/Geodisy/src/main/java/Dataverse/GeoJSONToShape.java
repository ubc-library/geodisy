package Dataverse;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.simple.SimpleFeatureType;



public class GeoJSONToShape {
    Logger logger = LogManager.getLogger(this.getClass());

    public boolean createShape(String filePathGeoJSON, String filePathShape) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(filePathGeoJSON));
            String origJSONString = new String(encoded, StandardCharsets.UTF_8);

            
        } catch (MalformedURLException e) {
            logger.error("Something wrong with the file path \"" + filePathGeoJSON + "\" or \"" + filePathShape + "\": " + e);
            return false;
        } catch (IOException e) {
            logger.error("Something went wring with converting the geoJSON to Shape :" + e);
            return false;
        }
        return true;
    }
    //TODO get JSON String
    private String readFile(String filePathGeoJSON, Charset utf8) {

        return null;
    }
}
