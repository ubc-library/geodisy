package BaseFiles;

import Dataverse.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Shell on top of a log4j logger
 */
public class GeoLogger {
    public Logger logger;

    public GeoLogger(String className) {
        logger = LogManager.getLogger(className);
    }

    public GeoLogger(Class classType){
        logger = LogManager.getLogger(classType);
    }

    public void error(String message){
        logger.error(message);
    }


    public void info(String message, DataverseJavaObject djo){
        if(djo==null)
            logger.error(message);
        else {
            DataverseRecordInfo dri = new DataverseRecordInfo(djo, this.getName());
            ExistingCallsToCheck efc = ExistingCallsToCheck.getExistingCallsToCheck();
            if (!efc.hasRecord(djo.getPID())) {
                efc.addOrReplaceRecord(dri, message);
            } else if (efc.isNewerRecord(dri, this.getName())) {
                efc.addOrReplaceRecord(dri, message);
            }

        }

    }
    public void info(String message, String fileUrl){
        logger.info("File being saved: "+ message +" was too long. URL of file was: " + fileUrl);
    }
    public void warn(String message){
        logger.warn(message);
    }

    public void debug(String message){
        logger.debug(message);
    }

    public String getName(){
        return logger.getName();
    }
}
