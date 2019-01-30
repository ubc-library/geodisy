/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geodisy;

import geodisy.DataSourceLocations.Dataverse;
import geodisy.Dataverse.DataverseAPI;
import geodisy.Dataverse.SourceAPI;


/**
 * This is the main activity class of the Geodisy middleware.
 * From here the calls for harvesting of Dataverse and exporting to Geoserver happen.
 * @author pdante
 */
class Geodisy {
    public Geodisy() {
    }
    
    public void harvestDataverse(){
        Dataverse dv = new Dataverse();
        String[] dataverses = dv.getDataverses();
        for(String s: dataverses){
        SourceAPI dvAPI = new DataverseAPI(createDataverseURL(s));
        }
    }
    /** Creates the universal part of the Dataverse API search/retrieve 
     * URLs "://{database name}/api/". 
     * Will still need to add http/http and whatever is needed at the end.*/
    
    private String createDataverseURL(String s) {
        String answer = "://" + s + "/api/";
        return answer;
    }
    public void exportToGeoserver(){
        //TODO
    }
}
