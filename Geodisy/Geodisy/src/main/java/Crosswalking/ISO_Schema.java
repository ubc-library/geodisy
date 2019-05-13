/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crosswalking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract class for general ISO schema crosswalking. Probably will only be for ISO 19115, but I'm including this so
 * Geodisy can more easily be extended to other ISO formats in the future, if necessary.
 * @author pdante
 */
public abstract class ISO_Schema implements MetadataSchema {
    Logger logger = LogManager.getLogger(this.getClass());
}
