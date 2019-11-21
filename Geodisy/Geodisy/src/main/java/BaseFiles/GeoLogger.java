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


    public void info(String message, DataverseJavaObject djo, String loggerName){
        if(djo==null)
            logger.error(message);
        else {
            DataverseRecordInfo dri = new DataverseRecordInfo(djo, loggerName);
            ExistingCallsToCheck efc = ExistingCallsToCheck.getExistingCallsToCheck();
            if (!efc.hasRecord(djo.getDOI())) {
                efc.addOrReplaceRecord(dri);
            } else if (efc.isNewerRecord(dri, loggerName)) {
                efc.addOrReplaceRecord(dri);
            }
        }
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
