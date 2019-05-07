/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataSourceLocations;

import static Dataverse.DVFieldNameStrings.BASE_DV_URL;

/**
 * Class holds the list of all the dataverse urls that are being harvested from
 * @author pdante
 */
public class Dataverse implements DataLocation {
    private String[] dataverseURLs;

    public Dataverse() {
        //enter dataverse urls into brackets including http/https part, comma separated
        dataverseURLs = new String[]{BASE_DV_URL};
    }
    @Override
    public String[] getDataLocationURLs() {
        return dataverseURLs;
    }
}
