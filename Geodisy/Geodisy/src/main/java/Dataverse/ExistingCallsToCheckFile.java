package Dataverse;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import Dataverse.ExistingCallsToCheck;
import Dataverse.ExistingSearches;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import static BaseFiles.GeodisyStrings.EXISTING_CALL_TO_CHECK;

public class ExistingCallsToCheckFile {

    private String path = EXISTING_CALL_TO_CHECK;
    GeoLogger logger = new GeoLogger(this.getClass());

    /**
     * Constructor to use for production environment;
     */
    public ExistingCallsToCheckFile() {
    }

    /**
     * Only use this constructor for testing by writing to a test Existing Call to Check file
     *
     * @param path
     */
    public ExistingCallsToCheckFile(String path) {
        this.path = path;
    }

    public void writeExistingCalls(ExistingCallsToCheck existingCallsToCheck) throws IOException {
        FileWriter writer = new FileWriter();
        writer.writeObjectToFile(existingCallsToCheck, path);
    }


    public ExistingCallsToCheck readExistingCalls() throws IOException {
        ExistingCallsToCheck ec;
        try {
            FileInputStream f = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(f);
            ec = (ExistingCallsToCheck) ois.readObject();
        } catch (FileNotFoundException e) {
            ec = ExistingCallsToCheck.getExistingCallsToCheck();
            writeExistingCalls(ec);
        } catch (ClassNotFoundException e) {
            logger.warn("something went wonky loading the existing searches from the file");
            return ExistingCallsToCheck.getExistingCallsToCheck();
        }
        return ec;
    }
}
