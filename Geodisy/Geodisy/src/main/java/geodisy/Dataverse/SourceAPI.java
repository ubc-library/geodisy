/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geodisy.Dataverse;

import geodisy.API;
import java.util.LinkedList;

/**
 *
 * @author pdante
 */
public abstract class SourceAPI implements API {
    abstract protected LinkedList<String> searchDV();
    abstract protected void downloadMetadata(LinkedList<String> dIOs);
    abstract protected void downloadDatasets(String dOI);
}
