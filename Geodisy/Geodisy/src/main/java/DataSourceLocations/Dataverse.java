/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataSourceLocations;

/**
 * Class holds the list of all the dataverse urls that are being harvested from
 * @author pdante
 */
public class Dataverse implements DataLocation {
    private String dataverseURL;
    private String[] sets;

    //TODO actually add dataverse urls to harvest from
    public Dataverse() {
        //add all dataverse URLs into the curly brackes, as comma separated strings
        dataverseURL = "placeholder";
        sets = new String[]{"placeholder"};
    }
    @Override
    public String getDataLocationURL() {
        return dataverseURL;
    }

    @Override
    public String[] getSet() {
        return sets;
    }


}
