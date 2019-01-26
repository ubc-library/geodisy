/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geodisy;

import geodisy.DataSourceLocations.Dataverse;
import geodisy.Dataverse.DataverseAPI;


/**
 *
 * @author pdante
 */
class Geodisy {
    public Geodisy() {
    }
    
    public void getMetadata(){
        Dataverse dv = new Dataverse();
        String[] dataverses = dv.getDataverses();
        for(String s: dataverses){
        DataverseAPI dvAPI = new DataverseAPI(createURL(s));
        }
    }
    //creates the universal part of the dataverse api search/retrieve URLs "://{database name}/api/". Will still need to add http/http and whatever is needed at the end.
    private String createURL(String s) {
        String answer = "://" + s + "/api/";
        return answer;
    }
}
