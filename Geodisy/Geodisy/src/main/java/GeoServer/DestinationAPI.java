/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeoServer;

import BaseFiles.API;
import BaseFiles.GeoLogger;
import Crosswalking.GeoBlacklightJson.JSONCreator;

/**
 *
 * @author pdante
 */
public abstract class DestinationAPI extends JSONCreator implements API {
    GeoLogger logger;

}
