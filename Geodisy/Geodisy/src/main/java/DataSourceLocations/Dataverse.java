/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataSourceLocations;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.BASE_DV_URL;

/**
 * Class holds the list of all the dataverse urls that are being harvested from
 * @author pdante
 */
public class Dataverse implements DataLocation {
    private String[] dataverseURLs;

    public Dataverse() {
        //enter dataverse urls into brackets, comma separated
        String[] dvs={BASE_DV_URL};
        dataverseURLs =dvs;
    }
    @Override
    public String[] getDataLocationURLs() {
        return dataverseURLs;
    }
}
